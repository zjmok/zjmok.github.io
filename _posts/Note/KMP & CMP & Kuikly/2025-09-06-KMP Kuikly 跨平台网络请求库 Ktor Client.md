---
layout: post
tags: KMP CMP Kuikly
---

# KMP Kuikly 跨平台网络请求库 Ktor Client

在 KMP / Kuikly 项目中，虽然可以使用默认的网络请求 API，但是功能比较单一，需要手动解析数据，管理不方便

而使用网络请求库 Ktor Client，可以有更完善的体验，例如 Json解析、拦截器、日志等

## 依赖

```kotlin
val ktorVersion = "3.1.3" // 注意使用 Kotlin 对应能用的版本

kotlin.sourceSets.commonMain.dependencies{
    implementation("io.ktor:ktor-client-core:$ktorVersion") // 核心库
    implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion") // 内容协商（用于JSON序列化）
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion") // Kotlinx.serialization JSON支持
}
kotlin.sourceSets.androidMain.dependencies{
    api("io.ktor:ktor-client-okhttp:$ktorVersion")
}
kotlin.sourceSets.iosMain.dependencies{
    api("io.ktor:ktor-client-darwin:$ktorVersion")
}
kotlin.sourceSets.jsMain.dependencies{
    api("io.ktor:ktor-client-js:$ktorVersion")
}
```

前提还需要配置 kotlinx-serialization 协程

## KMP 获取引擎

```kotlin
# commonMain
expect fun getEngine(): HttpClientEngine

# androidMain
actual fun getEngine(): HttpClientEngine {
    return OkHttp.create()
}

# iosMain
actual fun getEngine(): HttpClientEngine {
    return Darwin.create()
}

# jsMain
actual fun getEngine(): HttpClientEngine {
    return JsClient().create()
}
```

## HttpClient 简单封装

引擎必须由 KMP 的 expect/actual 机制获取对应平台的 HttpClient 引擎

```kotlin
val ktorClient
    get(): HttpClient {
        // 通过 KMP 的 expect/actual 获取适配各平台的 HttpClient 引擎
        val engine = getEngine()
        val client = HttpClient(engine) {
            engine {
                // 引擎配置...
            }
            // 序列化
            install(ContentNegotiation) {
                json()
            }
            // logger
            install(KtorLoggerInterceptor)
        }
        return client
    }
```

## Ktor Client 工具进一步封装

这里主要是将 请求过程可能抛出的异常 和请求失败 （还可以加业务错误） 组合到一起 在 onFailure 处理，成功则由 onSuccess 处理

```kotlin
sealed class KtorResult {
    data class Success(val response: HttpResponse) : KtorResult()
    data class Failure(val exception: Throwable) : KtorResult()
}

class RemoteException(val statusCode: Int, cause: Throwable?) : Exception(cause) {
    override fun toString(): String {
        return "RemoteException(statusCode=$statusCode, message=${message})"
    }
}

inline fun <T : CoroutineScope> T.runCatchingKtor(block: T.() -> HttpResponse): KtorResult {
    return try {
        KtorResult.Success(block())
    } catch (e: Throwable) {
        KtorResult.Failure(e)
    }
}

/**
 * code 在 [200, 300) 范围内
 */
inline fun KtorResult.onSuccess(action: (value: HttpResponse) -> Unit): KtorResult {
    if (this is KtorResult.Success) {
        if (this.response.status.isSuccess()) {
            action(this.response)
        }
    }
    return this
}

/**
 * @param T 需要解析的类型 可改为传入 BaseData<T> 返回 T 去掉一层包装
 * @param action 解析失败时 返回 null
 */
suspend inline fun <reified T> KtorResult.onSuccess(action: (value: T?) -> Unit): KtorResult {
    if (this is KtorResult.Success) {
        if (this.response.status.isSuccess()) {
            runCatching {
                this.response.body<T>()
            }.onFailure {
                action(null)
            }.onSuccess {
                action(it)
            }
        }
    }
    return this
}

/**
 * 异常 + code 不在 [200, 300) 范围内
 */
inline fun KtorResult.onFailure(action: (exception: RemoteException) -> Unit): KtorResult {
    if (this is KtorResult.Failure) {
        val exception = RemoteException(-999, this.exception)
        action(exception)
    } else {
        if (this is KtorResult.Success) {
            if (this.response.status.isSuccess().not()) {
                val statusCode = this.response.status.value
                val exception = RuntimeException("HTTP error with status code: $statusCode")
                val ktorException = RemoteException(statusCode, exception)
                action(ktorException)
            } else {

                // todo 还可以处理 成功返回 200 后 业务的 errorCode
                val errorCode = 0
                val message = ""
                // 上面是假数据

                // 解析数据，若 errorCode != 0 代表业务失败，则返回 BizException
                if (errorCode != 0) {
                    val exception = RuntimeException("服务器返回： errorCode = $errorCode, message = $message")
                    val bizException = RemoteException(errorCode, exception)
                    action(bizException)
                }
            }
        }
    }

    return this
}
```

Kuikly 使用示例

```kotlin
getPager().lifecycleScope.launch {
    runCatchingKtor {
        val url = WanAPI.BASE_URL + WanAPI.BANNER_LIST
        ktorClient.use {
            it.get(url) {
                header("Authorization", "Bearer your_token_here") // 认证头示例
            }
        }
    }.onFailure {
        // 异常 + code 不在 [200, 300) 范围内
        println("failure: $it")
    }.onSuccess<BaseData<List<BannerItem>>> {
        it?.let {
            println(it.toJson(false))
        } ?: run {
            println("解析失败")
        }
    }
}
```

---

## 其它示例

### get post put delete

```kotlin
ktorClient.get(url)

ktorClient.post(url)

ktorClient.put(url)

ktorClient.delete(url)
```

### header

```kotlin
ktorClient.get(url) {
    header("Authorization", "Bearer your_token_here") // 认证头示例
}
```

### post + parameter

- post + setBody + FormDataContent

FormDataContent 键值对表单

```kotlin
ktorClient.post(url) {
    setBody(
        FormDataContent(
            ParametersBuilder()
                .apply {
                    set("key1","value1")
                    append("key2", "value2")
                }
                .build()
        )
    )
}
```

- submitForm

FormDataContent，键值对表单

```kotlin
ktorClient.submitForm(
    url = url,
    formParameters = parameters {
        append("key1", "value1")
        append("key2", "value2")
    },
)
```

### 文件上传

- post + setBody + MultiPartFormDataContent

MultiPartFormDataContent，multipart表单

```kotlin
ktorClient.post(url) {
    setBody(
        MultiPartFormDataContent(
            formData {
                append(FormPart("key1","value1"))
                append("key2","value2")
            },
            boundary = "WebAppBoundary"
        )
    )
}
```

- submitFormWithBinaryData

MultiPartFormDataContent，multipart表单

```kotlin
ktorClient.submitFormWithBinaryData(
    url = url,
    formData = formData {
        append("key1", "value1")
        append("key2", byteArray)
    },
)
```

### post + json

- 对象 + application/json

会自动转换为 TextContent 的 json 字符串

```kotlin
ktorClient.post(url) {
    setBody(
        BaseData(
            errorCode = 1,
            errorMsg = "msg",
            data = "data",
        )
    )
    contentType(ContentType.parse("application/json"))
}
```

---

## 拦截器

```kotlin
HttpClient(engine) {
    install(ContentNegotiation) {
        json()
    }
    install(KtorLoggerInterceptor)
}
```

这里使用伴生对象，也可以使用 getter 等获取对象

```kotlin
class KtorLoggerInterceptor {
    companion object Feature : HttpClientPlugin<Unit, KtorLoggerInterceptor> {
        override val key: io.ktor.util.AttributeKey<KtorLoggerInterceptor>
            get() = io.ktor.util.AttributeKey("KtorLogInterceptor")

        override fun prepare(block: Unit.() -> Unit): KtorLoggerInterceptor = KtorLoggerInterceptor()

        override fun install(plugin: KtorLoggerInterceptor, scope: HttpClient) {
            scope.sendPipeline.intercept(HttpSendPipeline.State) {

                println("--> request ${context.method} to ${context.url}")
//                println("context.headers: ${context.headers::class.simpleName}") // HeadersBuilder
                if (context.headers.isEmpty().not()) {
                    println("--> request Headers: ${context.headers.entries()}}")
                }

                if (context.method != HttpMethod.Get) {
                    val body = context.body
                    (body as? OutgoingContent)?.let {
                        body.contentLength.takeNotNull {
                            if (it > 0) {
                                when (body) {
                                    is EmptyContent -> {}

                                    is TextContent -> {
                                        println("--> request Body (TextContent): ${body.text}")
                                    }

                                    is FormDataContent -> {
                                        println("--> request Body (FormDataContent): ${body.formData.entries()}")
                                    }

                                    is MultiPartFormDataContent -> {
                                        println("--> request Body (MultiPartFormDataContent) contentType = [${(body.contentType)}]")
                                    }

                                    is ByteArrayContent -> {
                                        println("--> request Body (ByteArrayContent) contentType = [${body.contentType}]")
                                    }

                                    else -> {
                                        println(
                                            "--> request Body (${
                                                body::class.simpleName?.split(".")?.last()
                                            }): ${body.toJson(false)}"
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                proceed()
            }
            scope.receivePipeline.intercept(HttpReceivePipeline.State) {
                val t1 = it.requestTime.timestamp
                val t2 = it.responseTime.timestamp
                println("<-- response ${it.status.value} from ${it.request.method} ${it.request.url} in ${(t2 - t1)} ms")
                // response.headers 是 EmptyHeaders，不包含任何值
//                println("it.request.headers: ${it.request.headers::class.simpleName}") // HeadersImpl
//                println("it.headers: ${it.headers::class.simpleName}") // null
                println("<-- response Headers: ${it.headers.entries()}}")
                println(it.bodyAsText())

                proceed()
            }
        }
    }
}
```

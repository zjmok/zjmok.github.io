---
layout: post
tags: Android OkHttp
---

# `addInterceptor` 和 `addNetworkInterceptor` 都是 OkHttp 中用于添加拦截器的方法，但它们有一些关键区别：

### `addInterceptor`
- **调用时机**：无论网络是否可用，`addInterceptor` 都会被调用。
- **调用次数**：只会被调用一次，即使 HTTP 响应是从缓存中获取的。
- **用途**：适用于应用层的拦截，可以在请求发送前或响应接收后进行处理。常用于添加通用的请求头、日志记录、重试机制等。
- **特点**：
  - 可以短路请求，不调用 `chain.proceed()`，直接返回自定义的响应。
  - 不关心 OkHttp 注入的头信息，如 `If-None-Match`。

### `addNetworkInterceptor`
- **调用时机**：只有在网络请求实际发送到服务器时才会被调用。如果请求从缓存中获取，则不会调用。
- **调用次数**：可以多次调用，特别是在重定向和重试的情况下。
- **用途**：适用于网络层的拦截，可以在请求发送到服务器前或响应接收后进行处理。常用于处理网络层的细节，如压缩、解压缩、网络日志等。
- **特点**：
  - 可以操作中间过程的响应，如重定向和重试。
  - 可以访问连接信息，通过 `chain.connection()` 获取连接的详细信息，如服务器的 IP 地址和 TLS 配置信息。

### 示例代码
```kotlin
// 应用层拦截器
val appInterceptor = Interceptor { chain ->
    val request = chain.request().newBuilder()
        .addHeader("User-Agent", "Your-App-Name")
        .build()
    chain.proceed(request)
}

// 网络层拦截器
val networkInterceptor = Interceptor { chain ->
    val request = chain.request()
    val response = chain.proceed(request)
    // 处理网络层的响应
    response
}

val okHttpClient = OkHttpClient.Builder()
    .addInterceptor(appInterceptor)
    .addNetworkInterceptor(networkInterceptor)
    .build()
```

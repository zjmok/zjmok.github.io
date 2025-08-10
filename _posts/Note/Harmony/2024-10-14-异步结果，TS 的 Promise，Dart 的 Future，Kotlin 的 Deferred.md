---
layout: post
tags: Harmony Flutter Kotlin
---

# 异步结果，TS 的 Promise，Dart 的 Future，Kotlin 的 Deferred

都表示一个尚未完成的计算，需要等待，使用 await 或者 then 来处理

await 是在异步环境里同步返回结果，then 是在同步环境回调里异步返回结果。差不多

## TS 的 Promise

### TS 处理 Promise 异步结果：Promise + async/await

```
async function fetchData(): Promise<string> {
    const result = await fetchDataFromNetwork(); // fetchDataFromNetwork 本身是 Promise 异步结果
    return result;
}
```

### TS 传统回调结果转为同步：Promise + resolve

```TypeScript
function fetchData(): Promise<string> {
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve('Hello from TypeScript!');
    }, 2000);
  });
}

// 使用 then
fetchData().then((data) => {
  console.log(data);
});

// 使用 async/await
async function fetchDataAsync() {
  const data = await fetchData();
  console.log(data);
}
```

## Dart 的 Future

### Dart 处理 Future 异步结果：Future + async/await

```Flutter
Future<String> fetchData() async {
  await Future.delayed(Duration(seconds: 2)); // delayed 返回的是 Future
  return 'Hello from Flutter!';
}

// 使用 then
fetchData().then((data) {
  print(data);
});

// 使用 async/await
void fetchDataAsync() async {
  final data = await fetchData();
  print(data);
}

fetchDataAsync();
```

### Dart 传统回调结果转为同步：Completer

```
Future<String> fetchData() {
  final completer = Completer<String>();
  
  fetchDataFromNetwork((result) {
    completer.complete(result);  // 完成并返回结果
  });
  
  return completer.future;  // 返回 Future
}

void main() async {
  String result = await fetchData();  // 使用 await 实现同步行为
  print(result);  // 输出 "Fetched Data"
}
```

## Kotlin

### Kotlin 处理协程异步结果：Deferred + async/await

```Kotlin
suspend fun fetchData(): Deferred<String> = GlobalScope.async {
    delay(2000)
    "Hello from Kotlin!" // 隐式 return
}

// 使用 await
fun main() = runBlocking {
    val data = fetchData().await()
    println(data)
}

// 使用 then 风格的方式（invokeOnCompletion 不能获取到结果，只能进行最终回调）
fun fetchDataWithThen(callback: (String) -> Unit) {
    GlobalScope.launch {
        val data = fetchData().await()
        callback(data)
    }
}

fun main() {
    fetchDataWithThen { data ->
        println(data)
    }
    Thread.sleep(3000) // 等待异步任务完成
}
```

### Kotlin 传统回调结果转为同步：协程的 suspendCoroutine、suspendCancellableCoroutine

```
suspend fun fetchData(): String = suspendCancellableCoroutine {
    fetchDataFromNetwork { result ->
        it.resume(result)
    }
}

fun main() = runBlocking {
    val result = fetchData()  // 可以像调用同步函数一样调用
    println(result)
}
```

## Rxjava

### Rxjava 传统回调结果转为同步：利用 Single + blockingGet

```
Single<String> fetchData() {
    return Single.create(emitter -> {
        fetchDataFromNetwork(new Callback() {
            @Override
            public void onSuccess(String result) {
                emitter.onSuccess(result);  // 发射结果
            }

            @Override
            public void onError(Throwable e) {
                emitter.onError(e);  // 发射错误
            }
        });
    });
}
```

```
String result = fetchData().blockingGet();  // 阻塞，直到获取结果
System.out.println(result);
```

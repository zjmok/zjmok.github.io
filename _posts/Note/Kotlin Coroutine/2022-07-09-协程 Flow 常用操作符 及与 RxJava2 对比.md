---
layout: post
tags: Android Coroutine
---

## `Flow` 常用操作符

### 1. **创建操作符**
- **`flow {}`**：创建一个 `Flow`，可以发射多个值。
  ```kotlin
  val myFlow = flow {
      emit(1)
      emit(2)
  }
  ```

- **`asFlow()`**：将集合或其他类型转换为 `Flow`。
  ```kotlin
  val listFlow = listOf(1, 2, 3).asFlow()
  ```

### 2. **转换操作符**
- **`map()`**：对发射的每个值进行转换。
  ```kotlin
  val mappedFlow = myFlow.map { it * 2 }
  ```

- **`flatMapConcat()`**：将发射的值转换为新的 `Flow` 并按顺序合并。
  ```kotlin
  val flatMappedFlow = myFlow.flatMapConcat { flowOf(it * 2) }
  ```

### 3. **过滤操作符**
- **`filter()`**：根据条件过滤值。
  ```kotlin
  val filteredFlow = myFlow.filter { it % 2 == 0 }
  ```

- **`take(n)`**：仅获取前 `n` 个值。
  ```kotlin
  val limitedFlow = myFlow.take(2)
  ```

- **`drop(n)`**：跳过前 `n` 个值。
  ```kotlin
  val skippedFlow = myFlow.drop(1)
  ```

### 4. **组合操作符**
- **`combine()`**：组合多个 `Flow` 的最新值。
  ```kotlin
  val combinedFlow = flow1.combine(flow2) { a, b -> a + b }
  ```

- **`zip()`**：按顺序组合多个 `Flow` 的值。
  ```kotlin
  val zippedFlow = flow1.zip(flow2) { a, b -> a + b }
  ```

### 5. **终止操作符**
- **`collect()`**：收集 `Flow` 中发射的所有值。
  ```kotlin
  myFlow.collect { value -> println(value) }
  ```

- **`first()`**：获取第一个发射的值并终止。
  ```kotlin
  val firstValue = myFlow.first()
  ```

### 6. **错误处理操作符**
- **`catch()`**：处理 `Flow` 中的异常。
  ```kotlin
  myFlow.catch { e -> println("Error: $e") }
  ```

- **`onCompletion()`**：在 `Flow` 完成时执行某个操作。
  ```kotlin
  myFlow.onCompletion { println("Flow completed") }
  ```

### 7. **流控制操作符**
- **`retry()`**：在发生错误时重新尝试收集 `Flow`。
  ```kotlin
  myFlow.retry(3)
  ```

- **`distinctUntilChanged()`**：只发射与上一个值不同的值。
  ```kotlin
  myFlow.distinctUntilChanged()
  ```

### 8. **调度器**
- **`flowOn()`**：改变 `Flow` 的上下文调度。
  ```kotlin
  myFlow.flowOn(Dispatchers.IO)
  ```

## `Flow` 和 `RxJava2` 操作符

| 类别              | Flow                        | RxJava2                     |
|-------------------|-----------------------------|-----------------------------|
| **创建操作符**     | `flow {}`                   | `Observable.create()`       |
|                   | `asFlow()`                  | `Observable.just()`         |
|                   |                             | `Observable.fromIterable()` |
| **转换操作符**     | `map()`                     | `map()`                     |
|                   | `flatMapConcat()`           | `flatMap()`                 |
|                   | `flatMapMerge()`            | `flatMap()` (并发) |
|                   | `transform()`               | `switchMap()`               |
| **过滤操作符**     | `filter()`                  | `filter()`                  |
|                   | `take(n)`                   | `take(n)`                   |
|                   | `drop(n)`                   | `skip(n)`                   |
|                   | `distinctUntilChanged()`     | `distinctUntilChanged()`     |
| **组合操作符**     | `combine()`                 | `zip()`                     |
|                   | `zip()`                     | `zip()`                     |
|                   | `merge()`                   | `merge()`                   |
| **错误处理操作符** | `catch()`                   | `onErrorReturn()`           |
|                   | `onCompletion()`            | `onError()`                 |
|                   |                             | `retry()`                   |
| **终止操作符**     | `collect()`                 | `subscribe()`               |
|                   | `first()`                   | `first()`                   |
| **流控制操作符**   | `retry()`                   | `retry()`                   |
|                   | `onCompletion()`            | `doOnTerminate()`           |
| **调度器**         | `flowOn()`                  | `subscribeOn()`             |
|                   |                             | `observeOn()`               |

Flow 操作符跟 RxJava2 差不多，但是需要在协程内使用

```
        // Flow
        lifecycleScope.launch {
            flow<Int> {
                for (i in 0 until 10) {
                    emit(i) // 在每次遍历时发出事件 
                }
            }.flowOn(Dispatchers.IO)
                .onStart { // 最先开始

                }.onEach { // 每次接收

                }.catch { // 异常接收

                }.onCompletion { // 整个流完成

                }.collect {  // 数据接收

                }
        }
		
		// RxJava2
		Observable.range(0, 10) // 创建一个发射 0 到 9 的 Observable
        .subscribeOn(Schedulers.io()) // 在 IO 线程上执行
        .doOnSubscribe(disposable -> {
            // 最先开始
        })
        .doOnNext(value -> {
            // 每次接收
        })
        .doOnError(throwable -> {
            // 异常接收
        })
        .doOnComplete(() -> {
            // 整个流完成
        })
        .observeOn(AndroidSchedulers.mainThread()) // 在主线程上处理结果
        .subscribe(
            value -> {
                // 数据接收
            },
            throwable -> {
                // 错误处理
            },
            () -> {
                // 完成处理
            }
        );
```

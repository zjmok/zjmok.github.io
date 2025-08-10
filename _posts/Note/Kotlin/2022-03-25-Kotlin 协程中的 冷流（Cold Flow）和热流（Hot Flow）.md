---
layout: post
tags: Kotlin
---

# 冷流（Cold Flow）和热流（Hot Flow）

普通 Flow 是冷流

SharedFlow StateFlow 是热流

### 冷流（Cold Flow）

冷流是一种惰性流，只有在有订阅者时才会开始执行数据流操作。每个订阅者都会独立地触发流的执行。

#### 示例：
```kotlin
fun getNumbersColdFlow(): Flow<Int> = flow {
    for (i in 1..5) {
        delay(1000)
        emit(i)
    }
}

fun main() = runBlocking {
    val numbersColdFlow = getNumbersColdFlow()
    numbersColdFlow.collect { println("1st Collector: $it") }
    delay(2500)
    numbersColdFlow.collect { println("2nd Collector: $it") }
}
```

#### 输出：
```
1st Collector: 1
1st Collector: 2
1st Collector: 3
1st Collector: 4
1st Collector: 5
2nd Collector: 1
2nd Collector: 2
2nd Collector: 3
2nd Collector: 4
2nd Collector: 5
```

在这个示例中，每个收集器都会独立地从头开始收集数据流。

### 热流（Hot Flow）

热流是一种主动推送数据的流，即使没有订阅者，数据也会持续产生并推送给所有已注册的订阅者。可以使用 `SharedFlow` 或 `StateFlow` 来实现热流。

#### 示例：
```kotlin
fun main() = runBlocking {
    val hotFlow = MutableSharedFlow<Int>()
    val sharedFlow = hotFlow.asSharedFlow()

    launch {
        for (i in 1..5) {
            delay(1000)
            hotFlow.emit(i)
        }
    }

    launch {
        sharedFlow.collect { println("1st Collector: $it") }
    }

    delay(2500)

    launch {
        sharedFlow.collect { println("2nd Collector: $it") }
    }
}
```

#### 输出：
```
1st Collector: 1
1st Collector: 2
1st Collector: 3
1st Collector: 4
1st Collector: 5
2nd Collector: 3
2nd Collector: 4
2nd Collector: 5
```

在这个示例中，第二个收集器在 2500 毫秒后开始收集，因此它只能收集到之后的数据。

### 热流的 SharedFlow StateFlow

**SharedFlow**：
   - `SharedFlow` 是一种可共享的流，可以有多个订阅者，支持重播和缓冲。
   - 通过参数可以配置重播的数量和缓冲区的大小。
   - 适合用于事件总线、多个观察者等场景。
   - 接收者会收到所有发射的值（如果接收者在发射之后才开始收集数据，会接收到缓冲的个数和之后发射的数据）

**StateFlow**：
   - `StateFlow` 是一种特殊类型的 `SharedFlow`，用于持有状态值并向多个观察者发送更新。
   - 总是有一个最新的状态值（即使没有任何消费者）。
   - 适合用于 UI 状态管理，确保始终能够获取最新的状态。
   - 接受者只能收到最新发射的一个值

---

普通 Flow 是冷流，每次调用时才会执行，如 `flow {}`

SharedFlow StateFlow 是热流，订阅模式，会主动执行推送数据，不管有没有订阅者，反正我就执行我的

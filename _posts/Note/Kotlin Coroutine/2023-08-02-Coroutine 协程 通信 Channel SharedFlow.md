---
layout: post
tags: Android Coroutine
---

# 协程通信 Channel

### Channel，通道，一对一

- **channel.receive()** 是一种挂起函数，用于从 Channel 中获取下一个元素。如果没有可用的元素，调用它的协程会挂起，直到有元素可以接收

- **`iterator()` + `hasNext()` + `next()`** 手动控制迭代流程的情况，尤其是在复杂迭代逻辑下

- **`for (item in channel)`** Kotlin 一个便捷的语法糖，即 for 循环来迭代 Channel。这其实是基于 channel.iterator() 实现的简化写法。当通道关闭时，循环自动结束

区别

| 特性                              | `receive()`                         | `iterator()` + `hasNext()` + `next()` | `for (item in channel)`            |
|------------------------------------|-------------------------------------|-------------------------------------|-------------------------------------|
| **挂起行为**                      | 挂起等待下一个元素                  | `hasNext()` 挂起，直到有新元素      | 基于迭代器行为                      |
| **接收单个/多个元素**             | 每次接收一个元素                    | 逐步接收多个元素                    | 逐步接收多个元素                    |
| **通道关闭时的行为**               | 通道关闭时抛出 `ClosedReceiveChannelException` | 迭代器自动结束，不抛异常          | 自动结束，不抛异常                  |
| **使用场景**                      | 适合只需要接收一个元素时             | 适合迭代接收多个元素                | 更加简洁方便的语法，适合迭代多个元素 |
| **简便性**                        | 需要手动控制接收和异常处理           | 需要手动控制迭代和检查              | 语法糖，最简洁的写法                |

#### 1. channel.receive()

```
suspend fun main(){
    val channel = Channel<Int>()
 
    val producer = GlobalScope.launch {
        var i = 0
        while (true){
            delay(100)
            channel.send(i++)
        }
    }
 
    val consumer = GlobalScope.launch {
        while (true){
            println(channel.receive()) // 每次接收一个元素
        }
    }

    // 这里在非阻塞协程中，手动 join 生产者和消费者
    producer.join()
    consumer.join()
}
```

#### 2. iterator() + hasNext + next()

1. 

iterator.hasNext()是挂起函数，在判断是否有下一个元素的时候就需要去Channel中读取元素了

```
suspend fun main() {
    val channel = Channel<Int>()
 
    val producer = GlobalScope.launch {
        var i = 0
        while (i <= 5) {
            delay(100)
 
            println("send before")
            channel.send(i++)
            println("send after")
 
        }
        //关闭通道
        channel.close()
    }
 
    val consumer = GlobalScope.launch {
        val iterator = channel.iterator()
        while (iterator.hasNext()) {
            println(iterator.next())
        }
 
        println("exit consumer")
    }
 
    // 这里在非阻塞协程中，手动 join 生产者和消费者
    producer.join()
    consumer.join()
}
```

#### 3. for (item in channel)

```
suspend fun main() = runBlocking { // runBlocking 是阻塞式的协程 不需要手动 join
    val channel = Channel<Int>()

    launch {
        for (i in 1..3) {
            channel.send(i)
        }
        channel.close()  // 关闭通道
    }

    for (item in channel) {
        println(item)  // 使用 for 循环遍历元素
    }
}
```

### ~~BroadcastChannel, 广播通道，一对多~~

(Kotlin 1.6+ 已被弃用，BroadcastChannel is deprecated in the favour of SharedFlow and StateFlow, and is no longer supported)

- **`broadcastChannel.send()` + `for (num in receiveChannel)`**

发送端和接收端在 Channel 中存在一对多的情形, 可以同时被多个 消费者协程 接收

```
runBlocking {
    // BroadcastChannel 广播通道, 传递 Int 类型数据
    val broadcastChannel = BroadcastChannel<Int>(Channel.BUFFERED)

    // 数据生产者协程
    val producer = GlobalScope.launch {
        for (i in 0..2) {
            delay(100)
            broadcastChannel.send(i)
            println("向通道中发送数据 $i")
        }
        // 关闭通道
        broadcastChannel.close()
    }
    
    List(3) { index ->
        // 数据消费者协程
        GlobalScope.launch {
            val receiveChannel = broadcastChannel.openSubscription()
            for (num in receiveChannel) {
                delay(1000)
                println("消费者协程 $index 从通道中接收数据 $num")
            }
        }
    }.joinAll()
}
```

### ShareFlow 和 StateFlow (BroadcastChannel 的替代方案)

1. ~~**BroadcastChannel**~~：
   - `BroadcastChannel` 允许多个消费者同时接收同一消息。
   - 它不支持重播（replay）和缓冲区大小的灵活配置。
   - 接收者会收到所有发射的值（如果接收者在发射之后才开始收集数据，只能接收到之后发射的数据）
   - 在 Kotlin 1.6 之后已被弃用。

2. **SharedFlow**：
   - `SharedFlow` 是一种可共享的流，可以有多个订阅者，支持重播和缓冲。
   - 通过参数可以配置重播的数量和缓冲区的大小。
   - 适合用于事件总线、多个观察者等场景。
   - 接收者会收到所有发射的值（如果接收者在发射之后才开始收集数据，会接收到缓冲的个数和之后发射的数据）

3. **StateFlow**：
   - `StateFlow` 是一种特殊类型的 `SharedFlow`，用于持有状态值并向多个观察者发送更新。
   - 总是有一个最新的状态值（即使没有任何消费者）。
   - 适合用于 UI 状态管理，确保始终能够获取最新的状态。
   - 接受者只能收到最新发射的一个值

#### 示例

**SharedFlow 示例**

```
fun main() = runBlocking {
    val sharedFlow = MutableSharedFlow<Int>(replay = 1) // 创建一个可重播的 SharedFlow

    // 启动生产者协程
    launch {
        for (i in 1..5) {
            sharedFlow.emit(i)
            delay(100) // 模拟一些耗时操作
        }
    }

    // 启动消费者协程
    launch {
        sharedFlow.collect { value ->
            println("Consumer 1 received: $value")
        }
    }

    // 启动另一个消费者协程
    launch {
        sharedFlow.collect { value ->
            println("Consumer 2 received: $value")
        }
    }
}
```

这个示例中 2 个接收者都能收到全部的 5 个数据


---

**SharedFlow 重播 示例**

即当生产者发射完毕后

```
fun main() = runBlocking {
    // 创建一个可重播的 SharedFlow，设置重播数量为 2
    val sharedFlow = MutableSharedFlow<Int>(replay = 2)

    // 启动生产者协程
    launch {
        for (i in 1..5) {
            sharedFlow.emit(i)  // 发射值
            delay(100)          // 模拟一些耗时操作
        }
    }

    // 启动第一个消费者协程
    launch {
        sharedFlow.collect { value ->
            println("Consumer 1 received: $value")
        }
    }

    // 等待一段时间后再启动第二个消费者
    delay(350) // 等待 350 毫秒，已经发射了 4 个数据（1、2、3、4)，还有一个（5）未发射
    // 那么第二个消费者应该是接受到缓冲区的 2 个（3、4）和未发射的 1 个（5），即（3、4、5）

    // 启动第二个消费者协程
    launch {
        sharedFlow.collect { value ->
            println("Consumer 2 received: $value")
        }
    }

    // 等待一段时间以确保所有输出都能被打印
    delay(500)
}
```

第一个接收者 收到（1、2、3、4、5）

第二个接收者 收到（3、4、5）

---

**StateFlow 示例**

```
fun main() = runBlocking {
    val stateFlow = MutableStateFlow(0) // 创建一个 StateFlow，初始值为 0

    // 启动生产者协程
    launch {
        for (i in 1..5) {
            stateFlow.value = i // 更新状态
            delay(100) // 模拟一些耗时操作
        }
    }

    // 启动消费者协程
    launch {
        stateFlow.collect { value ->
            println("Consumer received: $value")
        }
    }
}
```

StateFlow 通常作为 LivaData 的平替

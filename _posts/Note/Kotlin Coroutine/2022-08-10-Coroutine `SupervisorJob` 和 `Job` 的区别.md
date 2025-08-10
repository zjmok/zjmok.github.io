---
layout: post
tags: Kotlin
---

# Kotlin Coroutine 中，`SupervisorJob` 和 `Job` 的区别

`SupervisorJob` 和 `Job` 是 Kotlin 协程中的两种 `Job` 类型，它们的主要区别在于 **异常处理** 和 **子任务的传播行为**。让我们详细看看它们的不同点：

### 1. **`Job` (默认 Job)**

- **层级结构**：`Job` 是协程的基本任务单元，协程可以嵌套，父协程和子协程之间是层级关系。
- **异常传播**：默认情况下，如果一个子协程失败并抛出异常，异常会被传播到父协程，父协程会被取消，进而导致所有其他子协程也被取消。

#### 示例
```kotlin
val parentJob = Job()

val child1 = CoroutineScope(Dispatchers.Default + parentJob).launch {
    println("Child 1 started")
    delay(1000)
    println("Child 1 completed")
}

val child2 = CoroutineScope(Dispatchers.Default + parentJob).launch {
    println("Child 2 started")
    delay(500)
    throw Exception("Child 2 failed")  // 失败后会取消整个 parentJob
}

runBlocking {
    child1.join()
    child2.join()
}
```
在这个例子中，`child2` 抛出异常，导致 `parentJob` 和所有其他子协程（包括 `child1`）都被取消，`child1` 将无法完成。

### 2. **`SupervisorJob`**

- **层级结构**：`SupervisorJob` 也是协程层级结构的一部分，但它的行为不同于普通的 `Job`。
- **异常处理**：`SupervisorJob` 的一个关键特性是 **异常不传播**。如果一个子协程失败，不会影响其他子协程，也不会导致父协程被取消。每个子协程的异常只会影响它自己。

#### 示例
```kotlin
val supervisorJob = SupervisorJob()

val child1 = CoroutineScope(Dispatchers.Default + supervisorJob).launch {
    println("Child 1 started")
    delay(1000)
    println("Child 1 completed")
}

val child2 = CoroutineScope(Dispatchers.Default + supervisorJob).launch {
    println("Child 2 started")
    delay(500)
    throw Exception("Child 2 failed")  // 失败不会影响其他子协程
}

runBlocking {
    child1.join()
    child2.join()
}
```
在这个例子中，即使 `child2` 抛出异常并失败，`child1` 仍然可以继续执行并完成。

### 主要区别总结

| 特性                      | `Job`                          | `SupervisorJob`                |
|-------------------------|-------------------------------|-------------------------------|
| **异常传播**               | 子协程异常会取消父协程及其所有子协程 | 子协程异常不会影响父协程或其他子协程 |
| **父子协程的取消行为**         | 子协程异常会传播到父协程及其他子协程 | 父协程和其他子协程不受影响          |
| **适用场景**               | 子协程之间强耦合，某个子协程失败需要整体取消 | 子协程之间解耦，子任务独立运行         |

### 使用场景

1. **`Job` 适合的场景**：
   - 子协程之间存在强关联，一个子协程的失败意味着其他子协程也无法继续工作。
   - 需要整个任务组作为一个整体，要么全部成功，要么全部失败。

2. **`SupervisorJob` 适合的场景**：
   - 子协程任务是独立的，一个任务的失败不应该影响其他任务的执行。
   - 适用于 UI 操作或其他异步操作，某些任务失败后不应该影响其他任务的执行（例如：加载多个组件的异步操作，某个组件加载失败，其他组件仍应继续加载）。

### 什么时候使用 `Job` vs `SupervisorJob`？

- 如果你的协程需要彼此依赖，一个子协程失败时，你希望父协程和其他子协程都终止，使用 `Job`。
- 如果你的协程相对独立，子协程的失败不应该影响父协程或其他子协程，使用 `SupervisorJob`。

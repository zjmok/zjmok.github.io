---
layout: post
tags: Android Compose
---

# Compose 的 remember

在 Jetpack Compose 中，`remember` 是一个非常重要的概念，用于在组合函数中保存状态。以下是一个系统化的学习路径，帮助你全面理解 `remember` 及其相关知识点：

### 1. 基本概念
- **`remember`**：用于在组合函数中保存状态，确保在重组过程中状态不会丢失。
  ```kotlin
  val count = remember { mutableStateOf(0) }
  ```

### 2. `remember` 的使用场景
- **保存简单状态**：如计数器、开关状态等。
  ```kotlin
  var count by remember { mutableStateOf(0) }
  ```

- **保存复杂对象**：如列表、数据类等。
  ```kotlin
  val items = remember { mutableStateListOf<String>() }
  ```

### 3. `remember` 与 `mutableStateOf`
- **`mutableStateOf`**：创建一个可变的状态对象。
  ```kotlin
  var text by remember { mutableStateOf("Hello") }
  ```

### 4. `rememberSaveable`
- **`rememberSaveable`**：在配置更改（如屏幕旋转）时保存状态。
  ```kotlin
  var count by rememberSaveable { mutableStateOf(0) }
  ```

### 5. `remember` 与 `LaunchedEffect`
- **`LaunchedEffect`**：用于在组合函数中启动协程。
  ```kotlin
  LaunchedEffect(Unit) {
      // 协程代码
  }
  ```

### 6. `remember` 与 `DisposableEffect`
- **`DisposableEffect`**：用于在组合函数中执行清理操作。
  ```kotlin
  DisposableEffect(Unit) {
      // 初始化代码
      onDispose {
          // 清理代码
      }
  }
  ```

### 7. 实战示例
- **计数器示例**：
  ```kotlin
  @Composable
  fun Counter() {
      var count by remember { mutableStateOf(0) }
      Button(onClick = { count++ }) {
          Text("Count: $count")
      }
  }
  ```

- **保存状态的计数器**：
  ```kotlin
  @Composable
  fun SaveableCounter() {
      var count by rememberSaveable { mutableStateOf(0) }
      Button(onClick = { count++ }) {
          Text("Count: $count")
      }
  }
  ```

### 8. 进阶知识
- **`remember` 与 `derivedStateOf`**：用于创建派生状态。
  ```kotlin
  val derivedState = remember { derivedStateOf { count * 2 } }
  ```

- **`remember` 与 `snapshotFlow`**：用于将状态转换为流。
  ```kotlin
  val flow = remember { snapshotFlow { count } }
  ```

---

rememberCoroutineScope 获取组合感知作用域，以便可以在组合外启动协程

derivedStateOf 如果某个状态是从其他状态对象计算或者派生出来的，请使用 derivedStateOf，使用此函数可以确保当计算中使用的状态之一发生变化时才会进行计算

snapshotFlow 将 Compose 的 State 转为 Flow

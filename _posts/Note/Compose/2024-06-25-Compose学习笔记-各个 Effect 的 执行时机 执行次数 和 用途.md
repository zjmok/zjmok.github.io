---
layout: post
tags: Android Compose
---

# 在 Jetpack Compose 中，各个 Effect 的 执行时机 执行次数 和 用途：

### 1. LaunchedEffect
- 异步
- **执行时机**：当 `LaunchedEffect` 进入组合时，它会启动一个协程，并在该协程中执行代码块。如果 `LaunchedEffect` 退出组合，协程将取消。
- **执行次数**：当 `LaunchedEffect` 的键发生变化或首次进入组合时执行一次。如果键不变，则不会重新执行。如果 key 是 Unit 则会每次重组会重新执行
- **用途**：适用于在可组合项的生命周期内执行挂起函数。例如，加载数据或启动动画。

  ```kotlin
  @Composable
  fun MyComposable() {
      LaunchedEffect(userName: String) {
          // 仅在 userName 变化或首次组合时执行
          viewModel.updateUser(userName)
      }
  }
  ```

### 2. SideEffect
- 同步
- **执行时机**：在每次重组后都会执行。
- **执行次数**：在每次重组后都会执行。
- **用途**：适用于需要在每次重组后执行某些操作的情况，例如记录日志或更新外部状态。

  ```kotlin
  @Composable
  fun MyComposable() {
      SideEffect {
          // 每次重组后执行
          println("Composable has been recomposed")
      }
  }
  ```

### 3. DisposableEffect
- 异步
- **执行时机**：当 `DisposableEffect` 进入组合时执行，并在退出组合或键发生变化时进行清理。
- **执行次数**：当 `DisposableEffect` 的键发生变化或首次进入组合时执行一次，并在退出组合或键变化时进行清理。
- **用途**：适用于需要在可组合项退出组合时进行清理的情况，例如取消订阅或释放资源。

  ```kotlin
  @Composable
  fun MyComposable(data: String) {
      DisposableEffect(data) {
          // 进入组合时执行
          println("DisposableEffect started with data: $data")
          onDispose {
              // 退出组合或键变化时清理
              println("DisposableEffect disposed")
          }
      }
  }
  ```

### 4. rememberUpdatedState
- **执行时机**：用于在效应中引用值，而不希望效应重启时。它会记住最新的状态值，并在效应或回调中使用。
- **执行次数**：`rememberUpdatedState` 本身不会触发重新执行，但它会更新内部状态，使得效应或回调（例如 onClick）中使用的值始终是最新的。
- **用途**：适用于需要在效应或回调中捕获某个值，但不希望效应重启的情况。

  ```kotlin
  @Composable
  fun MyComposable(onTimeout: () -> Unit) {
      val currentOnTimeout by rememberUpdatedState(onTimeout)
      LaunchedEffect(Unit) {
          delay(1000)
          currentOnTimeout()
      }
  }
  ```

如果不使用 rememberUpdatedState，在重组过程中有可能会捕获了旧的值，而 rememberUpdatedState 可以保持最新的值

虽然它与 Effect API 有一定关联，但它的主要功能是处理状态更新的闭包问题，主要用于回调中，确保回调中的状态始终是最新的

#### rememberUpdatedState 场景示例

```
// 正确使用
@Composable
fun ExampleComponent() {
    // 计数状态，初始值为 0
    var count by remember { mutableStateOf(0) }
    
    // 使用 rememberUpdatedState 保持最新的 count 值
    val updatedCount = rememberUpdatedState(count)

    // 一个按钮，点击时增加计数
    Button(onClick = { count++ }) {
        Text("Increment Count: $count")
    }

    // 启动一个协程，模拟异步操作
    LaunchedEffect(Unit) {
        // 模拟延迟
        delay(2000)
        // 使用 updatedCount.value 访问最新的计数
        println("Latest Count after delay: ${updatedCount.value}")
    }
}

// （实测并非百分比出现）
// 错误使用
// 如果在 LaunchedEffect 启动时 count 的值是 0，即使你在延迟期间点击了按钮并增加了 count，在打印时仍然会输出 0。这是因为 LaunchedEffect 在首次组合时捕获了 count 的值
@Composable
fun ExampleComponent() {
    var count by remember { mutableStateOf(0) }

    // 按钮，点击时增加计数
    Button(onClick = { count++ }) {
        Text("Increment Count: $count")
    }

    // 启动一个协程，直接使用 count
    LaunchedEffect(Unit) {
        // 模拟延迟
        delay(2000)
        // 使用 count 访问当前计数
        println("Count after delay: $count")
    }
}
```

### 5. produceState
- **执行时机**：用于创建一个 `State` 对象，并在其生命周期内管理状态。
- **执行次数**：在 `produceState` 的键发生变化或首次进入组合时执行，并在其生命周期内管理状态。
- **用途**：适用于需要在可组合项中管理复杂状态的情况。

  ```kotlin
  @Composable
  fun MyComposable(data: String): State<Int> {
      return produceState(initialValue = 0, key1 = data) {
          // 在生命周期内管理状态
          value = loadData(data)
      }
  }
  ```

produceState 主要适用于需要直接在 Composable 中处理异步操作的情况

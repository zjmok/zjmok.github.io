---
layout: post
tags: Android Compose
---

# Compose 中，Composable 生命周期

### 1. **组合（Composition）**
- **首次组合（Initial Composition）**: 当 Composable 函数第一次被调用并添加到 UI 树时，会进行首次组合。在这个阶段，Compose 会执行 Composable 函数并创建 UI 组件。
  
- **定义**: 当一个 Composable 被第一次添加到 UI 树时，它会经历首次组合。这是 Composable 的生命周期的开始阶段。
- **特点**: 在这个阶段，所有的 `remember`、`LaunchedEffect(Unit)` 等操作将会执行一次。此时，任何状态、效果或副作用都是初始化的。

### 2. **重组（Recomposition）**
- **重组的触发**: 当 Composable 的输入状态发生变化时（如 State 或 LiveData 的变化），Compose 会重新执行该 Composable 函数，更新 UI。这被称为重组。重组是 Compose 的核心特性，使得 UI 可以根据数据变化自动更新。
- **重组与首次组合的区别**: 首次组合是指 Composable 函数的第一次执行，而重组是指由于状态变化导致的再次执行。

- **定义**: 当 Composable 的状态发生变化（例如通过 `State` 或 `MutableState`）时，它会被重新组合。这个过程是更新 UI 的过程。
- **特点**: 在重组期间，Compose 会重新执行 Composable 的内容，但会利用缓存的状态来减少不必要的计算和渲染，从而提高性能。比如，只有当输入的关键字发生变化时，`LaunchedEffect(key)` 的协程才会重新启动。
  
### 3. **状态管理（State Management）**
- **`remember`**: 用于在重组之间存储状态，保持状态的值在重组时不变。
- **`rememberSaveable`**: 除了在重组之间保持状态外，还可以在配置更改（如屏幕旋转）时保存状态。

### 4. **副作用（Side Effects）**
- **`LaunchedEffect`**: 在 Composable 函数首次组合时执行指定的代码。它可以通过传递 key 来控制副作用的重新执行。
  
    ```kotlin
    LaunchedEffect(key) {
        // 这个代码块在首次组合时执行，并且当 key 变化时重新执行
    }
    ```

- **`SideEffect`** 在每次重组后都会执行

- **`DisposableEffect`**: 在 Composable 被移除时执行清理操作，例如取消注册监听器或停止协程。

### 5. **环境（Environment）**
- Composable 可以访问 Android 环境和其他 Composable 提供的上下文信息。例如，`LocalContext` 提供了当前的 `Context`，而 `LocalLifecycleOwner` 提供了当前的生命周期拥有者。

### 6. **与 Android 组件结合**
- Composable 的生命周期与 Android Activity 和 Fragment 的生命周期相互作用。Activity 和 Fragment 的生命周期方法（如 `onCreate`, `onResume`, `onPause`）会影响 Composable 的组合和重组行为。

### 总结

- Composable 的生命周期由组合和重组机制驱动，通过使用状态和副作用管理，Composable 可以创建动态且响应式的 UI。

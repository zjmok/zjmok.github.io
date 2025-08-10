---
layout: post
tags: Android
---

# Android 五种 Activity 启动模式：

1. **`standard`**：默认模式，每次启动 Activity 都会创建一个新的实例，并将其放入任务栈中。
2. **`singleTop`**：栈顶复用模式，如果栈顶已经有该 Activity 的实例，则不会创建新的实例，而是复用栈顶的实例，并调用 `onNewIntent()` 方法。
3. **`singleTask`**：栈内复用模式，如果任务栈中已经有该 Activity 的实例，则会复用该实例，并将其上方的所有 Activity 移除，同时调用 `onNewIntent()` 方法。
4. **`singleInstance`**：单实例模式，该 Activity 会独占一个新的任务栈，并且在整个系统中只有一个实例。
5. **`singleInstancePerTask`**：Android 12 引入的新模式，结合了 `singleInstance` 和 `singleTask` 的特性，确保每个任务栈中只有一个该 Activity 的实例，但允许在不同的任务栈中存在多个实例

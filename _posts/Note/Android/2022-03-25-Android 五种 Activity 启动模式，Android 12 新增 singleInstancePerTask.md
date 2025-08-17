---
layout: post
tags: Android
---

# Android 五种 Activity 启动模式（4标准 + 1）：

1. **`standard`**：默认模式，每次启动 Activity 都会创建一个新的实例，并将其放入任务栈中。
2. **`singleTop`**：栈顶复用模式，如果栈顶已经有该 Activity 的实例，则不会创建新的实例，而是复用栈顶的实例，并调用 `onNewIntent()` 方法。
3. **`singleTask`**：栈内复用模式，如果任务栈中已经有该 Activity 的实例，则会复用该实例，并将其上方的所有 Activity 移除，同时调用 `onNewIntent()` 方法。
4. **`singleInstance`**：单实例模式，该 Activity 会独占一个新的任务栈，并且在整个系统中只有一个实例。
5. **`singleInstancePerTask`**：Android 12 引入的新模式，结合了 `singleInstance` 和 `singleTask` 的特性，确保每个任务栈中只有一个该 Activity 的实例，但允许在不同的任务栈中存在多个实例

# 相关的 Intent 标志

​​- FLAG_ACTIVITY_NEW_TASK​​
  - 类似于singleTask，但行为略有不同
  - 常用于从Service或非Activity上下文启动Activity
​​​​
- FLAG_ACTIVITY_CLEAR_TOP​​
  - 如果目标Activity已在当前任务栈中，则清除它上面的所有Activity
​​
- ​​FLAG_ACTIVITY_SINGLE_TOP​​
  - 等同于singleTop模式
​​
- ​​FLAG_ACTIVITY_CLEAR_TASK​​
  - 清除与Activity关联的整个任务栈

示例

```
// 启动一个 singleTask Activity 并清除其上的所有 Activity
Intent intent = new Intent(this, MainActivity.class);
intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
startActivity(intent);

// 在 Service 中启动 Activity
Intent activityIntent = new Intent(this, TargetActivity.class);
activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
startActivity(activityIntent);
```

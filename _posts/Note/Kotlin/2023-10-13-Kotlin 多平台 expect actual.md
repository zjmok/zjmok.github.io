---
layout: post
tags: Kotlin
---

# Kotlin 多平台 expect actual

### `expect`/`actual` 机制概述：

- **`expect`**：定义在共享代码模块中，表示某个功能或类需要在每个平台上有不同的实现。
- **`actual`**：在具体平台的代码模块中实现 `expect` 声明的内容。每个平台需要提供它自己的 `actual` 实现。

有点像接口和实现的关系

### 代码示例：

#### 共享代码模块：
```kotlin
// 在共享模块中，定义一个 expect 函数
expect fun getPlatformName(): String
```

#### Android 实现：
```kotlin
// Android 平台的实际实现
actual fun getPlatformName(): String {
    return "Android"
}
```

#### iOS 实现：
```kotlin
// iOS 平台的实际实现
actual fun getPlatformName(): String {
    return "iOS"
}
```

### 使用说明：
- `expect` 是声明，它定义了需要被实现的 API，而具体实现则由 `actual` 提供。
- `expect` 和 `actual` 不仅可以用于函数，还可以用于类、属性等。
- 每个平台（如 Android、iOS、JS、JVM）都可以提供自己的 `actual` 实现。

### 适用场景：
- **跨平台开发**：你可以在共享模块中定义一些通用的接口或逻辑，然后根据不同平台的特性分别实现。这使得你能够复用代码，同时又能在不同平台上灵活实现特定功能。

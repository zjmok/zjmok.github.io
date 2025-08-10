---
layout: post
tags: Flutter
---

Flutter 中的生命周期指的是 StatefulWidget 的生命周期。以下是 StatefulWidget 生命周期的主要阶段和对应的方法：

1. `createState()`: StatelessWidget 被转换为 StatefulWidget 时，会调用 `createState()` 方法来创建与该 Widget 关联的 State 对象。

2. `initState()`: 在 State 对象被插入到树中时调用，通常用于初始化状态、订阅流或监听器等操作。这个方法只会执行一次。

3. `didChangeDependencies()`: 在 `initState()` 之后立即调用，并且可能会被多次调用。它会在以下情况下被调用：
   - 当 State 对象的依赖关系发生变化时。例如，在 `build` 方法中使用了 `InheritedWidget`，当该 `InheritedWidget` 发生变化时，`didChangeDependencies` 就会被调用。
   - 在父级 Widget 调用 `didChangeDependencies` 时，子级 Widget 的 `didChangeDependencies` 也会被调用。

4. `build()`: 根据当前状态构建 Widget 树的方法。在这个方法中，可以返回一个新的 Widget 来显示应用程序的用户界面。

5. `didUpdateWidget()`: 在接收到新的配置后调用，可以用于比较旧的配置和新的配置，并根据需要更新状态或执行其他操作。

6. `setState()`: 用于更新 State 对象中的状态并重新构建 Widget 树。当需要改变 Widget 的外观或行为时，可以通过调用 `setState()` 来触发重新构建。

7. `deactivate()`: 当 State 对象从树中被移除时调用。可以在这里执行一些清理工作，比如取消订阅流或监听器。

8. `dispose()`: 在 State 对象永久从树中移除时调用。通常用于释放资源、取消订阅等最终的清理工作。

需要注意的是，上述方法并非全部都是必须重写的，只有在特定的情况下才会使用到。开发者可以根据需要选择重写哪些方法来控制 Widget 的行为和状态变化。

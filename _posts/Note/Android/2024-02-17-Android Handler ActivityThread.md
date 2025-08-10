---
layout: post
tags: Android
---

# ActivityThread

ActivityThread 不是一个实际的 Java 线程，只是一个 final 类

ActivityThread 是 Android 中管理应用程序主线程的核心类。

`ActivityThread` 是 Android 系统的核心类之一，它负责管理应用程序的主线程（即 UI 线程）和应用的生命周期事件。
每个 Android 应用程序进程中都运行着一个 `ActivityThread` 实例，它通过与 `AMS` (Activity Manager Service) 进行通信，处理启动 Activity、管理生命周期等操作。
它是 Android 的应用程序框架的核心部分，负责启动组件（如 `Activity`、`Service`、`BroadcastReceiver`）并管理它们的生命周期。

### 主要职责
`ActivityThread` 的主要职责包括：
1. **管理应用程序的主线程**：Android 应用程序中所有的 UI 事件（如绘制、用户交互）都在主线程中进行，`ActivityThread` 管理这个主线程，并处理来自系统的消息和事件。
2. **处理与 `AMS` 的通信**：`ActivityThread` 通过 `Binder` 机制与 `ActivityManagerService` 进行通信。当 `AMS` 请求启动或停止某个组件时，`ActivityThread` 负责执行具体的操作。
3. **组件的生命周期管理**：负责调度 `Activity`、`Service`、`BroadcastReceiver` 等组件的生命周期方法（如 `onCreate()`、`onStart()`、`onResume()` 等）。
4. **加载和初始化应用**：在应用启动时，`ActivityThread` 负责创建 `Application` 实例，并初始化必要的资源。

### `ActivityThread` 的工作机制

#### 1. 主线程的消息循环

`ActivityThread` 是基于消息循环（`MessageQueue` 和 `Looper`）来工作的。当应用程序启动时，`ActivityThread` 启动了一个 `Looper` 循环，处理来自 `AMS` 和应用程序内部的各种消息事件。每个生命周期事件（如 Activity 的启动、停止）都会被封装成消息，放入消息队列中，`ActivityThread` 的主线程会依次处理这些消息。

```java
public final class ActivityThread {
    public static void main(String[] args) {
        Looper.prepareMainLooper();  // 准备主线程的消息循环
        ActivityThread thread = new ActivityThread();
        thread.attach(false); // 连接到系统
        Looper.loop();  // 开启消息循环，主线程开始工作
    }
}
```

- `Looper.prepareMainLooper()`：为主线程创建一个消息队列。
- `Looper.loop()`：进入无限循环，处理消息队列中的消息。

#### 2. 处理与 `AMS` 的交互

`ActivityThread` 通过 `Binder` 与 `AMS` 进行通信。当 `AMS` 需要启动某个 `Activity` 时，会发送消息给 `ActivityThread`，`ActivityThread` 接收到消息后调用 `performLaunchActivity()`，执行启动操作。

```java
private void handleLaunchActivity(ActivityClientRecord r, Intent customIntent) {
    // 调用 performLaunchActivity 启动 Activity
    Activity a = performLaunchActivity(r, customIntent);

    if (a != null) {
        r.activity = a;
        a.mFinished = false;

        // 处理 Activity 的 onResume 生命周期
        handleResumeActivity(r.token, false, r.isForward,
                !r.activity.mFinished && !r.startsNotResumed);
    }
}
```

#### 3. 组件的生命周期管理

在 `ActivityThread` 中，所有组件（如 `Activity`、`Service`、`BroadcastReceiver`）的生命周期事件都是由它调度和管理的。比如，当一个 Activity 被启动时，`ActivityThread` 会调用 `performLaunchActivity()`，它会完成创建 Activity 实例、调用 `onCreate()`、`onStart()` 等生命周期方法。

```java
private Activity performLaunchActivity(ActivityClientRecord r, Intent customIntent) {
    // 加载 Activity 类
    Activity activity = mInstrumentation.newActivity(
            cl, component.getClassName(), r.intent);

    // 调用 onCreate()
    activity.performCreate(activity.mActivityInfo, null);
    return activity;
}
```

#### 4. Application 的启动

`ActivityThread` 在应用启动时，会先创建 `Application` 实例并调用其生命周期方法 `onCreate()`。它确保 `Application` 在任何组件创建之前被初始化。

```java
private void handleBindApplication(AppBindData data) {
    // 创建 Application 实例
    Application app = data.info.makeApplication(data.restrictedBackupMode, null);
    // 调用 Application 的 onCreate 方法
    app.onCreate();
}
```

#### 5. 管理多线程和消息调度

`ActivityThread` 是主线程，因此它负责处理 UI 操作。如果有耗时操作必须在后台执行，开发者可以使用线程或 `AsyncTask` 等工具，但所有 UI 更新必须通过 `ActivityThread` 的主线程处理，防止线程安全问题。

### 常见问题
- **内存泄漏**：如果开发者没有正确处理 Activity、Fragment 的销毁，可能会导致内存泄漏。`ActivityThread` 管理组件生命周期，但无法自动处理代码中的资源释放。
- **ANR**：如果 `ActivityThread` 中的主线程由于长时间阻塞（比如执行了耗时操作），可能会导致应用出现 "Application Not Responding" (ANR) 错误。

### 总结

`ActivityThread` 是 Android 系统中负责管理应用主线程和组件生命周期的核心类。它通过消息循环的方式，处理系统和应用之间的交互，并调度各个组件的生命周期方法。通过它，Android 实现了应用的启动、停止和生命周期的统一管理。

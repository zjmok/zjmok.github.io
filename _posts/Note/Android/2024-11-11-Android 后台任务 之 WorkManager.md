---
layout: post
tags: Android
---

# Android 后台任务 之 WorkManager

WorkerManager 官方文档 <https://developer.android.google.cn/topic/libraries/architecture/workmanager?hl=zh-cn>

Demo App <https://github.com/cymok/TestWorkManager>

---

> 优势：

- 任务持久化，重启后有效

- 支持任务链调用，任务依赖

- 约束条件，可以在 网络、充电等 多种状态条件下执行

- 高度封装，相比 比较底层的 JobService ，不需要过多手动配置

- 任务可控，返回值 WorkRequest，可以控制任务的生命周期、状态等

> 限制：

- 国产定制 ROM 可能需要开启 自启动 才能后台执行 (实测 MIUI 12 是需要开启 自启动，否则会在 APP 启动才能执行后台期间所有任务)

- 时间限制: 
  - 周期任务，最小 15分钟，若设置小于 15 分钟 会被强制在 15 分钟后执行
  - doWork 方法，10 分钟内执行完毕

## Work

### Work 的实现类

- 重写 doWork 方法，必须实现

任务具体的执行逻辑放在这里

doWork 方法的返回值 Result，3 个子类型：Success Failure Retry

```kotlin
override fun doWork(): Result {
    // do something
    return Result.success() // success failure failure
}
```

- 重写 getForegroundInfo 方法

如果未能实现对应的 getForegroundInfo 方法，那么在旧版平台上调用 setExpedited 时，可能会导致运行时崩溃

```kotlin
override fun getForegroundInfo(): ForegroundInfo {
    return ForegroundInfo(NOTIFICATION_ID, createNotification())
}

// 提供一个通知实例
private fun createNotification(): Notification {
    return NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.mipmap.ic_launcher)
        .setContentTitle("通知标题")
        .setContentText("通知内容")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setAutoCancel(true)
        .setContentIntent(null)
        .build()
}
```

### WorkRequest

通过 Work 的子类，创建 WorkRequest，加入到 WorkManager 的队列

支持多种类型 WorkRequest

- 一次性
- 周期 （可以定义的最短重复间隔是 15 分钟（与 JobScheduler API 相同））
- 一次性 + 单例
- 周期 + 单例

代码示例

```kotlin
// 一次性
val request = OneTimeWorkRequestBuilder<TestWorker>()
    .addTag("Tag-OneTimeWork")
    .build()

WorkManager.getInstance(context)
    .enqueue(request)
```

```kotlin
// 周期
// 可以定义的最短重复间隔是 15 分钟（与 JobScheduler API 相同）。
val request = PeriodicWorkRequestBuilder<TestWorker>(15, TimeUnit.MINUTES)
    .addTag("Tag-PeriodicWork")
    .build()

WorkManager.getInstance(context)
    .enqueue(request)
```

```kotlin
// 一次性 + 单例
// REPLACE 若队列中有相同标识的 Worker 则取消原有 Worker 然后新的 Worker 入队
// KEEP 若队列中有相同标识的 Worker 则放弃入队 保持原有 Worker
// APPEND_OR_REPLACE 若队列中有相同标识的 Worker 则附加在原有 Worker 后面，如果原有 Worker 执行失败，新 Worker 也跟随
// APPEND_OR_REPLACE 若队列中有相同标识的 Worker 则附加在原有 Worker 后面，如果原有 Worker 执行失败，新 Worker 会重新入队

// by id
val request = OneTimeWorkRequestBuilder<TestWorker>()
    .addTag("Tag-Singleton-OneTimeWork")
    .build()
WorkManager.getInstance(context)
    .enqueueUniqueWork("Name-Singleton-OneTimeWork", ExistingWorkPolicy.KEEP, request)
val id = request.id
WorkManager.getInstance(context).cancelWorkById(id)
```

```kotlin
// 周期 + 单例
// KEEP 若队列中有相同标识的 Worker 则放弃入队 保持原有 Worker
// CANCEL_AND_REENQUEUE 若队列中有相同标识的 Worker 则取消原有 Worker 然后新的 Worker 入队
// UPDATE 若队列中有相同标识的 Worker 则更新替代原有 Worker 并继承状态 (例如 8 小时的任务，剩下 5 小时将会执行，那么新的 Worker 将在 5 小时后执行)

val request = PeriodicWorkRequestBuilder<TestWorker>(15, TimeUnit.MINUTES)
    .addTag("Tag-Singleton-PeriodicWork")
    .build()

WorkManager.getInstance(context)
    .enqueueUniquePeriodicWork(
        "Name-Singleton-PeriodicWork", // 用于唯一标识工作请求
        ExistingPeriodicWorkPolicy.KEEP, // 如果已有使用该名称且尚未完成的唯一工作链，应执行什么操作
        request
    )
```

### 任务取消

3种:
- by id, `cancelWorkById`
- by name, `cancelUniqueWork`
- by tag, `cancelAllWorkByTag`
- 取消所有 `cancelAllWork`

```kotlin
// by id
val request = OneTimeWorkRequestBuilder<TestWorker>()
    .addTag("Tag")
    .build()
WorkManager.getInstance(context)
    .enqueueUniqueWork("Name", ExistingWorkPolicy.APPEND, request)
val id = request.id
WorkManager.getInstance(context).cancelWorkById(id)

// by tag
WorkManager.getInstance(context).cancelAllWorkByTag("Tag-OneTimeWork")
WorkManager.getInstance(context).cancelAllWorkByTag("Tag-PeriodicWork")

// by name
WorkManager.getInstance(context).cancelUniqueWork("Name-Singleton-OneTimeWork")
WorkManager.getInstance(context).cancelUniqueWork("Name-Singleton-PeriodicWork")

// 取消所有 Worker
WorkManager.getInstance(context).cancelAllWork()
```

### 任务链

链接多个 Worker

```kotlin
val request = OneTimeWorkRequestBuilder<TestWorker>()
    .addTag("Tag-request")
    .build()

val requestCache = OneTimeWorkRequestBuilder<TestWorker>()
    .addTag("Tag-requestCache")
    .build()

val requestUpload = OneTimeWorkRequestBuilder<TestWorker>()
    .addTag("Tag-requestUpload")
    .build()

WorkManager.getInstance(context)
    // request 也可以替代为传入 requestList, `beginUniqueWork` 提供了 request 和 requestList 的重载函数
    .beginUniqueWork("Name", ExistingWorkPolicy.APPEND, request)
    .then(requestCache)
    .then(requestUpload)
    .enqueue()
```

### 任务调度、约束

示例，联网时执行

```kotlin
// 设置网络约束
val constraints = Constraints.Builder()
    .setRequiredNetworkType(NetworkType.CONNECTED) // 任何网络、不计费网络、无网络 等
    .build()

val workRequest = OneTimeWorkRequestBuilder<TestWorker>()
    .setConstraints(constraints) // 设置约束
    .build()

WorkManager.getInstance(context)
    .enqueue(workRequest)
```

### 测试

- 在 [Demo App](https://github.com/cymok/TestWorkManager) 里可进行一些测试

- 命令 `adb shell dumpsys jobscheduler` 可查看任务记录, 负责管理 Worker 执行的组件为 `SystemJobService`

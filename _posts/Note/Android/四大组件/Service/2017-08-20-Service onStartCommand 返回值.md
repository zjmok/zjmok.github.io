---
layout: post
tags: Android
---

```
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

```

- START_STICKY 粘性, 异常被 kill 后会重启执行 onStartCommand 但不带Intent

- START_NOT_STICKY 非粘性, 异常被 kill 后不会重启

- START_REDELIVER_INTENT 粘性, 异常被 kill 后会重启执行 onStartCommand 并带Intent

- START_STICKY_COMPATIBILITY 兼容粘性, 异常被 kill 后不一定能重启


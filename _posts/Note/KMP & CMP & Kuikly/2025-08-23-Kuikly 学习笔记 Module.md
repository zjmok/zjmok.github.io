---
layout: post
tags: KMP Kuikly
---

获取方式，在 Pager 或 ComposeView 的 created 后执行，或其它地方通过 Pager 对象获取 `PagerManager.getCurrentPager()`

- `acquireModule` 返回 module 或报错

- `getModule` 返回 module 或 null

# Kuikly Module

- `MemoryCacheModule` 内存缓存

- `SharedPreferencesModule` 键值对磁盘缓存

- `RouterModule` 页面路由

- `NetworkModule` 网络请求

- `NotifyModule` 页面通知

- `SnapshotModule` 生成当前pager的快照，解决首屏0白屏体验，仅iOS

- `CodecModule` 字符串编解码

- `CalendarModule` 日期处理

- `PerformanceModule` 性能指标监控

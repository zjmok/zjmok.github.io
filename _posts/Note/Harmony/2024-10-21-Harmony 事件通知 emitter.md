---
layout: post
tags: Harmony
---

# Harmony 事件通知 emitter

相比借助 @StorageLink 的双向同步机制实现事件通知（不推荐），开发者可以使用emit订阅某个事件并接收事件回调的方式来减少开销，增强代码的可读性。

示例

```
// 导包
import { emitter } from '@kit.BasicServicesKit';

// 发送通知
let innerEvent: emitter.InnerEvent = { eventId: item.id }
let eventData: emitter.EventData = {
  data: {
    "colorTag": 1
  }
}
emitter.emit(innerEvent, eventData)

// 监听通知
let innerEvent: emitter.InnerEvent = { eventId: this.index }
emitter.on(innerEvent, data => {
  this.onTapIndexChange(data)
})
```


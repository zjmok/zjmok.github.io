---
layout: post
tags: Android Compose
---

# Compose 点击 手势 触控 滑动 拖拽 pointerInput detectXXXGestures

## 按压 点击 双击 长按

```
Modifier.clickable {
    // 点击
}

Button(onClick = {
    // 点击
}) { }

// pointerInput 通常处理复杂手势
Modifier.pointerInput(Unit){
    detectTapGestures(
        onPress = {
            // 按压
        },
        onTap = {
            // 点击
        },
        onDoubleTap = {
            // 双击
        },
        onLongPress = {
            // 长按
        },
    )
}
```

如果使用了 pointerInput 的 detectTapGestures，那么 clickable 将会被拦截，不触发

### 取消点击时的水波纹效果

```
Modifier.clickable(
    indication = null,  // 取消水波纹效果
    interactionSource = remember { MutableInteractionSource() }  // 使用一个新的 InteractionSource
){
    // click
}
```

## pointerInput

- Modifier 多次调用 pointerInput

### 自定义

查看 PointerEvent 的源码

```
private fun calculatePointerEventType(): PointerEventType {
        val motionEvent = motionEvent
        if (motionEvent != null) {
            return when (motionEvent.actionMasked) {
                MotionEvent.ACTION_DOWN,
                MotionEvent.ACTION_POINTER_DOWN -> PointerEventType.Press
                MotionEvent.ACTION_UP,
                MotionEvent.ACTION_POINTER_UP -> PointerEventType.Release
                MotionEvent.ACTION_HOVER_MOVE,
                MotionEvent.ACTION_MOVE -> PointerEventType.Move
                MotionEvent.ACTION_HOVER_ENTER -> PointerEventType.Enter
                MotionEvent.ACTION_HOVER_EXIT -> PointerEventType.Exit
                ACTION_SCROLL -> PointerEventType.Scroll

                else -> PointerEventType.Unknown
            }
        }
        // Used for testing.
        changes.fastForEach {
            if (it.changedToUpIgnoreConsumed()) {
                return PointerEventType.Release
            }
            if (it.changedToDownIgnoreConsumed()) {
                return PointerEventType.Press
            }
        }
        return PointerEventType.Move
    }
```

Compose 的 PointerEventType 与 Views 的 MotionEvent 对应

| PointerEventType.Press	| MotionEvent.ACTION_DOWN	|
| --- 						| ---						|
| PointerEventType.Move		| MotionEvent.ACTION_MOVE	|
| PointerEventType.Release	| MotionEvent.ACTION_UP		|

所以可以这样

```
Modifier
.pointerInput(Unit) {
    awaitPointerEventScope {
        // 循环等待其他事件，直到抬起
        do {
            val event = awaitPointerEvent()
            when (event.type) {
                PointerEventType.Press -> { // down
				    // 支持多指触控 这里简单处理单指
                    val offset = event.changes.first().position
					
                }

                PointerEventType.Move -> { // move
                    val offset = event.changes.first().position
					
                }

                PointerEventType.Release -> { // up

                }
            }
        } while (true)
    }
}
```

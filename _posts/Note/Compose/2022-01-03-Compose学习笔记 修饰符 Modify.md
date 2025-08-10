---
layout: post
tags: Android Compose
---

# Modifier

官方文档 [修饰符列表](https://developer.android.google.cn/develop/ui/compose/modifiers-list?hl=zh-cn)

## 常用修饰符

Modifier 是有序的, 例如 clickable padding background 设置不同的顺序 效果是不一样的

- size(): 设置组件的固定大小。
- height() 和 width(): 设置组件的固定高度和宽度。
- aspectRatio() 设置宽高比例

- align(): 设置组件在父容器中的对齐方式。

- offset(): 将组件在父容器中的位置偏移指定的距离。
- padding(): 在组件的周围添加内边距。
- paddingFromBaseline(): 在组件的基线周围添加内边距。

- fillMaxSize(): 将组件的大小设置为父容器的最大尺寸。
- wrapContentSize(): 将组件的大小调整为其内容的尺寸。可设置居中等
- requiredWidth(): 强制组件具有指定的宽度。
- requiredHeight(): 强制组件具有指定的高度。

- background(): 设置组件的背景颜色或绘制背景样式。
- border(): 在组件周围绘制边框。
- clip(): 将组件裁剪为指定形状。例如 `Modifier.clip(RoundedCornerShape(8.dp)) // 圆角`

- focusable(): 将组件设置为可获取焦点，并在获取焦点时执行指定的操作。
- clickable(): 将组件设置为可点击，并在点击时执行指定的操作。
- combinedClickable(): 将多个点击事件合并为一个。可设置 单击 双击 长按
- draggable(): 将组件设置为可拖动，并在拖动时执行指定的操作。

- scale(): 缩放组件的大小。
- rotate(): 旋转组件。
- alpha(): 设置组件的透明度。

- scrollable(): 将组件设置为可滚动，并指定滚动行为。
- horizontalScroll() 和 verticalScroll()
- pointerInput(): 处理触摸和手势事件。
- indication(): 设置组件的触摸反馈效果。

- keyboardActions(): 在键盘事件发生时执行指定的操作。

- shadow(): 添加阴影效果。

- testTag(): 给组件设置测试标签，用于自动化测试。

- statusBarsPadding 可以增加一个状态栏的间距
- systemBarsPadding

## 高级

- draggable 使元素可拖动，配合 offset 实现拖拽

```
@Composable
fun DraggableExample() {
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    Box(
        modifier = Modifier
            .size(100.dp)
            .background(Color.Blue)
            .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
            .draggable(
                orientation = Orientation.Horizontal,
                state = rememberDraggableState { delta ->
                    offsetX += delta
                }
            )
            .draggable(
                orientation = Orientation.Vertical,
                state = rememberDraggableState { delta ->
                    offsetY += delta
                }
            )
    )
}
```

- graphicsLayer 使元素可缩放，配合手势检测实现缩放

```
@Composable
fun ScalableExample() {
    var scale by remember { mutableStateOf(1f) }
    val scaleGestureModifier = Modifier.pointerInput(Unit) {
        detectTransformGestures { _, pan, zoom, _ ->
            scale *= zoom
        }
    }

    Box(
        modifier = scaleGestureModifier
            .size(100.dp)
            .background(Color.Red)
            .graphicsLayer(scaleX = scale, scaleY = scale)
    )
}
```

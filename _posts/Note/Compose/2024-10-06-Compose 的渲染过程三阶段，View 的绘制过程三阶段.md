---
layout: post
tags: Android Compose
---

## Compose 的渲染过程 三阶段

官方文档 [compose phases](https://developer.android.google.cn/develop/ui/compose/phases?hl=zh-cn)

1. **组合（Composition）**

要显示什么样的界面。Compose 运行可组合函数并创建界面说明。

Compose 运行时会执行可组合函数，并 输出一个表示界面的树结构。这个界面树由 包含下一阶段所需的所有信息的布局节点

2. **布局（Layout）**

要放置界面的位置。该阶段包含两个步骤：测量（measurement）和放置（placement）。对于布局树中的每个节点，布局元素都会根据 2D 坐标来测量并放置自己及其所有子元素。

Compose 会使用在组合阶段生成的界面树 作为输入。布局节点的集合包含 决定每个节点在 2D 空间的大小和位置

3. **绘制（Drawing）**

渲染的方式。界面元素会绘制到画布（通常是设备屏幕）中。

系统会再次从上到下遍历规则树， 然后，节点会在屏幕上自行绘制

### Compose 渲染 三阶段 总结

**组合**：根据状态构建 UI 层级

**布局**：计算每个 UI 元素的大小和位置

**绘制**：将 UI 元素渲染到屏幕上


这些阶段通常会以相同的顺序执行，让数据能够沿一个方向（从组合到布局，再到绘制）生成帧（也称为单向数据流）。

BoxWithConstraints 以及 LazyColumn 和 LazyRow 是值得注意的特例，其子级的组合取决于父级的布局阶段

## Android 中 View 的绘制过程 三阶段

1. **测量 (Measurement)**
   - 在这个阶段，View 根据其父 View 提供的约束条件（如大小限制）来计算自己的尺寸
   - 主要方法：`onMeasure()`。在这个方法中，你可以调用 `setMeasuredDimension()` 来设置 View 的最终宽度和高度

2. **布局 (Layout)**
   - 测量完成后，View 的位置被确定。这个阶段决定了 View 在父容器中的大小和位置
   - 主要方法：`onLayout()`。在这个方法中，你需要指定 View 的边界（left, top, right, bottom）

3. **绘制 (Drawing)**
   - 在此阶段，View 被实际绘制到屏幕上。你可以在这里执行任何绘图操作
   - 主要方法：`onDraw()`。在这个方法中，你可以使用 Canvas 对象绘制图形、文本等

这三个阶段是 View 渲染的关键部分，理解它们有助于优化绘制性能和自定义 View 的行为

### 自定义 View ViewGroup 示例

#### 自定义 View

```
class CircleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint().apply {
        color = Color.BLUE
        isAntiAlias = true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val desiredSize = 200
        val width = MeasureSpec.getSize(widthMeasureSpec).coerceAtLeast(desiredSize)
        val height = MeasureSpec.getSize(heightMeasureSpec).coerceAtLeast(desiredSize)
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val radius = (width.coerceAtMost(height) / 2).toFloat()
        canvas.drawCircle(width / 2f, height / 2f, radius, paint)
    }
}
```

#### 自定义 ViewGroup

自定义 ViewGroup 时 必须重写 onLayout，管理其子 View 的尺寸和位置

```
class SimpleLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        var totalHeight = 0

        for (i in 0 until childCount) {
            val child = getChildAt(i)
            measureChild(child, widthMeasureSpec, heightMeasureSpec)
            totalHeight += child.measuredHeight
        }

        setMeasuredDimension(width, totalHeight)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        var currentTop = top

        for (i in 0 until childCount) {
            val child = getChildAt(i)
            child.layout(left, currentTop, right, currentTop + child.measuredHeight)
            currentTop += child.measuredHeight
        }
    }
}
```

在布局文件中使用

```
<com.example.yourapp.SimpleLayout
    android:id="@+id/simpleLayout"
    android:layout_width="match_parent"
    android:layout_height="wrap_content">

    <TextView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Item 1" />

    <TextView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Item 2" />

    <TextView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Item 3" />

</com.example.yourapp.SimpleLayout>
```

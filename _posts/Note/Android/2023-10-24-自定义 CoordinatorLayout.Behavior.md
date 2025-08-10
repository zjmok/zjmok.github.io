---
layout: post
tags: Android CoordinatorLayout
---

# 自定义 `CoordinatorLayout.Behavior`

## Behavior 中可重写的方法

- onInterceptTouchEvent()：是否拦截触摸事件
- onTouchEvent()：处理触摸事件

- layoutDependsOn()：确定使用Behavior的View要依赖的View的类型
- onDependentViewChanged()：当被依赖的View状态改变时回调
- onDependentViewRemoved()：当被依赖的View移除时回调

- onMeasureChild()：测量使用Behavior的View尺寸
- onLayoutChild()：确定使用Behavior的View位置

- onStartNestedScroll()：嵌套滑动开始（ACTION_DOWN），确定Behavior是否要监听此次事件
- onStopNestedScroll()：嵌套滑动结束（ACTION_UP或ACTION_CANCEL）
- onNestedScroll()：嵌套滑动进行中，要监听的子 View的滑动事件已经被消费
- onNestedPreScroll()：嵌套滑动进行中，要监听的子 View将要滑动，滑动事件即将被消费（但最终被谁消费，可以通过代码控制）
- onNestedFling()：要监听的子 View在快速滑动中
- onNestedPreFling()：要监听的子View即将快速滑动

## 简单使用

我的练习项目 `TestCoordinatorLayoutBehavior`

### 拖拽跟随

MainActivity

```
    private var dX = 0f
    private var dY = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        // 拖拽逻辑
        binding.iv.setOnTouchListener { v, event ->
            when(event.action){
                MotionEvent.ACTION_DOWN -> {
                    dX = v.x - event.rawX
                    dY = v.y - event.rawY
                }
                MotionEvent.ACTION_MOVE -> {
                    // 平滑过渡
//                    v.animate()
//                        .x(event.rawX + dX)
//                        .y(event.rawY + dY)
//                        .setDuration(0)
//                        .start()
                    // 拖拽更适合使用直接 set 快速响应
                    v.x = event.rawX + dX
                    v.y = event.rawY + dY
                }
                else -> return@setOnTouchListener false
            }
            true
        }
    }

```

```
class CustomBehavior : Behavior<View> {

    constructor() : super()

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        // 指定依赖的 View 类型
        return dependency is ImageView
    }

    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {

        // 这里的逻辑是跟随改变位置
        val dX = dependency.x - child.width
        val dY = dependency.y - child.height
        val translationX =
            (dX * 2).coerceIn(0f, ScreenUtils.getScreenWidth().toFloat() - child.width)
        val translationY = (dY * 2).coerceIn(
            0f,
            ScreenUtils.getScreenHeight().toFloat() - child.height - BarUtils.getStatusBarHeight()
        )
        child.translationX = translationX
        child.translationY = translationY

        // 这里的逻辑是跟随改变透明度
        val dy = dependency.y - child.height
        val alpha = 1f - (dy.coerceAtLeast(0f) / ScreenUtils.getScreenHeight())
        child.alpha = alpha

        return true
    }

}
```

```
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/main"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

    <androidx.coordinatorlayout.widget.CoordinatorLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        >

        <ImageView
            android:id="@+id/iv"
            android:layout_width="100dp"
            android:layout_height="100dp"
            android:src="@mipmap/ic_launcher"
            />

        <TextView
            android:id="@+id/tv"
            android:background="@color/black"
            android:textColor="@color/white"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="textView"
            app:layout_behavior=".CustomBehavior"
            />

    </androidx.coordinatorlayout.widget.CoordinatorLayout>
	
</androidx.constraintlayout.widget.ConstraintLayout>
```

### RecyclerView 滚动跟随

MainActivity

```
        // 初始化 rv
        binding.rv.adapter = object : Adapter<ViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
                val textView = TextView(this@MainActivity).apply {
                    layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100)
                }
                return object : ViewHolder(textView) {}
            }

            override fun getItemCount(): Int {
                return 100
            }

            override fun onBindViewHolder(holder: ViewHolder, position: Int) {
                val tv = holder.itemView as TextView
                tv.text = "item: ${position}"
            }

        }
```

```
class CustomBehavior2 : Behavior<View> {

    constructor() : super()

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        // 指定依赖的 View 类型
        return dependency is RecyclerView
    }

    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int
    ): Boolean {
        // 返回 true 表示处理嵌套滚动事件
        return true
    }

    private var countY = 0

    override fun onNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        target: View,
        dxConsumed: Int, // 已经消耗的水平滚动距离
        dyConsumed: Int,
        dxUnconsumed: Int, // 如果 RecyclerView 只能处理部分滚动事件，而剩余的滚动距离是 20 像素，那么 dxUnconsumed 或 dyUnconsumed 就会是 20
        dyUnconsumed: Int,
        type: Int,
        consumed: IntArray // consumed 数组用于指示父视图消耗了多少滚动距离，数组里装x和y
    ) {
        Log.d(
            "onNestedScroll",
            "dyConsumed: ${dyConsumed} dyUnconsumed: ${dyUnconsumed} consumed[1]: ${consumed[1]}"
        )
        // dyConsumed 可处理的滑动px。实际滑动的px
        // dyUnconsumed 不可处理的滑动px。例如，下拉时到顶了再尝试继续下拉20个px，那么这个值就是20

        // 处理嵌套滚动事件
        // 例如：根据滚动距离调整 child 的位置

        countY += dyConsumed
//        child.translationY = countY.toFloat()

        val rv = target as RecyclerView
        // `computeVerticalScrollRange` rv可滑动的总y
        val allY = rv.computeVerticalScrollRange() - ScreenUtils.getScreenHeight() + BarUtils.getStatusBarHeight()
        val ratioY = countY.toFloat() / allY
        val y = ratioY * ScreenUtils.getScreenHeight() - BarUtils.getStatusBarHeight()

        Log.d("onNestedScroll", "y: ${y}, countY: ${countY}, allY: ${allY}")
        child.translationY = y.coerceIn(
            0f,
            ScreenUtils.getScreenHeight().toFloat() - child.height - BarUtils.getStatusBarHeight()
        )

//        child.translationY += dyConsumed
//        child.translationY += dyUnconsumed

    }

    override fun onNestedPreScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        target: View,
        dx: Int,
        dy: Int,
        consumed: IntArray,
        type: Int
    ) {
        // 处理嵌套滚动前的事件
    }

}
```

```
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/main"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

    <androidx.coordinatorlayout.widget.CoordinatorLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        >

        <androidx.recyclerview.widget.RecyclerView
            android:id="@+id/rv"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            app:layoutManager="androidx.recyclerview.widget.LinearLayoutManager"
            android:orientation="vertical"
            tools:itemCount="10"
            />

        <TextView
            android:id="@+id/tv2"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="textView2"
            app:layout_behavior=".CustomBehavior2"
            />

    </androidx.coordinatorlayout.widget.CoordinatorLayout>
	
</androidx.constraintlayout.widget.ConstraintLayout>
```

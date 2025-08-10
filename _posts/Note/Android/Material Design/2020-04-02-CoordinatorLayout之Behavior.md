---
layout: post
tags: Android CoordinatorLayout
---

`CoordinatorLayout` 的炫酷效果，少不了它的内部类 `Behavior`

### Behavior

自定义 Behavior 可以实现自己的各种效果

自定义 Behavior 可以选择重写以下的几个方法有：

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

### 使用场景

通常自定义Behavior分为两种情况：

- 某个View依赖另一个View，监听其位置、尺寸等状态的变化

- 某个View监听CoordinatorLayout内实现了NestedScrollingChild接口的子View的滑动状态变化(也是一种依赖关系)

### 使用方法

---

[参考链接](https://www.jianshu.com/p/b987fad8fcb4)


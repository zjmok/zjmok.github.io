---
layout: post
tags: Android
---

MDC 1.2.0  
即 Material Components for Android (MDC-Android)

---

#### Material 动效

[https://material.io/develop/android/theming/motion](https://material.io/develop/android/theming/motion)

Material 动效系统包含一套 (四种) 转场动画模式

- 容器变换
  - https://material.io/design/motion/the-motion-system.html#container-transform
- 共享轴
  - https://material.io/design/motion/the-motion-system.html#shared-axis
- 淡入淡出
  - https://material.io/design/motion/the-motion-system.html#fade-through
- 弹出
  - https://material.io/design/motion/the-motion-system.html#fade
  
这些转场模式可用于 Fragment (包括 Jetpack Navigation) 、Activity 和 View 之间的过渡。

Fragment 之间的容器变换 (使用 Jetpack Navigation)
```
<!-- fragment_a.xml -->
<View
    android:id="@+id/start_view"
    android:transitionName="start_container" />

<!-- fragment_b.xml -->
<View
    android:id="@+id/end_view"
    android:transitionName="end_container" />

// FragmentB.kt
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    ...
    sharedElementEnterTransition = MaterialContainerTransform()
}

// FragmentA.kt
fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    ...
    exitTransition = Hold()
}
...
val directions = FragmentADirections.actionFragmentAToFragmentB()
val extras = FragmentNavigatorExtras(startView to "end_container")
findNavController().navigate(directions, extras)
```

Fragment 之间的共享 Z 轴
```
// FragmentA.kt
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    ...
    reenterTransition = MaterialSharedAxis(
        MaterialSharedAxis.Z, /* forward = */ false)
    exitTransition = MaterialSharedAxis(
        MaterialSharedAxis.Z, /* forward = */ true)
}

// FragmentB.kt
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    ...
    enterTransition = MaterialSharedAxis(
        MaterialSharedAxis.Z, /* forward = */ true)
    returnTransition = MaterialSharedAxis(
        MaterialSharedAxis.Z, /* forward = */ false)
}
```

Fragment 之间的淡入淡出
```
// FragmentA.kt
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    ...
    exitTransition = MaterialFadeThrough()
}

// FragmentB.kt
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    ...
    enterTransition = MaterialFadeThrough()
}
```

弹出目标视图 (使用 TransitionManager)
```
val fade = MaterialFade()
TransitionManager.beginDelayedTransition(container, fade)
view.visibility = View.VISIBLE // Use View.GONE to fade out
```

---

另外 MDC 1.2.0 新增功能还有  
- [`Slider`](https://developer.android.com/reference/com/google/android/material/slider/Slider)  
  - 
- [`ShapeableImageView`](https://developer.android.com/reference/com/google/android/material/imageview/ShapeableImageView)  

- [`MaterialColors`](https://developer.android.com/reference/com/google/android/material/color/MaterialColors)  

- [`MaterialThemeOverlay`](https://developer.android.com/reference/com/google/android/material/theme/overlay/MaterialThemeOverlay)  

---
layout: post
tags: Android CoordinatorLayout
---

#### CoordinatorLayout 中 AppbarLayout 子View 的属性 layout_scrollFlags

** 辅助理解 **

- `enter` //进入页面

- `collapse` //折叠效果(minHeight)

- `snap` //自动贴边(SnapHelper类似)

---

** `app:layout_scrollFlags="xxx"` 的5个值 **

- `scroll` //view可跟随滚动

- `enterAlways` //下拉时view会优先进入页面，必须配合scroll使用(即 `app:layout_scrollFlags="scroll|enterAlways"` 下同)

- `enterAlwaysCollapsed` //上拉时view会优先进入折叠，必须配合enterAlways使用

- `exitUntilCollapsed` //上拉时优先折叠到`minHeight`，必须配合scroll使用

- `snap` //针对以上效果的自动贴边效果，五十五十，要么全部显示，要么全部隐藏

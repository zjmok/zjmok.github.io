---
layout: post
tags: Android CoordinatorLayout
---

#### CoordinatorLayout 中 AppbarLayout 子View 的属性 layout_scrollFlags

** 辅助理解 **

- `enter` // 进入页面

- `collapse` // 折叠效果(minHeight)

- `snap` // 自动贴边(SnapHelper类似)

---

** `app:layout_scrollFlags="xxx"` 的5个值 **

- `scroll` // view可跟随滚动

- `snap` // 自动贴边效果，即手指放开时要么全部显示要么全部隐藏

以下为效果，必须配合 `scroll` 使用

- `enterAlways` // 上拉时优先折叠，下拉时优先展开。滑动 AppbarLayout 可折叠可展开

- `enterAlwaysCollapsed` // 上拉时优先折叠，下拉时最后展开。下拉时会暂时展开 minHeight

- `exitUntilCollapsed` // 上拉时优先折叠，下拉时最后展开。上拉时折叠到 minHeight 并保持，下拉过程会暂时展示 pin

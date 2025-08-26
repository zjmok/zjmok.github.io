---
layout: post
tags: Android CoordinatorLayout
---

#### CoordinatorLayout 中 CollapsingToolbarLayout 的属性 `app:layout_collapseMode`

CollapsingToolbarLayout 标签设置以下属性

- `pin`：固定模式，在折叠的时候最后固定在顶端

- `parallax`：视差模式，在折叠时会有个视差折叠的效果。

  - 配合 `app:layout_collapseParallaxMultiplier` 使用，取值 [0.0, 1.0]，值越大视差越大

  - CollapsingToolbarLayout 最少高度少于 96dp 会进入折叠效果隐藏内容，想要少于96dp需要自定义，或直接不用 CollapsingToolbarLayout

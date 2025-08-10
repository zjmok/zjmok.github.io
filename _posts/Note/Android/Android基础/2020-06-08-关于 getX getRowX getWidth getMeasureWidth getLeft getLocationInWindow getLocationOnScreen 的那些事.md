---
layout: post
tags: Android
---

#### View

`view.getWidth()` 为view的最终宽度

`view.getMeasuredWidth()` 为view的测量宽度

`view.getX()` 为view(取左上角) 在父布局的x坐标

![view.getX()](/img/view_get_x.jpg)

`view.getLeft()` 为view的左边 在父布局的x坐标

`view.getRight()` 为view的右边 在父布局的x坐标

`view.getTop()` 为view的上边 在父布局的y坐标

`view.getBottom()` 为view的下边 在父布局的y坐标

![view.getLeft()](/img/view_get_left.jpg)

`view.getLocationInWindow` view 在当前窗口的xy坐标

`view.getLocationOnScreen` view 在屏幕的xy坐标

#### MotionEvent

`motionEvent.getX()` 触摸点 在触摸View的x坐标

![motionEvent.getX()](/img/motion_event_get_x.jpg)

`motionEvent.getRowX()` 触摸点 在屏幕左上角(坐标原点)的x坐标

![motionEvent.getRawX()](/img/motion_event_get_raw_x.jpg)

#### 总结

`getX` `getLeft` `getLocationInWindow` 是在上级的 坐标

`getRowX` `getLocationOnScreen` 是在屏幕的坐标

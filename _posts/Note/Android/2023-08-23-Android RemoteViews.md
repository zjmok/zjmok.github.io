---
layout: post
tags: Android
---

### RemoteViews

RemoteViews，从字面意思理解为它是一个远程视图。是一种远程的 View，它在其它进程中显示，却可以在另一个进程中更新。

RemoteViews 在Android中的使用场景主要有：自定义通知栏 和 桌面小部件。

在RemoteViews 的构造函数中，第二个参数接收一个 layout 文件来确定 RemoteViews 的视图；
然后，我们调用RemoteViews 中的 set 方法对 layout 中的各个组件进行设置，
例如，可以调用 setTextViewText() 来设置 TextView 组件的文本。
前面提到，小部件布局文件可以添加的组件是有限制的，它可以支持的 View 类型包括
四种布局：
```
FrameLayout、LinearLayout、RelativeLayout、GridLayout 
```

和 13 种View: 
```
AnalogClock、Button、Chronometer、ImageButton、ImageView、
ProgressBar、TextView、ViewFlipper、
ListView、GridView、StackView、
AdapterViewFlipper、ViewSub。
```
注意：RemoteViews 也并不支持上述 View 的子类。

### 设置属性

RemoteViews 提供了一系列 setXXX() 方法来为小部件的子视图设置属性。具体可以参考 API 文档。

### RemoteViews 中的 `集合 View`

`remoteView.setRemoteAdapter`, 并且需要接受一个 `RemoteViewsService` 的 Intent,  
而 `RemoteViewsService` 又需要在重写函数返回 `RemoteViewsFactory` 实例

#### RemoteViewsService

RemoteViewsService，是管理RemoteViews的服务。

一般，当AppWidget 中包含 GridView、ListView、StackView 等集合视图时，才需要使用RemoteViewsService来进行更新、管理。

RemoteViewsService 更新集合视图的一般步骤是：

1. 通过 setRemoteAdapter() 方法来设置 RemoteViews 对应 RemoteViewsService 。

2. 之后在 RemoteViewsService 中，实现 RemoteViewsFactory 接口。然后，在 RemoteViewsFactory 接口中对集合视图的各个子项进行设置，例如 ListView 中的每一Item。

#### RemoteViewsFactory

通过 RemoteViewsService 中的介绍，我们知道 RemoteViewsService 是通过 RemoteViewsFactory 来具体管理layout中集合视图的，
RemoteViewsFactory是RemoteViewsService中的一个内部接口。

RemoteViewsFactory提供了一系列的方法管理集合视图中的每一项。例如：

`RemoteViews getViewAt(int position)`

通过 getViewAt() 来获取“集合视图”中的第 position 项的视图，视图是以 RemoteViews 的对象返回的。

`int getCount()`

通过 getCount() 来获取“集合视图”中所有子项的总数。

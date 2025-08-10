---
layout: post
tags: Android Compose
---

# 可组合项

```

Button

Text
(JetPackCompose之Text使用指北)[https://juejin.cn/post/7064595591220690951]

TextField

Image

ProgressIndicator  
  CircularProgressIndicator  
  LinearProgressIndicator  


Column (垂直列表)  
  对其方式 
  horizontalAlignment = Alignment.CenterHorizontally  
  verticalArrangement = Arrangement.SpaceEvenly  
  verticalArrangement 可选对齐 SpaceAround SpaceBetween SpaceEvenly Top Center Bottom
    - Top
    - Center
    - Bottom
    - SpaceAround 它会在第一个和最后一个子组件之前和之后留出一半的间距，而在两个连续子组件之间留出完整的间距
    - SpaceBetween 第一个和最后一个子组件紧贴容器的两端，而其他子组件之间的间距均匀分布
    - SpaceEvenly 均匀分布 两端和子组件之间都均分
  
  // 类似 ScrollView 的滚动  
  val scrollState = rememberScrollState(0)
  Column(
      modifier = Modifier.verticalScroll(scrollState),
  ) {}

Row (水平)  
  verticalAlignment  
  horizontalArrangement 可选对齐 EqualWeight SpaceBetween SpaceAround SpaceEvenly Center Start End  

Box (层叠 FramLayout)
  子Widget要控制位置 添加参数即可 modifier = Modifier.align(Alignment.TopStart)

ConstraintLayout 在Compose中 优势已不明显 在View中是为了解决嵌套性能问题 而Compose嵌套不存在太大性能问题

LazyColumn (类似 RecyclerView 的可滚动列表)
  LazyColumn() {
      stickyHeader {} 可以增加粘性头
      item {} 可以增加非列表的固定项
	  stickyHeader {} 第二个粘性项 当滑动上去时会顶掉第一个
      items(items = getDatas()) { data ->
          ItemWidget(title = data)
      }
  }

LazyRow

LazyHorizontalGrid (网格布局)

LazyVerticalStaggeredGrid (瀑布流布局)

Divider

Spacer

TabRow

Tab

ScrollableTabRow (超出屏幕可滚动的TabRow)

Pager 接口
  实现类:
  
  HorizontalPager 如需在屏幕外加载更多页面，请将 beyondBoundsPageCount 设置为大于零的值
  
  VerticalPager

```

## Compose 第三方库

图片  
(coil-compose)[https://github.com/coil-kt/coil]
```
# dependencies 
implementation("io.coil-kt:coil-compose:2.4.0")
// implementation("io.coil-kt:coil:2.4.0")

# usage
AsyncImage(
	model = "https://img-blog.csdnimg.cn/20200401094829557.jpg",
	contentDescription = null
)

# or
Image(
    painter = rememberAsyncImagePainter(
    model = ImageRequest.Builder(LocalContext.current)
        .data("https://example.com/image.jpg")
        .size(Size.ORIGINAL) // Set the target size to load the image at.
        .build()
    ),
    contentDescription = null
)
```

## 官方导航库
navigation-compose
```
# dependencies
implementation 'androidx.navigation:navigation-compose:2.6.0'
```

列表 下拉刷新 等 多种自定义可组合项

[Compose中的列表](https://blog.csdn.net/lyabc123456/article/details/128510336)

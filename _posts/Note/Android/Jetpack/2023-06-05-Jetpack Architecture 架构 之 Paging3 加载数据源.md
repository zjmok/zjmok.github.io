---
layout: post
tags: Android Jetpack
---

Jetpack Architecture 架构 之 Paging 加载数据源

> Paging3 比 Paging2 的基本分页，还多了 Header 下拉刷新 和 Footer 上拉加载更多 的功能

paging库最重要的三个类就是

- `DataSource` 数据源，执行具体的数据载入工作。注意：数据的载入需要在工作线程中执行。数据可以来自网络，也可以来自本地数据库，如Room.根据分页机制的不同，Paging为我们提供了3种DataSource。
- `PageList` 负责通知DataSource何时获取数据，以及如何获取数据。例如，何时加载第一页/下一页、第一页加载的数量、提前多少条数据开始执行预加载等。需要注意的是，从DataSource获取的数据将存储在PagedList中
- `PageListAdapter` 适配器，RecyclerView的适配器，通过分析数据是否发生了变化，负责处理UI展示的逻辑(增加/删除/替换等)

---

# DataSource

有 3 种

- PositionalDataSource 适合固定数据源，指定位置加载多少条，start=3&count=10
- PageKeyedDataSource 适合常见的分页，page=2&page_size=10
- ItemKeyedDataSource 适合列表依赖某个字段的情况，评论，since=9527&page_size=5

`PageKeyedDataSource` 新版 api 已变为 `PagingSource` 实现类是 `LegacyPagingSource`

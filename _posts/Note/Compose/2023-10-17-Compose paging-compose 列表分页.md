---
layout: post
tags: Android Compose
---

## paging-compose 列表分页

paging3 列表分页

需要单独添加 `paging-compose` 依赖

```
implementation("androidx.paging:paging-compose:3.2.1")
```

### DataSource

[官方文档](https://developer.android.com/reference/kotlin/androidx/paging/compose/LazyPagingItems)

继承 PagingSource

```
class ArticleListDataSource(
    private val firstPage: Int,
    // 为了通用性，这里把网络请求的实现暴露出去
    private val request: suspend (page: Int) -> Articles
) : PagingSource<Int, DataX>() {

    override fun getRefreshKey(state: PagingState<Int, DataX>): Int? {
        // 刷新时的页码, 返回 null 则从首页开始重新加载
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DataX> {
        return try {

            // 当前页码
            val key = params.key ?: firstPage

            // 每页数量
            val pageSize = params.loadSize

            // 网络请求
            val result = request.invoke(key)

            // 前一页 页码
            val preKey = if (key > firstPage) {
                key - 1
            } else {
                null
            }

            // 下一页 页码
            // 判断是否还有下一页 有很多方式
            val nextKey = if (result.over) {
                null
            } else {
                key + 1
            }

            LoadResult.Page(result.datas, preKey, nextKey)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

}
```

### Pager

可以在 ViewModel 返回 pager

```
    fun getArticlesPager(): Pager<Int, DataX> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = {
                ArticleListDataSource(firstPage = 0) { page ->
                    WanRepository.getHomeList(page = page)
                }
            },
        )
    }
```

### UI

把 Pager 转换为 flow 再收集为 LazyPagingItems, 然后就可以在 LazyColumn 里使用了

```
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeComponent() {
    val viewModel: HomeViewModel = viewModel()
    val lazyPagingItems = viewModel.getArticlesPager().flow.collectAsLazyPagingItems()
	LazyColumn {
	
        // 分页 正在加载首页 loading
        if (lazyPagingItems.loadState.refresh == LoadState.Loading) {
            item {
                Text(
                    text = "Waiting for items to load from the backend",
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally)
                )
            }
        }

        // 分页 列表
	    items(count = lazyPagingItems.itemCount) { index ->
            val rowItems = lazyPagingItems[index]!!
            ArticleComponent(rowItems)
        }
		
        // 加载更多 loading
        if (lazyPagingItems.loadState.append == LoadState.Loading) {
            item {
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally)
                )
            }
        }
	}
}
```

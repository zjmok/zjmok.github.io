---
layout: post
tags: Harmony
---

# HarmonyOS 下拉刷新（Refresh） 加载更多（List.onReachEnd）

## 下拉刷新

API 参考 - [Refresh](https://developer.huawei.com/consumer/cn/doc/harmonyos-references-V5/ts-container-refresh-V5)

---

利用 Refresh，在原有列表包裹一层 Refresh，在 onRefreshing 响应刷新事件，执行刷新接口

```
  @State isRefreshing: boolean = false

  build() {
    Refresh({ refreshing: $$this.isRefreshing }) { // $$ 传递引用 双向绑定
      List() { // 列表
        // ... 其它组件
      }
    }
    .onStateChange((refreshStatus: RefreshStatus) => {
      console.info('Refresh onStatueChange state is ' + refreshStatus)
    })
    .onOffsetChange((value: number) => {
      console.info('Refresh onOffsetChange offset:' + value)
    })
    .onRefreshing(() => {
      setTimeout(() => { // 延迟后 恢复可刷新状态
        this.isRefreshing = false
      }, 2000)
      console.log('onRefreshing test')
      this.currentPage = 0
      this.loadData()
    })
    .backgroundColor(0x89CFF0)
    .refreshOffset(64)
    .pullToRefresh(true)
  }
```

## 加载更多

API 参考 - List.[onReachEnd](https://developer.huawei.com/consumer/cn/doc/harmonyos-references-V5/ts-container-list-V5#onreachend)

列表到底末尾位置时触发。

List边缘效果为弹簧效果时，划动经过末尾位置时触发一次，回弹回末尾位置时再触发一次。

---

利用 List 的 onReachEnd 可以监听到下拉到底的事件，但是会多次响应，需要额外处理一下，可以用一个 isLoadingMore 去限制执行次数

```
  @State isLoadingMore: boolean = false
  @State currentPage: number = 0

  build() {
    List(){ // 列表
      // ... 其它组件
    }
    // ... 其它属性
    .onReachEnd(() => {
      console.log("onReachEnd")
      if (!this.isLoadingMore) {
        this.isLoadingMore = true
		setTimeout(() => { // 延迟后 恢复可加载更多状态
          this.isLoadingMore = false
        }, 2000)
        this.currentPage++
        this.loadList()
        console.log(`loadMore: this.currentPage = ${this.currentPage}`)
      }
    })
    
    loadList() {
      service.getHomeList(this.currentPage).then(result => {
        if (this.currentPage == 0) {
          // 加载首页
          this.homeList = result.datas // 不要清空数组再 concat，否则页面会有一瞬间变空
        } else {
          // 加载更多
          this.homeList = this.homeList.concat(result.datas)
        }
      })
    }
  }
```

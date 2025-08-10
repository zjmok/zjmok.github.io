---
layout: post
tags: Harmony
---

# HarmonyOS学习笔记-路由 导航 页面跳转

router 简单直接 (官方不推荐) <https://developer.huawei.com/consumer/cn/doc/harmonyos-guides-V5/arkts-routing-V5>

```
// 1. 先在 main_pages 注册页面，然后使用一句代码即可
// 2. 页面跳转
router.pushUrl({url: "pages/SecondPage"}) // 传参时带上 params 值要是自定义类的对象，测试直接使用 string 无效
// 3. 页面返回
router.back(); 
router.back({url: 'pages/Home', params: {info: '来自Home页'}}); // 可返回指定页面，可携带返回参数
```

官方推荐 组件路由 (Navigation) <https://developer.huawei.com/consumer/cn/doc/harmonyos-guides-V5/arkts-navigation-navigation-V5>

## Navigation 导航组件

Navigation 组件是路由导航的根视图容器，一般作为 Page 页面的根容器使用

从API Version 9开始，推荐与 NavRouter 组件搭配使用。

从API Version 10开始，推荐使用 NavPathStack 配合 navDestination 属性进行页面路由。

```
@Provide('pageInfos') pageStack: NavPathStack = new NavPathStack()

build(){
  Navigation(pageStack) {
    // 布局内容
    
  }
  .title(this.NavigationTitle) // 一个 @Build 函数的 UI
  //.title('NavIndex')
  .menus(this.NavigationMenus) // 一个 @Build 函数的 UI
  .titleMode(NavigationTitleMode.Full)
  .toolbarConfiguration([
    // tab
    {
      value: $r("app.string.navigation_toolbar_add"),
      icon: $r("app.media.ic_public_highlightsed")
    },
    {
      value: $r("app.string.navigation_toolbar_app"),
      icon: $r("app.media.ic_public_highlights")
    },
    {
      value: $r("app.string.navigation_toolbar_collect"),
      icon: $r("app.media.ic_public_highlights")
    },
  ])
  .hideTitleBar(false)
  .hideToolBar(false)
  .onTitleModeChange((titleModel: NavigationTitleMode) => {
    console.info('titleMode' + titleModel)
  })
}
```


## NavPathStack 路由栈

### 系统路由表

[系统路由表](https://developer.huawei.com/consumer/cn/doc/harmonyos-guides-V5/arkts-navigation-navigation-V5#系统路由表)

在每个模块的 `module.json5` 注册路由表（名称对应路由表文件即可）

```
  {
    "module" : {
      "routerMap": "$profile:router_map"
    }
  }
```

在路由表对应文件里 `resource/base/profile/router_map.json` 注册页面

```
  {
    "routerMap": [
      {
        "name": "PageOne",
        "pageSourceFile": "src/main/ets/pages/PageOne.ets",
        "buildFunction": "PageOneBuilder",
        "data": {
          "description" : "this is PageOne"
        }
      }
    ]
  }
```

### 子页面获取 Navagation 持有的 pageStack 

官方使用 Provide/Consume 的方式传递，有耦合

参考 <https://blog.csdn.net/wexzmij54187/article/details/142555911>

推荐方式，通过 NavDestination 的 NavDestinationContext 这个上下文获取

```
pathStack: NavPathStack = new NavPathStack()

build() {
  NavDestination() {
    //
  }.onRedy((context) => {
    this.pathStack = context.pathStack
  })
}
```

### 页面切换

页面跳转

```
// 普通跳转，根据页面的 name 去跳转，并可以携带 param
this.pageStack.pushPath({ name: "PageOne", param: "PageOne Param" })
this.pageStack.pushPathByName("PageOne", "PageOne Param")

// 带返回回调的跳转
this.pageStack.pushPathByName('PageOne', "PageOne Param", (popInfo) => {
  console.log('Pop page name is: ' + popInfo.info.name + ', result: ' + JSON.stringify(popInfo.result))
});

// 带错误码的跳转
this.pageStack.pushDestinationByName('PageOne', "PageOne Param")
.catch((error: BusinessError) => {
  console.error(`Push destination failed, error code = ${error.code}, error.message = ${error.message}.`);
}).then(() => {
  console.error('Push destination succeed.');
});
```

页面返回

```
// 返回到上一页
this.pageStack.pop()
// 返回到上一个PageOne页面
this.pageStack.popToName("PageOne")
// 返回到索引为1的页面
this.pageStack.popToIndex(1)
// 返回到根首页（清除栈中所有页面）
this.pageStack.clear()
```

页面替换

```
// 将栈顶页面替换为PageOne
this.pageStack.replacePath({ name: "PageOne", param: "PageOne Param" })
this.pageStack.replacePathByName("PageOne", "PageOne Param")
```

页面删除

```
// 删除栈中name为PageOne的所有页面
this.pageStack.removeByName("PageOne")
// 删除指定索引的页面
this.pageStack.removeByIndexes([1,3,5])
```

参数获取1

```
// 获取栈中所有页面name集合
this.pageStack.getAllPathName()
// 获取索引为1的页面参数
this.pageStack.getParamByIndex(1)
// 获取PageOne页面的参数
this.pageStack.getParamByName("PageOne")
// 获取PageOne页面的索引集合
this.pageStack.getIndexByName("PageOne")
```

参数获取2，可以从 Build 函数获取（相对 方法1 减少了 name 的耦合）

```
@Builder
export function SecondPageBuilder(name: string, param: Object) {
  SecondPage({ name: name, param: param.toString() })
}

@Component
struct SecondPage {
  name: string = ""
  param: string = ""
  
  build () {
    // 
  }
}
```

跳转传参 + 返回数据 示例代码
```
// FirstPage
// 跳转
this.pathStack.pushPathByName("SecondPage", "我是来自 Index 页面的数据", (popInfo) => {
  // 这里处理返回数据
  const result = popInfo.result as Record<string, string>
  if(result) {
    this.result = result.result
    console.info(`++++++++++++${result.result}`)
  }
})

// SecondPage
// 获取
NavDestination() {
  Text("pathStack 获取参数：" + this.pathStack.getParamByName("SecondPage"))
}
.onBackPressed(() => {
  // 返回
  this.pathStack.pop({ result: "我是从 SecondPage 返回的数据" })
  return true // true 拦截返回键
}
```

### 路由拦截

```
this.pageStack.setInterception({
  willShow: (from: NavDestinationContext | "navBar", to: NavDestinationContext | "navBar",
    operation: NavigationOperation, animated: boolean) => {
    if (typeof to === "string") {
      console.log("target page is navigation home page.");
      return;
    }
    // 将跳转到PageTwo的路由重定向到PageOne
    let target: NavDestinationContext = to as NavDestinationContext;
    if (target.pathInfo.name === 'PageTwo') {
      target.pathStack.pop();
      target.pathStack.pushPathByName('PageOne', null);
    }
  }
})
```

## NavDestination 子页面

NavDestination 是 Navigation 子页面的根容器，用于承载子页面的一些特殊属性以及生命周期等

可作为弹窗

NavDestination 设置 mode 为 `NavDestinationMode.DIALOG` 弹窗类型，此时整个 NavDestination 默认透明显示

```
// Dialog NavDestination
@Entry
@Component
 struct Index {
   @Provide('NavPathStack') pageStack: NavPathStack = new NavPathStack()

   @Builder
   PagesMap(name: string) {
     if (name == 'DialogPage') {
       DialogPage()
     }
   }

   build() {
     Navigation(this.pageStack) {
       Button('Push DialogPage')
         .margin(20)
         .width('80%')
         .onClick(() => {
           this.pageStack.pushPathByName('DialogPage', '');
         })
     }
     .mode(NavigationMode.Stack)
     .title('Main')
     .navDestination(this.PagesMap)
   }
 }

 @Component
 export struct DialogPage {
   @Consume('NavPathStack') pageStack: NavPathStack;

   build() {
     NavDestination() {
       Stack({ alignContent: Alignment.Center }) {
         Column() {
           Text("Dialog NavDestination")
             .fontSize(20)
             .margin({ bottom: 100 })
           Button("Close").onClick(() => {
             this.pageStack.pop()
           }).width('30%')
         }
         .justifyContent(FlexAlign.Center)
         .backgroundColor(Color.White)
         .borderRadius(10)
         .height('30%')
         .width('80%')
       }.height("100%").width('100%')
     }
     .backgroundColor('rgba(0,0,0,0.5)')
     .hideTitleBar(true)
     .mode(NavDestinationMode.DIALOG)
   }
 }
```

## 页面转场

略 <https://developer.huawei.com/consumer/cn/doc/harmonyos-guides-V5/arkts-navigation-navigation-V5#页面转场>

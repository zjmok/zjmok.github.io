---
layout: post
tags: Android Compose
---

# Compose 调用系统返回逻辑 重写系统返回事件

```

@Composable
fun SettingsScreen() {
    // BackHandler 重写系统返回事件
    BackHandler {
        toast("backpress")
    }
	
    // 调用系统返回逻辑 onBackPressedDispatcher?.onBackPressed()
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
	Text("我是按钮", modifier = Modifier.clickable {
        onBackPressedDispatcher?.onBackPressed()
    })
}
```

# onBackPressed 过时 Deprecated 的替代 onBackPressedDispatcher

```
val isEnabled = true // 是否使用当前的回调，禁用了才可以调用系统默认的
// 使用 OnBackPressedDispatcher
onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(isEnabled) {
    override fun handleOnBackPressed() {
        // 在这里处理返回按钮事件
        // 如果需要，可以调用 isEnabled 来控制回调是否有效
        // 如果不想处理返回事件，可以调用 remove() 方法
        
    }
})
```

```
onBackPressedDispatcher.addCallback {
    if (webView.canGoBack()) {
        webView.goBack()
    } else {
        isEnabled = false // 是否使用当前的回调，禁用了才可以调用系统默认的
        onBackPressed() // 调用默认的返回操作
    }
}
```

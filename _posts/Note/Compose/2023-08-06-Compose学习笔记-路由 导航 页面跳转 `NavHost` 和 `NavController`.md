---
layout: post
tags: Android Compose
---

# Compose 的 路由 导航 页面跳转 `NavHost` 和 `NavController`

使用 `NavHost` 和 `NavController` 来实现页面跳转、参数传递和返回参数的功能

### 页面跳转带参数

1. **设置 NavHost：**

NavHost 是导航组件的容器，通常在应用的根 Composable 中定义，通常放在 setContent 函数中

```kotlin
@Composable
fun MyApp() {
    val navController = rememberNavController()  // 创建 NavController
    NavHost(navController, startDestination = "home") {
        composable("home") { HomeScreen(navController) }
        composable("detail/{param}") { backStackEntry ->
            val param = backStackEntry.arguments?.getString("param")
            DetailScreen(navController, param)
        }
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp()  // 调用 MyApp，开始导航
        }
    }
}
```

2. **主页：**

```kotlin
@Composable
fun HomeScreen(navController: NavController) {
    Button(onClick = {
        navController.navigate("detail/Hello")
    }) {
        Text("Go to Detail")
    }
}
```

3. **详情页：**

```kotlin
@Composable
fun DetailScreen(navController: NavController, param: String?) {
    Text("Received param: $param")
    Button(onClick = {
        navController.previousBackStackEntry?.savedStateHandle?.set("result", "Result from Detail")
        navController.popBackStack()
    }) {
        Text("Back with Result")
    }
}
```

### 接收返回参数

在主页中，可以通过 `savedStateHandle` 接收返回的参数：

```kotlin
@Composable
fun HomeScreen(navController: NavController) {
    val backStackEntry = navController.getBackStackEntry("home")
//   val backStackEntry = navController.currentBackStackEntry
    val result = backStackEntry.savedStateHandle.getLiveData<String>("result")

    result.observeAsState().value?.let {
        // 处理返回的参数
    }

    Button(onClick = {
        navController.navigate("detail/Hello")
    }) {
        Text("Go to Detail")
    }
}
```

### 关于出栈

`popUpTo` 在导航到新的页面前，从栈顶开始不断弹出页面直到找到指定页面，`inclusive` 是否包括指定页面。如果指定页面不存在，则会把所有页面弹出栈

示例：

```
// 假设栈中存在 `A -> B -> C`
navController.navigate("D") {
    popUpTo("B") { inclusive = true }
}
// 结果是 `A -> D`
```

```
// 假设栈中存在 `A -> B -> C`
navController.navigate("D") {
    popUpTo("B") { inclusive = false }
}
// 结果是 `A -> B -> D`
```

```
// 假设栈中存在 `A -> B -> C`
navController.navigate("D") {
    popUpTo("Z") { inclusive = false }
}
// 结果是 `D`
```

### 其它

关于每个 Screen 需要 参数传递 NavController 的问题

- LocalContext

  1. 定义 LocalContext

  ```
// 创建一个全局的 CompositionLocal 用于 NavController
val LocalNavController = compositionLocalOf<NavController> {
    error("NavController not provided")
}
  ```

  2. 使用 CompositionLocalProvider 给 LocalNavController 提供 navController 实例

  ```
val navController = rememberNavController()
CompositionLocalProvider(LocalNavController provides navController) {
    // 这里可以放置其他 Composable
	NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreen() }
        composable("details") { DetailsScreen() }
    }
}
  ```

  3. 获取 navController

  ```
@Composable
fun HomeScreen() {
    val navController = LocalNavController.current  // 获取 NavController
    Button(onClick = { navController.navigate("details") }) {
        Text("Go to Details")
    }
}
  ```

- 结合 Hilt 和 ViewModel

如果页面状态和 NavController 紧密关联，可以通过 Hilt 注入 ViewModel，在 ViewModel 中管理导航逻辑


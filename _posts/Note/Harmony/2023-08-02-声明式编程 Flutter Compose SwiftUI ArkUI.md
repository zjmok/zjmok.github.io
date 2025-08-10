---
layout: post
tags: Android Flutter Compose
---

# 声明式编程

## 简介

- 命令式
  
  传统模式，通过手动找到控件进行调用

- 响应式

  指界面能自动响应数据的变化。当状态或数据发生变化时，界面会自动更新，不需要手动调用刷新操作。响应式更新是许多声明式框架的特性，但声明式本身不一定都是响应式的

- 声明式

  指的是描述界面应该是什么样子，而不是如何一步步实现。开发者只需要定义最终状态，框架负责自动将界面更新到这一状态。
  Flutter Compose SwiftUI ArkUI React 都是声明式的 UI 框架，这样可以让代码更清晰，开发效率更高

---

## 状态

### Flutter

#### 本地状态管理

对于较小规模的应用或简单组件，可以使用 StatefulWidget 和 State 类来管理本地状态。
将需要被更新的数据放在 State 对象中，并使用 setState() 方法来通知框架进行重建。这种方法适用于局部的、相对简单的状态管理。

#### Provider

##### 依赖

在 pubspec.yaml 文件中添加 provider 依赖项，并运行 flutter pub get 安装依赖包

```yaml
dependencies:
  flutter:
    sdk: flutter
    
  provider: ^5.0.0
```

##### 创建数据模型类

创建 Model 类，并继承 ChangeNotifier，使用 notifyListeners() 方法在状态发生变化时通知相应的侦听器

```dart
import 'package:flutter/foundation.dart';

class CounterModel extends ChangeNotifier {
  int _counter = 0;

  int get counter => _counter;

  void increment() {
    _counter++;
    notifyListeners();
  }
}
```

##### 在顶层使用 Provider

在 MaterialApp 中使用 MultiProvider 将你的数据模型注册为全局可用的状态

```dart
void main() {
  runApp(
    MultiProvider(
      providers: [
        ChangeNotifierProvider<CounterModel>(create: (_) => CounterModel()),
      ],
      child: MyApp(),
    ),
  );
}
```

##### 在组件中使用状态

使用 Provider.of<T>(context) 来获取状态实例，并在 UI 中使用它

```dart
class MyHomePage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    final counter = Provider.of<CounterModel>(context);

    return Scaffold(
      appBar: AppBar(title: Text('Flutter Provider Example')),
      body: Center(
        child: Text(
          'Count: ${counter.counter}',
          style: TextStyle(fontSize: 24),
        ),
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: () => counter.increment(),
        child: Icon(Icons.add),
      ),
    );
  }
}
```

#### BLoC（Business Logic Component）

#### Redux

#### InheritedWidget

### Compose

mutableStateOf、collectAsState、observeAsState 等，将数据转为 Compose 的状态

### ArkUI

- 状态管理 V1 版

@State @Prop @Link @Provide/@Consume @ObjectLink @LocalStorageProp @LocalStorageLink @StorageProp @StorageLink 等注解声明的变量为状态

- 状态管理 V2 版

略，目前还没 release

## 路由

### Flutter

- 命名路由

```dart
// 在 MaterialApp 中定义命名路由
routes: {
  '/': (context) => HomePage(),
  '/details': (context) => DetailsPage(),
}

// 导航到命名路由
Navigator.pushNamed(context, '/details');
```

- 构建函数路由

```dart
// 导航到构建函数路由
Navigator.push(
  context,
  MaterialPageRoute(
    builder: (context) => DetailsPage(),
  ),
);
```

- 返回上一个页面
```dart
// 返回上一个页面
Navigator.pop(context);
```

- 传递数据

```dart
// 通过构造函数传递数据
Navigator.push(
  context,
  MaterialPageRoute(
    builder: (context) => DetailsPage(data: 'Hello'),
  ),
);

// 在接收数据的页面获取数据
class DetailsPage extends StatelessWidget {
  final String data;
  
  DetailsPage({required this.data});

  // ...
}
```

- 返回数据

```dart
// 在当前页面调用 Navigator.pop 方法
Navigator.pop(context, "Hello from second screen");

// 在上一个页面接收 pop 方法返回的数据
var result = await Navigator.push(
  context,
  MaterialPageRoute(builder: (context) => SecondScreen()),
);

// 处理接收到的数据
if (result != null) {
  // 打印接收到的数据
  print(result);
}
```

### Compose

NavHost + NavController，配置

```
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
```

页面跳转

```
navController.navigate("detail/Hello")
```

navController 通过传参获得，或者创建全局的 CompositionLocal

```
// 1. 创建一个全局的 CompositionLocal 用于 NavController
val LocalNavController = compositionLocalOf<NavController> {
    error("NavController not provided")
}

// 2. 使用 CompositionLocalProvider 包裹在外层 并赋值
CompositionLocalProvider(LocalNavController provides navController) {
    NavHost(navController = navController, startDestination = "main") {
        composable("login/{param}") { backStackEntry ->
            val param = backStackEntry.arguments?.getString("param")
            LoginScreen(param = param)
        }
        composable("main") { MainScreen() }
        // ...
    }
}

// 3. 直接通过 LocalNavController.current 获取
@Composable
fun HomeScreen() {
    val navController = LocalNavController.current
    // ...
}
```

### ArkUI

Navigation 导航组件

```
// 主页面

pathStack: NavPathStack = new NavPathStack()

build(){
  Navigation(pathStack) {
    // 布局内容
    
  }
  // ...
}
```

可通过 NavDestination 的 NavDestinationContext 这个上下文获取 NavPathStack

```
// 子页面

pathStack: NavPathStack = new NavPathStack()

build() {
  NavDestination() {
    //
  }.onRedy((context) => {
    this.pathStack = context.pathStack
  })
}
```

跳转传参 + 返回数据

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

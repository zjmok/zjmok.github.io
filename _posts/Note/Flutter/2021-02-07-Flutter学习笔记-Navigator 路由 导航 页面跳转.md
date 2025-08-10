---
layout: post
tags: Flutter
---

# Flutter 路由 导航 页面跳转 Navigator

1. 使用`MaterialPageRoute`进行基本页面跳转：
```dart
Navigator.push(context, MaterialPageRoute(builder: (context) => SecondScreen()));
```
这会将当前页面推入导航器堆栈，并切换到名为`SecondScreen`的新页面。

2. 在目标页面返回时获取结果：
```dart
Navigator.push(context, MaterialPageRoute(builder: (context) => SecondScreen()))
  .then((result) {
    // 在SecondScreen返回时，可以通过result获取返回的数据
    if (result != null) {
      // 处理返回的结果
    }
});
```
在目标页面（`SecondScreen`）中，你可以调用`Navigator.pop(context, result)`来返回结果，其中`result`是你想传递回上一个页面的数据。

3. 使用命名路由进行页面跳转：
首先，在`MaterialApp`的`routes`属性中定义命名路由：
```dart
MaterialApp(
  routes: {
    '/': (context) => HomeScreen(),
    '/second': (context) => SecondScreen(),
  },
);
```
然后，你可以使用命名路由进行跳转：
```dart
Navigator.pushNamed(context, '/second');
```

4. 替换当前页面：
```dart
Navigator.pushReplacement(context, MaterialPageRoute(builder: (context) => NewScreen()));
```
这会将当前页面替换为新的页面。（打开新的页面并关闭当前页面）

5. 返回到上一个页面：
```dart
Navigator.pop(context);
```
这会从导航器堆栈中弹出当前页面，并返回到上一个页面。

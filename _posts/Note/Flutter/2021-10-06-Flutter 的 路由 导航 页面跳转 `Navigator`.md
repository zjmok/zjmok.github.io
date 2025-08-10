---
layout: post
tags: Flutter
---

# Flutter 的 页面跳转 `Navigator`

使用 `Navigator` 来实现页面跳转、参数传递和返回参数的功能

## MaterialPageRoute 方式

### 1. 创建主页面

```dart
import 'package:flutter/material.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Navigation',
      home: HomeScreen(),
    );
  }
}

class HomeScreen extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text('Home')),
      body: Center(
        child: ElevatedButton(
          onPressed: () async {
            // 跳转到 DetailScreen，并传递参数
            final result = await Navigator.push(
              context,
              MaterialPageRoute(
                builder: (context) => DetailScreen(param: 'Hello from Home!'),
              ),
            );

            // 接收返回的参数
            if (result != null) {
              ScaffoldMessenger.of(context).showSnackBar(
                SnackBar(content: Text('Received: $result')),
              );
            }
          },
          child: Text('Go to Detail'),
        ),
      ),
    );
  }
}
```

### 2. 创建详情页面

```dart
class DetailScreen extends StatelessWidget {
  final String param;

  DetailScreen({required this.param});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text('Detail')),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Text('Received param: $param'),
            ElevatedButton(
              onPressed: () {
                // 返回并携带参数
                Navigator.pop(context, 'Result from Detail');
              },
              child: Text('Back with Result'),
            ),
          ],
        ),
      ),
    );
  }
}
```

## 命名路由方式

命名路由是 Flutter 的另一种导航方式，可以使代码更清晰，特别是在大型应用中

### 1. 主页面

```dart
import 'package:flutter/material.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Navigation',
      initialRoute: '/',
      routes: {
        '/': (context) => HomeScreen(),
        '/detail': (context) => DetailScreen(),
      },
    );
  }
}

class HomeScreen extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text('Home')),
      body: Center(
        child: ElevatedButton(
          onPressed: () async {
            // 跳转到 DetailScreen，并传递参数
            final result = await Navigator.pushNamed(
              context,
              '/detail',
              arguments: 'Hello from Home!',
            );

            // 接收返回的参数
            if (result != null) {
              ScaffoldMessenger.of(context).showSnackBar(
                SnackBar(content: Text('Received: $result')),
              );
            }
          },
          child: Text('Go to Detail'),
        ),
      ),
    );
  }
}
```

### 2. 详情页面

```dart
class DetailScreen extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    // 获取传递的参数
    final String param = ModalRoute.of(context)!.settings.arguments as String;

    return Scaffold(
      appBar: AppBar(title: Text('Detail')),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Text('Received param: $param'),
            ElevatedButton(
              onPressed: () {
                // 返回并携带参数
                Navigator.pop(context, 'Result from Detail');
              },
              child: Text('Back with Result'),
            ),
          ],
        ),
      ),
    );
  }
}
```

### 代码解析

1. **命名路由**：
   - 在 `MaterialApp` 中定义 `routes`，将路由名称映射到对应的页面构建函数。
   - 使用 `initialRoute` 设置初始页面。

2. **跳转和传参**：
   - 使用 `Navigator.pushNamed` 进行页面跳转，同时通过 `arguments` 传递参数。

3. **接收参数**：
   - 在 `DetailScreen` 中，通过 `ModalRoute.of(context)!.settings.arguments` 获取传递的参数。

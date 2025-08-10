---
layout: post
tags: Android Flutter
---

# 现有的 Android 项目中集成 Flutter

### 集成 Flutter 模块

#### 源码方式

1. 创建 flutter module

```bash
flutter create -t module --org com.example module_flutter
```

2. 执行 include flutter 脚本

module_flutter 的 .android/Flutter 是依赖的 Lib, 把 Lib 作为源码依赖使用 Lib 参考 .android 的 setting.gradle

module_flutter 的 .android/app 是可以独立运行的 app, 是使用 Lib 的形式

```groovy
setBinding(new Binding([gradle: this]))

def flutterModulePath = rootDir.toString() + "/module_flutter"
def filePath = flutterModulePath + "/.android/include_flutter.groovy"

//apply from: filePath // 报错，参考 flutter module 的 `.android/settings.gradle` 用 evaluate
evaluate(new File(filePath)) // 执行 Flutter 作为 Lib 的脚本 include_flutter
```

3. 依赖

集成时还需要修改 repositoriesMode

```groovy
// settings.gradle
dependencyResolutionManagement {
//    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositoriesMode = RepositoriesMode.PREFER_SETTINGS
    // ...
}
```

添加 maven

```groovy
// settings.gradle
dependencyResolutionManagement {
    // ...
    repositories {
        maven { url "https://storage.flutter-io.cn/download.flutter.io" }
        maven { url "https://storage.googleapis.com/download.flutter.io" }
    }
}
```

添加依赖

```groovy
    implementation project(':flutter')
```

4. 添加变体 profile

flutter 有三种模式: 

- debug		适用于开发阶段，启用调试功能，性能较低
- profile	适用于性能调试，启用部分优化
- release	适用于发布到生产环境，完全优化，性能最佳

```groovy
android{
    buildTypes {
        debug {
            debuggable true
            minifyEnabled false
            shrinkResources false
            signingConfig signingConfigs.config
        }
        profile {
            initWith buildTypes.release
            matchingFallbacks = ['release']
            //
            debuggable false
            minifyEnabled true
            shrinkResources true
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
            signingConfig signingConfigs.config
        }
        release {
            debuggable false
            minifyEnabled true
            shrinkResources true
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
            signingConfig signingConfigs.config
        }
}
```

5. 其它修改

flutter 支持的三种架构

```groovy
android {
    // ...
    defaultConfig {
        ndk {
            // Filter for architectures supported by Flutter
            abiFilters "armeabi-v7a", "arm64-v8a", "x86_64"
        }
    }
}
```

#### aar 方式

- `flutter build aar --release` 执行后会生成 repo 目录的 maven 本地仓库，可以添加 maven 后 使用 implement 依赖方式集成

```groovy
dependencyResolutionManagement {
    repositories {
        maven(url = "https://storage.googleapis.com/download.flutter.io")
        maven(url = "module_flutter/build/host/outputs/repo")
    }
}
```

```groovy
dependencies {
    // ...
    debugImplementation("com.example.module_flutter:flutter_debug:1.0")
    releaseImplementation("com.example.module_flutter:flutter_release:1.0")
    add("profileImplementation", "com.example.module_flutter:flutter_profile:1.0")
}
```

- 或者使用手动导入 aar 文件的普通方式

变体等参考上面的

### 使用 Flutter 的页面

```dart
// Activity 方式
val intent: Intent = FlutterActivity.createDefaultIntent(this)

// Fragment 方式
val flutterFragment: FlutterFragment = FlutterFragment.createDefault()
```

### 与 Flutter 通信

通过 **平台通道**（Platform Channels）在 Flutter 和 Android 原生代码之间传递数据。
Flutter 与 Android 之间可以通过消息通道来进行双向通信。

#### 发送消息 Android --> Flutter

在 Android 中发送消息

```kotlin
val String CHANNEL = "com.example.myapp/channel"
MethodChannel(getFlutterEngine().getDartExecutor(), CHANNEL)
    .invokeMethod("sendData", "Hello from Android")
```

在 Flutter 中接收消息

```dart
class MyFlutterApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    // 定义 MethodChannel
    const platform = MethodChannel('com.example.myapp/channel');

    platform.setMethodCallHandler((MethodCall call) async {
      if (call.method == 'sendData') {
        print("Received data: ${call.arguments}");
      }
    });

    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(title: Text('Flutter in Android')),
        body: Center(child: Text('Hello, Flutter!')),
      ),
    );
  }
}
```

#### 发送消息 Flutter --> Android

在 Flutter 中发送消息（调用 Android 方法）

```dart
// 定义 MethodChannel
static const platform = MethodChannel('com.example.myapp/channel');

// 向 Android 发送消息并获取返回值
Future<void> _sendDataToAndroid() async {
  try {
    final String result = await platform.invokeMethod('getDataFromAndroid');
    print("Received data from Android: $result");
  } on PlatformException catch (e) {
    print("Failed to get data from Android: '${e.message}'.");
  }
}
```

在 Android 端接收 Flutter 消息并处理

```kotlin
class MainActivity: FlutterActivity() {
    private val CHANNEL = "com.example.myapp/channel"

    override fun configureFlutterEngine() {
        super.configureFlutterEngine()

        // 设置 MethodChannel
        MethodChannel(flutterEngine?.dartExecutor, CHANNEL).setMethodCallHandler { call, result ->
            when (call.method) {
                "sendData" -> {
                    // 从 Flutter 收到消息并处理
                    val data = call.arguments as String
                    println("Received from Flutter: $data")
                    result.success("Message received from Android: $data")
                }
                "getDataFromAndroid" -> {
                    // Flutter 请求从 Android 获取数据
                    val dataFromAndroid = "Data from Android"
                    result.success(dataFromAndroid)
                }
                else -> {
                    result.notImplemented()
                }
            }
        }
    }
}
```

## git submodule 管理子模块

假设目前 git 管理了模块代码，切换到使用 git submodule 来管理

1. 移除 module_flutter 的 Git 跟踪（此操作不会删除工作区文件）

```bash
git rm -r --cached module_flutter
```

2. 进入 module_flutter 创建模块的 git 仓库，并提交远端仓库

```bash
cd module_flutter

git init
git add .
git commit -m "New Module"
git remote add origin ssh://admin@192.168.1.211:29418/module_flutter.git
git push -u origin master # 推送到远端
```

3. 把目录文件移除（或移动到别的地方）

4. 在目录不存在的前提下，在主仓库根目录执行添加 git submodule 的命令

```bash
git submodule add ssh://admin@192.168.1.211:29418/module_flutter.git module_flutter
```

会生成 `.submodule` 文件，
git 会把子仓库 module_flutter 整个目录识别成文件，内容是子仓库的 HEAD 的 commitId

提交 `.submodule` 和 `module_flutter` 到远端即可

后续初始化更新子模块

```bash
git submodule update --init
```

之后可以进入 submodule 使用 git 操作

5. 到此基本完成 git submodule 的管理

可直接拉取远端最新代码

可选择恢复原来目录的文件（可以恢复 build 目录等忽略的文件），
`.git` 文件和目录不要动，剪切移动除了 `.git` 外的文件恢复到原来目录

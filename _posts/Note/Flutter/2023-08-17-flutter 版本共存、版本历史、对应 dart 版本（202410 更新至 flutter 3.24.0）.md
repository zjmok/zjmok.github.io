---
layout: post
tags: Flutter
---

# flutter 版本共存、版本历史、对应 dart 版本

## flutter 与 dart sdk 版本 对应关系

 Flutter版本		| 发布日期	| Dart版本	| 新特性
 ---				| ---		| ---		| ---
Flutter 1.0			| 2018.12	|			| First stable release, Android and iOS
Flutter 1.2			| 2019.01	|			| DevTools
Flutter 1.12		| 2019.12	|			| Add to App
Flutter 1.17		| 2020.05	|			| Metal
Flutter 1.20		| 2020.08	|			| 
Flutter 1.22		| 2020.10	| Dart 2.10	| Native platform views
Flutter 2.0			| 2021.03	| Dart 2.12	| Sound Null Safety by default, 正式支持 Web, desktop preview
Flutter 2.2			| 2021.05	| Dart 2.13	| 
Flutter 2.5			| 2021.09	| Dart 2.14	| Null safety 空安全, iOS15 and Android12, Material 3
Flutter 2.8			| 2021.12	| Dart 2.15	| Flame (2D 游戏引擎)
Flutter 2.10		| 2022.02	| Dart 2.16	| 正式支持 Windows
Flutter 3.0			| 2022.05	| Dart 2.17	| 正式支持 Mac and Linux, 改进 Material 3
Flutter 3.3			| 2022.08	| Dart 2.18	| TextInputPlugin, 改进, 改进 Material 3
Flutter 3.7			| 2023.01	| Dart 2.19	| 改进 Material 3, 自定义菜单栏, 级联菜单
**Flutter 3.10**	| 2023.05	| Dart 3.0	| 升级到 **Dart 3.0**, 改进 Material 3, iOS性能优化, 其它改进
Flutter 3.13		| 2023.08	| Dart 3.1	| 折叠屏API支持, 程序生命周期监听, iOS性能优化, 其它改进
Flutter 3.16		| 2023.11	| Dart 3.2	| 默认启用 Material 3
Flutter 3.19		| 2024.02	| Dart 3.3	| Windows Arm64 支持, 停止对 Windows 7 和 8 的支持, 自定义文本选择菜单项
Flutter 3.22		| 2024.05	| Dart 3.4	| 预测返回手势支持, Gradle Kotlin DSL, 最低 Android 版本 api 21
Flutter 3.24		| 2024.07	| Dart 3.5	| 全新 Sliver TreeView, 官方轮播图 CarouselView

## `fvm` - flutter 多版本共存。（类似 rvm 使用 ruby 多版本）

摆脱 手动切换 flutter 仓库，手动变更 环境变量，使用 fvm 可以方便管理使用多版本 flutter

支持 mac windows linux 多系统

github <https://github.com/leoafarias/fvm>

官网 <https://fvm.app/documentation/getting-started/installation>

基本使用 <https://fvm.app/documentation/guides/basic-commands>

下载安装后，把 fvm 配置到 Path 环境变量

- `fvm use {version}` - 进入到 flutter 项目根目录使用，配置指定版本

- `fvm install` - # Installs version found in project config
- `fvm install {version}` - # 安装指定版本

- `fvm remove {version}` - 移除指定版本

- `fvm releases`

- `fvm doctor`

### fvm 的 flutter 使用

直接在签名加上 fvm 就行

例如

```
fvm flutter --version

fvm flutter clean

fvm flutter pub get

fvm flutter build apk
```

### fvm 使用 第三方 flutter

例如，使用 支持鸿蒙的 flutter `https://gitee.com/openharmony-sig/flutter_flutter.git`

直接把 flutter sdk 克隆到 `~/fvm/versions` 目录下（自定义缓存目录可修改环境变量 `FVM_CACHE`）

注意仓库前缀必须是 `custom_`，例如 `custom_harmony`

然后 `fvm list` 可以见到自定义的 sdk，使用 `fvm use custom_harmony` 就可以使用了

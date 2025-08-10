---
layout: post
tags: Mac Linux Windows Dev Ruby Java JavaScript Python
---

### 系统级包管理器（操作系统层）

| 工具名						| 适用平台				| 说明									|
|----						|----					|----									|
| **APT**					| Debian/Ubuntu			| `.deb` 包管理，支持依赖解析和更新		|
| **YUM / DNF**				| RedHat/CentOS			| `.rpm` 包管理，DNF 是 YUM 的升级版		|
| **Pacman**				| Arch Linux			| 简洁高效，滚动更新机制					|
| **Zypper**				| openSUSE				| `.rpm` 包管理，支持仓库和依赖管理		|
| **Homebrew**				| macOS/Linux			| 用户空间包管理器，适合开发工具安装		|
| **Chocolatey**			| Windows				| Windows 下的命令行包管理器				|
| **Winget**				| Windows 10+			| 微软官方包管理器，支持 GUI 应用安装		|

### 平台级包管理器（语言或运行时层）

| 工具名						| 所属平台 / 语言		| 说明									|
|----						|----					|----									|
| **npm / Yarn / pnpm**		| Node.js / JavaScript	| 管理 JS 库和工具，支持项目依赖和脚本		|
| **pip / Conda**			| Python				| pip 是官方工具，Conda 支持环境隔离		|
| **gem / Bundler**			| Ruby					| gem 是基础工具，Bundler 管理项目依赖	|
| **Composer**				| PHP					| 管理 PHP 项目依赖，使用 Packagist 仓库	|
| **Cargo**					| Rust					| Rust 官方包管理器，集成构建功能			|
| **Go Modules**			| Go					| Go 的模块化依赖管理工具					|
| **Maven / Gradle**		| Java					| Java 项目构建与依赖管理工具				|
| **pub / FVM**				| Dart / Flutter		| pub 管理库，FVM 管理 Flutter SDK 版本	|

### 应用级包管理器（项目或框架层）

| 工具名						| 适用场景				| 说明									|
|----						|----					|----									|
| **CocoaPods**				| iOS / MacOS 项目		| 管理 Xcode 项目的第三方库				|
| **SwiftPM**				| Swift 项目				| Apple 官方 Swift 包管理工具				|
| **Poetry**				| Python 项目			| pip 的高级封装，支持虚拟环境和依赖锁		|
| **Angular CLI**			| Angular 前端项目		| 管理 Angular 项目结构和依赖				|
| **Vite / Webpack**		| 前端构建工具			| 管理 JS 模块打包和构建流程				|
| **Helm**					| Kubernetes 应用部署	| 管理 K8s 应用的 Chart 包				|
| **Terraform Registry**	| 基础设施即代码			| 管理 Terraform 模块和依赖				|

---

https://github.com/Homebrew/brew.git  
MacOS 包管理

homebrew-core

homebrew-cask

https://github.com/CocoaPods/CocoaPods.git  
The Cocoa dependency manager
XCode 包管理

https://github.com/CocoaPods/Specs.git  
The CocoaPods Master Repo

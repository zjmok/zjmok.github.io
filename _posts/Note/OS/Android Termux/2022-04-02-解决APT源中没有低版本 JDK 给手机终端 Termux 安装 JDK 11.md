---
layout: post
tags: Android Termux
---

Termux 源默认的 JDK 版本是 JDK-17, 而不是主流的 JDK-8 和 JDK-11

去 Oracle 下载 JDK 的 arm 版本也不能用

---

#### Android 可用的 JDK

然后在网上找资料过程中发现 Jvdroid 是直接可以用的，似乎也还有别的 Android 端的 IDE 工具，但是大多是很多年不更新了，而且 Jvdroid 是带命令终端的，所以网上也有人直接用终端打包 Jvdroid 的 JDK 直接导出到 sdcard 然后导入到 Termux

但是有个问题，这样操作的 JDK 是混有很多别的包，例如 maven busybox

其实，直接解压 Jvdroid 的 apk 可以找到纯净的 JDK

```
/assets/finalpackage_arm64.tar.xz
```

解压出来就是 JDK 了，可以在Android上直接使用，理论上别的 Android 端的 IDE 工具也有 JDK，我们找一个版本更新时间比较近的，而且像这种以 assets 资源存放方式的就可以了

---

#### 操作步骤

1. 下载 apk，[apkpure.com](https://apkpure.com/) 可以下载 Google Play 里 App 的 apk

2. 解压 apk 获取 JDK 包，Windows 可以用 7zip 解压, 类 Unix 直接用 `tar` 命令解压

3. 把 JDK 包放到 Termux，然后设置 Termux 环境变量即可
```
# JDK
JAVA_HOME=~/.app/jdk-11.0.10
export CLASSPATH=.:$JAVA_HOME/lib/tools.jar:$JAVA_HOME/lib/dt.jar:$CLASSPATH
export PATH=$JAVA_HOME/bin:$PATH
# 把 JDK 的 lib 添加到 Linux 动态链接库
export LD_LIBRARY_PATH=$JAVA_HOME/lib:$JAVA_HOME/lib/jli:$LD_LIBRARY_PATH
```

---
layout: post
tags: Java
---

这次编译的版本是 OpenJDK 的 [mobile](https://github.com/openjdk/mobile.git) 项目，也就是运行在手机上的 JDK

编译环境: Windows 10 Pro 21H2 (19044.1586) 的 WLS (Ubuntu-20.04-LTS) 

---

#### 开始干活

1.**下载源码**

```
git clone https://github.com/openjdk/mobile.git openjdk-mobile && cd openjdk-mobile
```

2.**检查环境**

```
./configure
```

根据提示安装编译环境缺失的库，记录下来我安装了以下缺失的库，可以直接执行一步到位
```
sudo apt install -y \
unzip zip libasound2-dev libcups2-dev libfontconfig1-dev libx11-dev libxext-dev libxrender-dev libxrandr-dev libxtst-dev libxt-dev
```

小插曲
```
Your Boot JDK version must be one of: 18 19 
```

根据提示去安装 JDK18 即可，是需要低点的版本的 SDK, 官方称之 Boot JDK

可能是 WSL 环境的问题 网上说用以下命令检查可通过
```
./configure --build=x86_64-unknown-linux-gnu --host=x86_64-unknown-linux-gnu
```

3.**编译**

```
make images
```

小插曲
```
ERROR: Build failed for target 'images' in configuration 'linux-x86_64-server-release' (exit code 2)

No indication of failed target found.
HELP: Try searching the build log for '] Error'.
HELP: Run 'make doctor' to diagnose build problems.

make[1]: *** [/mnt/c/Workspaces/openjdk-mobile/make/Init.gmk:315: main] Error 2
make: *** [/mnt/c/Workspaces/openjdk-mobile/make/Init.gmk:186: images] Error 2
```

bash configure 的时候如果没有加入--disable-warnings-as-errors参数，编译过程出现警告信息会终止编译
```
By default, the JDK has a strict approach where warnings from the compiler is considered errors which fail the build. For very new or very old compiler versions, this can trigger new classes of warnings, which thus fails the build.
Run configure with --disable-warnings-as-errors to turn of this behavior. (The warnings will still show, but not make the build fail.)
```

加上继续

4.**验证成果**

打包 JDK 通过 SCP 或者SFTP 放到手机 在终端 Termux 解压运行
```
./jdk/bin/java -version
```

编译完成

在手机配置 JAVA 环境就可以正常使用了
```
# JDK
JAVA_HOME=~/.app/jdk
export CLASSPATH=.:$JAVA_HOME/lib/tools.jar:$JAVA_HOME/lib/dt.jar:$CLASSPATH
export PATH=$JAVA_HOME/bin:$PATH
# 把 JDK 的 lib 添加到 Linux 动态链接库
export LD_LIBRARY_PATH=$JAVA_HOME/lib:$JAVA_HOME/lib/jli:$LD_LIBRARY_PATH
```

---

[官方文档](https://github.com/openjdk/mobile/blob/master/doc/building.md)

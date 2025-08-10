---
layout: post
tags: 音视频 ffmpeg
---

# ndk 交叉编译 运行在 Android 的 ffmpeg

ffmpeg 版本 `n4.4.5`

ndk 版本 `21.4.7075529` `26.1.10909125` `26.3.11579264` `27.2.12479018` 试过都没问题

## 准备

由于 gcc 从 ndk 中移除，需要在 configure 时使用 --cc --cxx --ld 来分别指定 c/c++编译器和链接器。
找 ndk 的 clang 文件的位置，可以看见谷歌已经为我们写好各个架构和不同android版本的运行脚本。

## 下载 ffmpeg 源码

`git clone https://github.com/FFmpeg/FFmpeg.git` 并切换到需要编译的对应版本 tag 的 commit 上

## 安装软件

[下载 Visual Studio](https://visualstudio.microsoft.com/) 并安装 (记得勾选MSVC)

[下载 msys2](https://github.com/msys2/msys2-installer/releases/) 并安装，打开

这两个软件是为了在 Windows 使用 Linux 环境去编译，即 Windows 原生 + msys2（使用 Windows 的 ndk）

此外还可以通过 WSL 也可以直接使用 Linux 环境（使用 Linux 的 ndk）

## 配置 msys2 环境

执行命令，可合并成一条命令
```
pacman -Syu
pacman -S base-devel
pacman -S yasm nasm gcc
pacman -S mingw-w64-x86_64-toolchain
pacman -S git
pacman -S make
pacman -S automake
pacman -S autoconf
pacman -S perl
pacman -S pkg-config
```

## 避免 msys2的link命令 与 linux的link命令冲突

将 `C:\msys64\usr\bin\link.exe` 文件，重命名为 `link.exe.back`

## 配置和编译 脚本

`build_ffmpeg_by_ndk.sh`

```
#!/bin/bash

set -e

# --arch=? 参数
# arm aarch64 i686 x86_64
ARCH=aarch64
# --cpu=? 参数
# armv7-a armv8-a
CPU=armv8-a
# jniLibs 目录下文件夹的名称
# armeabi-v7a arm64-v8a x86 x86_64
PLATFORM=arm64-v8a
# ndk 最小支持的 api
MIN_API=21

SRC_DIR=/z/Workspaces/FFmpeg
OUTPUT_DIR=/z/Workspaces/FFmpegAndroid/${PLATFORM}

cd ${SRC_DIR}

# NDK 路径，测试了新版需要加参数 `--disable-stripping`
# NDK=/z/AwesomeProgram/Android/sdk/ndk/21.4.7075529
NDK=/z/AwesomeProgram/Android/sdk/ndk/26.1.10909125

# 编译工具前缀
# TOOLS_BIN=/z/AwesomeProgram/Android/sdk/ndk/26.1.10909125/toolchains/llvm/prebuilt/windows-x86_64/bin
TOOLS_BIN=${NDK}/toolchains/llvm/prebuilt/windows-x86_64/bin

# C编译器，`aarch64-linux-android21-clang` 中的 `aarch64` 是对应Android设备的架构， `21` 是最低支持Android版本的API
# NDK_CC=/z/AwesomeProgram/Android/sdk/ndk/26.1.10909125/toolchains/llvm/prebuilt/windows-x86_64/bin/aarch64-linux-android21-clang
NDK_CC=${TOOLS_BIN}/aarch64-linux-android${MIN_API}-clang

# C++编译器
NDK_CXX=${TOOLS_BIN}/aarch64-linux-android${MIN_API}-clang++

# 链接器
NDK_LD=${TOOLS_BIN}/aarch64-linux-android${MIN_API}-clang

# 交叉编译工具前缀，取对应编译器路径 加 文件名前缀 加 `-`，具体查看NDK里的文件结构
CROSS_PREFIX=${TOOLS_BIN}/aarch64-linux-android-

echo ">> configure ..."

./configure \
  --cc=${NDK_CC} --cxx=${NDK_CXX} --ld=${NDK_LD} \
  --target-os=android --arch=${ARCH} --cpu=${CPU} \
  --enable-cross-compile --cross-prefix=${CROSS_PREFIX} \
  --prefix=${OUTPUT_DIR} \
  --enable-jni \
  --disable-static --enable-shared \
  --enable-small \
  --disable-asm \
  --disable-doc \
  --disable-debug \
  --disable-stripping \
  --enable-ffmpeg \
  --enable-ffprobe \
  --disable-ffplay \

echo "<< configure OK"

echo "<< build ..."

make clean

# 根据CPU实际设置线程数，我的 5600G 6核12线程
make -j12 & make install

echo "<< build OK"
```

### ffmpeg 的 `configure` 配置的参数 部分说明

`configure --help` 查看全部

- `--cc` C编译器，
- `--cxx` C++编译器，
- `--ld` 链接器，

- `--enable-cross-compile` 允许交叉编译，
- `--cross-prefix` 交叉编译所使用的工具路径(取对应编译器路径 加 文件名前缀 加 `-`，如 --cross-prefix=${TOOLS_BIN}/aarch64-linux-android-)，

- `--prefix` 编译后的输出目录，

- `--disable-static` 静态 xxx.a，一般禁用
- `--enable-shared` 动态库 共享库 xxx.so，一般启用

- `--disable-doc` 文档相关，不禁止可能报错，

- `--enable-small` 优化大小，时间换空间

- `--disable-programs` 不构建可执行文件

- `--disable-asm` 取消汇编优化

- `--disable-stripping` 禁用可执行文件和共享库的剥离，若不禁用 高版本ndk缺少 `aarch64-linux-android-strip` 时会报错


- `--disable-everything` 禁用所有组件，可以之后单独开启需要的，减少体积

- `--disable-encoders` 禁用所有编码器，可以之后单独开启需要的，减少体积
- `--enable-encoder=NAME` 启用编码器NAME，如mp4

- `--disable-decoders` 禁用所有解码器
- `--enable-decoder=NAME` 启用解码器NAME

- `--disable-muxers` 禁用所有复用器
- `--enable-muxer=NAME` 启用复用器NAME

- `--disable-demuxers` 禁用所有解复用器
- `--enable-demuxer=NAME` 启用解复用器NAME

- `--disable-parsers` 禁用所有解析器
- `--enable-parser=NAME` 启用解析器NAME

- `--disable-bsfs` 禁用所有比特流过滤器
- `--enable-bsf=NAME` 启用比特流过滤器NAME

- `--disable-protocols` 禁用所有协议
- `--enable-protocol=NAME` 启用协议NAME

- `--disable-filters` 禁用所有滤镜
- `--enable-filter=NAME` 启用滤镜NAME

- `--disable-indevs` 禁用所有输入设备
- `--enable-indev=NAME` 启用输入设备NAME

- `--disable-outdevs` 禁用所有输出设备
- `--enable-outdev=NAME` 启用输出设备NAME

- `--disable-devices` 禁用所有设备

更多 [FFmpeg中configure的参数配置解释 https://blog.csdn.net/Mr_Tony/article/details/131052939](https://blog.csdn.net/Mr_Tony/article/details/131052939)

## 脚本执行

1. 先创建脚本，并执行

`msvc_msys2.bat`

```
@echo off
:: 1. 初始化 Visual Studio 环境
call "C:\Program Files\Microsoft Visual Studio\2022\Community\VC\Auxiliary\Build\vcvars64.bat"
:: 2. 启动 MSYS2 Shell (MinGW 64-bit 环境)
C:\msys64\msys2_shell.cmd -mingw64
```

2. 在打开的 msys2 窗口执行前面的 `build_ffmpeg_by_ndk.sh`

```
bash /z/WorkspacesMine/script/build_ffmpeg_by_ndk.sh
```

注意使用对应路径

3. 等待编译完成，输出的 so 文件在脚本的 OUTPUT_DIR 目录的 lib 下

4. 需要定制功能就编辑前面的 `configure` 命令去定制

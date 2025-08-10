---
layout: post
tags: 音视频 ffmpeg
---

# 在 Windows 中（非WSL） 编译 FFmpeg

ffmpeg 版本 n4.4.5

## 编译相关工具介绍

[CMake、Make、MinGw、Clang、Llvm、GCC、MSVC的区别](https://blog.csdn.net/m0_37698164/article/details/127811508)

- MSYS2
Windows 的 Linux 兼容层，相当于桥接提供了 shell 环境

- MinGw
C/C++ 编译器，而且是一套 GNU 工具集合

- MSVC
Visual Studio 的 MSVC 编译器，C 编译器

## 安装软件

[下载 Visual Studio](https://visualstudio.microsoft.com/) 并安装 (记得勾选MSVC)

[下载 msys2](https://github.com/msys2/msys2-installer/releases/) 并安装，打开

## 任意位置新增脚本 `msvc_msys2.bat`

vcvars64.bat 的路径改为自己VS中的对应路径，msys2_shell.cmd 的路径改为自己msys2中的对应路径，这里的作用是在后面的msys2中使用msvc编译器
```
call "C:\Program Files\Microsoft Visual Studio\2022\Community\VC\Auxiliary\Build\vcvars64.bat"
C:\msys64\msys2_shell.cmd -mingw64
```

之后直接双击运行此批处理执行配置和编译命令即可

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

## 修改 C:\msys64\msys2_shell.cmd 文件

取消 rem 注释 将 `rem set MSYS2_PATH_TYPE=inherit` 修改成 `set MSYS2_PATH_TYPE=inherit`

## 避免 msys2的link命令 与 linux的link命令冲突

将 `C:\msys64\usr\bin\link.exe` 文件，重命名为 `link.exe.back`

## 下载 ffmpeg 源码

`git clone https://github.com/FFmpeg/FFmpeg.git` 并切换到需要编译的对应版本 tag 的 commit 上

## 配置

cd 到 ffmpeg 源码目录，执行命令

```
# 配置 静态库 动态库 指定系统和架构 编译工具链 等，--prefix是指定编译后输出的二进制文件路径
./configure --enable-static --enable-shared --disable-debug --target-os=win64 --arch=x86_64 --toolchain=msvc --prefix=/z/Workspaces/FFmpegWindows
```

## 修改 `ffmpeg\config.h` 文件

将 `#define CC_IDENT "用于 x64 的 Microsoft (R) C/C++ 优化编译器 19.38.33133 版"` 的中文删除，否则make编译报错

## 编译
 
```
# 根据CPU实际设置线程数，我的 5600G 6核12线程
make -j12 & make install
```

等待编译成功

`ffmpeg.exe` 有问题的话 使用 [Dependencies](https://github.com/lucasg/Dependencies) 检查 缺了什么依赖

---

参考

[Windows下编译ffmpeg（MinGW+MSYS2）](https://blog.csdn.net/qq_43627907/article/details/127173406)

[在windows 10 使用msys2 + MSVC(VS2017)编译ffmpeg6.0源码 & ffplay播放器移植到 win32工程](https://blog.csdn.net/qq00769539/article/details/130269463)

---
layout: post
tags: 音视频 ffmpeg
---

# Android 使用 编译后的 ffmpeg

## 创建 Native C++ 项目

cpp文件夹下有两个文件，一个是native-lib.cpp文件，一个是CMakeLists.txt文件。CMakeLists.txt文件是cmake脚本配置文件，cmake会根据该脚本文件中的指令去编译相关的C/C++源文件，并将编译后产物生成共享库或静态块，然后Gradle将其打包到APK中。

## Java 调用 native层 c/c++ 代码

在 MainActivity.java，static{}语句加载so库，在类加载中只执行一次。

```
static {
    System.loadLibrary("cxx");
}
```

然后，编写了原生的函数，函数名中要带有native。

```
public native String stringFromJNI();
```

编写相对应的c函数 Java_com_example_test_cxx_MainActivity_stringFromJNI，注意函数名的构成，Java、带包名的完整类名、方法名，用下划线连在一起。

## 引入 FFmpeg so库

- 添加ffmpeg so库

新建 app/src/main/jniLibs/arm64-v8a 目录，并把 FFmpeg 编译得到的所有 so库 放入

arm/x86 32/64 位的目录名 ` armeabi-v7a` `arm64-v8a` `x86` `x86_64`

- 添加 FFmpeg so库 的 头文件

新建 cpp/ffmpeg 目录，把编译后得到的 include 文件夹全部放入

- 配置 CMakeLists.txt

上面已经把 so 和 头文件 放置到对应的目录中了，但是编译器是不会把它们编译、链接、并打包到 Apk 中的，我们还需要在 CMakeLists.txt 中显性的把相关的 so 添加和链接起来

```
cmake_minimum_required(VERSION 3.10.2)

# 支持gnu++11
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=gnu++11")

# 1. 定义so库和头文件所在目录，方面后面使用
set(ffmpeg_lib_dir ${CMAKE_SOURCE_DIR}/../jniLibs/${ANDROID_ABI})
set(ffmpeg_head_dir ${CMAKE_SOURCE_DIR})

# 2. 添加头文件目录
include_directories(${ffmpeg_head_dir}/include)

# 3. 添加ffmpeg相关的so库
add_library( avutil
        SHARED
        IMPORTED )
set_target_properties( avutil
        PROPERTIES IMPORTED_LOCATION
        ${ffmpeg_lib_dir}/libavutil.so )

add_library( swresample
        SHARED
        IMPORTED )
set_target_properties( swresample
        PROPERTIES IMPORTED_LOCATION
        ${ffmpeg_lib_dir}/libswresample.so )

add_library( avcodec
        SHARED
        IMPORTED )
set_target_properties( avcodec
        PROPERTIES IMPORTED_LOCATION
        ${ffmpeg_lib_dir}/libavcodec.so )

add_library( avfilter
        SHARED
        IMPORTED)
set_target_properties( avfilter
        PROPERTIES IMPORTED_LOCATION
        ${ffmpeg_lib_dir}/libavfilter.so )

add_library( swscale
        SHARED
        IMPORTED)
set_target_properties( swscale
        PROPERTIES IMPORTED_LOCATION
        ${ffmpeg_lib_dir}/libswscale.so )

add_library( avformat
        SHARED
        IMPORTED)
set_target_properties( avformat
        PROPERTIES IMPORTED_LOCATION
        ${ffmpeg_lib_dir}/libavformat.so )

add_library( avdevice
        SHARED
        IMPORTED)
set_target_properties( avdevice
        PROPERTIES IMPORTED_LOCATION
        ${ffmpeg_lib_dir}/libavdevice.so )

# 查找代码中使用到的系统库
find_library( # Sets the name of the path variable.
        log-lib

        # Specifies the name of the NDK library that
        # you want CMake to locate.
        log )

# 配置目标so库编译信息
add_library( # Sets the name of the library.
        native-lib

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        native-lib.cpp
        )

# 指定编译目标库时，cmake要链接的库
target_link_libraries(

        # 指定目标库，native-lib 是在上面 add_library 中配置的目标库
        native-lib

        # 4. 连接 FFmpeg 相关的库
        avutil
        swresample
        avcodec
        avfilter
        swscale
        avformat
        avdevice

        # Links the target library to the log library
        # included in the NDK.
        ${log-lib} )
```

## 验证使用 FFmpeg

要检查 FFmpeg 是否可以使用，可以通过获取 FFmpeg 基础信息来验证。

- 在 native-lib.cpp 中添加对应的 JNI 层方法。

```
#include <jni.h>
#include <string>
#include <unistd.h>

extern "C" {
    #include <libavcodec/avcodec.h>
    #include <libavformat/avformat.h>
    #include <libavfilter/avfilter.h>
    #include <libavcodec/jni.h>

    JNIEXPORT jstring JNICALL
    Java_com_example_test_cxx_MainActivity_ffmpegInfo(JNIEnv *env, jobject  /* this */) {

        char info[40000] = {0};
        AVCodec *c_temp = av_codec_next(NULL);
        while (c_temp != NULL) {
            if (c_temp->decode != NULL) {
                sprintf(info, "%sdecode:", info);
            } else {
                sprintf(info, "%sencode:", info);
            }
            switch (c_temp->type) {
                case AVMEDIA_TYPE_VIDEO:
                    sprintf(info, "%s(video):", info);
                    break;
                case AVMEDIA_TYPE_AUDIO:
                    sprintf(info, "%s(audio):", info);
                    break;
                default:
                    sprintf(info, "%s(other):", info);
                    break;
            }
            sprintf(info, "%s[%s]\n", info, c_temp->name);
            c_temp = c_temp->next;
        }
        
        return env->NewStringUTF(info);
    }
}
```

首先，我们看到代码被包裹在 extern "C" { } 当中，和前面的系统创建的稍微有些不同，通过这个大括号包裹，我们就不需要每个方法都添加单独的 extern "C" 开头了。 
另外，由于 FFmpeg 是使用 C 语言编写的，所在 C++ 文件中引用 #include 的时候，也需要包裹在 extern "C" { }，才能正确的编译。

- 在 MainActivity 中添加一个外部方法 ffmpegInfo

```
public native String ffmpegInfo();
```

- 如果一切正常，App运行后，就会显示出 FFmpeg 音视频编解码器的信息

done.

---

参考

[android使用ffmpeg.so](https://blog.csdn.net/minping9101/article/details/131804184)

[Android FFmpeg 编译和集成](https://cloud.tencent.com/developer/article/1773965)

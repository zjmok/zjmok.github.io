---
layout: post
tags: Android
---

Android 11+ 版本 无需数据线 支持直接在IDE和手机操作（Android 11+ 可通过 ADB over Network 功能来远程执行 adb 命令）

以下是通用方法

# 手机端

## 无root设备 （缺点：需要先用数据线连接）

利用**数据线连接电脑**，打开adb默认端口监听
```
adb tcpip 5555
```

开启后就可以拔掉数据线用电脑连接进行无线调试了

## 有root设备

1. 有root权限 可以利用调试APP直接在手机端打开端口监听

2. 或者在手机端执行命令，利用超级终端（Terminal Emulator）
```
# root权限执行
su
# 设置系统属性 adb的tcp远程端口为5555
setprop service.adb.tcp.prot 5555
# 重启adb服务
stop adbd
start adbd
```

3. 或者写个APP，通过socket连接127.0.0.1:5555，判断设备adb的默认端口5555是否开启
```
int port = 5555;
Runtime.getRuntime().exec("su");
Runtime.getRuntime().exec("setprop service.adb.tcp.port " + port);
Runtime.getRuntime().exec("stop adbd");
Runtime.getRuntime().exec("start adbd");
```

4. 甚至可以将prop属性写到系统里，重启也能自动开启

    - 前提是root
    
    - 解锁system
    
    ```
    adb root
    adb disable-verity
    ```
    
    - 修改属性文件 `/default.prop` 或 `/system/build.prop` 增加属性
    
    ```
    setprop service.adb.tcp.prot 5555
    ```
    
    - system分区只读处理
    
    ```
    # 可读写
    mount -o rw,remount /system
    # 只读
    mount -o ro,remount /system
    ```

# 电脑端
```
# 连接
adb connect 192.168.31.211:5555

# 查看
adb devices
```

# 遇到的问题

目前遇到过 Mi 10 Ultra 偶尔连接不上的情况，

解决办法：

1. `adb devices` 查看，目标设备可能处于已连接，但显示的不是 device 而是 offline 状态

2. `adb disconnect 设备IP` 断开连接（设备从 `adb devices` 的执行结果里看）

3. 进入开发者设置，撤销USB调试授权，重新打开 `adb 调试`

4. 数据线连接，执行 `adb -s 设备 tcpip 5555` 打开adb连接的默认端口监听，拔掉数据线

5. `adb connect 设备IP` + 手机端授权，重新连接

6. 再次查看能否成功进行调试安装等操作


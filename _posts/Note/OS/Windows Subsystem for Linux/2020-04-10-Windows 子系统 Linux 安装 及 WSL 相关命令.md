---
layout: post
tags: WSL
---

- 开启Windows开发者选项  
设置 -> 更新和安全 -> 开发者选项 -> 开发人员模式

- 打开子系统功能  
应用和功能 -> 程序和功能 -> 启用或关闭Windows功能 -> 适用于Linux的Windows子系统

- 下载  
`curl.exe -L -o ubuntu-1804.appx https://aka.ms/wsl-ubuntu-1804`

- 解压  
`Add-AppxPackage .\app_name.appx`  
或用7zip直接解压

- 安装  
在windows启动栏找到ubuntu打开安装  
如果用7zip解压的话找到解压的`ubuntu.exe`文件直接管理员运行

---

重启linux

`services.msc` -> 重启`LxssManager`服务

---

直接执行命令`wslconfig`查看命令使用

参数
```
/l 列出
/s 默认
/t 终止
/u 取消
/upgrade 升级至WslFs文件系统格式
```

如查看已安装的子系统
```
wslconfig /l
```

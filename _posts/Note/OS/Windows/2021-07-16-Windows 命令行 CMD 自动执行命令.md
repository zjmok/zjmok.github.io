---
layout: post
tags: Windows
---

### CMD

打开注册表
```
计算机\HKEY_LOCAL_MACHINE\SOFTWARE\Microsoft\Command Processor
```

添加字符串值
```
key=AutoRun
value=D:\cmd_auto_run.bat
```

在对应路径新建bat脚本
`D:\cmd_auto_run.bat`
```
@echo off

doskey ls=dir $*
doskey gbk=chcp 936
doskey utf8=chcp 65001
doskey ip=ipconfig
doskey myip=curl myip.ipip.net

echo 你好，靓仔！
```

打开CMD就能见到效果了
```
Microsoft Windows [版本 10.0.19041.153]
(c) 2020 Microsoft Corporation. 保留所有权利。
你好，靓仔！

C:\Users\mok>
```

### POWER SHELL

使用Powershell的配置文件来实现

执行 `echo $PROFILE` 查看 Powershell 的 profile 文件位置，类似 Linux 的`.bashrc`
```
PS C:\Users\mok> echo $PROFILE
C:\Users\mok\Documents\WindowsPowerShell\Microsoft.PowerShell_profile.ps1
```

修改文件 `Microsoft.PowerShell_profile.ps1` ，没有则创建，参考 `function 别名 { 需要替代的命令 }`
```
function gbk { [System.Console]::OutputEncoding=[System.Text.Encoding]::GetEncoding(936) }
function utf8 { [System.Console]::OutputEncoding=[System.Text.Encoding]::GetEncoding(65001) }
function ip { ipconfig }
function myip { curl myip.ipip.net }

echo 你好，靓仔！
echo `t
```

设置允许 Powershell 执行脚本，输入命令
```
Set-ExecutionPolicy RemoteSigned
```

#### powershell-脚本运行权限政策

获取当前策略：`Get-ExecutionPolicy`

设置当前策略：`Set-ExecutionPolicy Unrestricted`

- `Restricted` —— 默认的设置， 不允许任何script运行
- `AllSigned` —— 只能运行经过数字证书签名的script
- `RemoteSigned` —— 运行本地的script不需要数字签名，但是运行从网络上下载的script就必须要有数字签名
- `Unrestricted` —— 允许所有的script运行

---

参考

<https://blog.csdn.net/lei_qi/article/details/106592404>

<https://www.zhihu.com/question/54724102/answer/140852198>

<https://www.cnblogs.com/spring1997/p/13629850.html>

---
layout: post
tags: Windows
---

在CMD中使用一下命令配置好环境只在当前会话有效  
例如设置编码为UTF-8
```
chcp 65001
```
但是下次启动新的CMD窗口会话时就失效了


如何使CMD终端配置全局有效？

### Windows配置CMD终端全局环境

可以在注册表里设置一个打开CMD终端时自动执行的脚本，就能实现伪全局的配置了  
在 `HKEY_CURRENT_USER\Software\Microsoft\Command Processor` 路径下新建字符串值，
key为 `AutoRun`，value为脚本路径，例如 `C:\cmd\cmd_init.cmd`

脚本内容填写需要执行的命令，文件名随意但要对应
```
doskey ls=dir $*
doskey gbk=chcp 936
doskey utf8=chcp 65001
doskey myip=curl myip.ipip.net

chcp 65001
```

---

### 使用命令设置系统环境变量

永久有效(设置系统环境变量需要`以管理员身份运行`CMD，不带`/M`为设置用户变量)
```
setx /M http_proxy socks5://127.0.0.1:7890/
setx /M https_proxy socks5://127.0.0.1:7890/
setx /M all_proxy socks5://127.0.0.1:7890/
```

效果跟在GUI设置一样

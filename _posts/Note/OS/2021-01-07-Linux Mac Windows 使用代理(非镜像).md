---
layout: post
tags: WSL Linux Windows Mac
---

检查ip的地理地址
```
curl myip.ipip.net
```

Windows 设置 CMD 为 UTF-8 编码 (测试只在当前会话有效)
```
chcp 65001
```

---

Linux/Mac代理

在 `~/.bashrc` 添加
```
export http_proxy="socks5://127.0.0.1:7890"
export https_proxy="socks5://127.0.0.1:7890"
# all_proxy
export all_proxy="socks5://127.0.0.1:7890"
```
代理地址替换成对应地址，协议://IP:端口

完成后source一下
```
source ~/.bashrc
```

---

Windows CMD 使用代理
当前会话有效
```
set http_proxy=socks5://127.0.0.1:7890/
set https_proxy=socks5://127.0.0.1:7890/
set all_proxy=socks5://127.0.0.1:7890/
```
永久有效(设置系统环境变量需要`以管理员身份运行`CMD，不带`/M`为设置用户变量)
```
setx /M http_proxy socks5://127.0.0.1:7890/
setx /M https_proxy socks5://127.0.0.1:7890/
setx /M all_proxy socks5://127.0.0.1:7890/
```

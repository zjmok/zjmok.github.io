---
layout: post
tags: WSL
---

Ubuntu版本升级（例如 LTS2004->LTS2204）

查看发行版本
```
lsb_release -a
```

查看版本更新设置
```
vim /etc/update-manager/release-upgrades
```

文件内容包含如下内容则是检测更新 Ubuntu LTS 版本
```
Prompt=lts
```

Ubuntu版本更新命令
```
sudo do-release-upgrade
```

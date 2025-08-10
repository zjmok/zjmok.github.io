---
layout: post
tags: Linux Termux
---

### Termux 关联设备的sdcard

```
termux-setup-storage
```

执行后可访问 `/sdcard`

会挂载在 `~/storage/shared` 同时 `~/storage/` 下会挂载其它媒体目录

### AnLinux 关联设备的sdcard

先关联termux的`~/storage`

把 `~/start-kali.sh` 启动脚本的 sdcard对应行的注释去掉, 重新进入 AnLinux 的系统即可访问 `/sdcard`

---

#### 要处理的问题

- 连接Termux的SSH
```
#安装OpenSSH (Termux定制的OpenSSH 应该说这里的所有包都是定制的)
pkg install openssh

#运行SSH Server
sshd

#设置密码
passwd 

#获得Android IP
ifconfig

#客户端运行 这里用用户名密码登录 不需要配置RSA公钥
ssh android_ip -p 8022
```

安装 SSH 后在电脑使用 SFTP 工具连接手机更方便

---

- Kali 执行 `apt update` 时 报错 `the repository is not signed`

方法1, 允许过期证书

try run it
```
sudo apt update --allow-unauthenticated
```

if that doesn’t work, try run it
```
sudo apt update --allow-insecure-repositories
```

方法2, 更新证书(kali是更新了证书到证书机构的, 但是已发布的发行版Kali是旧版的证书, 可能已经过期了)
```
具体更新方法有以下几种方法：（任选一种即可）
1、
# apt-key adv --keyserver keys.gnupg.net --recv-keys ED444FF07D8D0BF6
2、
# wget -q -O - archive.kali.org/archive-key.asc | apt-key add
3、
# gpg --keyserver hkp://pgpkeys.mit.edu --recv-key ED444FF07D8D0BF6
# gpg -a --export ED444FF07D8D0BF6 | sudo apt-key add -
4、
# wget https://http.kali.org/kali/pool/ ... ring_2018.1_all.deb
# apt install ./kali-archive-keyring_2018.1_all.deb
```

---

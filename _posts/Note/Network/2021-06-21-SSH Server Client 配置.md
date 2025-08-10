---
layout: post
tags: SSH Network
---

### ssh client 配置

- 私钥

将 `id_rsa` 置于 `~/.ssh/` 目录下即可

### ssh server 配置

- 公钥

将 `id_ras.pub` 里的公钥写入到 `~/.ssh/authorized_keys` 文件里 此文件必须每行只包含一个公钥 此文件出于安全考虑必须是 600 权限


- 配置文件

注意配置文件 `/etc/ssh/sshd_config` 里面是否 include 引用 `sshd_config.d` 目录下的 `*.conf`
```
Include /etc/ssh/sshd_config.d/*.conf
```
没有此行代码则手动添加即可

将 ssh 配置文件 `*.conf` 置于 ssh 的配置目录: `/etc/ssh/sshd_config.d/` (ssh是否在etc目录取决于安装路径 下同)

可自定义配置端口号和登录权限等 (测试 CentOS 无法使用 Include, 直接把配置添加在 sshd_config 文件里即可)
```
Port 8022
PubkeyAuthentication yes
PasswordAuthentication yes
PermitRootLogin yes
```

- ssh server 状态 开启 重启 关闭 的命令
```
/etc/init.d/ssh status
/etc/init.d/ssh start
/etc/init.d/ssh restart
/etc/init.d/ssh stop
```

centOS
```
systemctl status sshd.service
systemctl start sshd.service
systemctl restart sshd.service
systemctl stop sshd.service
```

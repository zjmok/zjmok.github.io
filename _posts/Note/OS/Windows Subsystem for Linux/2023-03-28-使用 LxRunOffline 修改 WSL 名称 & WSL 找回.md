---
layout: post
tags: WSL
---

# 使用 LxRunOffline 修改 WSL 名称

查看 WSL 名称
```
wsl -l
```

查看 WSL 安装目录
```
lxrunoffline di -n <WSL名称>
```

导出指定的 WSL 配置文件到目标路径
```
lxrunoffline ec -n <WSL名称> -f <配置文件路径>.xml
```

配置信息可以输入 `lxrunoffline sm -n <WSL名称>` 查看

取消注册（这个操作不会删除目录）
```
lxrunoffline ur -n <WSL名称>
```

使用新名称注册
```
lxrunoffline rg -n <WSL名称> -d <WSL路径> -c <配置文件路径>.xml
```

---

# 使用 LxRunOffline 找回 WSL

WSL 组成关键: 系统文件 + 配置信息

如果主机出问题了不能打开子系统，而又还没有导出备份或不能导出备份，
可以利用 `LxRunOffline` 在新系统上重新注册 WSL，总之就是只有一份系统文件的情况（WSL1的`rootfs`，WSL2的`ext4.vhdx`），
只需要用文件管理器或磁盘工具复制一份 WSL 的系统文件，在加上手写一份配置文件，重新注册即可

配置文件示例: `Ubuntu_LTS_2.xml`
```
<config>
    <envs>
        <env>HOSTTYPE=x86_64</env>
        <env>LANG=en_US.UTF-8</env>
        <env>PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin:/usr/games:/usr/local/games</env>
        <env>TERM=xterm-256color</env>
    </envs>
    <uid>0</uid>
    <kernel-cmd>BOOT_IMAGE=/kernel init=/init ro</kernel-cmd>
    <flags>15</flags>
</config>
```

重新注册示例
```
lxrunoffline rg -n Ubuntu_LTS_2 -d D:\WSL\Ubuntu_LTS_2 D:\WSL\Ubuntu_LTS_2.xml
```

---

# WSL TO GO

可以用同一份系统文件注册，即一个WSL1的`rootfs`或WSL2的`ext4.vhdx`可以注册多个WSL，
甚至可以放在移动硬盘里做成 "WSL TO GO"，然后写一个通用注册脚本，插上移动硬盘就可以在安装有WSL的电脑上通用了

需要注意，可能需要给整个系统文件添加Users用户组的完全控制权限，给父目录添加 Users完全控制 然后勾选替换子对象即可


---
layout: post
tags: Linux SSH
---

# ssh-agent

`ssh-agent` 是一个用于管理 SSH 密钥的程序，它可以帮助你在使用 SSH 连接时更加方便和安全。主要功能包括：

1. **密钥管理：** `ssh-agent` 可以将你的 SSH 私钥加载到内存中，而不是每次 SSH 连接时都要求你输入密码。这样一来，你只需在每个登录会话中输入一次密码，`ssh-agent` 就会在内存中持有你的密钥，供后续 SSH 连接使用。

2. **无需重复输入密码：** 通过 `ssh-agent`，你不必每次进行 SSH 连接时都输入密码，因为私钥已经被 `ssh-agent` 缓存，自动用于身份验证。

3. **安全性增强：** 通过使用 `ssh-agent`，你可以在一定程度上提高安全性。私钥不会明文存储在磁盘上，而是存储在内存中，并且 `ssh-agent` 可以帮助你限制密钥的使用范围，使其仅在特定的会话中有效。

4. **生命周期管理：** `ssh-agent` 在后台运行，并可以在系统启动时启动。你可以在需要时启动、停止或重新启动它。这样你就能够更灵活地管理你的密钥。

> 由于是添加到缓存的，所以每次重启都要重新添加

### 使用 `ssh-agent` 的基本步骤：

1. 启动 `ssh-agent`：

    ```bash
    eval "$(ssh-agent -s)"
    ```

    这会输出一些环境变量，并启动 `ssh-agent`。

    ```
    # 停止
    ssh-agent -k
    ```

    或者使用 `kill` 命令手动终止相关进程。

2. 添加 SSH 私钥到 `ssh-agent`：

    ```bash
    ssh-add ~/.ssh/id_rsa
    ```

    这会将指定的私钥加载到 `ssh-agent` 中。

    ```
    # 查看已添加的密钥的公钥
    ssh-add -L
    # 查看已添加的密钥的指纹
    ssh-add -l

    # 删除全部密钥
    ssh-add -D
    # 删除特定私钥
	ssh-add -d ~/.ssh/id_rsa
    ```

3. 使用 SSH 连接，`ssh-agent` 将会自动提供密钥进行身份验证。

使用 `ssh-agent` 可以极大地简化 SSH 密钥的管理，并提高安全性和便利性。

### expect

使用 expect 脚本 执行某条命令后并自动输入密码

安装 expect

```
apt install expect
```

新增脚本 `vim ~/.ssh/expect_ssh-add.exp`

```
#!/usr/bin/expect

set key_path "/root/.ssh/id_rsa" # 替换为你的私钥的全路径
set passphrase "password" # 替换为你的密码

spawn ssh-add $key_path
expect "Enter passphrase"
send "$passphrase\r"
expect eof
```

> expect 脚本里需要使用绝对全路径，不能使用 `~/.ssh/id_rsa`

> 查看全路径 `ll ~/.ssh/id_rsa`

### 添加到环境变量

`vim ~/.bashrc`

增加以下内容

```
# ssh-agent
# 启动密钥管理工具
eval "$(ssh-agent -s)"
# 执行添加密钥的脚本
~/.ssh/expect_ssh-add.exp
```

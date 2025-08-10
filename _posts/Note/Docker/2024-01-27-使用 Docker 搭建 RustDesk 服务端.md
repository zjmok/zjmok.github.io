---
layout: post
tags: Docker RustDesk
---

# 利用 docker 搭建 rustdesk 服务端

RustDesk - 开源安全可控的远程桌面软件，可替代 TeamViewer、向日葵、AnyDesk、ToDesk

客户端 <https://github.com/rustdesk/rustdesk.git>

服务端 <https://github.com/rustdesk/rustdesk-server.git>

## 前提条件

1. 安装好 Docker 和 docker-compose

2. 拉取 Docker 镜像需要科学上网

## 创建工作目录

```
mkdir ~/.app/rustdesk-server
mkdir ~/.app/rustdesk-server/hbbs
mkdir ~/.app/rustdesk-server/hbbr

cd ~/.app/rustdesk-server
```

以上目录在后面会挂载到对应服务的数据目录，可以修改为自己的目录

hbbs 对应 ID注册服务器，hbbr 对应 中继服务器

## 配置脚本

`vim docker-compose.yml`

```
version: '3'

networks:
  rustdesk-net:
    external: false

services:
  hbbs:
    container_name: rustdesk-hbbs
    ports:
      - 21115:21115
      - 21116:21116 # 自定义 hbbs 映射端口
      - 21116:21116/udp  # 自定义 hbbs 映射端口
    image: rustdesk/rustdesk-server:latest
    # 请把 172.20.215.67:21117 修改为自己的 域名或IP:端口
    # 如果您禁止没有key的用户建立非加密连接，请在运行hbbs和hbbr的时候添加 `-k _` 参数
    command: hbbs -r 172.20.215.67:21117
    volumes:
      - ./hbbs:/root # 自定义挂载目录
    networks:
      - rustdesk-net
    depends_on:
      - hbbr
    restart: unless-stopped
    deploy:
      resources:
        limits:
          memory: 64M

  hbbr:
    container_name: rustdesk-hbbr
    ports:
      - 21117:21117
    image: rustdesk/rustdesk-server:latest
    command: hbbr
    volumes:
      - ./hbbr:/root # 自定义挂载目录
    networks:
      - rustdesk-net
    restart: unless-stopped
    deploy:
      resources:
        limits:
          memory: 64M
```

## 执行脚本

启动服务

```
docker-compose up -d
```

以下是其它命令，以后需要的时候可以参考

```
# 上线（创建）容器，`-d` 是后台运行
docker-compose up -d

# 下线（删除）容器
# 删除容器不会删除挂载到docker容器外目录的数据
docker-compose down

# 启动容器
docker-compose start

# 停止容器
docker-compose stop

# 重启容器
docker-compose restart
```

## 查看 key （密钥/公钥）

```
.
├── docker-compose.yml
├── hbbr
│   ├── id_ed25519
│   └── id_ed25519.pub
└── hbbs
    ├── db_v2.sqlite3
    ├── db_v2.sqlite3-shm
    ├── db_v2.sqlite3-wal
    ├── id_ed25519
    └── id_ed25519.pub
```

手动 copy 密钥

```
cp ./hbbs/id_ed25519.pub ./hbbr/
cp ./hbbs/id_ed25519 ./hbbr/
```

目前版本 key 是强制的，但是不用你自己设置。hbbs在第一次运行时，会自动产生一对加密私钥和公钥（分别位于运行目录下的id_ed25519和id_ed25519.pub文件中），其主要用途是为了通讯加密

如果客户端没有填写 Key (公钥文件 `id_ed25519.pub` 中的内容)，不影响连接，但是连接无法加密

如果要更改key，请删除 `id_ed25519` 和 `id_ed25519.pub` 文件并重新启动 hbbs/hbbr，hbbs将会产生新的密钥对

Key 就是公钥的文本内容

```
cat ./id_ed25519.pub
```

## 客户端配置

默认填写 ID注册服务器 和 Key 即可

我在 Windows 的 WSL2 运行 Docker

Windows 的 IP 是 `192.168.31.202`，WSL2 的 IP 是 `172.20.215.67` (这个 IP 可以在 Windows 访问，但不能在其它实体设备访问，所以使用前面的 IP)

在外网服务器上应该使用服务器本身的 `域名或IP:21116`

```
ID注册服务器：
192.168.31.202:21116

Key：
egofhgpGdFs67swO46dDiTIFDLFmhdcC+CNFWSpaPMI=
```

这里 Key 就是 `cat ./id_ed25519.pub` 的文本

另外两个地址可以不填，RustDesk会自动推导（如果没有特别设定），中继服务器指的是hbbr（21117）端口。

填完即可使用客户端（主控/被控）

## 端口说明

组件 | 功能 | 使用的端口
- | - | -
hbbs | RustDesk ID 注册服务器 | 21115(tcp)：用作 NAT 类型测试 <br/> 21116(udp)：用作 ID 注册与心跳服务 <br/> 21116(tcp)：用作 TCP 打洞与连接服务
hbbr | RustDesk 中继服务器 | 21117(tcp)：用作中继服务

---

## 参考

官方文档

<https://rustdesk.com/docs/zh-cn/self-host/rustdesk-server-oss/install/>

第三方文档

<https://www.hash070.top/archives/rustdesk-docker.html>

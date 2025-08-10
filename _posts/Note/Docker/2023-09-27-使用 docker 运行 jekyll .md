---
layout: post
tags: jekyll Docker
---

先安装 Docker 并运行

## 方式一 从原镜像创建容器 初始项目环境并运行

我自己的 jekyll 项目是 基于版本 3.8 的，拉取对应版本镜像

Linux/Mac 当前目录 `$(pwd)`, (Windows 的 WLS2 权限问题未找到解决办法)

`$(pwd)` 目录包含空格时，可用引号包住 `"$(pwd)"`

```
docker run \
--name jekyll \
-p 4000:4000 \
-v $(pwd):/srv/jekyll \
-it \
jekyll/jekyll:3.8 \
sh -c "bundle install; jekyll serve --watch --host 0.0.0.0"
```

Windows 当前目录 `%cd%`

```
docker run ^
--name jekyll ^
-p 4000:4000 ^
-v %cd%:/srv/jekyll ^
-it ^
jekyll/jekyll:3.8 ^
sh -c "bundle install; jekyll serve --watch --host 0.0.0.0"
```

## 方式二 构建带项目环境的镜像 再创建容器运行

1. 在 Jekyll 项目根目录下创建一个 Dockerfile 文件

```
FROM jekyll/jekyll:3.8

# 拷贝项目文件到容器内
COPY ./Gemfile /srv/jekyll

# 设置工作目录
WORKDIR /srv/jekyll

# 安装依赖
RUN bundle install

# 启动 Jekyll 服务器
CMD ["jekyll", "serve", "--watch", "--host", "0.0.0.0"]
```

2. 构建 Docker 镜像

```
docker build -t jekyll-image .
```

3. 运行 Jekyll 容器

```
docker run \
--name jekyll-container \
-p 4000:4000 \
-v $(pwd):/srv/jekyll \
jekyll-image
```

Linux/Mac 当前目录 `$(pwd)`, Windows 当前目录 `%cd%`

## 注意事项

若 提示无权限 或提示 `jekyll 3.8.7 | Error:  Permission denied @ dir_s_mkdir - /srv/jekyll/_site`

Linux 可能会遇到此权限问题, WSL2 若使用挂载 Windows 的目录 目前未找到解决办法

完整 Linux 系统可以尝试执行以下命令, 不出意外的话问题就解决了

```
chmod -R 777 /path/to/your/project
```

> 缺点：无法热更新，在 wsl1 运行的 可以热更新

## 后台运行

1. run 时带 `-d` , 但当前会话无法显示 log

2. 若 run 时没有带 `-d` 参数，`ctrl+c` 停止正在执行的容器再重新运行容器即可 `docker start jekyll-container`

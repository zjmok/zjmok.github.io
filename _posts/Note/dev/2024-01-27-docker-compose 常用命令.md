---
layout: post
tags: Docker
---

# docker-compose 常用命令

Docker Compose 是一个用于定义和运行多容器 Docker 应用程序的工具。

它使用一个 `docker-compose.yml` 或 `docker-compose.yaml` 文件来配置服务、网络和卷等方面的参数。

以下是一些常用的 Docker Compose 命令：

1. **启动服务：**
   
   ```bash
   docker-compose up
   ```

   默认情况下，该命令会在前台启动服务，并将日志输出到终端。可以使用 `-d` 参数将服务在后台运行。

   ```bash
   docker-compose up -d
   ```

   如果找不到配置文件，或配置描述文件的文件名不一致，可以使用 `-f` 参数指定配置文件的路径。（下同）

   ```bash
   docker-compose -f ~/another-docker-compose-config.yml up
   ```

2. **停止服务：**
   
   ```bash
   docker-compose down
   ```

   该命令停止并移除通过 `docker-compose up` 启动的服务。使用 `-v` 参数可以同时移除关联的数据卷。

   ```bash
   docker-compose down -v
   ```

3. **查看服务状态：**
   
   ```bash
   docker-compose ps
   ```

   该命令显示通过 `docker-compose up` 启动的服务的状态。

4. **构建服务：**
   
   ```bash
   docker-compose build
   ```

   该命令根据 `docker-compose.yml` 文件中的配置构建服务。通常，在修改配置后或初次运行时需要执行构建。

5. **查看日志：**
   
   ```bash
   docker-compose logs
   ```

   该命令显示服务的日志输出。可以使用 `-f` 参数来实时查看日志。

   ```bash
   docker-compose logs -f
   ```

6. **进入服务容器：**
   
   ```bash
   docker-compose exec <service-name> bash
   ```

   该命令允许你进入指定服务的容器，并启动一个交互式 shell。将 `<service-name>` 替换为实际的服务名称。

7. **重建并启动服务：**
   
   ```bash
   docker-compose up --build
   ```

   该命令会重新构建服务镜像并启动服务。适用于在修改了 Dockerfile 或其他构建文件后需要更新服务。

更多 `docker-compose` 支持的命令和参数及具体用法可以查看 [Docker Compose 官方文档](https://docs.docker.com/compose/reference/) 以获取更详细的信息。

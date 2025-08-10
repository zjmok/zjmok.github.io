---
layout: post
tags: WSL Docker
---

要在WSL 2中使用Docker Desktop，可以按照以下步骤进行设置：

1. 安装WSL 2：确保已经在Windows上启用了WSL 2，并且已经安装了适当的Linux发行版（如Ubuntu）。您可以参考Microsoft官方文档或其他教程来详细了解WSL 2的安装和配置过程。

2. 安装Docker Desktop：从Docker官网下载并安装Docker Desktop。请注意，在安装期间选择适用于WSL 2的Docker后端。

3. 配置Docker与WSL 2集成：打开Docker Desktop并转到"Settings"（设置）页面。在左侧导航栏中选择"WSL"选项卡。选择要与WSL 2集成的Linux发行版，然后单击"Apply & Restart"（应用并重启）按钮。

  - 打开 Windows 的 Docker Desktop --> Settings --> Resources --> WSL integration, 把需要集成的 WSL2 选中，然后 Apply & restart

4. 运行Docker命令：现在，您可以在WSL 2中打开终端并运行Docker命令，例如docker run或docker build，它们将在WSL 2中的Docker环境中执行。

请注意，使用Docker Desktop与WSL 2集成时，Docker引擎运行在WSL 2的虚拟机中。
这意味着您可以在WSL 2中构建和运行容器，而无需在Windows主机上额外安装和配置Docker。

最后，确保您对WSL 2和Docker的版本有适当的兼容性，并按照官方文档和指南进行配置。

---

注意事项，wsl 必须默认为 wsl2
```
wsl -s <某个wsl2>
```

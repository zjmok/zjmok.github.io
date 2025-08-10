---
layout: post
tags: Android ADB
---

# pm 命令 禁用和启用应用

在 `adb shell` 中，你可以通过 `pm` 命令来禁用和启用应用

### 1. 禁用应用
禁用应用将停止它的运行，并且防止它自动启动。可以使用以下命令来禁用应用：

```bash
adb shell pm disable-user --user 0 <package-name>
```

其中，`<package-name>` 是要禁用的应用的包名。例如，要禁用 `com.example.app` 应用，可以使用以下命令：

```bash
adb shell pm disable-user --user 0 com.example.app
```

### 2. 启用应用
启用已禁用的应用，可以使用以下命令：

```bash
adb shell pm enable <package-name>
```

例如，要启用 `com.example.app` 应用，可以使用以下命令：

```bash
adb shell pm enable com.example.app
```

### 注意事项：
- `pm disable-user` 命令可以禁用应用，但不会完全卸载它。如果需要彻底卸载，可以使用 `adb uninstall` 命令。
- 使用 `--user 0` 是指定对主用户进行操作。如果有多个用户，`--user 0` 仅会影响主用户。
- 如果在设备上没有 `root` 权限，可能会受到限制。在没有 `root` 权限的情况下，可以执行这些命令，但是有些设备或应用的特性可能会影响命令的执行。

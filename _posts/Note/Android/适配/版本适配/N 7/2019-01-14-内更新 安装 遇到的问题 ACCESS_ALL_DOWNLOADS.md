---
layout: post
tags: Android 适配
---

授予临时权限

内更新 安装 遇到的问题 ACCESS_ALL_DOWNLOADS

`android.permission.ACCESS_ALL_DOWNLOADS`, or `grantUriPermission()`

下载完成安装时 调用安装器intent需要加 `Intent.FLAG_GRANT_READ_URI_PERMISSION`
```
intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
```

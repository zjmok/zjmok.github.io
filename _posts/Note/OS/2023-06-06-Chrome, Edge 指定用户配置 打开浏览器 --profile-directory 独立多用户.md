---
layout: post
tags: Windows Linux Mac
---

用户配置存放路径

```
// Chrome
~\AppData\Local\Google\Chrome\User Data
// Edge
~\AppData\Local\Microsoft\Edge\User Data
```

可以找到配置文件夹名称

默认用户 `--profile-directory="Default"`

用户配置 2 `--profile-directory="Profile 1"`

用户配置 3 `--profile-directory="Profile 2"`

and so on...

所有 Chromium 浏览器以此类推

---

使用方法 带上参数 `--profile-directory="Default"`

Windows 设置可桌面图标：  
目标：`"C:\Program Files (x86)\Microsoft\Edge\Application\msedge.exe" --profile-directory="Default"`
起始位置：`"C:\Program Files (x86)\Microsoft\Edge\Application"`

Linux
```
google-chrome --profile-directory=Default
```

Mac
```
open -a "Google Chrome" --args --profile-directory=Default
```

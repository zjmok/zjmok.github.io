---
layout: post
tags: Git
---

# git 回归原点

## git bash

其实 git 本身就很强大，可以不用第三方客户端

需要 更改设置 优化一下

- 解决 `git status` 不能显示中文

```
git config --global core.quotepath false
```

- MINGW64, 右击标题栏 Option 修改 Text 设置

Locale 选项改为 `zh_CN`

Character set 选项改为 `UTF-8`

## git gui

- git GUI 修改编码

打开 Edit, Options..., 修改右侧 Global 的选项 `Default File Contents Encoding` 
将默认的 `cp936` 切换为 `utf-8`, Save 保存

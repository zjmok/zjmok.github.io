---
layout: post
tags: Git
---

# git 项目丢失 index object 等导致 git 无法执行任何命令的补救办法

起因：因某次停电，导致电脑死机，当时正在进行 git 操作，重新开机后执行 git 命令，提示 index 损坏

解决：

首先了解 3 个区

- ​​工作区（Working Directory），当前使用的本地文件
- 暂存区（Index/Stage），存储在 `.git/index`
- 仓库（Repository），存储在 `.git/object`

解决办法，直接删除损坏的 `.git/index` 文件，删除后会把已暂存的文件恢复到未暂存状态，但不影响正在使用的工作区文件

---

为了避免 object 文件也有丢失，直接 clone 了一个新的仓库，把新 `.git` 目录与原来的工作区合并

1. 去 `.git` 的 config 文件，查看 remote 的 url
2. 从 remote 克隆一个新的项目，checkout 到对应的分支，删掉工作区，仅保留 `.git`
3. 从原来的项目把工作区文件剪切过去，目的是尽可能保留未提交的内容

---

此次事件得到教训是，尽量保持文件同步，勤拉代码推代码

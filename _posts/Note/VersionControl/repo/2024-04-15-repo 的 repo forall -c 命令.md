---
layout: post
tags: Repo
---

# repo forall -c <command>

## 查看路径 `pwd`

```
repo forall -c pwd
```

在每个 repo 项目中执行指定命令的 Repo 工具的命令。
这个命令会遍历当前目录下所有的 repo 项目，
并在每个项目中执行指定的命令，然后输出结果。

具体来说：

- `repo forall` 表示对当前目录下的所有 repo 项目执行命令。
- `-c` 选项表示后面跟着要执行的命令。
- `pwd` 是一个 shell 命令，用于打印当前工作目录的路径。

因此，`repo forall -c pwd` 的含义是在当前目录下的每个 repo 项目中执行 `pwd` 命令，即打印每个项目的当前工作目录路径。

这个命令通常用于在 Repo 中管理的多个项目中执行相同的命令，比如查看每个项目的状态或者执行某些特定操作。

## 查看状态 `git status`

```
repo forall -c git status
```

在 repo 的每个 git 执行 `git status`

## 回滚 `git reset --hard`

```
repo forall -c git reset --hard
```

它会在当前 Repo 项目中执行 `git reset --hard` 命令。
这个命令会将当前项目中的所有更改（包括暂存区和工作目录中的更改）都恢复到上一次提交的状态，
相当于回滚到最近一次提交的状态。



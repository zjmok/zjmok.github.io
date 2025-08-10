---
layout: post
tags: Git
---

## 基本使用

git submodule 管理子模块


直接添加一个submodule
```
git submodule add [URL] [SUBMODULE_NAME]
```

## 简单描述

执行上面命令后，会在根目录会生成一个 `.gitmodules` 文件
```
[submodule "[SUBMODULE_NAME]"]
	path = [SUBMODULE_PATH]
	url = [URL]
```

用于描述对应的子模块仓库

位于 path 的子模块仓库的 `.git` 是个文件，内容 `gitdir: ../.git/modules/xxx` 指向主仓库里，所以统一在主仓库的 `.git` 文件夹管理git

在主模块使用 `git status` 会发现

- 如果子模块的HEAD是主模块对应的commitId  
  - 在子仓库目录[SUBMODULE_PATH]添加一个文件，会由子模块仓库管理，而主仓库是干净的  

- 如果子仓库的HEAD不是主模块对应的commitId  
  - 子仓库目录[SUBMODULE_PATH]将是modified状态，内容类似是这样 `Subproject commit 1801682bb79741d6fc61a7bf7e4af41f0589a157`，实际是 子仓库的git管理文件 `gitdir: ../.git/modules/xxx/HEAD` 指向了 `1801682bb79741d6fc61a7bf7e4af41f0589a157` 这个 ref 引用

## 目录结构

- 主仓库只负责管理本身仓库和使用 `.gitmodules` 引用子仓库

- 子仓库 从 `.gitmodules` 对应的 url 找到远程仓库，对应本地的源码路径 path

```
[submodule "子仓库"]
	path = src/xx/xx
	url = xxx.git
```

然后从上面 path 目录的 `.git` 文件指向主仓库的 `.git` 目录 `主仓库/.git/modules/xxx`，接着从此目录的 `HEAD` 指向某个引用找到子仓库对应的源码

## 场景示例，git submodule 管理子模块

假设目前 git 管理了模块代码，切换到使用 git submodule 来管理

1. 移除 module_flutter 的 Git 跟踪（此操作不会删除工作区文件）

```
git rm -r --cached module_flutter
```

2. 进入 module_flutter 创建模块的 git 仓库，并提交远端仓库

```
cd module_flutter

git init
git add .
git commit -m "New Module"
git remote add origin ssh://admin@192.168.1.211:29418/module_flutter.git
git push -u origin master # 推送到远端
```

3. 把目录文件移除（或移动到别的地方）

4. 在目录不存在的前提下，在主仓库根目录执行添加 git submodule 的命令

```
git submodule add ssh://admin@192.168.1.211:29418/module_flutter.git module_flutter
```

会生成 `.submodule` 文件，
git 会把子仓库 module_flutter 整个目录识别成文件，内容是子仓库的 HEAD 的 commitId

提交 `.submodule` 和 `module_flutter` 到远端即可

后续初始化更新子模块
```
git submodule update --init
```

之后可以进入 submodule 使用 git 操作

5. 到此基本完成 git submodule 的管理

可直接拉取远端最新代码

可选择恢复原来目录的文件（可以恢复 build 目录等忽略的文件），
`.git` 文件和目录不要动，剪切移动除了 `.git` 外的文件恢复到原来目录

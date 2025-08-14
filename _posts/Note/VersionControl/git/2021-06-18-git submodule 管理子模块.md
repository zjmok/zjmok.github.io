---
layout: post
tags: Git
---

# git submodule 管理子模块

## 基本使用

直接添加一个 submodule

```
git submodule add 子仓库远程地址 子仓库名称
```

## 目录结构

1. 主仓库工作区根目录的 `.gitmodules` 文件

```
[submodule "子仓库名称"]
	path = 子仓库工作区目录
	url = 子仓库远程地址
```

.git/config 会包含远程仓库

```
[submodule "子仓库名称"]
	url = 子仓库远程地址
	active = true
```

2. 子仓库的本地仓库 `.git/modules/子仓库名称`

3. 子仓库的工作区 `子仓库名称`，里面有个 `.git` 文件，内容是指向子仓库的本地仓库

```
gitdir: ../.git/modules/子仓库名称
```

### 主仓库如何定位子仓库源码

通过子仓库 HEAD 去使用

1. 从 `主仓库/.gitmodules` 找到子仓库
2. 子仓库的 `path` 找到子仓库工作区目录
3. `子仓库工作区目录/.git` 文件，指向子仓库本地仓库
4. 子仓库本地仓库 `.git/modules/子仓库本地仓库`
5. 子仓库本地仓库的 `.git/modules/子仓库本地仓库/HEAD` 文件，指向某个 Object 引用，即找到子仓库对应的源码

```
主仓库
├── .gitmodules              → 定义子仓库 path/url
├── 子仓库名称/               → 子仓库工作区，源码
│   └── .git                 → 指向 `../.git/modules/子仓库路径`
└── .git/
    └── modules/
        └── 子仓库名称/       → 子仓库本地仓库，完整 Git 仓库
            ├── HEAD         → 当前提交哈希
            └── objects/     → 源码对象存储
```

### 主仓库如何跟踪子仓库的变动

主仓库的 index （暂存区）记录了子仓库的 commitId（提交哈希）

主仓库无法跟踪子仓库未提交的代码，但工作区可以使用

当子仓库 HEAD 指向新的 commit 时，通过主仓库 index 记录的 commitId 比较子仓库的 HEAD 的 commitId

## 场景示例，git submodule 管理子模块

假设目前 git 管理了模块代码，切换到使用 git submodule 来管理

假设 submodule 是 module_flutter 目录

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

---

## 修复 submodule

若主仓库里包含子仓库 `.git/modules/module_flutter` ，但是 git 识别不到

1. 移除 module_flutter 的 Git 跟踪（此操作不会删除工作区文件）

```
git rm -r --cached module_flutter
```

2. 保证子仓库工作区 module_flutter 目录不包含 .git 文件或文件夹，有就删除

3. 执行添加命令，会复用 `.git/modules/` 里已存在的子仓库

```
git submodule add ssh://admin@192.168.1.211:29418/module_flutter.git module_flutter
```

子仓库工作区 module_flutter 目录已存在话，要加上 `-f` 参数

```
git submodule add -f ssh://admin@192.168.1.211:29418/module_flutter.git module_flutter
```

执行完就可以恢复 submodule 管理了

---

## 

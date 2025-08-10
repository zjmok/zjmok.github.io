---
layout: post
tags: Git
---

如果我们需要删除commit可以用到 `git rebase` , 改写前一个提交可以用到`git commit --amend` , 但是如果删除某个历史中的一个文件却没这么简单能做到

可以用到命令 `git filter-branch` , 

从git仓库中彻底删除文件，例如误提交的账号密码配置文件或不必要的大文件等，  
还可以修改作者/邮箱/时间，commit信息等，  
看名字就知道这是个过滤器，至于怎么用呢

是需要带上过滤的参数后，再加上需要执行的命令

这是一个删除文件的例子
```
git filter-branch --force --index-filter "git rm --cached --ignore-unmatch resume/resume.pdf" --prune-empty --tag-name-filter cat -- --all
```

---

使用命令都在这里

```
git filter-branch [--setup <command>] [--env-filter <command>]
        [--tree-filter <command>] [--index-filter <command>]
        [--parent-filter <command>] [--msg-filter <command>]
        [--commit-filter <command>] [--tag-name-filter <command>]
        [--subdirectory-filter <directory>] [--prune-empty]
        [--original <namespace>] [-d <directory>] [-f | --force]
        [--] [<rev-list options>…​]
```

先简单描述一下，<command>是命令，意思就是在`*-filter`过滤器下遍历`rev-list`指定的节点的提交，在符合条件的提交执行<command>命令改写提交

接下来分析一下

首先

- `--setup <command>` 是在过滤器执行循环前执行的一次命令

然后是过滤器

- `--subdirectory-filter <directory>` 只查看触及给定子目录的历史记录。结果将包含该目录（并且仅包含该目录）作为其项目根目录。意味着重新映射到祖先。

- `--env-filter <command>` 如果您只需要修改将在其中执行提交的环境，则可以使用此过滤器。具体来说，您可能想要重写作者/提交者名称/电子邮件/时间环境变量

- `--tree-filter <command>` 这是用于重写树及其内容的过滤器。参数在shell中计算，工作目录设置为签出树的根。然后按原样使用新树（自动添加新文件，自动删除消失文件 - 既没有.gitignore文件也没有任何其他忽略规则有任何影响！）。

- `--index-filter <command>` 这是重写索引的过滤器。

- `--parent-filter <command>` 这是用于重写提交的父列表的过滤器。

- `--msg-filter <command>` 这是用于重写提交消息的过滤器。

- `--commit-filter <command>` 这是执行提交的过滤器。

- `--tag-name-filter <command>` 这是用于重写标记名称的过滤器。
  - 传递时，将为每个指向重写对象（或指向重写对象的标记对象）的标记ref调用它。原始标记名称通过标准输入传递，新标记名称在标准输出上是预期的。
  - 原始标签不会被删除，但可以被覆盖;使用“—tag-name-filter cat”来简单地更新标签。
  - 根据定义，不可能保留签名。也不支持更改作者或时间戳（或标记消息）

接着

- `--prune-empty` 某些过滤器将生成空提交，使树保持不变。

- `--original <namespace>` 使用此选项可设置将存储原始提交的命名空间。默认值为 refs / original 。

- `-d <directory>` 使用此选项可设置用于重写的临时目录的路径。

- `-f` `--force` git filter-branch 拒绝以现有的临时目录开始，或者当已经使用 refs / original / 开始refs时，除非被强制。

- `--state-branch <branch>` 此选项将导致在启动时从命名分支加载从旧对象到新对象的映射，并在退出时将其保存为该分支的新提交，从而实现大树的增量。如果< branch> 不存在它将被创建。

还有

- `--` 在最后指定范围前有两个横杠

最后

- `<rev-list options>…​` 作用范围，节点间区间。这些选项包含的所有正面参考都被重写。您也可以指定 `--all` 等选项
  - 具体可以另外查找 `rev-list` 相关的资料

---
layout: post
tags: Git
---

## git rebase 流程

rebase变基,会将当前分支从指定提交上重新衍合,相当于重新提交之间的提交,成一条直线的提交

首先说明一下

谨慎使用rebase，请勿在其他开发者未知情的情况下对有副本的节点进行rebase，否则你可能会被打

#### rebase 方式合并分支

一般流程:
	master 是 rebase 的目标分支
	切换到当前分支 假设是dev
	
```
	git add .
	git commit -m 'msg'
```
	
	主要步骤1, rebase
	
```
	git rebase master
```
	
	此时rebase算是完成，但还没提交被目标分支拉取
	
	主要步骤2, 切换分支, 拉取rebase好的提交到目标分支master
	
```
	git checkout master
```
	
	已经切换到 master 分支
	主要步骤3, 合并目标分支到要rebase到的分支
	
```
	git merge dev
	git pull --rebase
	git push
```

	当然也可以只对commit进行rebase

#### rebase 修改当前分支的提交

```
	git rebase -i [要变基到的commit-id]
```

	修改 commit 描述前的单词, pick edit drop 分别是 摘取 修改 移除, 也可以是首字母
	
	提交后尝试解决冲突，解决后重新提交。注意提交使用的参数是 --amend 即

```
	git commit --amend -m 'msg'
	
	git rebase --continue
```

	如未完成则反复提交所有的 commit 
	
	直到rebase完成
	
	[参考]
	(https://www.cnblogs.com/oloroso/p/9723783.html)

---
layout: post
tags: Git
---

提交commit合并提交前到最新commit命令

`git commit --amend -m "new commit"` 

会合并当前提交和上一次的提交，如果当前提交有注释，则以当前的注释为合并后的提交的注释，若当前提交没有注释，则以上一次提交的注释作为合并后的提交的注释

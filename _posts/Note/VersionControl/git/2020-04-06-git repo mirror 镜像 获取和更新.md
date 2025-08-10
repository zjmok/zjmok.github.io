---
layout: post
tags: Git
---

学习Android源码过程中接触到repo的--mirror，  
repo是git的工具集，  
这里对比一下 `git` 和 `repo` 的 `--mirror` 的使用

---

## git镜像

```
# 创建
git clone --mirror git_url

# 更新
git remote update
```

---

## repo镜像

```
# 创建
repo init -u repo_url --mirror

# 更新
repo sync
```

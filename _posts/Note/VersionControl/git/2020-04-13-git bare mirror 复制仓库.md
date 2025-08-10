---
layout: post
tags: Git
---

[复制仓库](https://help.github.com/cn/github/creating-cloning-and-archiving-repositories/duplicating-a-repository)

---

- `git clone origin-url`（非裸）：您将得到所有复制的标签，一个本地分支master (HEAD)追踪远程分支origin/master和远程分支origin/next，origin/pu和origin/maint。设置了跟踪分支，这样如果你做了类似的事情git fetch origin，它们就会像你期望的那样被提取。任何远程分支（在克隆的远程中）和其他引用都被完全忽略。

- `git clone --bare origin-url`：您将获得全部复制的标签，地方分支机构master (HEAD)，next，pu，和maint，没有远程跟踪分支。也就是说，所有分支都按原样复制，并且它设置为完全独立，不期望再次获取。任何远程分支（在克隆的远程中）和其他引用都被完全忽略。

- `git clone --mirror origin-url`：这些引用中的每一个都将按原样复制。你会得到所有的标签，地方分支机构master (HEAD)，next，pu，和maint，远程分支机构devA/master和devB/master其他裁判refs/foo/bar和refs/foo/baz。一切都与克隆的遥控器完全一样。设置远程跟踪，以便在运行时，git remote update所有引用都将从原点覆盖，就像您刚删除镜像并重新克隆它一样。正如文档最初所说，它是一面镜子。它应该是功能相同的副本，可与原始版本互换。

---

创建仓库的裸克隆
```
git clone --bare https://github.com/exampleuser/old-repository.git
```

镜像推送至新仓库
```
cd old-repository.git
git push --mirror https://github.com/exampleuser/new-repository.git
```

----
Mirroring a repository that contains Git Large File Storage objects

创建仓库的裸克隆
```
git clone --bare https://github.com/exampleuser/old-repository.git
```

拉取仓库的 Git Large File Storage 对象
```
git lfs fetch --all
```

镜像推送至新仓库
```
git push --mirror https://github.com/exampleuser/new-repository.git
```

将仓库的 Git Large File Storage 对象推送至镜像
```
git lfs push --all https://github.com/exampleuser/new-repository.git
```

---
Mirroring a repository in another location

创建仓库的裸镜像克隆
```
git clone --mirror https://github.com/exampleuser/repository-to-mirror.git
```

设置到镜像的推送位置
```
cd repository-to-mirror.git
git remote set-url --push origin https://github.com/exampleuser/mirrored
```

----
设置推送 URL 可简化至镜像的推送。 如需更新镜像，请获取更新和推送
```
git fetch -p origin
git push --mirror
```

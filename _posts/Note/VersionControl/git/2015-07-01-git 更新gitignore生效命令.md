---
layout: post
tags: Git
---

```
git rm -r --cached .  #清除缓存
git add . #重新trace file
git commit -m "update .gitignore" #提交和注释

git push origin master #可选，如果需要同步到remote上的话
```

==============

```
git rm --cached --force "file_path"
```
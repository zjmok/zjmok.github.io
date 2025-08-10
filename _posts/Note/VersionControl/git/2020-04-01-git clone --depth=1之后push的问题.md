---
layout: post
tags: Git
---

当使用浅克隆

```
git clone --depth=1 xxxxxx
```

有时候，我们总是图一时之快，而忽略了其他的问题。

`--depth`浅克隆之后可能导致push到其它仓库时时可能会出现`shallow update not allowed`错误  
（本人测试自己部署的gitlab不支持浅更新）

解决办法，需要把前面所有commit拉下来

```
git fetch --unshallow
```

然后重新push

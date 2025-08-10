---
layout: post
tags: Git
---

在 repo sync 时遇到错误
```
...
fatal: The remote end hung up unexpectedly
fatal: early EOF
fatal: index-pack failed
...
```

原因，文件太大

网上找到几种解决方案

1.修改git的缓存大小, 1048576000(1G)
```
git config --global http.postBuffer 1048576000
```

2.修改git的最低速限制和最低速时间, 86400秒
```
git config --global http.lowSpeedLimit 0
git config --global http.lowSpeedTime 86400
```

3.修改git的compression压缩配置, -1是zlib默认 0不压缩 1~9压缩等级逐级增加
```
git config --global core.compression -1
```

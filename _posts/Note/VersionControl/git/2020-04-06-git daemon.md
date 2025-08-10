---
layout: post
tags: Git
---

在像AOSP这样的超级项目中，也许clone一次代码都要很麻烦，  
如果团队中已经有人获取到完整仓库了，那就可以通过git镜像`git daemon`来share了

---

Service
```
git daemon --export-all --verbose --base-path=.
```

`--enable=receive-pack`允许client端向server端push代码

`--export-all`share “base-path” 下所有的repo

`--base-path=.`定义了folder为当前目录

`--verbose`任何操作都会给当前repo通知


还可以在上一条command后面添加其他参数：

`--informative-errors`给client提供简洁的错误信息

`--reuseaddr`允许快速重启server

Client就可使用`git://[SERVER_IP]/[REPO_NAME]`作为镜像
```
cd <base-path>
git clone git://127.0.0.1/your_mirror_repo.git
```

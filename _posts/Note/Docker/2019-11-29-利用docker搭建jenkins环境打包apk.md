---
layout: post
tags: Docker Jenkins
---

拉取docker镜像
```
docker pull xmartlabs/jenkins-android
```

运行容器，并挂载宿主目录
```
docker run --name android-jenkins \
-p 8080:8080 -p 50000:50000 \
-v /Users/mok/data/jenkins:/var/jenkins_home \
xmartlabs/jenkins-android
```

---

docker疑问

挂载 `/usr/bin` 目录使用 git maven 等？

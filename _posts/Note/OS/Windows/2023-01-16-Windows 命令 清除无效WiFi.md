---
layout: post
tags: Windows
---

清除所有wifi连接保存的热点
```
netsh wlan delete profile name=* i=*
```

清除某个热点
```
netsh wlan delete profile name=AndroidAP i=*
```

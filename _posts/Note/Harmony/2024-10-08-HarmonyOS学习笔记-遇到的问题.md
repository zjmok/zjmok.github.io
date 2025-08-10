---
layout: post
tags: Harmony
---

# HarmonyOS学习笔记-遇到的问题

目前使用 IDE 版本：5.0.3.900，compatibleSdkVersion：5.0.0(12)

#### 修改 `AppScope/app.json` 的 `bundleName` 后无法运行

解决：

重新生成 signingConfigs，可以把根目录 `build-profile.json5` 中的 `signingConfigs` 直接注释掉再 sync 即可，之后去掉注释还原

#### 页面传参不能直接传 string

改用对象字面量正常传值，获取时可用 Record<string, string> 直接根据键读取值（不用创建相应格式的类）

#### 

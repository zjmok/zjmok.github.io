---
layout: post
tags: Android
---

### AndroidManifest.xml中替换第三方sdk的AndroidManifest.xml属性

极光 在sdk中的AndroidManifest.xml已经设置了OAuthActivity节点的android:screenOrientation

这样能在app模块的AndroidManifest.xml覆盖第三方sdk的

app模块:
```
<activity
	android:name="com.cmic.sso.sdk.activity.OAuthActivity"
	android:configChanges="orientation|keyboardHidden|screenSize"
	android:launchMode="singleTop"
	android:screenOrientation="portrait"
	tools:replace="android:screenOrientation">
```

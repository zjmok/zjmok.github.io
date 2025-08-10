---
layout: post
tags: Android
---

由于selector不能设置item的宽、高，如果其默认显示的item是通过指定了宽、高的shape实现，而其它的state是通过image资源实现时，则在不同分辨率的手机上，操作时就存在宽、高不一致的适配问题

解决方案:
可利用layer-list的item设置宽高

```
<?xml version="1.0" encoding="utf-8"?>
<selector xmlns:android="http://schemas.android.com/apk/res/android">

<!-- 不能设置大小 -->
<!--
    <item android:drawable="@drawable/icon_accept_checked" android:state_checked="true"/>
    <item android:drawable="@drawable/icon_accept_uncheck" android:state_checked="false"/>
-->

    <!--设置大小-->
    <item android:state_checked="true">
        <layer-list>
            <item android:drawable="@drawable/icon_accept_checked"
                android:width="14dp"
                android:height="14dp"
                />
        </layer-list>
    </item>

    <item android:state_checked="false">
        <layer-list>
            <item android:drawable="@drawable/icon_accept_uncheck"
                android:width="14dp"
                android:height="14dp"
                />
        </layer-list>
    </item>

</selector>

```

---

已知bug：Flyme低版本不兼容
建议：乖乖使用图片吧

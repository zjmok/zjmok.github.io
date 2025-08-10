---
layout: post
tags: Android Compose
---

<https://jetpackcompose.cn/docs/elements/text#5-fontfamily-参数>

---

- 传统 Android

将字体放到资产目录，`assets/fonts/JetbrainsMono-Light.ttf`

```kotlin
@Suppress("NOTHING_TO_INLINE")
inline fun TextView.setTypeface(nameFromAssets: String) {
    this.typeface = Typeface.createFromAsset(context.assets, nameFromAssets)
}

// 使用ttf字体
TextView(context).apply {
    layoutParams = LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.MATCH_PARENT,
        LinearLayout.LayoutParams.WRAP_CONTENT,
    )
    gravity = Gravity.START
    setPadding(16.dp2px, 16.dp2px, 16.dp2px, 16.dp2px)
    text = "qwer123456"
	// 使用ttf字体
    setTypeface("fonts/JetBrainsMono-Light.ttf")
}
```

- Jetpack Compose

把字体放到资源目录，遵循资源文件命名规则，`res/font/jetbrainsmono_light.ttf`

```
Text(
    text = "qwer123456",
    fontSize = 14.sp,
    fontFamily = FontFamily(
        // 使用ttf字体
        Font(R.font.jetbrainsmono_light),
    ),
)
```

---
layout: post
tags: Android
---

# Android 使用画中画 (Picture-in-Picture, PiP) 模式来实现视频小窗播放

### 1. 声明画中画支持
首先，在你的 `AndroidManifest.xml` 文件中声明支持画中画模式：

```xml
<activity
    android:name=".YourActivity"
    android:supportsPictureInPicture="true"
    android:configChanges="screenSize|smallestScreenSize|screenLayout|orientation">
</activity>
```

### 2. 切换到画中画模式
在你的 `Activity` 中，当需要切换到画中画模式时，可以调用 `enterPictureInPictureMode()` 方法：

```kotlin
fun enterPipMode() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val aspectRatio = Rational(16, 9)
        val pipParams = PictureInPictureParams.Builder()
            .setAspectRatio(aspectRatio)
            .build()
        enterPictureInPictureMode(pipParams)
    }
}
```

### 3. 处理画中画模式的回调
你可以覆盖 `onPictureInPictureModeChanged` 方法来处理进入和退出画中画模式的回调：

```kotlin
override fun onPictureInPictureModeChanged(
    isInPictureInPictureMode: Boolean,
    newConfig: Configuration?
) {
    super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)
    if (isInPictureInPictureMode) {
        // 隐藏不必要的 UI 元素
    } else {
        // 恢复 UI 元素
    }
}
```

### 4. 处理用户交互
你可以为画中画模式添加自定义操作，例如播放、暂停等：

```kotlin
fun createPipActions() {
    val intent = PendingIntent.getActivity(
        this, 0, Intent(this, YourActivity::class.java), 0
    )
    val icon = Icon.createWithResource(this, R.drawable.ic_play)
    val action = RemoteAction(icon, "Play", "Play", intent)
    val pipParams = PictureInPictureParams.Builder()
        .setActions(listOf(action))
        .build()
    setPictureInPictureParams(pipParams)
}
```

### 5. 处理配置变更
确保你的 `Activity` 能够处理配置变更，以避免在画中画模式下重建：

```xml
<activity
    android:name=".YourActivity"
    android:configChanges="screenSize|smallestScreenSize|screenLayout|orientation">
</activity>
```

### 6. 示例代码
以下是一个完整的示例，展示如何在 `Activity` 中实现画中画模式：

```kotlin
class YourActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_your)

        // 进入画中画模式的按钮点击事件
        findViewById<Button>(R.id.enter_pip_button).setOnClickListener {
            enterPipMode()
        }
    }

    private fun enterPipMode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val aspectRatio = Rational(16, 9)
            val pipParams = PictureInPictureParams.Builder()
                .setAspectRatio(aspectRatio)
                .build()
            enterPictureInPictureMode(pipParams)
        }
    }

    override fun onPictureInPictureModeChanged(
        isInPictureInPictureMode: Boolean,
        newConfig: Configuration?
    ) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)
        if (isInPictureInPictureMode) {
            // 隐藏不必要的 UI 元素
        } else {
            // 恢复 UI 元素
        }
    }
}
```

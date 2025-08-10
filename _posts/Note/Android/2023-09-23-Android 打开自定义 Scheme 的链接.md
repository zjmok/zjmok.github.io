---
layout: post
tags: Android
---

# Android 打开自定义 Scheme 的链接

在 Android 中，自定义 URL scheme 可以让你的应用响应特定的 URI，从而实现应用间的跳转或从浏览器打开应用。以下是如何在 Android 应用中设置自定义 URL scheme 的步骤：

### 步骤一：在 `AndroidManifest.xml` 中添加 Intent Filter

在你的 `AndroidManifest.xml` 文件中，为目标 `Activity` 添加一个 `intent-filter`，指定自定义的 scheme。例如，假设你想要定义一个名为 `myapp` 的自定义 scheme：

```xml
<activity android:name=".MyActivity">
    <intent-filter>
        <action android:name="android.intent.action.VIEW" />

        <category android:name="android.intent.category.DEFAULT" />
        <category android:name="android.intent.category.BROWSABLE" />

        <!-- 还可以匹配 host path pathPrefix pathPattern -->
        <data android:scheme="wanandroid" />
        <data android:scheme="http" />
        <data android:scheme="https" />
        <data android:scheme="market" />
        <data android:scheme="myapp" android:host="example" />
    </intent-filter>
</activity>
```

### 步骤二：处理 Intent 数据

在你的 `Activity` 中，重写 `onCreate` 方法以处理传入的 Intent 数据：

```kotlin
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my)

        handleIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        val action = intent.action
        val data: Uri? = intent.data

        if (Intent.ACTION_VIEW == action && data != null) {
            // 处理自定义 scheme 的数据
            val host = data.host
            val path = data.path
            val query = data.query

            // 根据 host、path 和 query 处理不同的逻辑
            // 例如：跳转到不同的页面或执行不同的操作
        }
    }
}
```

### 示例链接

假设你定义了 `myapp://example/path?param=value`，当用户点击这个链接时，系统会启动你的应用并传递相应的数据到 `MyActivity`。

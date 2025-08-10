---
layout: post
tags: Android
---

# TileService

官方文档

<https://developer.android.com/develop/ui/views/quicksettings-tiles?hl=zh-cn>

<https://developer.android.com/reference/android/service/quicksettings/TileService>

快捷开关是Android 7（api 24）的新能力

TileService 是通过 bindService 进行交互，除了 Service 本身生命周期的方法外，主要通过扩展以下回调方法进行交互

- onTileAdded 添加时

- onTileRemoved 移除时

- onStartListening 下拉快捷设置栏时（添加后会调用一次，onCreate -> onBind -> onTileAdded -> onStartListening）

- onStopListening 关闭快捷设置栏时（移除前会调用一次，onStopListening -> onTileRemoved -> onUnbind -> onDestroy）

- onClick 点击时

方法调用

- requestListeningState 静态方法，主动模式时配合清单 meta-data 使用，通常是在外部调用，例如在 activity

- showDialog 显示一个 dialog 并折叠快捷设置栏

- startActivityAndCollapse 打开一个 Activity 并折叠快捷设置栏，activity 记得 addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

- unlockAndRun 传入一个 Runnable，操作前会请求用户解锁

## 使用

1. 继承 TileService 类

```
class TestTileService : TileService() {
    // 系统会从下面清单声明自动识别此快捷设置 Tile
}
```

2. 清单声明组件

```
<service
    android:name=".TestTileService"
    android:label="快捷设置的标题"  
    android:icon="快捷设置的图标"
    android:exported="true"
    android:permission="android.permission.BIND_QUICK_SETTINGS_TILE">
    <intent-filter>
        <action android:name="android.service.quicksettings.action.QS_TILE" />
    </intent-filter>
</service>
```

需要给 service 配置的权限，BIND_QUICK_SETTINGS_TILE 即允许应用程序绑定到第三方快速设置。必须配置。

除了前三项的值，后面的是固定代码

添加以上基本代码，就可以在快捷设置栏的编辑页面添加一个 Tile 块（快捷设置，快捷开关）

## 监听模式

1. 主动模式

主动模式需要在清单声明 TileService 服务时带上 `meta-data` 标签，然后通过 `requestListeningState` 进行绑定，缺一不可

TileService 被请求时该服务会被绑定，并且 TileService 的 onStartListening 也会被调用

（在 MIUI12 测试，下拉时不再回调 onStartListening，但折叠时会调用 onStopListening 并且有几率解绑销毁服务，这是系统 BUG ?!）

```
// 此方法进行主动监听，可以在 Activity 使用
TileService.requestListeningState(applicationContext, ComponentName(
    BuildConfig.APPLICATION_ID,
    TestTileService::class.java.name
))

// 并且在清单声明 meta-data
<service
    android:name=".TestTileService"
    android:exported="true"
    android:icon="@drawable/ic_launcher_foreground"
    android:label="测试TestTileService"
    android:permission="android.permission.BIND_QUICK_SETTINGS_TILE">
    <intent-filter>
        <action android:name="android.service.quicksettings.action.QS_TILE" />
    </intent-filter>
    <!-- 主动模式需要添加的 meta-data，配合 requestListeningState 使用 -->
    <meta-data
        android:name="android.service.quicksettings.ACTIVE_TILE"
        android:value="true" />
</service>
```

主动模式无论是通过用户下拉快捷设置栏还是代码调用 requestListeningState 都会拉起 TileService 所在进程，进行服务绑定

2. 默认模式（被动）

默认模式在清单中声明服务时不带上 meta-data 标签或 value 为 false

TileService 可见时（即用户下拉快捷设置栏时）该服务会被绑定，onStartListening 被调用。被动模式下，当折叠快捷设置栏后，系统可能会随时解绑服务并销毁。

(定制系统可能将任务栏和快捷设置栏分开，需要确认下拉的栏是有快捷开关的)

## 踩坑

- 请不要重写 onBind 方法尝试绑定服务，请通过主动模式交互。被动模式下，当折叠快捷设置栏后，系统可能会随时解绑服务并销毁；但主动模式可以保持服务绑定，此时可以通过广播等方式通信

## 长按事件

官方文档 <https://developer.android.com/develop/ui/views/quicksettings-tiles?hl=zh-cn#handle-taps>

官方文档 <https://developer.android.com/reference/android/service/quicksettings/TileService#ACTION_QS_TILE_PREFERENCES>

跳转来源参考 <https://blog.csdn.net/smallbabylong/article/details/102514392>

长按功能块会提示用户打开应用信息界面。如需替换此行为并启动用于设置偏好设置的 activity，
请使用 `ACTION_QS_TILE_PREFERENCES` 将 `<intent-filter>` 添加到其中一个 activity。

```
<intent-filter>
    <action android:name="android.service.quicksettings.action.QS_TILE_PREFERENCES"/>
</intent-filter>
```

只需要在将要跳转到的 activity 添加以上 action 即可

若要区分是从哪个 TileService 跳转过来的，查看组件名称即可

```
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ...

        val componentName = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            intent.extras?.get(Intent.EXTRA_COMPONENT_NAME) as ComponentName?
        } else {
            // Android 8 以下未获取到来源
            null
        }

        val className = componentName?.className ?: "未知来源"
        log("来源: \n$className")
    }
```

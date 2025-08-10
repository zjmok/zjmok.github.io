---
layout: post
tags: Android 适配
---

# 仿 bilibili 启动页面 无缝过渡, Android 12 SplashScreen 启动页适配 + 适配低版本

## SplashScreen 使用

添加相关依赖 版本是 1.0.1, API 可能与网上旧版本不太一样

```kotlin
implementation("androidx.core:core-splashscreen:1.0.1")
```

其它可选依赖自行添加 lifecycle Splitties AndroidUtilCode ImmersionBar

SplashScreen 基本使用

```kotlin
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // installSplashScreen 必须在 setContentView 前执行
        val splashScreen = installSplashScreen()
        // 状态栏导航栏相关设置 可选
        immersionBar {
            statusBarColor(R.color.black)
            navigationBarColor(R.color.black)
            statusBarDarkFont(false)
        }
        setContentView(binding.root)
        // 设置 SplashScreen
        setSplashScreen(splashScreen)
    }

    private fun setSplashScreen(splashScreen: SplashScreen) {
        var isShowSplash = true

        // 每次 UI 绘制前，会判断 SplashScreen 是否继续展示在屏幕上，直到不再满足条件时，展示完毕并执行 setOnExitAnimationListener
        splashScreen.setKeepOnScreenCondition {
            isShowSplash
        }

        lifecycleScope.launch {
            // SplashScreen 展示时长
            delay(1000) // SplashScreen 仅用于过渡启动, 这里设置 0, 后续展示 AD 或倒计时使用自己的页面
            // SplashScreen 展示完毕
            isShowSplash = false
        }

        // SplashScreen 展示完毕的监听方法
        splashScreen.setOnExitAnimationListener { splashScreenViewProvider ->
            splashScreenViewProvider.view.animate().alpha(0f).setDuration(250L).start()

            // 在此执行后续操作

            // 这里是一些弹框和倒计时的操作，之后跳转到 MaiActivity，在此之间有需要的话可以自己添加广告展示等
            initPrivacyDialog {
                initSDKWithPrivacy {
                    initView {
                        start<MainActivity> {}
                        finish()
                    }
                }
            }
        }
    }
```

SplashScreen 之后的 启动页面

可以自行添加倒计时或广告等

布局

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@color/black">

    <ImageView
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:background="@drawable/splash_1080"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintDimensionRatio="1080:1369"
        app:layout_constraintTop_toTopOf="parent" />

    <androidx.appcompat.widget.AppCompatTextView
        android:id="@+id/tv_skip"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginHorizontal="50dp"
        android:layout_marginVertical="100dp"
        android:background="#fff"
        android:padding="10dp"
        android:text="跳过"
        android:textSize="20sp"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

    <!-- 设置 ImageView 对应 windowSplashScreenBrandingImage
    源码中的宽高 dimen
    starting_surface_brand_image_width 200dp
    starting_surface_brand_image_height 80dp
    布局属性
    android:scaleType="fitXY"
    android:layout_marginBottom="60dp"
    -->
    <ImageView
        android:layout_width="200dp"
        android:layout_height="80dp"
        android:layout_marginBottom="60dp"
        android:scaleType="fitXY"
        android:src="@drawable/bilibili"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent" />

</androidx.constraintlayout.widget.ConstraintLayout>
```

## Android 12

values-v31/themes.xml

```xml
<resources>

    <!-- https://developer.android.com/develop/ui/views/launch/splash-screen -->
    <style name="SplashScreen" parent="Theme.SplashScreen">
        <!-- 启动画面 背景颜色 -->
        <item name="windowSplashScreenBackground">#000000</item>
        <!-- 启动画面 中心 icon, 这里可以是图片、帧动画等 -->
        <item name="windowSplashScreenAnimatedIcon">@drawable/shape_transparent</item>
        <!-- 启动画面 中心 icon 背景颜色 -->
        <item name="windowSplashScreenIconBackgroundColor">#00000000</item>
        <!-- icon 动画时长, 最长时间为 1000 毫秒, 这个属性不会对屏幕显示的实际时间产生任何影响 -->
        <item name="windowSplashScreenAnimationDuration">0</item>
        <!--
        启动画面 底部的品牌图片, 图片会被拉伸,
        从模拟器截图测量比例为 2.5:1 (即 1坤:1, 或 🐓:1)
        官方文档和源码表明此会被设置为 200×80 dp
        -->
        <item name="android:windowSplashScreenBrandingImage">@drawable/bilibili</item>
        <!-- 必选项, Splash 退出后的主题 -->
        <item name="postSplashScreenTheme">@style/AppTheme</item>
    </style>

</resources>
```

drawable/shape_transparent

```xml
<?xml version="1.0" encoding="utf-8"?>
<shape>
    <!-- 这只是相当于一张透明的 drawable -->
</shape>
```

## Android 6

将 SplashScreen 的背景颜色设置为透明, 并使用 windowBackground 指定 layer-list 实现 windowSplashScreenBrandingImage 的效果

values/themes.xml

```xml
<!-- https://developer.android.com/develop/ui/views/launch/splash-screen -->
    <style name="SplashScreen" parent="Theme.SplashScreen">
        <!-- 利用 layer-list 适配低于 Android 12 的 启动白屏页 SplashScreen 品牌图片，但低于 Android 6.0 的还不能完美适配 -->
        <item name="android:windowBackground">@drawable/splash_bg</item>
        <!-- 启动画面 背景颜色 -->
        <item name="windowSplashScreenBackground">#00000000</item>
        <!-- 启动画面 中心 icon, 这里可以是图片、帧动画等 -->
        <item name="windowSplashScreenAnimatedIcon">@drawable/shape_transparent</item>
        <!-- 启动画面 中心 icon 背景颜色 -->
        <item name="windowSplashScreenIconBackgroundColor">#00000000</item>
        <!-- icon 动画时长, 最长时间为 1000 毫秒, 这个属性不会对屏幕显示的实际时间产生任何影响 -->
        <item name="windowSplashScreenAnimationDuration">0</item>
        <!--
        启动画面 底部的品牌图片, 图片会被拉伸,
        从模拟器截图测量比例为 2.5:1 (即 1坤:1, 或 🐓:1)
        Branded image: this must be 200×80 dp.
        -->
        <!--<item name="android:windowSplashScreenBrandingImage" tools:targetApi="s">
            @drawable/bilibili
        </item>-->
        <!-- 必选项, Splash 退出后的主题 -->
        <item name="postSplashScreenTheme">@style/AppTheme</item>
    </style>
```

windowSplashScreenBrandingImage 的相关属性参数是: 200x80 dp, `gravity="center|bottom"`, layout_marginBottom="60dp"

drawable-v23/splash_bg.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<layer-list xmlns:android="http://schemas.android.com/apk/res/android">
    <item android:drawable="@color/black" />
    <!-- 低于 API23 不能指定 bitmap 宽高, 还需要另外适配 -->
    <item
        android:width="200dp"
        android:height="80dp"
        android:bottom="60dp"
        android:gravity="center|bottom">
        <bitmap android:src="@drawable/bilibili" />
    </item>
</layer-list>
```

## 更低版本

低版本的 layer-list 每个层都会被拉伸

低版本屏幕都是 16:9 可以直接使用一张 16:9 的图片 作为 windowBackground

bilibili_5_0.jpg 是一张普通图片

values/splash_bg.xml
```xml
<?xml version="1.0" encoding="utf-8"?>
<bitmap xmlns:android="http://schemas.android.com/apk/res/android"
    android:src="@drawable/bilibili_5_0" />
```

可能还需要考虑系统底部导航栏的问题。用户存量已经很少了，不再做特别适配

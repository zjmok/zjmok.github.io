---
layout: post
tags: Android Unity
---

### AI课堂 Unity包 更新

- 替换 `/AgoraRtcEngineKit.plugin/libs` --> `/lib_agora/libs` (最好是先到对应目录下删干净所有文件再复制一份下来, 而不是直接覆盖掉, 避免流下非必要的残留文件, 下同)

- 替换 `/libs` --> `/lib_unity/libs`

- 替换 `/src` --> `/lib_unity/src` 目录下的文件

- 权限和各种版本号之类的一般不会有多大变化( `AndroidManifest.xml` 和 `build.gradle` 等), 一般不用动, 也可每次更新检查一下是否需要增删改

### AI课堂 Android Unity 接入

其实就是将 aar so jar 资源 引入到Android工程

- `AgoraRtcEngineKit.plugin` 声网库
  将文件夹下 `libs` 所有内容放到AIClss项目的 `lib_agora`

- `libs` Unity相关的库
  将文件夹下内容放到AIClss项目的 `lib_unity`

- `\src\main` 源码 资源 和so库
  `assets` `java` `jniLibs` `res` 四个文件夹 以及 `AndroidManifest` 复制到 `lib_unity` 的相应位置

- AIClass的 `app` 模块引用 `lib_unity`

  `app` 的 `build.gradle`

```groovy
android {
    sourceSets {
        main {
            jniLibs.srcDirs = ['../app/libs', '../lib_unity/libs', '../lib_agora/libs']
        }
    }
}
//module中引入的aar和so也需要在app中指定路径
repositories {
    flatDir {
        dirs '../lib_unity/libs', '../lib_agora/libs'
    }
}
dependencies {
    //AI课堂 Unity包
    implementation project(':lib_unity')
}
```

- AIClass的 `lib_unity` 模块引用 `lib_agora`

  `lib_unity` 的 `build.gradle`

```groovy
repositories {
    flatDir {
        dirs '../lib_unity/libs', '../lib_agora/libs'
    }
}
dependencies {
    //AI课堂 Unity声网包
    api project(':lib_agora')
    //aar
    api(name: 'UniWebView', ext: 'aar')
}
```

- AIClass的 `lib_agora` 模块

  `lib_agora` 的 `build.gradle`

```groovy
repositories {
    flatDir {
        dirs '../lib_agora/libs'
    }
}
```

其余的 版本号等 跟项目统一一下就行了

---

### 路上的坑

#### 退出时闪退

##### 原因

查看 `UnityPlayerActivity` 的 `onDestroy` 是这样的
```java
// Quit Unity
@Override protected void onDestroy ()
{
    mUnityPlayer.quit();
    super.onDestroy();
}
```

UnityPlayer#quit点开 里面有一行代码
```
this.kill();
```

点开看是这样的
```
Process.killProcess(Process.myPid());
```

就是**杀掉当前进程**

##### 解决办法

- 法1(推荐):

  独立一个进程打开unity, `android:process` 给Activity指定一个进程
```xml
<activity
    android:name="com.game.aiClass.UnityPlayerActivity"
    android:configChanges="mcc|mnc|locale|touchscreen|keyboard|keyboardHidden|navigation|orientation|screenLayout|uiMode|screenSize|smallestScreenSize|fontScale|layoutDirection|density"
    android:launchMode="singleTask"
    android:process=":unity"
    android:screenOrientation="sensorLandscape" />
```

注意继承他的类也需要需要在同一个进程

- 法2:

  处理掉杀进程的代码, 继承 `UnityPlayer` 重写 `kill()` 空重写
```java
public class NoKillUnityPlayer extends UnityPlayer {
    public NoKillUnityPlayer(Context context) {
        super(context);
    }

    @Override
    protected void kill() {
    }
}
```

然后替换掉 `UnityPlayerActivity` 里创建实例的代码
```java
// Setup activity layout
@Override protected void onCreate(Bundle savedInstanceState)
{
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    super.onCreate(savedInstanceState);

//    mUnityPlayer = new UnityPlayer(this);
    mUnityPlayer = new NoKillUnityPlayer(this);
    setContentView(mUnityPlayer);
    mUnityPlayer.requestFocus();
}
```

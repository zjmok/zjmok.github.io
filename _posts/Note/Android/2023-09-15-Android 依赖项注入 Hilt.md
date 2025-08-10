---
layout: post
tags: Android
---

# Hilt

[官方文档](https://developer.android.com/training/dependency-injection/hilt-android?hl=zh-cn#kts)

[mvn hilt](https://mvnrepository.com/artifact/com.google.dagger/hilt-android)

IOC(Inversion of Control, 控制反转), 一种编程思路

DI(Dependency Injection, 依赖注入), IOC 的具体实现

---

关于注入的种类

- view注入 ButterKnife

- 参数注入 Arouter

- 对象注入 Dagger2 Hilt SpringBoot

## 注解

组件注解

- @HiltAndroidApp 在注入的 Application 使用
- @HiltViewModel 在注入的 ViewModel 使用
- @AndroidEntityPoint 在注入的 Android 组件使用，支持 Activity Fragment View Service BroadcastReceiver

定义安装模块

- @Module 标记是创建对象的类 (相当于 SpringBoot 的 @Configuration)
- @InstallIn(ApplicationComponent::class) 同样是标记在类 声明此类的方法创建的对象的生命周期  
  可选 SingletonComponent(旧版命名为ApplicationComponent) ActivityRetainedComponent ActivityComponent FragmentComponent ViewComponent ServiceComponent

依赖提供注解

- @Provides 注册方法 把返回值放到容器 (相当于 SpringBoot 的 @Bean)  
  用在 object 类的方法上 在方法中手动创建对象，
- @Binds  注册方法 把返回值放到容器 (相当于 SpringBoot 的 @Bean)  
  用在 抽象类的抽象方法上 在方法中传入实现类， 返回值是接口 传参是返回值接口的实现类，应用场景是使用 @Provides 无法获得某些参数传入时使用

作用域注解，配合上面的依赖提供注解使用

- @Singleton 声明是单例，
- @ActivityRetainedScoped 对象在 Activity 的整个生命周期内保持存在，特别是当 Activity 被销毁和重新创建时（例如，因配置更改）。主要用于 ViewModel
- @ActivityScoped 对象的生命周期与 Activity 本身相同，当 Activity 被销毁时，相关的依赖项也会被销毁
- @FragmentScoped
- @ViewScoped
- @ServiceScoped

注入注解

- @Inject 注入对象 (相当于 SpringBoot 的 @Autowride)

标记注解

- @ApplicationContext 标记方法中的 Context 参数是 Application 的
- @ActivityContext 标记方法中的 Context 参数是 Activity 的
  需要在方法上使用 @Inject 标记

## 实践

### 1. 添加 Hilt 依赖

build.gradle.kts [project]

```
plugin {
    id("com.google.dagger.hilt.android") version "2.44" apply false
}
```

build.gradle.kts [app]

```
plugins {
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}

dependencies {
    // hilt
    // https://developer.android.com/training/dependency-injection/hilt-android?hl=zh-cn#kts
    implementation("com.google.dagger:hilt-android:2.44")
    kapt("com.google.dagger:hilt-android-compiler:2.44")
}
```

### 2. 必须在 Application 设置 Hilt

```
@HiltAndroidApp
class App : Application() {}
```

### 3. 创建依赖模块，使用 @Module 和 @InstallIn

创建一个 Hilt 模块，使用 @Module 和 @InstallIn 注解来提供依赖项

```
@Module
@InstallIn(SingletonComponent::class) // 可在应用的整个生命周期中使用的依赖
object AppModule {
    @Provides
    @Singleton
    fun provideMyService(): MyService {
        return MyServiceImpl()
    }
}
```

这里会把方法返回的类型添加到容器中，之后使用 @Inject 可以自动注入该类型

### 4. 注入依赖，使用 @AndroidEntryPoint 和 @Inject

使用 @AndroidEntryPoint 注解来标记 Activity，然后通过 @Inject 注入依赖

```
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var myService: MyService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 使用 myService
    }
}
```

Hilt 目前支持标记以下 Android 类：

- Application（通过使用 @HiltAndroidApp）
- ViewModel（通过使用 @HiltViewModel）
- Activity （@AndroidEntryPoint）
- Fragment
- View
- Service
- BroadcastReceiver

```
@HiltViewModel
class MyViewModel @Inject constructor(
    private val myRepository: MyRepository
) : ViewModel() {
    // ViewModel 逻辑
}
```

如果您使用 @AndroidEntryPoint 为某个 Android 类添加注解，则还必须为依赖于该类的 Android 类添加注解。
例如，如果您为某个 fragment 添加注解，则还必须为使用该 fragment 的所有 activity 添加注解

至此，可以满足基本使用

### 5. 测试中的 Hilt

```
@HiltAndroidTest
class MyActivityTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun testMyActivity() {
        // 测试逻辑
    }
}
```

## Module 类

只要在 @Module 的类中的某个带返回类型的方法进行 @Provides 提供实例，然后在别的地方就能使用 @Inject 自动注入这个返回类型的实例

- 生命周期（用在 Module 类的 @InstallIn 注解，配合 @Module），可选其一

- @SingletonComponent (旧版命名为ApplicationComponent) 在进程内一直是单例
- @ActivityRetainedComponent (ViewModel) 在 Activity 生命周期里是单例，包括销毁重建
- @ActivityComponent 在 Activity 生命周期里是单例
- @FragmentComponent
- @ViewComponent
- @ServiceComponent


- 作用域（用在 Module 类中标记了 @Provides 提供实例的方法，配合 @Provides 或 @Binds），可选其一

- @Singleton
- @ActivityRetainedScoped
- @ActivityScoped
- @FragmentScoped
- @ViewScoped
- @ServiceScoped

不加作用域注解，注入对象时就不会是单例

示例

```
@Module
@InstallIn(ActivityRetainedComponent::class) // 生命周期，可选其一
object MainModule {
    // Provides 用在手动创建对象的情况
    @Provides
    // 声明作用域 需要与生命周期对应 否则报错
	// 不加作用域注解则注入时的实例不是单例
    @Singleton // 作用域，可选其一
    fun getUser(): User {
        return User(1, "ZhangSan")
    }
}

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class MainModule {
    // Binds 用在传入实现类时的情况
    @Binds
    @ActivityRetainedScoped
    abstract fun login(login: LoginImpl) : ILogin
}

data class User(
    var id: Int,
    var username: String
)
```

使用 注入对象，也可在构造函数使用

```
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var user: User
}

class UserRemoteSource @Inject constructor(
    private val apiService: ApiService // Hilt 自动注入 ApiService 实例
) {
    suspend fun fetchUser(): User {
        return apiService.getUser() // 通过 API 获取用户数据
    }
}
```

---

## 解惑

- 被注入对象的构造方法有无参数

有参构造函数：Hilt 自动处理，通过 @Inject 注解标记，简化了依赖的管理。
无参构造函数：Hilt 无法自动管理，因此需要显式提供方法来创建实例。

通常建议使用有参构造函数来充分利用 Hilt 的依赖注入特性，这样可以减少手动管理依赖的需要

- 关于类的生命周期和方法的作用域

Q1:

```
@Module
@InstallIn(ActivityComponent::class)
object MyModule {
    @Provides
    @Singleton
    fun provideSharedRepository(): SharedRepository {
        return SharedRepository()
    }
}
```

A1: 生命周期是 Activity，作用域 单例。会在每个使用的 Activity 都生成一个单例，生命周期跟随 Activity

Q2:

```
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @ActivityScoped
    fun provideSharedRepository(): SharedRepository {
        return SharedRepository()
    }
}
```

A2: 报错，两者作用域是互相冲突的，需要 ActivityComponent + ActivityScoped  
还有其它一些会冲突的情况，例如  
ActivityComponent + ServiceScoped （因为 Activity 的生命周期与 Service 的生命周期不同）  
ActivityComponent + ViewModelScoped  
ActivityComponent + FragmentScoped  
SingletonComponent + ActivityScoped  
等，  
需要选择适当的作用域与安装组件，避免以上列举的冲突组合

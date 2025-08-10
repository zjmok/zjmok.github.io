---
layout: post
tags: Maven
---

# 将 Library 发布到 Maven 仓库，可发布模块产物 jar aar

## 一. Java Library 发布 jar

### 1. 创建一个新的 Gradle 模块

创建一个新的 Gradle 模块（Java or Kotlin），例如 `local-libs`

### 2. 配置 `build.gradle.kts` 文件

在 `local-libs` 模块的 `build.gradle.kts` 文件中，使用 `maven-publish` 插件，并将多个依赖项添加到 `dependencies` 块中

```kotlin
plugins {
    `maven-publish`
}

group = "your.group" // 设置您的组 ID
version = "1.0.0" // 设置版本

dependencies {
    // 添加一些远程依赖
    // https://github.com/square/okhttp
    implementation("com.squareup.okhttp3:okhttp:4.12.0") // implementation 作用域为 runtime
    // https://github.com/Blankj/AndroidUtilCode
    api("com.blankj:utilcodex:1.31.1") // api 作用域为 compile
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"]) // 发布 Java 组件

            // 设置 Maven 坐标
            groupId = "your.group"
            artifactId = "local-libs"
            version = "1.0.0"
        }
    }
    repositories {
        mavenLocal() // 发布到本地 Maven 仓库
    }
}

```

### 3. 发布到本地 Maven 仓库

在命令行中，切换到 `local-libs` 模块目录并运行以下命令
（或在 IDE 右侧的 Gradle 窗口中找到模块对应的 publishing 执行里面的 publishToMavenLocal）

```bash
../gradlew publishToMavenLocal
```

### 4. 在其他项目中使用

在其他项目的 `build.gradle.kts` 中，添加本地 Maven 仓库并引用您发布的库

```kotlin
repositories {
    mavenLocal() // 添加本地 Maven 仓库
}

dependencies {
    implementation 'your.group:local-libs:1.0.0' // 引用发布的库
}
```

## 二. Android Library 发布 aar

### 1. 创建

创建 Android Library 模块（Application 模块不可行），这里命名为 `android-libs`

### 2. 配置

`build.gradle.kts`

```
plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    `maven-publish`
}

android {
    // ...
    buildTypes {
        release {
            // ...
        }
        debug {
            // ...
        }
    }
}

dependencies {
    // ...
	// 添加一些远程依赖
    // https://github.com/square/okhttp
    implementation("com.squareup.okhttp3:okhttp:4.12.0") // implementation 作用域为 runtime
    // https://github.com/Blankj/AndroidUtilCode
    api("com.blankj:utilcodex:1.31.1") // api 作用域为 compile
}

// android module 的 publishing 需要放到 afterEvaluate 里面
afterEvaluate {
    println("afterEvaluate components.names = ${components.names}") // 打印所有可用组件名称
    publishing {
        publications {
            // android
            create<MavenPublication>("maven") {
                from(components["release"]) // android: components.names = [debug, release]
                // 设置 Maven 坐标
                groupId = "your.group"
                artifactId = "android-libs"
                version = "1.0.0"
            }
        }
        repositories {
            mavenLocal() // 发布到本地 Maven 仓库
        }
    }
}
```

groovy 的 `build.gradle`

```
id 'maven-publish'
```

### 3. 执行 发布

然后 cd 到 module 目录

```bash
../gradlew publishToMavenLocal
```

### 4. 在其它项目中使用

在其他项目的 `build.gradle.kts` 中，添加本地 Maven 仓库并引用您发布的库

```kotlin
repositories {
    mavenLocal() // 添加本地 Maven 仓库
}

dependencies {
    implementation 'your.group:android-libs:1.0.0' // 引用发布的库
}
```

---

## 三. 补充

### 需要注意

- 发布的模块不能包含 jar aar 文件，如果有则建议在其它项目使用时直接使用这些文件

---

### Maven 仓库认证

把 repositories 节点修改如下

```kotlin
    repositories {
        maven {
            url = uri("https://your.maven.repository.url") // 替换为您的 Maven 仓库 URL
            credentials {
                username = findProperty("mavenUsername") as String? ?: System.getenv("MAVEN_USERNAME")
                password = findProperty("mavenPassword") as String? ?: System.getenv("MAVEN_PASSWORD")
            }
        }
    }
```

在 gradle.properties 配置用户名和密码

```properties
mavenUsername=your_username
mavenPassword=your_password
```

然后可正常发布

```bash
./gradlew publish
```

---
layout: post
tags: Android
---

Android Studio / Gradle / Kotlin / Java 版本对应关系

---

- KGP (即 Kotlin Gradle plugin)

- AGP (即 Android Gradle plugin)

### KGP 与 Gradle

Kotlin 官方文档 KGP <https://kotlinlang.org/docs/gradle-configure-project.html>

KGP				| Gradle		| AGP
---				| ---			| ---
1.7.20–1.7.22	| 6.7.1–7.1.1	| 3.6.4–7.0.4
1.8.20–1.8.22	| 6.8.3–7.6.0	| 4.1.3–7.4.0
1.9.20–1.9.25	| 6.8.3–8.1.1	| 4.2.2–8.1.0

### KGP 与 KSP

KSP <https://github.com/google/ksp/releases>

从 release 查看对应版本

### Kotlin 与 Compose

Android Compose 官方文档 对照表 <https://developer.android.com/jetpack/androidx/releases/compose-kotlin>

Compose 的 kotlinCompilerExtensionVersion 会稍慢于 Kotlin 版本的更新

### AGP 与 Gradle

Android 官方文档 AGP <https://developer.android.com/build/releases/gradle-plugin>

AGP | Gradle
--- | ---
7.4 | 7.5 +
8.0 | 8.0 +
8.1 | 8.0 +
8.2 | 8.2 +
8.3 | 8.4 +
8.4 | 8.6 +
8.5 | 8.7 +
8.6 | 8.7 +
8.7 | 8.9 +

### AGP 与 Android Studio

Android Studio					| AGP
---								| ---
Flamingo, 2022.2.1				| 3.2-8.0
Giraffe, 2022.3.1				| 3.2-8.1
Hedgehog, 2023.1.1				| 3.2-8.2
Iguana, 2023.2.1				| 3.2-8.3
Jellyfish, 2023.3.1				| 3.2-8.4
Koala, 2024.1.1					| 3.2-8.5
Koala Feature Drop, 2024.1.2	| 3.2-8.6
Ladybug , 2024.2.1				| 3.2-8.7

### AGP 与 Android SDK API

Android SDK API | AGP
---				| ---
33				| 7.2+
34				| 8.1.1 +
35				| 8.6.0 +

### Gradle 与 JDK

Android Studio 中的 Java 版本 官方文档 <https://developer.android.com/build/jdks>

Gradle	| JDK
---		| ---
8.0 +	| 17
7.0 +	| 11
< 7.0	| 8

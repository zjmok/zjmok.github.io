---
layout: post
tags: Android
---

# 使用 Preferences DataStore

这里对比两种基础键值对的存储方式。（Proto DataStore 支持复杂数据类型，在另一篇）

- SharedPreferences
- Preferences DataStore

## SharedPreferences 使用, sp, prefs, 持久化

### Java

```java
// 声明
private SharedPreferences prefs;

// 初始化，在 onCreate 等
prefs = getSharedPreferences("float_window_prefs", MODE_PRIVATE);

// 获取
String scheme = prefs.getString("scheme", "https");

// 写入
prefs.edit().putString("scheme", resultScheme).apply();

```

### KTX

```kotlin
// 声明 初始化
private val prefs: SharedPreferences by lazy { getSharedPreferences("float_window_prefs", MODE_PRIVATE) }

// 获取
val scheme = prefs.getString("scheme", "https")

// 写入
prefs.edit { putString("scheme", resultScheme) }

```

## Preferences DataStore 使用, pd, pds, ds, 持久化

### Java

略

### KTX

```kotlin
// 在顶层 声明 初始化（或 Application 类中初始化）
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "float_window_prefs")

// 定义 Key（可以不统一定义，直接使用 `stringPreferencesKey(key)`）
private object PrefsKeys {
    val SCHEME = stringPreferencesKey("scheme") // 类型安全的 Key
}

// 读取 需要在协程内调用
// dataStore.data 是 flow
// first 获取第一个值并取消流，若无值会报错，或可用 catch 处理上游异常 emit 一个默认值，或可用 firstOrNull
// collect 每次数据有更新都能获取
// first 和 collect 需要在协程内调用
lifecycleScope.launch {
    val schemeFlow: Flow<String> = dataStore.data
        .map { prefs ->
//            prefs[stringPreferencesKey("scheme")] ?: "https"
            prefs[PrefsKeys.SCHEME] ?: "https"
        }
    // Flow收集方式获取
    schemeFlow.collet { scheme ->
        val lastScheme =  scheme
    }
    // 单次获取并取消流
    val scheme = schemeFlow.first() // 或 schemeFlow.firstOrNull()
}

// 写入 需要在协程内调用
lifecycleScope.launch {
    // 更新数据 使用 updateData
    dataStore.updateData { prefs ->
//        prefs[stringPreferencesKey("scheme")] = resultScheme
        prefs[PrefsKeys.SCHEME] = resultScheme
    }
}

```

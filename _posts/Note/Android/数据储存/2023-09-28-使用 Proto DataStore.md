---
layout: post
tags: Android
---

# 使用 Proto DataStore

参考 [Proto DataStore - 官方文档](https://developer.android.google.cn/codelabs/android-proto-datastore?%3Bauthuser=0&hl=vi#4)

## 添加依赖项

- 添加协议缓冲区插件
- 添加协议缓冲区和 Proto DataStore 依赖项
- 配置协议缓冲区

```kotlin
plugins {
    ...
    id("com.google.protobuf") version "0.9.4"
}

dependencies {
    implementation("androidx.datastore:datastore:1.1.1")
    implementation("com.google.protobuf:protobuf-javalite:3.21.0")
    ...
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.14.0"
    }

    // Generates the java Protobuf-lite code for the Protobufs in this project. See
    // https://github.com/google/protobuf-gradle-plugin#customizing-protobuf-compilation
    // for more information.
    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                id("java") {
                    option("lite")
                }
            }
        }
    }
}
```

## 定义和使用 protobuf 对象

**创建 proto 文件**

[Proto 语言指南](https://developers.google.cn/protocol-buffers/docs/overview?hl=vi)

创建文件 `main/proto/UserPreferences.proto`
```
syntax = "proto3";

option java_package = "com.example.datastore";
option java_multiple_files = true;

message UserPreferences {
  // 在 Proto 定义中，数字（如 1 和 2）代表字段的唯一标识符（或字段编号）。这些编号在序列化和反序列化过程中使用，用于区分消息中的不同字段。
  string username = 1;
  int32 age = 2;
}
```

UserPreferences 类在编译时会从 proto 文件中定义的 message 中生成。**请务必重新构建该项目**(`Rebuild Project`)。

**创建序列化器**

如需告知 DataStore 如何读取和写入我们在 proto 文件中定义的数据类型，我们需要实现序列化器。如果磁盘上没有数据，序列化器还会定义默认返回值。

创建序列化器，位置在 kotlin 代码的包名下即可
```
object UserPreferencesSerializer : Serializer<UserPreferences> {
    override val defaultValue: UserPreferences = UserPreferences.getDefaultInstance()
    override suspend fun readFrom(input: InputStream): UserPreferences {
        try {
            return UserPreferences.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: UserPreferences, output: OutputStream) = t.writeTo(output)
}
```

## 使用 Proto DataStore

**创建 DataStore 实例**

可以在 Activity 等页面创建

```
    // dataStore 实例
    private val dataStore: DataStore<UserPreferences> by dataStore(
        fileName = "user_preferences.pb", // 自定义名称
        serializer = UserPreferencesSerializer // 序列化器
    )
```

**使用 Proto DataStore**

```
class DataStoreRepository(private val dataStore: DataStore<UserPreferences>) {

    // 写入数据
    suspend fun updateUserPreference(username: String, age: Int) {
        // 更新数据 使用 updateData
        dataStore.updateData { userPreferences ->
            userPreferences.toBuilder()
                .setUsername(username)
                .setAge(age)
                .build()
        }
    }
	
	// 读取数据 订阅模式
    suspend fun observeUsername(listener: (String?, Int) -> Unit) {
        // dataStore.data 是 flow
        // collect 是收集之后所有 edit 的数据
        // 每次修改后会实时回调，相当于 viewModel 的 observe
        // 读取数据 使用 data
        dataStore.data.collect { preferences ->
            val username = preferences.username
            val age = preferences.age
            listener.invoke(username, age)
        }
    }
	
	// 读取数据
    suspend fun getUsername(): String? {
        // dataStore.data 是 flow
        // first 是收集最新的可用数据，最后 edit 的数据，没有 edit 则返回初始或空值
        // 读取数据 使用 data
        val userPreferences = dataStore.data
            // 捕获 dataStore 的 data 流中的异常
            .catch { exception ->
                // dataStore.data throws an IOException when an error is encountered when reading data
                if (exception is IOException) {
                    Log.e("TAG", "读取时出错", exception)
                    // 出错后给一个默认对象
                    emit(UserPreferences.getDefaultInstance())
                } else {
                    throw exception
                }
            }
            .first()
        return userPreferences.username
    }
	
    // clear field
    // clear 后返回默认值。String 类型返回空串 `""`，Int 类型返回 `0`
    // 在 Proto 3 中，string 类型的字段默认值是空字符串 ""，而不是 null
    suspend fun clearUsername() {
        dataStore.updateData { userPreferences ->
            userPreferences.toBuilder()
                .clearUsername() // 清空 username
//                .clearAge() // 清空 age
//                .clear() // 清空所有字段
                .build()
        }
    }

    // clear all field
    suspend fun clearAllField() {
        dataStore.updateData { userPreferences ->
            userPreferences.toBuilder()
                .clear() // 清空所有字段
                .build()
        }
    }

}
```

---

## 定义 List

```
syntax = "proto3";

option java_package = "com.example.wan.android.data";
option java_multiple_files = true;

// WebPage
message WebPage {
  string url = 1;
  string title = 2;
}

// List<WebPage>
message WebHistory {
  repeated WebPage pages = 1;
}
```

```
    private val historyList = mutableListOf<WebPage>()

    // dataStore 实例
    private val dataStore: DataStore<WebHistory> by dataStore(
        fileName = "web_page_history.pb", // 自定义名称
        serializer = WebHistorySerializer // 序列化器
    )
```


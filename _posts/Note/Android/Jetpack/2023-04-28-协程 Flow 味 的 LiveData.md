---
layout: post
tags: Android LiveData
---

## 普通 LiveData

```
    // 可变 不对外暴漏
    private val _data = MutableLiveData<String>()
    // 不可变 对外暴漏
    val data: LiveData<String>
        get() = _data
    
    fun action() {
        // 经过一系列数据处理
        _data.value = "new_value"
    }
```

`action()` 可能会被多次调用，导致多次的数据处理，例如多次请求网络

解决办法，可以放在 `ViewModel` 的 `init{}` 构造代码块，但是代码可读性和简洁性会降低

于是，便有了下文 `CoroutineLiveData`

## `CoroutineLiveData` build 模式构建

```
    implementation 'androidx.lifecycle:lifecycle-livedata-ktx:2.6.1'
```

通过 `liveData(){}` 创建, CoroutineLiveData 是内部类 不能直接使用

简单使用

```
    var data = MutableLiveData<Int>()

    var testData: LiveData<Int> = liveData {
        // 发生单个数据
        emit(666)
        // 发生一个相应的 LiveData 对象
        emitSource(data)
    }
```

通过 `livedata{}` 扩展方法动态构建一个不可变的 `LiveData`，  
调用 `emit()` 或 `emitSource()` 就相当于调用之前 `LiveData` 的 `setValue()`

官方例子

```
val user = liveData<Model> {
    var backOffTime = 1_000
    var succeeded = false
    while(!succeeded) {
        try {
            emit(api.fetch(id))
            succeeded = true
        } catch(ioError : IOException) {
            delay(backOffTime)
            // 每次轮询执行的时间间隔都会增加，最大60s
            backOffTime *= minOf(backOffTime * 2, 60_000)
        }
    }
}

```

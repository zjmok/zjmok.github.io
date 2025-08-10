---
layout: post
tags: Kotlin
---

`lateinit` `by lazy` `getter`

---

`lateinit` 仅能配合 `var` 使用，可定义不可空变量时不初始化，但以后使用前也必须先赋值
```
    private lateinit var context: Context
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this
    }
```

`by lazy` 仅能配合 `val` 使用，第一次调用变量时就会初始化，且仅会执行一次代码块，以后每次调用变量都返回第一次的结果
```
    private val number by lazy {
        AtyMainBinding.inflate(layoutInflater)
    }
```

`getter` 配合 `var` `val` 都可，但 `val` 的变量是不可变的 不能设置 `setter` ,  
每次调用变量都会相当于调用一个get()函数，都会执行一遍代码块
```
    private var string = "qwer"
        set(value) {
            field = value + "asdf"
        }
        get() {
            return field + "zxcv"
        }

    private val string1 = "qwer"
//        set(value) {
//            field = value + "asdf"
//        }
        get() {
            return field + "zxcv"
        }
```

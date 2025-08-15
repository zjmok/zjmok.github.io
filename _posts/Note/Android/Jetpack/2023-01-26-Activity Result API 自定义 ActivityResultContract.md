---
layout: post
tags: Android
---

# Activity Result API

作用，替代 onActivityResult，减少代码耦合

## Android 预定义的 ActivityResultContract

- ActivityResultContracts

ActivityResultContracts 里有一堆预定义好的 ActivityResultContract 实现类

```
// 传入 Intent 自定义跳转
ActivityResultContracts.StartActivityForResult
// 拍照
ActivityResultContracts.TakePicture
// 内容选择 传入 content-type 获取相应文件
ActivityResultContracts.GetContent
// ... 还有很多
```

---

## 自定义实现 ActivityResultContract

- 定义，继承 ActivityResultContract

SecondActivityResultContract.kt
```
class SecondActivityResultContract : ActivityResultContract<String, String?>() {

    override fun createIntent(context: Context, input: String): Intent {
        return Intent(context, SecondActivity::class.java).apply {
            putExtra("data", input)
        }
    }

    override fun parseResult(resultCode: Int, intent: Intent?): String? {
	    return intent?.takeIf { resultCode == Activity.RESULT_OK }?.getStringExtra("result")
    }

}
```

- 使用，在 registerForActivityResult 注册自定义的 ActivityResultContract

> registerForActivityResult 必须要在 onStart 之前执行（定义），可以放在成员变量或 onCreate 里

MainActivity.kt
```
// ...
    private val secondActivityLauncher = registerForActivityResult(SecondActivityResultContract()) {
        Toast.makeText(this, "返回 result: ${it}", Toast.LENGTH_SHORT).show()
    }
// ...
    view.onClick {
        secondActivityLauncher.launch(">>>")
    }
// ...
```

SecondActivity.kt
```
// ...
    val data = intent.getStringExtra("data")
// ...
    setResult(RESULT_OK, Intent().apply {
        putExtra("result", "<<<")
    })
    finish()
// ...
```


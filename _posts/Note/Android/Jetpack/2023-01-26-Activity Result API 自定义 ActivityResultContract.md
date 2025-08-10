---
layout: post
tags: Android
---

# Activity Result API

代替 onActivityResult

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

- ActivityResultContract 自定义实现

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

MainActivity.kt
```
// ...
    // registerForActivityResult 必须要在 onStart 之前执行
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


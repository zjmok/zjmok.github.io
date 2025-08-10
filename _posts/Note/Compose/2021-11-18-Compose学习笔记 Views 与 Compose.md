---
layout: post
tags: Android Compose
---

[Google 官方文档](https://developer.android.google.cn/jetpack/compose/interop?hl=zh-cn)


## Compose 与 Android 框架
CompositionLocal 用于为 Compose 中的 Android 框架类型（例如 Context、Configuration 或 View）传递值，
其中 Compose 代码托管在相应的 `LocalContext`、`LocalConfiguration` 或 `LocalView` 中。
请注意，CompositionLocal 类的前缀是 Local，IDE 中的自动补全功能可以很轻松地检测到这些类。
```
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val view = LocalView.current
    val windowInfo = LocalWindowInfo.current
//    val xxx = LocalXxx.current
    
}
```

## Android Views 中的 Compose , `ComposeView` 作为桥梁

将 Compose 界面内容并入 fragment 或现有视图布局 `ComposeView.setContent()` (ComposeView也可写在布局)

并入 fragment 关联生命周期
```
override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
    return ComposeView(requireContext()).apply {
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        setContent {
            Text("HomeFragment")
        }
    }
}
```

如果同一布局中存在多个 `ComposeView` 元素，每个元素必须具有唯一的 ID 才能使 `savedInstanceState` 发挥作用，
```
addView(ComposeView(...).apply {
        id = R.id.compose_view_x
        ...
      }
```

`ComposeView` ID 在 `res/values/ids.xml` 文件中进行定义
```
<resources>
    <item name="compose_view_x" type="id" />
    <item name="compose_view_y" type="id" />
</resources>
```

## Compose 中的 Android Views , `AndroidView` 作为桥梁

系统会向 AndroidView 传递一个返回 View 的 lambda。AndroidView 还提供了在视图膨胀时被调用的 update 回调。每当在该回调中读取的 State 发生变化时，AndroidView 都会重组

```
@Composable
fun CustomView() {
    val selectedItem = remember { mutableStateOf(0) }

    // Adds view to Compose
    AndroidView(
        modifier = Modifier.fillMaxSize(), // Occupy the max size in the Compose UI tree
        factory = { context ->
            // Creates custom view
            CustomView(context).apply {
                // Sets up listeners for View -> Compose communication
                myView.setOnClickListener {
                    selectedItem.value = 1
                }
            }
        },
        update = { view ->
            // View's been inflated or state read in this block has been updated
            // Add logic here if necessary

            // As selectedItem is read here, AndroidView will recompose
            // whenever the state changes
            // Example of Compose -> View communication
            view.coordinator.selectedItem = selectedItem.value
        }
    )
}

@Composable
fun ContentExample() {
    Column(Modifier.fillMaxSize()) {
        Text("Look at this CustomView!")
        CustomView()
    }
}
```

如需嵌入 XML 布局

使用 `androidx.compose.ui:ui-viewbinding` 库 提供的 `AndroidViewBinding`  
layout_example.xml 生成的
```
@Composable
fun AndroidViewBindingExample() {
    AndroidViewBinding(ExampleLayoutBinding::inflate) {
        exampleView.setBackgroundColor(Color.GRAY)
    }
}
```

## `compose-bom` Bill of Material 的缩写

```
    // Compose BoM
    def composeBomVersion = "2023.06.01"
    api  platform("androidx.compose:compose-bom:${composeBomVersion}")
    androidTestApi  platform("androidx.compose:compose-bom:${composeBomVersion}")
```

可以统一管理 Compose 库版本，确保应用中的 Compose 库版本兼容，这只是管理版本，还是要自己添加使用的库到项目的

---
layout: post
tags: Android Compose
---

## Compose 中的 Dp sp px 互转

```
// compose px2dp
@Composable
fun Float.px2dp(): Dp {
    return with(LocalDensity.current) {
        this@px2dp.toDp()
    }
}

// compose px2sp
@Composable
fun Float.px2sp(): TextUnit {
    return with(LocalDensity.current) {
        this@px2sp.toSp()
    }
}

// compose dp2px
@Composable
fun Dp.dp2px(): Float {
    return with(LocalDensity.current) {
        this@dp2px.toPx()
    }
}

// compose sp2px
@Composable
fun TextUnit.sp2px(): Float {
    return with(LocalDensity.current) {
        this@sp2px.toPx()
    }
}
```

## 在 Jetpack Compose 中，`Divider`、`Spacer` 和 `Box` 都可以用于创建分割和间隙：

### Divider
- **用途**：用于在两个组件之间添加一条分割线。
- **特点**：视觉上明显的分割线，通常用于分隔内容区域。
- **示例**：

  ```kotlin
  @Composable
  fun MyComposable() {
      Column {
          Text("Top")
          Divider(color = Color.Gray, thickness = 1.dp)
          Text("Bottom")
      }
  }
  ```

### Spacer
- **用途**：用于在组件之间添加空白间隙。
- **特点**：可以灵活调整间隙的大小，适用于需要动态调整布局的情况。
- **示例**：

  ```kotlin
  @Composable
  fun MyComposable() {
      Column {
          Text("Top")
          Spacer(modifier = Modifier.height(16.dp))
          Text("Bottom")
      }
  }
  ```

### Box
- **用途**：用于包裹其他组件，可以通过设置 `padding` 来创建间隙。
- **特点**：灵活性高，可以包含多个子组件并进行复杂布局。
- **示例**：

  ```kotlin
  @Composable
  fun MyComposable() {
      Box(modifier = Modifier.padding(16.dp)) {
          Text("Content inside a Box")
      }
  }
  ```

### 选择建议
- **Divider**：适用于需要明确视觉分割的情况，例如列表项之间的分割。
- **Spacer**：适用于需要灵活调整间隙大小的情况，例如在垂直或水平布局中创建空白间隙。
- **Box**：适用于需要包裹多个组件并进行复杂布局的情况，可以通过 `padding` 来创建间隙。

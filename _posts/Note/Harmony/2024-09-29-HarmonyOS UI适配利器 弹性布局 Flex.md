---
layout: post
tags: Harmony
---

# HarmonyOS 弹性布局 Flex

## 布局方向 direction

```
Flex({ direction: FlexDirection.Row }) {

}
```

direction 可选

- `Row`
- `RowReverse` 水平，反向
- `Column`
- `ColumnReverse` 

## 布局换行 wrap

```
Flex({ wrap: FlexWrap.NoWrap }) {

}
```

wrap 可选

- `NoWrap` 默认值，不换行
- `Wrap` 换行
- `WrapReverse` 反向+换行

## 主轴对齐方式 justifyContent

```
Flex({ justifyContent: FlexAlign.Start }) {

}
```

justifyContent 可选

- `Start` 
- `Center`
- `End`
- `SpaceBetween` 撑开
- `SpaceAround` 每个元素左右都有相等的距离，即间隔 1:2:2:...:2:2:1
- `SpaceEvenly` 所有间隔相对，即间隔 1:1:1:...:1:1:1

## 交叉轴对齐方式

### 容器组件设置交叉轴对齐 alignItems

```
Flex({ alignItems: ItemAlign.Auto }) {

}
```

alignItems 可选

- `Auto` 默认值，
- `Start` 顶部对齐
- `Center` 居中对齐
- `End` 底部对齐
- `Stretch` 在未设置尺寸时，拉伸到容器尺寸，顶部和底部都对齐
- `Baseline` 文本基线对齐

### 子元素设置交叉轴对齐 alignSelf

会覆盖父元素设置的

```
Flex({ direction: FlexDirection.Row, alignItems: ItemAlign.Center }) { // 容器组件设置子元素居中
  Text('alignSelf Start').width('25%').height(80)
    .alignSelf(ItemAlign.Start) // 子元素单独设置自己的对齐方式为 Start，将覆盖父容器的
    .backgroundColor(0xF5DEB3)

  Text('no alignSelf').width('25%').height(100)
    .backgroundColor(0xF5DEB3)
    .alignSelf(ItemAlign.End)  // End

  Text('no alignSelf').width('25%').height(100)
    .backgroundColor(0xF5DEB3)
}
```

alignSelf 可选，同 alignItems

- `Auto` 默认值，
- `Start` 顶部对齐
- `Center` 居中对齐
- `End` 底部对齐
- `Stretch` 在未设置尺寸时，拉伸到容器尺寸，顶部和底部都对齐
- `Baseline` 文本基线对齐

### 行对齐（内容对齐） alignContent

只在多行的Flex布局中生效（效果是每一行当作一个元素，像主轴对齐方式 justifyContent）

```
Flex({ justifyContent: FlexAlign.SpaceBetween, wrap: FlexWrap.Wrap, alignContent: FlexAlign.Start }) {
  Text('1').width('30%').height(20).backgroundColor(0xF5DEB3)
  Text('2').width('60%').height(20).backgroundColor(0xD2B48C)
  Text('3').width('40%').height(20).backgroundColor(0xD2B48C)
  Text('4').width('30%').height(20).backgroundColor(0xF5DEB3)
  Text('5').width('20%').height(20).backgroundColor(0xD2B48C)
}
.width('90%')
.height(100)
.backgroundColor(0xAFEEEE)          
```

alignContent 可选，同 justifyContent

- `Start` 
- `Center`
- `End`
- `SpaceBetween` 撑开
- `SpaceAround` 每个元素左右都有相等的距离，即间隔 1:2:2:...:2:2:1
- `SpaceEvenly` 所有间隔相对，即间隔 1:1:1:...:1:1:1

## 自适应拉伸

### flexBasis 主轴方向上的基准尺寸（row 方向时覆盖 width，column 方向时覆盖 height）

- `.flexBasis('auto')` 使用 width/height
- `.flexBasis(100)` 覆盖 width/height
- flexBasis 和 width/height 都未设置时，为内容自身宽度

### flexGrow 剩余空间分配给此属性所在组件的比例（row 方向时分配 width，colume 方向时分配 height）

假设是 row 方向时，总宽度减去元素的原始宽度是剩余空间，再把剩余空间按照 flexGrow 设置的比例分配

```
Flex() {
  Text('flexGrow(2)')
    .flexGrow(2)
    .width(100)
    .height(100)
    .backgroundColor(0xF5DEB3)

  Text('flexGrow(3)')
    .flexGrow(3)
    .width(100)
    .height(100)
    .backgroundColor(0xD2B48C)

  Text('no flexGrow')
    .width(100)
    .height(100)
    .backgroundColor(0xF5DEB3)
}.width(420).height(120).padding(10).backgroundColor(0xAFEEEE)
```

### flexShrink 空间不足时，子元素的压缩比例（row 方向时压缩 width，colume 方向时压缩 height）

flexShrink 值越大，压缩后的值越小

```
Flex({ direction: FlexDirection.Row }) {
  Text('flexShrink(3)')
    .flexShrink(3)
    .width(200)
    .height(100)
    .backgroundColor(0xF5DEB3)
  
  Text('no flexShrink')
    .width(200)
    .height(100)
    .backgroundColor(0xD2B48C)

  Text('flexShrink(2)')
    .flexShrink(2)
    .width(200)
    .height(100)
    .backgroundColor(0xF5DEB3)  
}.width(400).height(120).padding(10).backgroundColor(0xAFEEEE) 
```

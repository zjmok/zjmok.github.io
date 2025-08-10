---
layout: post
tags: Harmony
---

# HarmonyOS学习笔记-基础组件

[使用指南](https://developer.huawei.com/consumer/cn/doc/harmonyos-guides-V5/arkts-ui-development-V5)

[API 参考](https://developer.huawei.com/consumer/cn/doc/harmonyos-references-V5/arkui-declarative-comp-V5)

ArkTS 组件

- 通用
  - 通用事件
  - 通用属性
  - 手势处理
  
- UI 组件

  - 布局
  
    - 线性布局 (Row/Column)
    - 层叠布局 (Stack)
    - 弹性布局 (Flex)
    - 相对布局 (RelativeContainer)
    - 栅格布局 (GridRow/GridCol)
    - 媒体查询 (@ohos.mediaquery)
    - 创建列表 (List)
    - 创建网格 (Grid/GridItem)
    - 创建轮播 (Swiper)
    - 选项卡 (Tabs)
	
  - 组件
  
    - 按钮 (Button)
    - 单选框 (Radio)
    - 切换按钮 (Toggle)
    - 进度条 (Progress)
    - 显示图片 (Image)
    - 视频播放 (Video)
    - 自定义渲染 (XComponent)
    
    - 气泡提示 (Popup)
    - 菜单（Menu）
	
	- Refresh 刷新
	- SwipeRefresher 刷新加载组件
	
  - 导航
    
	- 组件导航 (Navigation)(推荐)
    - 页面路由 (@ohos.router)(不推荐)
    - Router切换Navigation
	
  - 文本
  
    - 文本显示 (Text/Span)
    - 文本输入 (TextInput/TextArea)
    - 富文本（RichEditor）
    - 图标小符号 (SymbolGlyph/SymbolSpan)
    - 属性字符串（StyledString/MutableStyledString）

  - 空白与分隔

    - Blank
    - Divider

  - 弹窗
  
    - 模态弹窗 (ModelDialog)
    - 不依赖UI组件的全局自定义弹窗 (推荐)
    - 自定义弹窗 (CustomDialog)

  - 图形
  
    - 绘制几何图形 (Shape)
    - 使用画布绘制自定义图形 (Canvas)
	
  - 动画
  
    - 属性动画
    - 转场动画
    - 粒子动画
    - 组件动画
    - 动画曲线
    - 动画衔接
    - 动画效果
    - 帧动画
	
---

图片缩放类型 <https://developer.huawei.com/consumer/cn/doc/harmonyos-guides-V5/arkts-graphics-display-V5#设置图片缩放类型>

Row/Column 对齐方式 <https://developer.huawei.com/consumer/cn/doc/harmonyos-guides-V5/arkts-layout-development-linear-V5#column容器内子元素在垂直方向上的排列>

Row/Column 自适应缩放，按比例分配子元素 <https://developer.huawei.com/consumer/cn/doc/harmonyos-guides-V5/arkts-layout-development-linear-V5#自适应缩放>

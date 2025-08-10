---
layout: post
tags: Harmony
---

# HarmonyOS学习笔记-组件生命周期

<https://developer.huawei.com/consumer/cn/doc/harmonyos-guides-V5/arkts-custom-components-V5>

1. 页面生命周期（`@Entry` 组件）

- onPageShow
- onPageHide
- onBackPress

2. 组件生命周期（`@Component` 组件）

- aboutToAppear
- onBindBuild
- aboutToDisappear（组件的销毁是从组件树上直接摘下子树，顺序是先调用父组件）

3. 整体生命周期流程图

```
                                          ┌─<─────────────────────────────────────────────<┐
                                          │    (页面显示、路由、切换到前台)                │
                                          │                                                │
                                          │                (页面隐藏、路由、切换到后台)    │
                                          │                                 ┌──────>┐      │
                                          │                                 │       │      │
aboutToAppear ──> build ──> onDiBuild ──> onPageShow ──（Component is visible）──> onPageHide ──> aboutToAppear
                                          │                                         │
                                          │                                         │
                                          │                                         │
                                          │             (点击返回按钮)              │
                                          └─────────────> onBackPress ─────────────>┘
```

4. 包含子组件的流程

冷启动的初始化流程（MyComponent 是 @Entry 的，Child 是 MyComponent 的子组件）

```
MyComponent aboutToAppear --> MyComponent build --> MyComponent onDidBuild                                                              --> MyComponent onPageShow
                                                                           --> Child aboutToAppear --> Child build --> Child onDidBuild
```


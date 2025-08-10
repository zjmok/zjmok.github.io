---
layout: post
tags: Flutter
---

在 Flutter 中，常常提到的 "三棵树" 是指 Widget 树、Element 树和 RenderObject 树。这些树形结构相互关联，用于描述和渲染 Flutter 应用程序的界面

# 三棵树

## Widget

- Widget 树是由各种不同类型的 Widget 组成的层次结构。
- Widget 是 Flutter UI 构建的基本单元，表示各种不同的 UI 组件。
- Widget 树通过组合各种小部件来创建整个应用程序的界面布局。
- Widget 是不可变的，就像构建块一样，可以嵌套和组合形成复杂的界面。

## Element

- Element 树是 Widget 树的实例化和运行时表示。
- 每个 Widget 在运行时都会被实例化为一个对应的 Element。
- Element 负责管理与 Widget 相关的状态和生命周期，并负责处理事件和更新 UI。
- Element 树与 Widget 树有着一一对应的关系。

## Render

- RenderObject 树是 Flutter 渲染引擎内部使用的树形结构。
- 它是从 Widget 树和 Element 树派生出来的，用于最终将界面渲染到屏幕上。
- RenderObject 包含具体的布局和绘制信息，并负责计算布局、处理绘制和处理点击事件等底层操作。

这三棵树共同工作，将我们定义的 Widget 转化为可视化的用户界面。
Widget 树提供了抽象的 UI 组件，Element 树管理状态和生命周期，RenderObject 树负责计算布局和绘制。
它们相互协作，实现了 Flutter 强大的声明式 UI 编程模型。

---

# Widget 分类

## 常见分类 (3种)：

### 基础 Widget

- 基础 Widget 是 Flutter 提供的一组预定义的基本 UI 组件，用于构建应用程序的用户界面。
- 这些组件包括 Text、Image、Button、Container、Row、Column 等。
- 基础 Widget 提供了常见的布局和样式功能，可以满足大多数简单的 UI 需求。

### 组合 Widget

- 组合 Widget 是通过将多个基础 Widget 或其他组合 Widget 组合在一起来构建复杂的 UI 组件。
- 通过嵌套和组合不同类型的 Widget，可以创建出具有更高层次结构和复杂功能的 UI 元素。
- 组合 Widget 的目的是将 UI 分解为更小的可重用部分，使代码更易维护和扩展。

### 自定义 Widget

- 自定义 Widget 是根据特定需求开发者自己定义的 UI 组件。
- 通过继承现有的基础 Widget 或组合其他 Widget 来创建新的自定义 Widget。
- 自定义 Widget 具有自己的属性和行为，可以封装复杂的业务逻辑，并提供更高级别的抽象。

这种分类方式是一种常见的方式，它帮助开发者理解和组织 Flutter 应用程序中的各种 UI 组件。
无论是基础 Widget、组合 Widget 还是自定义 Widget，它们都可以在 Flutter 应用程序中相互配合使用，以创建各种丰富和灵活的用户界面。

## 根据功能和作用分类 (3种)：

### 组合类 Widget
   
- 组合类 Widget 是由多个子 Widget 组合而成的 Widget。
- 这些 Widget 通过将其他 Widget 嵌套或排列在一起来构建复杂的 UI 布局。
- 例如，Row、Column、Stack、ListView 等都属于组合类 Widget。
- 组合类 Widget 可以帮助开发者创建灵活且可重用的布局结构。

Flutter 中比较熟悉的 StatelessWidget & StatefulWidget，都属于组合类的 Widget，实际上他们并不负责绘制，仅仅起到组合子 Widget 的作用
   
### 代理类 Widget

- 代理类 Widget 是一种中介或包装器 Widget，它将某个部分的渲染委托给其他 Widget 处理。
- 这些 Widget 可以修改传递给它们的子 Widget 的行为或外观。
- 例如，ProxyWidget、AnimatedBuilder、LayoutBuilder 等都属于代理类 Widget。
- 代理类 Widget 允许开发者对特定 Widget 进行修饰或添加额外的功能。

### 绘制类 Widget

- 绘制类 Widget 用于直接在屏幕上绘制自定义图形或效果。
- 这些 Widget 提供了更底层的绘制能力，需要开发者自己处理绘制逻辑。
- 例如，CustomPaint、RepaintBoundary、ClipRect 等都属于绘制类 Widget。
- 绘制类 Widget 允许开发者创建自定义的绘制操作，实现特定的视觉效果。

这些分类是为了更好地理解和组织 Flutter 中的不同类型的 Widget。
但需要注意，某个 Widget 可能同时属于多个类别，因为它可能具有组合、代理和绘制的功能。
了解这些不同类别的 Widget 可以帮助开发者更好地选择和使用适当的 Widget 来构建 Flutter 应用程序的用户界面。

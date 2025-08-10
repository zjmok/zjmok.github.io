---
layout: post
tags: Harmony
---

## @Build @LocalBuilder @BuildParam 注解

### @Build

@Build 的函数只能被 build 或其它自定义构造函数调用。

可以在 Component 中定义为私有，也可定义为全局函数（代码位置跟 Component 同级）

私有
```
@Builder MyBuilderFunction() { ... } // 使用 @Build 时，里面的 this 是调用处的 this
// 在 build 方法使用
this.MyBuilderFunction()
```

全局
```
@Builder function MyGlobalBuilderFunction() { ... } // 如果不涉及组件状态变化，建议使用全局的自定义构建方法
// 在 build 方法使用
MyGlobalBuilderFunction()
```

### @LocalBuilder

```
@LocalBuilder MyBuilderFunction() { ... } //  使用 @LocalBuilder 时，里面的 this 是定义处的 this
// 在 build 方法使用
this.MyBuilderFunction()
```

### @BuildParam

@BuilderParam 用来装饰指向 @Builder 方法的变量（@BuilderParam 是用来承接 @Builder 函数的）

```
@Builder function overBuilder() {}

@Component
struct Child {
  @Builder doNothingBuilder() {};

  // 使用自定义组件的自定义构建函数初始化@BuilderParam
  @BuilderParam customBuilderParam: () => void = this.doNothingBuilder;
  // 使用全局自定义构建函数初始化@BuilderParam
  @BuilderParam customOverBuilderParam: () => void = overBuilder;
  
  build(){}
}
```

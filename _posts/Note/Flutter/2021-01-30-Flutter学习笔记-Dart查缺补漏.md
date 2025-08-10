---
layout: post
tags: Flutter
---

Dart

初始化列表  
在构造方法后加`:xxx`

```
class A{
  int a = 0;
  A(): a++{
  
  }
}
```

接口

mixins  
- 作为mixins只能继承自Object
- 作为mixins不能有构造方法
- 一个类可以mixins多个mixins类
- mixins不是继承也不是实现接口，而是一种全新的特性
- extent with 后面类的方法覆盖前面类的方法

---

Flutter
热加载(R) 显示网格(P) iOS/Android显示UI切换(O)

---

PageView 默认不保存状态，体验不佳  
解决方案: AutomaticKeepAliveClientMixin

---

PageView 代码设置 index
animateToPage 或 jumpToPage

---

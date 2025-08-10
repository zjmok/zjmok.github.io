---
layout: post
tags: Android
---

ViewBinding

区别 

- 与 findViewById 的区别：空安全和类型安全，不存在因引用了一个错误的id而导致的空指针异常或者类型转换异常。

- 与 DataBinding 的区别：DataBinding 仅处理使用 <layout 代码创建的数据绑定布局；ViewBinding 不支持布局变量或布局表达式，因此它不能用于在xml中将布局与数据绑定。

- 与 kotlin-android-extensions 的区别：在使用上，后者简单粗暴，直接id进行访问，而 ViewBinding 需要创建绑定类的实例；后者有一些不友好的地方，比如相同的id存在于多个xml，容易导错包，如果包导错了，会有可能别的View用错id导致空指针，而 ViewBinding 显然不会有这种情况。

- 与 anko 的区别：跟 kotlin-android-extensions 差不多，但 anko 支持更简洁的代码

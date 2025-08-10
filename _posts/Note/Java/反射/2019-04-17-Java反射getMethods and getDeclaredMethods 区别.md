---
layout: post
tags: Java
---

反射中 `getMethods()` 与 `getDeclaredMethods()` 的区别

- public Method[] getMethods()

返回某个类的所有公用（public）方法包括其继承类的公用方法，当然也包括它所实现接口的方法。

- public Method[] getDeclaredMethods()

对象表示的类或接口声明的所有方法，包括公共、保护、默认（包）访问和私有方法，但不包括继承的方法。当然也包括它所实现接口的方法。

后者为暴力反射

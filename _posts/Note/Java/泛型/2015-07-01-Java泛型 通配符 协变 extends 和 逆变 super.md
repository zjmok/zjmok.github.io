---
layout: post
tags: Java Kotlin
---

## Java 泛型

#### 定义

逆变与协变用来描述类型转换（type transformation）后的继承关系，其定义：如果A、B表示类型，f(⋅)表示类型转换，≤表示继承关系（比如，A≤B表示A是由B派生出来的子类）

f(⋅)是逆变（contravariant）的，当A≤B时有f(B)≤f(A)成立；  
f(⋅)是协变（covariant）的，当A≤B时有f(A)≤f(B)成立；  
f(⋅)是不变（invariant）的，当A≤B时上述两个式子均不成立，即f(A)与f(B)相互之间没有继承关系  

#### 协变（用于生成者，即只能作为输出）

`<? extends>` 实现了泛型的协变
```
public void process(List<? extends Number> list) {
    for (Number number : list) {
        System.out.println(number);
    }
}
```
`?` 是子类，协变同时固定上边界

#### 逆变（用于消费者，即只能作为输入）

`<? super>` 实现了泛型的逆变
```
public void addToList(List<? super Integer> list) {
    list.add(1);
    list.add(1.2f);
}
```
`?` 是父类，逆变同时固定下边界

#### 上限、下限边界

- 使用 extends 关键字来限制泛型的上限，确保泛型类型是特定类或接口的子类。

```
public <T extends Number> void printNumber(T number) {
    System.out.println(number);
}

// 使用
printNumber(42); // 整数
printNumber(3.14); // 浮点数
```

这里使用 `T extends Number` 是 `T` 不是 `?`

- Java 不支持下限边界，但可以通过使用通配符 ? super T 来实现某种程度的下限约束。

不能直接支持，所以要用逆变的通配符 `?`

#### 使用时机：

- 要从泛型类取数据时，用 extends

- 要往泛型类写数据时，用 super

- 既要取又要写，就不用通配符（即 extends 与 super 都不用）

《Effective Java》中提到: producer-extends, consumer-super.

## Kotlin 泛型

- 协变 `<out T>`
```
class Box<out T>(private val value: T) {
    fun getValue(): T = value
}
```

- 逆变 `<in T>`
```
class Box<in T> {
    fun putValue(value: T) {
        // 处理输入
    }
}
```

- 类型上限 `<T: Number>`
```
fun <T : Number> printNumber(value: T) {
    println(value)
}

// 使用
printNumber(42) // 输出: 42
printNumber(3.14) // 输出: 3.14
```

- 类型下限。Kotlin 不直接支持类型下限的语法，但可以通过使用 in 关键字来实现某种程度的下限约束

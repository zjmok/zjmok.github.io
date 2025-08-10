---
layout: post
tags: Kotlin
---

| 函数名称   | 返回值类型              | `this`/`it` 使用方式       | 使用场景                                               | 示例代码                                                                                             |
|------------|-------------------------|----------------------------|--------------------------------------------------------|------------------------------------------------------------------------------------------------------|
| **`let`**  | 闭包结果                | 使用 `it` 作为上下文对象   | 适合对非空对象执行操作或链式调用                       | ```kotlin<br>val result = str?.let { it.uppercase() }<br>println(result) // 输出大写字符串```        |
| **`run`**  | 闭包结果                | 使用 `this` 作为上下文对象 | 适合在对象上执行代码块并返回计算结果                   | ```kotlin<br>val result = str?.run { this.uppercase() }<br>println(result) // 输出大写字符串```      |
| **`with`** | 闭包结果                | 使用 `this` 作为上下文对象 | 适合不返回对象本身而对对象进行操作                     | ```kotlin<br>val result = with(str) { this.uppercase() }<br>println(result) // 输出大写字符串```     |
| **`apply`**| 对象本身（`this`）      | 使用 `this` 作为上下文对象 | 修改对象自身属性并返回对象本身，通常用于对象初始化     | ```kotlin<br>val person = Person().apply { name = "Tom"; age = 20 }<br>println(person.name)```       |
| **`also`** | 对象本身（`it`）        | 使用 `it` 作为上下文对象   | 用于对象链式调用中，同时执行额外操作，如日志或调试信息 | ```kotlin<br>val person = Person().also { println("Created: $it") }<br>```                           |

---

如何选择标准函数

需要返回值本身(this)

- YES 返回调用者

  传递参数

  - this -> T.apply()

  - it -> T.also()

- NO 返回闭包结果

  需要扩展函数(空检测 链式调用)

  - YES 

    传递参数

    - this -> T.run()

    - it -> T.let()

  - NO 

    传递参数

    - this -> with(T)

    - it -> run(T)

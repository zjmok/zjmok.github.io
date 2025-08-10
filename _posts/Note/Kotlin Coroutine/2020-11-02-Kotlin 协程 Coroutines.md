---
layout: post
tags: Kotlin
---

协程

---

GlobalScope.launch{} 在后台启动一个新的协程并继续

delay 非阻塞的等待(让出CPU资源), 挂起协程，并且只能在协程中使用

Thread.sleep 阻塞所在线程

runBlocking{} 阻塞线程 直到 runBlocking 内部的协程执行完毕
  - 挂起函数
  ```
  fun testMySuspendingFunction() = runBlocking<Unit> {
        // 这里我们可以使用任何喜欢的断言风格来使用挂起函数
    }
  ```

Job.join 从 launch 返回 Job对象 调用 join 函数 即可马上启动一个协程
```
val job = GlobalScope.launch { // 启动一个新协程并保持对这个作业的引用
    delay(1000L)
    println("World!")
}
println("Hello,")
job.join() // 等待直到子协程执行结束
```

协程作用域中 所有协程(即包含协程中创建的新协程)执行完才结束


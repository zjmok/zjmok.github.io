---
layout: post
tags: Android
---

# Handler

---

Handler 使用中隐藏的坑

1. delay 的时间过长，导致 activity 未被回收内存泄漏以及逻辑错误  
可以将 Handler 写成 static 静态内部类，或者而降 handler 中引用的 activity 为软引用 `WeakReference`

2. new 了过多的 message，导致内存泄漏，应该在处理后 remove 这些 msg

3. Activity finish() 后应该 remove 所有的 msg 和 runable

---

## 各种类的含义

- Thread：线程

- Looper：消息泵

- MessageQueue：消息队列

- Message：消息

- Handler：处理者

---

- 一个线程对应一个Looper

- 一个Looper对应一个MessageQueue

- 一个Looper可以对应多个Handler

- 不确定当前线程时，更新UI时尽量调用post方法

---

主线程创建 Handler 会自动创建 Looper

子线程中的 Handler 一定要手动创建 Looper

---

## handler 处理 message 延时的机制

发送消息后都会走到 `sendMessageAtTime` 方法，参数是 Message 和 一个 uptimeMillis ，这个时间是当前时间+延时时间

到这里handler的任务就完成了，把 message 发送到 messageQueue 里面，每个消息都会带有一个 uptimeMillis 参数，这就是延时的时间

接下来会将msg根据实际执行时间进行排序插入到queue里面

接着会调用 `nativiePollOnce` 去调用native函数 `epoll_wait`

epoll：linux的一个I/O多路复用机制、一种I/O事件通知机制

Linux里的I/O多路复用机制：举个例子就是我们钓鱼的时候，为了保证可以最短的时间钓到最多的鱼，我们同一时间摆放多个鱼竿，同时钓鱼。然后哪个鱼竿有鱼儿咬钩了，我们就把哪个鱼竿上面的鱼钓起来。这里就是把这些全部message放到这个机制里面，那个time到了，就执行那个message。一个空间换时间的方式

[参考链接](https://www.jianshu.com/p/68083d432b3f)

---

### 主线程的死循环一直运行是不是特别消耗CPU资源呢？Handler 死循环为何不会卡死？

其实不然，这里就涉及到Linux pipe/epoll机制，简单说就是在主线程的MessageQueue没有消息时，便阻塞在loop的queue.next()中的nativePollOnce()方法里，此时主线程会释放CPU资源进入休眠状态，直到下个消息到达或者有事务发生，通过往pipe管道写端写入数据来唤醒主线程工作。这里采用的epoll机制，是一种IO多路复用机制，可以同时监控多个描述符，当某个描述符就绪(读或写就绪)，则立刻通知相应程序进行读或写操作，本质同步I/O，即读写是阻塞的。 所以说，主线程大多数时候都是处于休眠状态，并不会消耗大量CPU资源。

[参考链接](https://segmentfault.com/a/1190000022221446)

---

在Android中，`Looper.loop()`在主线程中运行一个无限循环，但它不会导致主线程卡死。这是因为`Looper.loop()`的实现方式非常高效，主要有以下几个原因：

1. **消息队列机制**：`Looper.loop()`会不断地从消息队列中读取消息并处理。如果消息队列中没有消息，它会让线程进入等待状态，而不是占用CPU资源¹。

2. **阻塞与唤醒机制**：当没有消息时，`Looper`会调用`MessageQueue.next()`方法，这个方法会阻塞当前线程，直到有新的消息到来。这样，主线程在没有消息时不会消耗CPU资源²。

3. **事件驱动模型**：Android的主线程是事件驱动的，所有的UI操作和事件处理都是通过消息机制来完成的。当有新的消息（如用户输入、网络请求等）到来时，`Looper`会被唤醒并处理这些消息³。

4. **高效的数据结构和算法**：`Looper.loop()`使用了高效的数据结构和算法来管理消息的存储和分发，确保消息能够及时处理而不会导致主线程卡顿³。

这些机制共同确保了`Looper.loop()`在主线程中运行时不会导致应用卡死或出现ANR（应用无响应）问题。

## 实践 - 子线程创建 Handler

```
class MyWorkerThread : Thread() {
    private lateinit var handler: Handler

    override fun run() {
        // 创建一个与当前线程的 Looper 关联的 Handler
        Looper.prepare() // 准备当前线程的 Looper
        handler = Handler(Looper.myLooper()!!) // 使用当前线程的 Lopper 创建 Handler
        Looper.loop() // 开始处理消息循环
    }

    fun postTask(task: Runnable) {
        handler.post(task) // 将任务发送到 Handler 处理
    }
}
```

## 避免 Handler 内存泄漏

- 使用 WeakReference

当 Activity 或 Fragment 被销毁时，引用会被垃圾回收器清除，避免内存泄漏

```
class MyHandler(activity: MainActivity) : Handler(Looper.getMainLooper()) {
    private val weakActivity = WeakReference(activity)

    override fun handleMessage(msg: Message) {
        val activity = weakActivity.get()
        activity?.let {
            // 安全地使用 activity
        }
    }
}
```

- 在适当时机移除消息和回调

```
override fun onDestroy() {
    super.onDestroy()
    handler.removeCallbacksAndMessages(null) // 移除所有消息
}
```

- 使用 Lifecycle

结合使用 ViewModel 和 LiveData，可以在 ViewModel 中处理长时间运行的操作，而不是直接使用 Handler。这样可以更好地管理组件的生命周期

```
class MyViewModel : ViewModel() {
    private val handler = Handler(Looper.getMainLooper())

    fun doSomething() {
        handler.post {
            // 执行操作
        }
    }

    override fun onCleared() {
        super.onCleared()
        handler.removeCallbacksAndMessages(null) // 清除所有消息
    }
}
```

- 使用 Coroutine 替代 Handler

直接 CoroutineScope，或使用 LifecycleScope

```
class MyActivity : AppCompatActivity() {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        coroutineScope.launch {
            // 执行耗时操作
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel() // 取消协程
    }
}
```

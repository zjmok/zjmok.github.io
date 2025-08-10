---
layout: post
tags: Linux Android Shell
---

Unix IPC 有以下几种: 

- [管道/匿名管道/无名管道(pipe)](管道)

- [命名管道/有名管道(FIFO 或 named PIPE)](命名管道)

- [信号(Signal)](#信号)

---

#### 管道

基于 fork 子进程，调用 pipe 函数在内核中开辟一块缓冲区(伪文件)，称为管道，它有一个读端和一个写端，父子或同根进程各关闭相对的一端，一读一写，即可实现单向通信，属半双工通信

pipe 函数 `int pipe(int pipefd[2]);` 返回值: 成功 0 失败 -1

函数调用成功返回r/w两个文件描述符。无需open，但需手动close。规定：fd[0] → r； fd[1] → w (就像0对应标准输入，1对应标准输出一样)。向管道文件读写数据其实是在读写内核缓冲区。

Example 1
```
ls -l | grep "ssh"
```

ls 遍历结果 跟 grep 查找 两个进程通过管道通信

Example 2

Android 5.1 及以下的源码中，使用了 pipe 管道唤醒主线程 Looper  
(Android 6.0+ 使用 `IO多路复用` 机制 `epoll` 唤醒)

Android 5.1.1_r6 , `/system/core/libutils/Looper.cpp` 源码 [在线查看](http://androidxref.com/5.1.1_r6/xref/system/core/libutils/Looper.cpp#Looper): 
```
Looper::Looper(bool allowNonCallbacks) :
        mAllowNonCallbacks(allowNonCallbacks), mSendingMessage(false),
        mResponseIndex(0), mNextMessageUptime(LLONG_MAX) {
    int wakeFds[2];
    int result = pipe(wakeFds);
    LOG_ALWAYS_FATAL_IF(result != 0, "Could not create wake pipe.  errno=%d", errno);

    mWakeReadPipeFd = wakeFds[0];
    mWakeWritePipeFd = wakeFds[1];

    result = fcntl(mWakeReadPipeFd, F_SETFL, O_NONBLOCK);
    LOG_ALWAYS_FATAL_IF(result != 0, "Could not make wake read pipe non-blocking.  errno=%d",
            errno);

    result = fcntl(mWakeWritePipeFd, F_SETFL, O_NONBLOCK);
    LOG_ALWAYS_FATAL_IF(result != 0, "Could not make wake write pipe non-blocking.  errno=%d",
            errno);

    mIdling = false;

    // Allocate the epoll instance and register the wake pipe.
    mEpollFd = epoll_create(EPOLL_SIZE_HINT);
    LOG_ALWAYS_FATAL_IF(mEpollFd < 0, "Could not create epoll instance.  errno=%d", errno);

    struct epoll_event eventItem;
    memset(& eventItem, 0, sizeof(epoll_event)); // zero out unused members of data field union
    eventItem.events = EPOLLIN;
    eventItem.data.fd = mWakeReadPipeFd;
    result = epoll_ctl(mEpollFd, EPOLL_CTL_ADD, mWakeReadPipeFd, & eventItem);
    LOG_ALWAYS_FATAL_IF(result != 0, "Could not add wake read pipe to epoll instance.  errno=%d",
            errno);
}
```

#### 命名管道

基于文件(特殊文件)，解决了匿名管道需要依赖父子或同根进程关系的问题

`int mkfifo(const char *pathname,mode_t mode);` 返回值: 成功 0 失败 -1。pathname 是一个普通的路径名，是FIFO的名字，mode指定文件的权位

Example

Shell并发，命名管道的实现

通过一个有名管道充当任务队列，准确地说是令牌队列。
进程从队列中取得令牌后才可以运行，运行结束后将令牌放回队列。
没有取得令牌的进程不能运行。令牌的数目即允许并发的最大进程数。

```
#!/bin/bash

#sub process do something
function a_sub_process { 
    echo "processing in pid [$$]"
    sleep 1
}
 
# Step1 创建命名管道
FIFO_FILE=/tmp/$.fifo
mkfifo $FIFO_FILE

# 创建文件描述符，以可读 `<` 可写 `>` 的方式关联管道文件，这时候文件描述符就有了有名管道文件的所有特性
exec 10<>$FIFO_FILE # 文件描述符是正整数即可
# 关联后的文件描述符拥有管道文件的所有特性，所以这时候管道文件可以删除，留下文件描述符来用就可以了
rm $FIFO_FILE

# 令牌数(最大进程数)
PROCESS_NUM=5

# Step2 创建令牌(向文件描述符写入 $PROCESS_NUM 个空行,代表允许的最大进程数)
for ((idx=0;idx<$PROCESS_NUM;idx++));
do
    echo >&10
done

# Step3 取令牌，并发处理业务
for ((idx=0;idx<20;idx++));
do
    read -u10  # read -u10 命令执行一次，获取 1 个令牌(尝试从文件描述符中读取删除 1 个空行)，如果获取不到，则阻塞
    {
      a_sub_process && {
         echo "sub_process is finished"
      } || {
         echo "sub error"
      }
      #  执行完将令牌放回管道(写入空行)
      echo >&10
    } & # 子进程放在后台执行
done

# 关闭文件描述符的读写
exec 10<&-
exec 10>&- 
```

#### 信号

略

---

参考

[shell并发](https://www.cnblogs.com/shiyublog/p/13561100.html)

[Shell脚本并发及并发数的控制](https://www.jianshu.com/p/701952ffb755)

[Android进程间通信机制-管道](https://www.jianshu.com/p/115cf0e519c2)

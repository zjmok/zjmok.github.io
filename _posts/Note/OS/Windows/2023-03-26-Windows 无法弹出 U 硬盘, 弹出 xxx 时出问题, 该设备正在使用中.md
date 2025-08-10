---
layout: post
tags: Windows
---

Windows无法弹出U/硬盘

- 方法〇：使用 [Powertoys](https://github.com/microsoft/PowerToys) 的 `File Locksmith` 插件，直接在资源管理器中 右击盘符 检查被占用的进程并结束掉

（2024.03 更新，现在只推荐 方法〇）

- 方法一：利用第三方软件，如360等。管理小流氓的都是大流氓，不推荐

- 方法二：磁盘管理，选择U/硬盘，脱机

- 方法三：事件查看器，Windows日志，系统，事件ID为225的警告（可以筛选），
点击item查看下方常规里的描述，可知道是什么进程阻碍弹出，
详细信息里可以查看进程命令行（xx.exe），
详细信息里可以查看ProcessId（进程id），
可手动关闭软件，或去任务管理器关闭ProcessId对应的进程

---

附：

强杀进程命令

```
// 查看进程列表
tasklist

// 按进程名杀 test.exe替换为进程名
taskkill /im test.exe -f

// 按进程id杀 processid替换为进程id
taskkill /pid processid -f
```

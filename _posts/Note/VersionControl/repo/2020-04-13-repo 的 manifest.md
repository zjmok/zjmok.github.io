---
layout: post
tags: AOSP Repo
---

manifest.xml
 
- manifest 根标签

  - include 标签 
    - name 属性 可以引入另外一个manifest文件(路径相对与当前的manifest.xml 的路径)
  
  - remote 标签 远程仓库 可以有多个
    - name 属性 在每一个.git/config文件的remote项中用到这个name，即表示每个git的远程服务器的名字
    - alias 属性 可以覆盖之前定义的remote name，name必须是固定的，但是alias可以不同，可以用来指向不同的remote url
    - fetch 属性 是服务器地址，可以用`..`来代替，代表上一级目录，所有git url真正路径的前缀，所有git 的project name加上这个前缀，就是git url的真正路径
	- review 属性 指定Gerrit的服务器名，用于repo upload操作。如果没有指定，则repo upload没有效果
	- revision 属性 默认的git分支

  - default 标签
    - revision 属性 所有git仓库默认的分支
    - remote 属性 远程服务器的名字 指定上面某个remote标签对应的name 多个remote的时候需要指定
	- sync-j 属性 repo sync 默认的并行数目
	- sync-c 属性 如果设置为true，则只同步指定的分支(revision 属性指定)，而不是所有的ref内容
	- sync-s 属性 如果设置为true，则会同步git的子项目submodule

  - manifest-server 标签
    - url 属性

  - project 标签
    - path 属性 clone到本地的git的工作目录，如果没有配置的话，跟name一样
    - name 属性 git 的名称，用于生成git url。URL格式是：${remote fetch}/${project name}.git 其中的 fetch就是上面提到的remote 中的fetch元素，name 就是此处的name
    - groups 属性 列出project所属的组，以空格或者逗号分隔多个组名。所有的project都自动属于"all"组。每一个project自动属于name:'name' 和path:'path'组。例如<project name="monkeys" path="barrel-of"/>，它自动属于default, name:monkeys, and path:barrel-of组。如果一个project属于notdefault组，则，repo sync时不会下载
    - revision 属性 指定需要获取的git提交点，可以定义成固定的branch，或者是明确的commit 哈希值
    - remote 属性 定义remote name，如果没有定义的话就用default中定义的remote name

    - copyfile 标签

    - linkfile 标签
	
  - remove-project 标签 从内部的manifest表中删除指定的project。经常用于本地的manifest文件，用户可以替换一个project的定义

---

参考链接 [构建自己的Android代码托管服务器](https://blog.csdn.net/billpig/article/details/7604828)  
参考链接 [android repo中manifest.xml的详解](https://blog.csdn.net/shift_wwx/article/details/19557031)  

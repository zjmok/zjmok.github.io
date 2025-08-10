---
layout: post
tags: Linux Shell
---

[Linux which 命令](https://www.runoob.com/linux/linux-comm-which.html)

Linux which命令用于查找文件。

which指令会在环境变量$PATH设置的目录里查找符合条件的文件。

```
which [文件...]
```

---

[Linux whereis 命令](https://www.runoob.com/linux/linux-comm-whereis.html)

Linux whereis命令用于查找文件。

该指令会在特定目录中查找符合条件的文件。这些文件应属于原始代码、二进制文件，或是帮助文件。

该指令只能用于查找二进制文件、源代码文件和man手册页，一般文件的定位需使用locate命令。

```
whereis [-bfmsu][-B <目录>...][-M <目录>...][-S <目录>...][文件...]
``` 

---

[Linux locate 命令](https://www.runoob.com/linux/linux-comm-locate.html)

Linux locate命令用于查找符合条件的文档，他会去保存文档和目录名称的数据库内，查找合乎范本样式条件的文档或目录。

一般情况我们只需要输入 **locate your_file_name** 即可查找指定文件

```
locate [文件...]
```

这个命令也是通过数据库查找文件，但是这个命令的适用范围就比whereis大多了。这个命令可以找到任意你指定要找的文件，并且可以只输入部分文件名（前面两个命令是要输入完整文件名的）。同时locte还可以通过-r选项使用正则表达式，功能十分强大。

---

[Linux find 命令](https://www.runoob.com/linux/linux-comm-find.html)

Linux find 命令用来在指定目录下查找文件。任何位于参数之前的字符串都将被视为欲查找的目录名。如果使用该命令时，不设置任何参数，则 find 命令将在当前目录下查找子目录与文件。并且将查找到的子目录和文件全部进行显示。

```
find path -option [-print] [-exec -ok command] {} \;
```

find命令要比前边几条复杂的多，功能也强大的多。这条命令是通过直接搜索硬盘的方式查找的，所以可以保证查找的信息绝对可靠。并且支持各种查找条件。但是功能强大肯定是有代价的，那就是搜索速度慢。所以一般前边几种找不出来的情况下才会使用find。另外如果要实现某些特殊条件的查找，比如找出某个用户的size最大的文件，那就只能用find了。

---

[Linux grep 命令](https://www.runoob.com/linux/linux-comm-grep.html)

用于查找文件里符合条件的字符串。

```
grep [-abcEFGhHilLnqrsvVwxy][-A<显示行数>][-B<显示列数>][-C<显示列数>][-d<进行动作>][-e<范本样式>][-f<范本文件>][--help][范本样式][文件或目录...]
```

---
layout: post
tags: Dev
---

Q: 为什么在平时看到 Linux/Mac 使用 tar 而 Windows 使用 zip/7z

A: 因为 7z 和zip 压缩格式都不能保留 unix 风格的文件权限，比如解压出个可执行文件要重新chmod chown 才能恢复正常。 而tar 格式可以。 而tar 本身不提供压缩，无非就是把包括所有文件的內容和权限拼成一个文件而己，所以用另外如gzip 格式压缩。

---

#### tar 命令 常用参数

```
  -c, --create               create a new archive 归档，打包/压缩到档案
  -x, --extract, --get       extract files from an archive 提取，从档案提取/解压
  
  -f, --file=ARCHIVE         use archive file or device ARCHIVE 指定文件/目录，执行的文件/目录
  -C, --directory=DIR        change to directory DIR 指定目标目录，输出的目录
  
  -v, --verbose              verbosely list files processed 显示指令执行过程

  -j, --bzip2                filter the archive through bzip2 压缩/解压 bz2/bzip2 格式
  -J, --xz                   filter the archive through xz 压缩/解压 xz 格式
  -z, --gzip, --gunzip, --ungzip   filter the archive through gzip 压缩/解压 gzip 格式
  
  -t, --list                 list the contents of an archive 列出档案里的内容

  -r, --append               append files to the end of an archive 在档案里追加内容，只能对未压缩的档案使用

```

---

tar 格式 不负责压缩，所以 CPU 资源和处理时间都消耗比较少

#### 压缩格式 常见的有 gzip bz2 xz

  - gzip 格式 通过 `tar` 加 `-z` 参数来处理

  - bz2/bzip2 格式 通过 `tar` 加 `-j` 参数来处理

  - xz 格式 通过 `tar` 加 `-J` 参数来处理

    通过另外利用 xz-utils 的 `xz` 命令单独处理

    - 压缩参数 `-z`: 压缩 tar 成 tar.xz `xz -z pkg_name.tar`

    - 解压参数 `-d`: 解压 tar.xz 成 tar `xz -d pkg_name.tar.xz`

#### 常用格式还有 7z 和 zip , 因为不能保存文件权限，通常在 Windows 中使用比较多

  - 7z 格式 需要安装 p7zip

  - zip 格式 通过 zip/unzip 程序来处理

    - 压缩 `zip target.zip dir_or_file`

    - 解压 `unzip pakage_name.zip`

#### Example

列出档案里的内容（只能对未压缩的档案使用）
```
$ tar -ztvf home-script.tar.gz
-rwx------ u0_a446/u0_a446  62 2022-04-21 00:51 gitblit-start.sh
-rwxrw---- u0_a446/everybody 61 2022-04-03 20:35 tomcat-start.sh
-rwxrw---- u0_a446/everybody 62 2022-04-03 20:36 tomcat-stop.sh
```

在档案里追加内容，只能对未压缩的档案使用
```
$ tar -cvf archive.tar a.txt
a.txt
$ tar -rvf archive.tar b.txt
b.txt
$ tar -tvf archive.tar
-rw------- u0_a543/u0_a543   0 2022-04-25 01:50 a.txt
-rw------- u0_a543/u0_a543   0 2022-04-25 01:51 b.txt
```

---

参考链接

[linux系统下tar/gz/7z/xz/bz2/zip等各种格式的打包压缩解压](https://blog.csdn.net/LEON1741/article/details/54317715)

[linux xz压缩解压](http://www.noobyard.com/article/p-pyxyanch-bz.html)


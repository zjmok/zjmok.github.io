---
layout: post
tags: Windows Linux
---

#### Windows

利用 Windows 自带工具 certutil 查看文件Hash校验值 MD5 SHA1 SHA256

```
certutil -hashfile [FILE] MD5

certutil -hashfile [FILE] SHA1

certutil -hashfile [FILE] SHA256
```

#### Linux

利用 Linux 自带工具 xxxsum 查看文件Hash校验值 MD5 SHA1 SHA256

```
sha1sum [FILE]

sha256sum [FILE]

md5sum [FILE]
```

可多个文件一起计算

```
$ sha256sum file1.txt file2.txt file3.txt > hash.sha256
$ cat hash.sha256
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx  file1.txt
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx  file2.txt
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx  file3.txt
```

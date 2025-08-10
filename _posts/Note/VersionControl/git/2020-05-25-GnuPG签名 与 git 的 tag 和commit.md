---
layout: post
tags: Git
---

GPG (GnuPg), 是加密软件，可以使用GPG生成的公钥在网上安全的传播你的文件、代码。

---

在Android源码repo中, Google给git的里程碑tag都采用了带GPG加密验证.  
如果你要修改tag引用的commit节点, 同事在更新项目后，出现版本GPG签名验证错误，不能正常使用

---

### 生成 `GPG Key`

执行命令生成key
```
gpg --full-generate-key
```

需要填写
- 真实姓名
- 电子邮箱
- 密码

生成过程大概是这样的
```
$ gpg --full-generate-key
gpg (GnuPG) 2.2.17-unknown; Copyright (C) 2019 Free Software Foundation, Inc.
This is free software: you are free to change and redistribute it.
There is NO WARRANTY, to the extent permitted by law.

gpg: directory '/c/Users/mok/.gnupg' created

此处省略n行

public and secret key created and signed.

pub   rsa2048 2020-05-25 [SC]
      C5CEF7F8A009F94DB8924642442F9459F79980AD
uid                      Mok Chiyin <mzx@zxmo.xyz>
sub   rsa2048 2020-05-25 [E]
```

### 查看生成的key
```
gpg --list-keys
```

```
$ gpg --list-keys
gpg: checking the trustdb
gpg: marginals needed: 3  completes needed: 1  trust model: pgp
gpg: depth: 0  valid:   1  signed:   0  trust: 0-, 0q, 0n, 0m, 0f, 1u
/c/Users/mok/.gnupg/pubring.kbx
-------------------------------
pub   rsa2048 2020-05-25 [SC]
      C5CEF7F8A009F94DB8924642442F9459F79980AD
uid           [ultimate] Mok Chiyin <mzx@zxmo.xyz>
sub   rsa2048 2020-05-25 [E]

```

自定义生成密钥对
```
gpg --full-generate-key
```

### 签名 commit

获取公钥, 后面的id对应上面获取到的
```
gpg --armor --export pub C5CEF7F8A009F94DB8924642442F9459F79980AD
```

执行后返回类似内容
```
$ gpg --armor --export pub C5CEF7F8A009F94DB8924642442F9459F79980AD
-----BEGIN PGP PUBLIC KEY BLOCK-----
# 此处省略公钥内容
-----END PGP PUBLIC KEY BLOCK-----
```

复制BEGIN行到END行即可配置公钥到github或gitee等

全局配置
```
# 配置git
git config --global user.signingkey C5CEF7F8A009F94DB8924642442F9459F79980AD

# 开启commit的签名
git config --global commit.gpgsign true
```

关闭改为false即可

可不设置全局 不使用参数 `--global` , 即对单个git仓库使用, 会在 `.git` 文件夹的 `config` 文件添加对应信息
```
# 配置git
git config user.signingkey C5CEF7F8A009F94DB8924642442F9459F79980AD

# 开启commit的签名
git config commit.gpgsign true
```

也可不配置到git, 直接在每次提交时手动启用签名, 带上参数 `-s`
```
git commit -s
```

配置好提交时会要求输入key的密码, 在github提交成功后可看到 `Verified` 字样

实践证明, github只认email, 要gpg签名email对应github账号认证过的email, 才提示验证成功, name可以不一致

gitee, gitlab 等 没做过实验, 估计差别不大

---

签名tag

git使用GPG签名打tag, 带上 `-u` 参数, 值是你在生成key的时候输入的 `Real name`, `-s`参数开启签名
```
git tag -u "Mok Chiyin" -s 2.9.8 -m "signed version 2.9.8 released" a7797b2

```

---

### 验证TAG

...continue

---

### 附上几条gpg命令
```
#查看生成的key
gpg --list-keys
#删除私钥
gpg --delete-secret-keys Key-ID
#删除公钥 需要先删除私钥
gpg --delete-key Key-ID
#生成密钥对
gpg --gen-key
#自定义生成密钥对
gpg --full-generate-key
gpg --recipient [用户ID] --output demo.en.txt --encrypt demo.txt
#解密
gpg demo.en.txt
#生成签名文件
gpg -a -b  demo.en.txt
#验证签名是否完整
gpg --verify -vdemo.en.txt.asc ldemo.en.txt
```

---
layout: post
tags: Termux
---

最新的 git 2.35.2 的 safe.directory 问题，每个仓库要手动添加一次，操作挂载目录不太舒服...

于是，用回 2.35.1 试试？用源码手动编译吧

---

利用 github 的 history 查找 git 包 对应 commit

termux git: `2.35.1`

git commit: `4577a0cf695646f76eb33a4c47a7a9a295deb556`

#### >> Linux 环境

```
git clone https://github.com/termux/termux-packages.git
git reset --hard 4577a0cf695646f76eb33a4c47a7a9a295deb556
```

官方推荐 docker 环境编译

执行配置 docker 环境的脚本，会自动拉取 docker 镜像，并创建进入 docker 容器
```
cd termux-packages
CONTAINER_NAME=git-builder ./scripts/run-docker.sh
```

#### >> Docker 容器

默认 builder 用户权限不够的，设置 root 密码并切到 root
```
sudo passwd root
su
```

代理以及 hosts, 我本地网络 dns 解析有问题，处理一下 (域名的 IP 自己上 Google 查)
```
export all_proxy=socks5://192.168.31.224:7890
echo '185.199.108.133 raw.githubusercontent.com' >> /etc/hosts
```

用户 builder 没有权限，转到用户 root 去干活
```
ln -s /home/builder/lib/ /root/lib
ln -s /home/builder/termux-packages /root/termux-packages
```

进入源码目录 编译
```
cd ~/termux-packages
./build-package.sh git
```

---

#### 路上的坑

- 出错

```
Failed to download https://www.zlib.net/zlib-1.2.11.tar.xz
```

浏览器测试一下，是404，去 https://www.zlib.net/ 查看一下发现已经更新到 1.2.12 版本

grep 命令 查找文件，然后修改版本号
```
grep -r www.zlib.net ~/termux-packages/packages
```

在目标文件修改版本号和对应的 SHA256，系统默认没有编辑器，编写前可以装一个 vim 或者 nano
```
nano ~/termux-packages/packages/zlib/build.sh
```

版本: 1.2.12
SHA256: 7db46b8d7726232a621befaab4a1c870f00a90805511c0e0090441dac57def18

修改完就可以继续操作了

---

- 一顿操作猛如虎，最后从头来

恢复最新代码，把 git 编译版本改回 2.35.1 版本
```
cd ~/termux-packages
git reset --hard HEAD && git pull
nano packages/git/build.sh
```

直接修改 VERSION 和对应的 SHA256, 因为目前版本还比较近，改动的不多
```
TERMUX_PKG_VERSION=2.35.1
TERMUX_PKG_SHA256=d768528e6443f65a203036266f1ca50f9d127ba89751e32ead37117ed9191080
```

再执行操作
```
./build-package.sh git
```

破电脑 等了半个多小时 终于编译成功
```
make: Entering directory '/root/.termux-build/git/src/contrib/subtree'
make -C ../../ GIT-VERSION-FILE
make[1]: Entering directory '/root/.termux-build/git/src'
make[1]: 'GIT-VERSION-FILE' is up to date.
make[1]: Leaving directory '/root/.termux-build/git/src'
asciidoc -b docbook -d manpage -f ../../Documentation/asciidoc.conf \
        -agit_version=2.35.1  git-subtree.txt
xmlto -m ../../Documentation/manpage-normal.xsl  man git-subtree.xml
install -d -m 755 /data/data/com.termux/files/usr/share/man/man1
install -m 644 git-subtree.1 /data/data/com.termux/files/usr/share/man/man1
make: Leaving directory '/root/.termux-build/git/src/contrib/subtree'
termux-elf-cleaner: Replacing unsupported DF_1_* flags 134217729 with 1 in './libexec/git-core/git'
termux-elf-cleaner: Replacing unsupported DF_1_* flags 134217729 with 1 in './libexec/git-core/git-http-fetch'
termux-elf-cleaner: Replacing unsupported DF_1_* flags 134217729 with 1 in './libexec/git-core/git-imap-send'
termux-elf-cleaner: Replacing unsupported DF_1_* flags 134217729 with 1 in './libexec/git-core/git-daemon'
termux-elf-cleaner: Replacing unsupported DF_1_* flags 134217729 with 1 in './libexec/git-core/git-shell'
termux-elf-cleaner: Replacing unsupported DF_1_* flags 134217729 with 1 in './libexec/git-core/git-http-backend'
termux-elf-cleaner: Replacing unsupported DF_1_* flags 134217729 with 1 in './libexec/git-core/git-sh-i18n--envsubst'
termux-elf-cleaner: Replacing unsupported DF_1_* flags 134217729 with 1 in './libexec/git-core/git-remote-http'
termux-elf-cleaner: Replacing unsupported DF_1_* flags 134217729 with 1 in './bin/git-shell'
termux - build of 'git' done
```

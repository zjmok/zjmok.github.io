---
layout: post
tags: Termux
---

最近有需要使用到 ruby 2.6 , 尝试了在 termux 上安装 rvm , 但不能正常运行，也找不到 aarch64 的 deb 安装包，所以决定用源码手动编译

---

利用 github 的 history 查找 ruby 包 对应 commit

termux ruby: `2.6.5`

git commit: `5123b0cffaad65ca19fd18001a1401ed787bf9eb`

#### >> Linux 环境

把源码拉下来，checkout 到 2.6 版本的 commit , 复制 ruby 库出来，再切换回 master 最新代码，删掉 package 下的 ruby ，把前面的 ruby 放回来

```
git clone https://github.com/termux/termux-packages.git
git checkout 5123b0cffaad65ca19fd18001a1401ed787bf9eb
cp -r packages/ruby ~/ruby2.6.5
git checkout master
rm -rf packages/ruby
mv ~/ruby2.6.5 packages/ruby
```

官方推荐 docker 环境编译

执行配置 docker 环境的脚本，会自动拉取 docker 镜像，并创建进入 docker 容器
```
cd termux-packages
CONTAINER_NAME=ruby-builder ./scripts/run-docker.sh
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
echo '185.199.108.133 raw.githubusercontent.com' >> /etc/hosts # 美国加利福尼亚旧金山
echo '20.205.243.166 github.com' >> /etc/hosts # 新加坡 微软云
echo '85.187.148.2 www.zlib.net' >> /etc/hosts # 荷兰阿姆斯特丹
echo '23.2.129.55 www.openssl.org' >> /etc/hosts # 日本东京 Akamai
```

用户 builder 没有权限，转到用户 root 去干活
```
ln -s /home/builder/lib/ /root/lib
ln -s /home/builder/termux-packages /root/termux-packages
```

进入源码目录 编译
```
cd ~/termux-packages
./build-package.sh ruby
```

---

#### 路上的坑

- 报错

```
/usr/lib/x86_64-linux-gnu/ruby/2.7.0/rbconfig.rb:13:in `<module:RbConfig>': ruby lib version (2.7.4) doesn't match executable version (2.6.5) (RuntimeError)
```

利用 rvm 切换对应版本 ruby
```
rvm use 2.6.5 --default
```

- 如果 apt 不能更新，需要校正时间才能更新 (修改启动脚本在 `docker run` 加参数 `--privileged=true` 创建的容器才有权限，所以删掉容器重新创建吧)
校正时间格式
```
date -s 'MM/dd/yyyy HH:mm'
```

---

等待编译了很久...失败了...

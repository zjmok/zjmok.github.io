---
layout: post
tags: Linux Git Flutter
---

注意，一般代理协议都是 socks5 和 http, 并没有 https

#### apt

apt 使用 socks5 代理

- 仅命令行一次性使用代理：
使用命令时, 加上 `-o` 参数
```
sudo apt -o Acquire::socks::proxy="socks5://127.0.0.1:7890/" update
```

- 一次配置永远享受

在 `/etc/apt/apt.conf.d/12proxy` 追加以下内容
```
Acquire::socks::proxy "socks5://127.0.0.1:1080/";
```

#### git

在項目或全局的 `.gitconfig` 文件裏，添加
```
[http]
	proxy = socks5://127.0.0.1:7891
[https]
	proxy = socks5://127.0.0.1:7891
```
分別代理http和https

也可用命令
```
# 设置 http https 代理
git config --global http.proxy socks5://127.0.0.1:7891
git config --global https.proxy socks5://127.0.0.1:7891

# 查看 http https 代理配置情况
git config --global --get http.proxy
git config --global --get https.proxy

# 取消 http https 代理配置
git config --global --unset http.proxy
git config --global --unset https.proxy
```

#### Gradle

在 GRADLE_USER_HOME 的 `gradle.properties` 全局添加，或在项目的 `gradle.properties` 对当前项目添加
```
# If you are behind an HTTP proxy, please configure the proxy settings either in IDE or Gradle.
# 使用代理对应的host和port端口
# http
# systemProp.http.proxyHost="127.0.0.1"
# systemProp.http.proxyPort="7890"
# https
# systemProp.https.proxyHost="127.0.0.1"
# systemProp.https.proxyPort="7890"
# socks5
# org.gradle.jvmargs=-DsocksProxyHost=127.0.0.1 -DsocksProxyPort=7890
# systemProp.socks.proxyHost="127.0.0.1"
# systemProp.socks.proxyPort="7890"

# 验证和直连 http https socks5 替换中间的协议即可
# 需要验证时
# systemProp.https.proxyUser=username
# systemProp.https.proxyPassword=password
# 直接连接而不走代理设置 多个地址用|隔开 支持通配符*
# systemProp.https.nonProxyHosts=localhost|*.acadsoc.com
```

#### sdkmanager

wsl2 使用 sdkmanager

如果使用代理，需要加代理参数，注意替换 IP 和 Port

```
--verbose --no_https --proxy=http --proxy_host=192.168.31.201 --proxy_port=7890
```

Example

获取列表
```
sdkmanager --list --verbose --no_https --proxy=http --proxy_host=192.168.31.201 --proxy_port=7890
```

下载 ndk
```
sdkmanager "ndk;21.4.7075529"  --verbose --no_https --proxy=http --proxy_host=192.168.31.201 --proxy_port=7890
```

#### Android Studio

File --> Setting --> Appearance & Behavior --> System Settings --> HTTP Proxy

#### Android SDK 的 模拟器 emulator

```
emulator -avd Nexus_5X_API_29 -http-proxy http://127.0.0.1:7890
```

#### flutter dio

用 dio 时抓包必须在代码里设置代理

```
(httpClientAdapter as DefaultHttpClientAdapter).onHttpClientCreate =
    (client) {
  client.findProxy = (uri) {
    // proxy all request
    return 'PROXY 127.0.0.1:7890';
    // 不设置代理
    // return 'DIRECT';
  };
  // 忽略证书
  client.badCertificateCallback = (cert, host, port) => true;
};
```

#### DartTS 

#### Ktor Client

```
            client.apply {
                // 代理
                // 有效: Android(OkHttp)、Desktop(JVM)、iOS(NSURLSession)
                // 无效: Web
                // JS（Web） 平台在浏览器中没有直接的代理配置机制。浏览器的代理设置通常是由浏览器本身进行管理，而不是在 JavaScript 中配置
                this.engineConfig.proxy =
                    ProxyBuilder.socks("192.168.1.202", 7890)
//                    ProxyBuilder.http(Url("http://192.168.1.202:7890/"))
//                    ProxyBuilder.socks("127.0.0.1", 7890)
//                    ProxyBuilder.http(Url("http://127.0.0.1:7890/"))
            }.get(API_URL).body()
```

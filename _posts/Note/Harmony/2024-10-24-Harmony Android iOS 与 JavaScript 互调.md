---
layout: post
tags: Harmony Android iOS
---

# Harmony Android iOS 与 JavaScript 互调

Html 页面，里面包含一些 JavaScript 互调的方法

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Multi-Platform Communication</title>
    <script>
        // JavaScript 调用 Harmony Android iOS
        function callPlatform() {
            if (typeof Harmony !== 'undefined') {
                Harmony.onMessageReceived("Hello from HTML!");
            } else if (typeof Android !== 'undefined') {
                Android.onMessageReceived("Hello from HTML!");
            } else if (typeof iOS !== 'undefined') {
                iOS.onMessageReceived("Hello from HTML!");
            }
        }

        // Harmony 调用 JavaScript
        function receiveMessageFromHarmony(message) {
            console.log("Received from Harmony: " + message);
            document.getElementById('messageBox').innerText = message;
        }

        // Android 调用 JavaScript
        function receiveMessageFromAndroid(message) {
            console.log("Received from Android: " + message);
            document.getElementById('messageBox').innerText = message;
        }

        // iOS 调用 JavaScript
        function receiveMessageFromIOS(message) {
            console.log("Received from iOS: " + message);
            document.getElementById('messageBox').innerText = message;
        }
    </script>
</head>
<body>
<h1>Multi-Platform Communication</h1>
<button onclick="callPlatform()">Send Message to Platform</button>
<p>
<div id="messageBox">text</div>
</body>
</html>
```

## Harmony

JavaScript 调用 Harmony。用 Web 的 `javaScriptProxy`（只注册一个对象），或 WebviewController 的 `registerJavaScriptProxy`（支持注册多个对象）

```javascript
Web({ src: this.url, controller: this.webviewController })
  .javaScriptAccess(true)
  .javaScriptProxy({
    object: {
      onMessageReceived(data: string) {
        promptAction.showToast({
          message: `data = ${data}`
        })
      }
    }, // 也可以指向 this（当前类写了对应方法）或成员变量（值是包含对应方法的回调）
    name: "Harmony", // JavaScript 定义的对象名称
    methodList: ["onMessageReceived"], // JavaScript 定义的对象调用的方法，object 的方法也要对应这里
    // asyncMethodList: [], // 异步调用，一般用来处理耗时任务
    controller: this.webviewController, // Web 组件的 controller
  })
```

Harmony 调用 JavaScript

```arkts
// 通过传入 Web 的 webviewController
this.webviewController.runJavaScript("receiveMessageFromHarmony('Response from Harmony')")
```

## Andoid

JavaScript 调用 Android

```kotlin
webView.settings.javaScriptEnabled = true
webView.addJavascriptInterface(WebAppInterface(), "Android")

// 定义一个类
inner class WebAppInterface {
    @JavascriptInterface
    fun onMessageReceived(message: String) {
        toast("Received message from JavaScript: $message")
    }
}
```

Android 调用 JavaScript

```kotlin
webView.evaluateJavascript("receiveMessageFromAndroid('Response from Android')", null)
```

## iOS (待验证)

JavaScript 调用 iOS

```
class ViewController: UIViewController, WKScriptMessageHandler {
    var webView: WKWebView!

    override func viewDidLoad() {
        super.viewDidLoad()

        let contentController = WKUserContentController()
        contentController.add(self, name: "iOS")

        let config = WKWebViewConfiguration()
        config.userContentController = contentController

        webView = WKWebView(frame: self.view.frame, configuration: config)
        self.view.addSubview(webView)

        if let url = Bundle.main.url(forResource: "your_page", withExtension: "html") {
            webView.loadFileURL(url, allowingReadAccessTo: url)
        }
    }

    func userContentController(_ userContentController: WKUserContentController, didReceive message: WKScriptMessage) {
        if message.name == "iOS", let messageBody = message.body as? String {
            print("Received message from JavaScript: \(messageBody)")
        }
    }
}
```

iOS 调用 JavaScript

```
webView.evaluateJavaScript("receiveMessageFromiOS('Response from iOS')", completionHandler: nil)
```

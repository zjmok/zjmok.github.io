---
layout: post
tags: IDE Android-Studio
---

# `HTTP Client` 插件

在 Android Studio Meerkat 开始，`HTTP Client` 插件终于适配 AS 了。在 IDEA Ultimate 是自带这个插件的，很早的版本就有了

用过的都知道，比 PostMan 还好用

插件地址 <https://plugins.jetbrains.com/plugin/13121-http-client>

## 用法

### 注释和分隔符

- `#` 是普通注释，不会影响执行逻辑。也可以是 `##`

- `###` 是请求分隔符，用来分隔多个 HTTP 请求。也可以是更多个符号 `######`

### 示例

新建 http 文件

`test_api.http`

```http
# GET

### 测试 query 参数
GET http://localhost:8080/testGet?userId=99

### 测试 路径参数
GET http://localhost:8080/testGet/1234

### 测试 Headers
GET http://127.0.0.1:8080/testHeaders
# Headers 可以多次设置，每一次设置一个值，不是以 `,` 或 `;` 来分隔单词传入。
# 可以理解为 Map<String, List<String>>
# name 会忽略大小写，值不会忽略大小写
HeaderName1: HeaderValue1 # 设置一个值
HeaderName1: HeaderValue11 # 再设置一个值，不会覆盖
HeaderName2: HeaderValue2, HeaderValue22 # 这里是一个值
HeaderName3: HeaderValue3; HeaderValue33 # 这里是一个值

# POST

### 测试 post json
POST http://localhost:8080/testPost/json
Content-Type: application/json

{
  "title": "My New Post",
  "desc": "123456",
  "userId": 2
}

### 测试 post 表单
POST http://localhost:8080/testPost/form
Content-Type: application/x-www-form-urlencoded

title=qwer&body=23456

### 测试 post 多表单, 上传文件（当前路径是相对本http文件路径）（在这里不要选择 http 文件，上传 http 文件时会有 bug，会执行文件中的 `<` 命令）
POST http://localhost:8080/testPost/multi-form
Content-Type: multipart/form-data; boundary=WebAppBoundary

--WebAppBoundary
Content-Disposition: form-data; name="file"; filename="test.http"

< ./.gitignore
# < ./test_api.http # 会出问题
--WebAppBoundary--

### 测试 post 多表单, 参数 + 上传文件（当前路径是相对本http文件路径）（在这里不要选择 http 文件，上传 http 文件时会有 bug，会执行文件中的 `<` 命令）
POST http://localhost:8080/testPost/multi-form
Content-Type: multipart/form-data; boundary=WebAppBoundary

--WebAppBoundary
Content-Disposition: form-data; name="title"

title1234
--WebAppBoundary
Content-Disposition: form-data; name="desc"

description1234
--WebAppBoundary
Content-Disposition: form-data; name="file"; filename="test.http"

< ./.gitignore
# < ./test_api.http # 会出问题
--WebAppBoundary--

# PUT

### test PUT 更新
PUT http://127.0.0.1:8080/task/update/cleaning
Content-Type: application/json

{
  "name": "NewCleaning",
  "description": "Clean the house, Again",
  "priority": "High"
}

# DELETE

### test DELETE 删除
DELETE http://localhost:8080/task/delete/NewCleaning
Content-Type: application/json

```

### 环境

这个工具还可以使用不同的环境，然后给变量相应的赋值

使用环境切换，可以更方便进行调试

#### 环境文件里配置变量

在 `Run with` 这里可以 add 一个配置

- Public file 可以理解为通用配置，用于项目共享环境（通用的 baseUrl 等）
- Private file 可以理解为私有配置，用于本地敏感信息（token、密码 等），可以去 `.gitignore` 忽略掉

```json
// http-client.env.json
{
  "dev": {
    "baseUrl": "https://api-dev.example.com"
  },
  "prod": {
    "baseUrl": "https://api.example.com"
  }
}

// http-client.private.env.json
{
  "dev": {
    "token": "your-dev-token-here"
  },
  "prod": {
    "token": "your-prod-token-here"
  }
}
```

#### 环境的变量使用

在界面上方 `Run with`，或者左边的运行图标

```http
### 测试 插件的 `Run with` 环境
GET {{baseUrl}}/testRunWith
Authorization: Bearer {{token}}

# 环境指令，只是提示或默认指示，真正运行哪个环境，由 IDE 的环境切换按钮决定
### @env dev
GET {{baseUrl}}/v1/status
Authorization: Bearer {{token}}
```

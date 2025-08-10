---
layout: post
tags: Harmony
---

# Harmony ArkTS 的 JSON字符串 对象 互转

## json 字符串解析为对象

```
let result = '{"data":null,"errorCode":-1,"errorMsg":"账号密码不匹配！"}'
// json 字符串解析为相应对象，需要先 `JSON.stringify` 处理
let obj = JSON.parse(result) as ApiResult<UserInfo>
console.log(`obj = ${obj}`)
```

## 对象转为 json 字符串

```
let arr = Array(1,2,3);
let json = JSON.stringify(arr);
console.error(`json = ${json}`) // 输出：json = [1,2,3]
```

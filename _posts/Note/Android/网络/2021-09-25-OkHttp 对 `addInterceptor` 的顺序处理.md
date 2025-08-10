---
layout: post
tags: Android OkHttp
---

# OkHttp 对 `addInterceptor` 的顺序处理

### 如何处理
1. **执行顺序**：
   - 拦截器按照添加的顺序依次执行。第一个添加的拦截器会最先处理请求，最后处理响应。最后一个添加的拦截器会最先处理响应，最后处理请求。

2. **请求和响应的处理**：
   - **请求处理**：每个拦截器可以修改请求或决定是否继续传递请求。拦截器可以添加、修改或删除请求头，改变请求体，甚至短路请求（直接返回响应而不继续传递）。
   - **响应处理**：每个拦截器可以修改响应或决定是否继续传递响应。拦截器可以添加、修改或删除响应头，改变响应体，甚至短路响应（直接返回自定义响应）。

### 示例
假设你有两个拦截器 `InterceptorA` 和 `InterceptorB`，并且它们的顺序如下：

```kotlin
val okHttpClient = OkHttpClient.Builder()
    .addInterceptor(InterceptorA())
    .addInterceptor(InterceptorB())
    .build()
```

在这种情况下：
- **请求顺序**：`InterceptorA` -> `InterceptorB` -> 网络请求
- **响应顺序**：网络响应 -> `InterceptorB` -> `InterceptorA`

### 影响总结
- **顺序依赖**：如果拦截器之间存在依赖关系，顺序会影响它们的行为。例如，如果 `InterceptorA` 添加了一个请求头，而 `InterceptorB` 依赖于这个请求头，那么 `InterceptorA` 必须在 `InterceptorB` 之前执行。
- **性能**：由于 `addInterceptor` 会在每次请求时调用，可能会对性能有一定影响，特别是在大量请求的情况下。
- **功能**：某些功能可能需要特定的顺序。例如，日志拦截器通常放在最前面，以便记录所有请求和响应的详细信息。

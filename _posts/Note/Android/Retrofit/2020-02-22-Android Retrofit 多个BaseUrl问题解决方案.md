---
layout: post
tags: Android Retrofit
---

# 方案1
## 使用 `@Url` 注解直接使用完整Url

```
@Streaming
@GET
Observable<ResponseBody> downloadFile(@Url String fileUrl);//@Url使用参数url则不会用retrofit的baseUrl
```


# 方案2
## 利用`okhttp3.OkHttpClient.Builder#addInterceptor(okhttp3.Interceptor)`拦截器，和`service`的相关注解

- 1.在相应的`service`设置一个用作标记的`@Header`
```
@Headers("base_url:https://www.google.com/")//在拦截器里修改BaseUrl并去除此header
@POST("/path")
Observable<ResponseBody> action(@QueryMap Map<String, String> map);
```

- 2.在`OkHttpClient.Builder`的拦截器处理步骤1设置的`base_url`，更新`BaseUrl`
```
addInterceptor(chain -> {
	//获取request
	Request request = chain.request();
	//从request中获取原有的HttpUrl实例oldHttpUrl
	HttpUrl oldHttpUrl = request.url();
	//获取request的创建者builder
	Request.Builder builder1 = request.newBuilder();
	//从request中获取作为标记的headers
	List<String> headers = request.headers("base_url");
	if (headers != null && headers.size() > 0) {
		//如果有这个header，先将配置的header删除，因此header仅用作app和okhttp之间使用
		builder1.removeHeader("base_url");
		//匹配获得新的BaseUrl
		String header = headers.get(0);
		HttpUrl newHttpUrl = HttpUrl.parse(header);
		//重建新的HttpUrl，修改需要修改的url部分
		HttpUrl newFullUrl = oldHttpUrl
				.newBuilder()
				.scheme(newHttpUrl.scheme())//更换网络协议
				.host(newHttpUrl.host())//更换域名
				.build();
		//重建这个request，通过builder.url(newFullUrl).build()；
		// 然后返回一个response至此结束修改
		Logger.t("Request").i(
				oldHttpUrl.scheme() + "://" + oldHttpUrl.host()
						+ " --base_url已修改为--> "
						+ newHttpUrl.scheme() + "://" + newHttpUrl.host()
		);
		return chain.proceed(builder1.url(newFullUrl).build());
	}
	return chain.proceed(request);
})
```

另外有一个注意点，拦截器会按顺序执行，若有log打印相关的拦截器，建议放到上面拦截器的后面，避免打印出来的是BaseUrl处理前的Request信息

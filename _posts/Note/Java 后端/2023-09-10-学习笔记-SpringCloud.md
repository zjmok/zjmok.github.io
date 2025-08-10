---
layout: post
tags: Java
---

# SpringCloud

RestTemplate 封装了以下 http 服务调用的 http 客户端
- httpClient
- okHttp
- JDK 原生 UrlConnection

```kotlin
@Test
fun testRestTemplate() {
    val url = "https://open.tophub.today/hot"
    // 可接收 实体类
    val map = RestTemplate().getForObject(url, HashMap::class.java)
    println("result=${Gson().toJson(map)}")
}
```

获取 VM 参数 设置项目端口
```properties
# 先读取 VM 参数的 port 变量(VM options: `-Dport 9527`), 没有值才设置 8080
server.port=${port:8080}
```

## Eureka

进行服务管理，定期检查服务状态，返回服务地址列表

以下代码端口设置：
- 服务端: 8181, 8182, ...
- 客户端 基础服务: 8081, 8082, ...
- 客户端 调用基础服务的上层服务: 8091, 8092, ...

### Server 服务注册中心

依赖
```groovy
// https://mvnrepository.com/artifact/org.springframework.cloud/spring-cloud-starter-netflix-eureka-server
implementation 'org.springframework.cloud:spring-cloud-starter-netflix-eureka-server:4.0.3'
```

Application
```kotlin
// 声明是 Eureka 服务端
@EnableEurekaServer
@SpringBootApplication
class EurekaServerApplication

fun main(args: Array<String>) {
    runApplication<EurekaServerApplication>(*args)
}
```

配置
```properties
# app name
spring.application.name=SpringServer01
# port 优先读取 VM 参数的变量(VM options: `-Dport 9527`)
server.port=${port:8181}

# 集群时向其它服务端注册的地址, 多个以逗号隔开
# 优先读取 VM 参数的变量 
eureka.client.service-url.defaultZone="${defaultZone:http://127.0.0.1:8182/eureka}"
# 将自己注册到其它服务端，集群选 true
eureka.client.register-with-eureka=false
# 是否拉取其它服务端中已注册的客户端, 集群选 true
eureka.client.fetch-registry=false

# Eureka Server
# 服务失效剔除时间间隔, 默认 60 s
eureka.server.eviction-interval-timer-in-ms=60
# 自我保护模式(因网络原因而暂不剔除服务), 默认 true
eureka.server.enable-self-preservation=true
```

### Client  Base 注册

依赖
```groovy
// https://mvnrepository.com/artifact/org.springframework.cloud/spring-cloud-starter-netflix-eureka-client
implementation 'org.springframework.cloud:spring-cloud-starter-netflix-eureka-client:4.0.3'
```

Application
```kotlin
// 开启 Eureka 客户端发现，即注册到服务端
@EnableDiscoveryClient
@SpringBootApplication
class EurekaClientBase01Application

fun main(args: Array<String>) {
    runApplication<EurekaClientBase01Application>(*args)
}
```

配置
```properties
# app name
spring.application.name=EurekaClientBase01
# port 优先读取 VM 参数的变量(VM options: `-Dport 9527`)
server.port=${port:8081}

# 向服务端注册的地址
# 优先读取 VM 参数的变量 
eureka.client.service-url.defaultZone="${defaultZone:http://127.0.0.1:8181/eureka}"

# Eureka Client Base
# 使用 ip 地址
eureka.instance.prefer-ip-address=true
# ip 地址
eureka.instance.ip-address=127.0.0.1
# 续约间隔, 默认 30 s
eureka.instance.lease-renewal-interval-in-seconds=30
# 服务失效时间, 默认 90 s
eureka.instance.lease-expiration-duration-in-seconds=90
```

提供 service
```kotlin
@Controller("/base")
class BaseController {

    @GetMapping("/{id}")
    fun getDataById(@PathVariable id: String): String {
        return """
            |{id:${id},data:"qwer"}
        """.trimMargin()
    }

}
```

### Client  Consumer 发现  (调用  Client Base)

依赖
```groovy
// https://mvnrepository.com/artifact/org.springframework.cloud/spring-cloud-starter-netflix-eureka-client
implementation 'org.springframework.cloud:spring-cloud-starter-netflix-eureka-client:4.0.3'
```

Application
```kotlin
// 开启 Eureka 客户端发现，即注册到服务端
@EnableDiscoveryClient
@SpringBootApplication
class EurekaClientConsumerApplication

fun main(args: Array<String>) {
    runApplication<EurekaClientConsumerApplication>(*args)
}
```

配置
```properties
# app name
spring.application.name=EurekaClientConsumer01
# port 优先读取 VM 参数的变量(VM options: `-Dport 9527`)
server.port=${port:8091}

# 向服务端注册的地址
# 优先读取 VM 参数的变量 
eureka.client.service-url.defaultZone="${defaultZone:http://127.0.0.1:8181/eureka}"

# Eureka Client Consumer
# 拉取服务列表时间间隔, 默认 30 s
eureka.client.registry-fetch-interval-seconds=30
```

调用基础服务
```kotlin
@RestController
@RequestMapping("/consumer")
class TestController {

    @Autowired
    lateinit var restTemplate: RestTemplate

    @Autowired
    lateinit var discoveryClient: DiscoveryClient

    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: String): String {
        val serviceList = discoveryClient.getInstances("EurekaClientBase01")
        val service = serviceList[0] // 假设只有 1 个

        val url = "${service.scheme}://${service.host}:${service.port}/base/${id}"
        val result = restTemplate.getForObject(url, String::class.java)
        println("result=${result}")
        return result ?: "null"
    }

}
```

## Eureka 高可用 集群 配置

多个 Eureka 集群时，某一个有新服务注册时，同步到各个 Eureka

将 自己 也作为一个服务 注册到其它 Eureka Server, 这样多个 Eureka Server 之间就能互相发现，同步已注册的服务，实现集群

### 服务端配置

配置
```properties
# app name
spring.application.name=SpringServer01
# port 优先读取 VM 参数的变量(VM options: `-Dport 9527`)
server.port=${port:8091}

# 集群时向其它服务端注册的地址, 多个以逗号隔开
# 优先读取 VM 参数的变量 
eureka.client.service-url.defaultZone="${defaultZone:http://127.0.0.1:8182/eureka}"
# 将自己注册到其它服务端，集群选 true
eureka.client.register-with-eureka=true
# 是否拉取其它服务端中已注册的客户端, 集群选 true
eureka.client.fetch-registry=true
```

## Ribbon 负载均衡

根据负载均衡算法获取某个服务地址

### Client  Consumer

1. 给 `restTemplate` 添加负载均衡注解 `@LoadBalanced`
```kotlin
@Bean
@LoadBalanced
fun restTemplate: RestTemplate {
   return RestTemplate()
}
```

2. host 使用基础服务模块的名称， Ribbon 会拦截 url 中的 host 名称 `EurekaClientBase01` 替换为负载均衡算法获取到的真实的 host
```kotlin
@RestController
@RequestMapping("/consumer")
class TestController {

    @Autowired
    lateinit var restTemplate: RestTemplate

    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: String): String {
        val url = "http://EurekaClientBase01/base/${id}"
        val result = restTemplate.getForObject(url, String::class.java)
        println("result=${result}")
        return result ?: "null"
    }

}
```

3. 负载均衡策略配置
```properties
# 服务名称.ribbon.NFLoadBalancerRuleClassName=策略
user-service.ribbon.NFLoadBalancerRuleClassName=com.netflix.loadbalancer.RandomRule
```

## Hystrix 熔断器

延迟和容错库，用于隔离访问远程服务、第三方库，防止出现级联失败

若有服务出错，多个请求等待返回，导致线程阻塞，从而引起雪崩效应

Hystrix 解决 雪崩效应 的方式
- 线程隔离：为服务分配一个小的线程池，满了就不排队，加速失败判断事件
- 服务降级：例如返回失败的结果

代码实现

Hystrix 依赖 (项目在维护状态 最后更新事件 2021-11-17)
```
// https://mvnrepository.com/artifact/org.springframework.cloud/spring-cloud-starter-netflix-hystrix
implementation 'org.springframework.cloud:spring-cloud-starter-netflix-hystrix:2.2.10.RELEASE'
```

开启熔断, 在 Application 添加注解
```
@SpringBootApplication
@EnableCircuitBreaker
class ConsumerApplication

fun main(args: Array<String>) {
    runApplication<SpringbootApplication>(*args)
}
```

服务降级
```kotlin
@RestController
@RequestMapping("/consumer")
// 服务降级 类所有方法有效 方法指定比类指定的优先级更高
@DefaultProperties(defaultFallback = "defaultFallback")
class TestController {

    @GetMapping("/{id}")
    @HystrixCommand(fallbackMethod = "getUserByIdOnError")
    fun getUserById(@PathVariable id: String): String {
        val url = "http://EurekaClientBase01/base/${id}"
        val result = restTemplate.getForObject(url, String::class.java)
        println("result=${result}")
        return result ?: "null"
    }

    // 指定该方法的方法有效
    fun getUserByIdOnError(): String {
        return "服务器在捉迷藏，稍后再来看看吧。"
    }

    // 类的方法都有效，默认方法
    fun defaultFallback(): String {
        return "服务器太拥挤了，请稍后再试。"
    }

}
```

配置
```properties
# 熔断超时时间
hystrix.command.default.execution.isolation.thread.timeoutInMilliseconds=5000
# 触发熔断错误比例阈值，默认值50%
hystrix.command.default.execution.circuitBreaker.errorThresholdPercentage=50
# 熔断后休眠时长，默认值5秒
hystrix.command.default.execution.circuitBreaker.sleepWindowInMilliseconds=10000
# 熔断触发最小请求次数，默认值是20
hystrix.command.default.execution.circuitBreaker.requestVolumeThreshold=10
```

## Feign

## Spring Cloud Gateway

过滤和路由

### Rout 路由

### Filter 过滤器

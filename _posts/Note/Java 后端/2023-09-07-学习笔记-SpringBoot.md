---
layout: post
tags: Java SpringBoot
---

# SpringBoot

## 注解

- `@Configuration` 标志配置类

- `@Bean` 注册到容器

- `@Value` 注入值

- `@PropertySource` 引入配置文件

## 配置

```
# jdbc
jdbc.driverClassName=com.mysql.cj.jdbc.Driver
jdbc.username=root
jdbc.password=root
jdbc.url=jdbc:mysql://localhost:3306/mytest
```

方式1 Spring 注解 @Value 的方式注入到普通属性上再去使用
```
@Configuration
@PropertySource("classpath:jdbc.properties")
class JdbcConfig {
    @Value("${jdbc.driverClassName}")
    val driverClassName: String
	// ...
}
```

方式2 先读取并自动注入到实体类 (从 `application.properties` 读)
```
@ConfigurationProperties(prefix = "jdbc")
data class JdbcProperties(
    val driverClassName: String,
    val url: String,
    val username: String,
    val password: String,
)

@Configuration
@EnableConfigurationProperties(JdbcProperties::class)
class JdbcConfig {
    @Bean
    fun getDataSource(jdbcProperties: JdbcProperties): DataSource {
        return DruidDataSource().apply {
            driverClassName = jdbcProperties.driverClassName
            url = jdbcProperties.url
            username = jdbcProperties.username
            password = jdbcProperties.password
        }
    }
}
```

方式3 直接读取并自动注入到方法 (从 `application.properties` 读)
```
@Configuration
class JdbcConfig {
    @Bean
    @ConfigurationProperties(prefix = "jdbc")
    fun getDataSource(): DataSource {
	    // 不需要手动 set, 配置注解后会自动注入相应属性
        return DruidDataSource()
    }
}
```

## 整合

### SpringMVC

### 连接池

### Mybatis

整合后，Mybatis 事务, 在方法加注解 `@Transactional`

注意: 这个注解需要整合 SpringBoot 后由 SpringBoot 接管事务后才能使用, 也就是直接用数据源操作数据库的方式使用此注解是无效的

### Redis

### Ktorm

Ktorm 对 Spring 的支持就基于 Spring JDBC 模块，需要依赖
```groovy
// jdbc
implementation 'org.springframework.boot:spring-boot-starter-jdbc'
// mysql
implementation 'com.mysql:mysql-connector-j:8.1.0'
// ktorm
implementation 'org.ktorm:ktorm-core:3.6.0'
// ktorm 的 entity/json 转换, ktorm 用接口定义实体 不能直接用 jackson 和 gson 进行转换
implementation 'org.ktorm:ktorm-jackson:3.6.0'
// ktorm 启用 mysql 方言
implementation 'org.ktorm:ktorm-support-mysql:3.6.0'
```

配置文件, dataSource 的连接参数
```properties
# springboot 默认连接池 HikariCP
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/mytest
spring.datasource.username=root
spring.datasource.password=root
```

注入 `Database` 和 `KtormModule` 的实例到 spring 容器
```kotlin
@Configuration
class KtormConfig {

    @Bean
    fun database(@Qualifier("dataSource") dataSource: DataSource): Database {
        return Database.connectWithSpringSupport(dataSource)
    }

    @Bean
    fun ktormModule(): Module {
        return KtormModule()
    }

}
```

使用
```kotlin
@Autowired
lateinit var database: Database

@Test
fun ktormSelect() {
    // 查询 user 表 id=2 的 用户信息
    val userId = 2
    val user = database.users.find { it.id eq userId }
    println("user = ${user.toJson()}")
}
```

#### 事务

整合前

整合前，可 `useTransaction` 设置隔离级别
```
database.useTransaction(isolation = TransactionIsolation.REPEATABLE_READ) {
    // DML
}
```

整合后，事务已经委托给 Spring 来处理，事务相关 使用 `@Transactional` 即可，且 `database.useConnection{ }` 无效了

## 打包

1. Gradle 项目
  - gradle 执行任务 `Tasks/build/bootJar`, `Tasks/build/bootWar`, 
  - 输出目录为 `build/libs/`
  - `java -jar 打出来的jar文件`

2. Maven 项目
  - 添加插件 `spring-boot-maven-plugin`, 不配置插件的话 打出来的 jar 包没有清单文件
  - `maven package`, 可以勾选 `skip test` 跳过所有单元测试
  - `java -jar 打出来的jar文件`

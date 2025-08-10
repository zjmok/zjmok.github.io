---
layout: post
tags: 数据库
---

# Ktorm

没有配置文件、没有 xml、没有注解、甚至没有任何第三方依赖、轻量级、简洁易用

### 使用

依赖
```groovy
implementation 'org.ktorm:ktorm-core:3.6.0'
```

官方文档 [https://www.ktorm.org/zh-cn/](https://www.ktorm.org/zh-cn/)

- 简单使用, 可以查询简单数据

```kotlin
// 定义表 跟数据库对应
object Users : Table<Nothing>("user") {
    val id = int("id").primaryKey()
    val userName = varchar("username")
}

val database = Database.connect("jdbc:mysql://localhost:3306/mytest", user = "root", password = "root")
database.from(Users)
//    .select(Users.columns) // 查询所有字段
//    .select(Users.id, Users.userName) // 查询多个字段
    .select(Users.userName) // 查询单个字段
    .where{
        (Users.id eq 2) or (Users.id eq 4)
    }
    .forEach {
        println("User(id=${it[Users.id]}, userName=${it[Users.userName]})")
    }
```

### 操作符、运算符

ktorm|sql
-|-
eq|=
notEq 或 neq|<>
greater 或 gt|>
greaterEq 或 gte|>=
less 或 lt|<
lessEq 或 lte|<=
between|between ... and ...
notBetween|not between ... and ...
inList|in(...)
notInList|not in(...)
isNull|is null
isNotNull|is not null
not|not
plus|+
minus|-
times|*
div|/
rem|%
like|like
notLike|not like
and|and
or|or
xor|xor

less than, greater than, eq 等 很好记

## SQL DSL, 基于 SQL 语言习惯的操作

### DML 增删改

```kotlin
// 增
// insertAndGenerateKey 可以返回自增的主键
// batchInsert 可以提高批量操作的性能, 用于插入多条数据, 基于 JDBC 的 executeBatch
database.insert(Users) {
    set(it.userName, "法外狂徒张三")
    set(it.age, 18)
}

// 删
// database.users.removeIf { (it.id eq 2) or (it.userName like "%三") }
database.delete(Users) {
    (it.id eq 2) or (it.userName like "%三")
}

// 改
// batchUpdate 可以提高批量操作的性能, 用于更新多条数据, 基于 JDBC 的 executeBatch
database.update(Users) {
    set(it.userName, "法外狂徒男枪")
    where {
        it.id eq 2
    }
}
```

### DQL 查

```kotlin
// 查
val query = database.from(Users)
    // 不给参数就是 `*`
    // select 支持表达式和聚合函数
    // selectDistinct 去重, 对应于 SQL 中的 `select distinct`
    .select()
    .where {
        Users.id eq 2
    }
val result = query.map { row -> "${row[Users.id]}" }
println("result = ${result.toJson()}")

// 动态查询
val nameLike = "%三%"
val ageLess = 18
val query2 = database.from(Users)
    // 不给参数就是 `*`
    // select 支持表达式和聚合函数
    // selectDistinct 去重, 对应于 SQL 中的 `select distinct`
    .select(Users.userName)
    // whereWithConditions, 用 `and` 连接动态条件
    // whereWithOrConditions, 用 `or` 连接动态条件
    .whereWithConditions {
        if (nameLike != null) {
            it += (Users.userName like nameLike)
        }
        if (ageLess != null) {
            it += (Users.age lt ageLess)
        }
    }
val result2 = query2.map { row -> "${row[Users.id]} ${row[Users.userName]}" }
println("result = ${result2.toJson()}")

// 分组查询
// 聚合函数 可能要用到字段别名
val avgAge = avg(Users.age).aliased("avgAge")
val query3 = database.from(Users)
    // SQL 语法规定，在使用 group by 时，select 子句中出现的字段，要么是 group by 中的列，要么被包含在聚合函数中
    // 因为, 分组后 是一个整体, 要么是分组字段, 要么是聚合函数, 聚合函数是这个整体的运算后的结果
    .select(Users.deptId, avgAge)
    .groupBy(Users.deptId) // 分组
    .having { avgAge gtEq 30.0 } // 分组后的条件
val result3 = query3.map { row -> "部门ID:${row[Users.deptId]}, 平均年龄:${row[avgAge]}" }
println("result = ${result3.toJson()}")

// 排序
val query4 = database.from(Users)
    .select()
    .where {
        Users.id lt 10
    }
    .orderBy(Users.userName.desc(), Users.id.asc())
val result4 = query4.map { row -> "${row[Users.id]} ${row[Users.userName]}" }
println("result = ${result4.toJson()}")

// 分页查询
// ktorm 适配了各种数据库, 需要添加相关依赖启用方言
val page = 1 // 页码从 1 开始
val pageSize = 3
// page 从 0 开始的话 `offset = page * pageSize`,
// page 从 1 开始的话 `offset = (page - 1) * pageSize`
val offset = (page - 1) * pageSize
val query5 = database.from(Users)
    .select()
    // offset: 需要返回的第一条记录相对于整个查询结果的位移，从 0 开始
    // limit: 需要返回的记录的数量
    .limit(offset, pageSize) // 页数 = offset * 页大小
val result5 = query5.map { row -> "${row[Users.id]} ${row[Users.userName]}" }
println("result = ${result5.toJson()}")
```

### 联表查询

- 使用别名

```kotlin
val alias = table.aliased("别名") // 用于自关联的查询等
```

但由于 Kotlin 单例的限制，直接使用会有问题。需要将 object 的表声明为 class，格式如下

```kotlin
open class Employees(alias: String?) : Table<Nothing>("t_employee", alias) {
    companion object : Employees(null)
    override fun aliased(alias: String) = Employees(alias)

    val id = int("id").primaryKey()
    val name = varchar("name")
    val job = varchar("job")
    val managerId = int("manager_id")
    val hireDate = date("hire_date")
    val salary = long("salary")
    val departmentId = int("department_id")
}
```

- 连接函数

连接类型 | 扩展函数名 | 对应的 SQL 关键字
---|---|---
内连接 | innerJoin | inner join
左连接 | leftJoin | left join
右连接 | rightJoin | right join
交叉连接 | crossJoin | cross join

## 实体类 API

需要用接口作实体类 才能使用 `实体API`, 表继承 Table 才能用, 任意实体类是继承 BaseTable

### 绑定实体

Ktorm 的实体类使用接口代理实现 (可以支持 `data class` 等任意类的实体类, 但有限制: 不能使用针对实体对象的增删改 API, [相关限制](https://www.ktorm.org/zh-cn/define-entities-as-any-kind-of-classes.html#%E7%9B%B8%E5%85%B3%E9%99%90%E5%88%B6))

```kotlin
// 表, 这里是全局的对象, 如果表只是临时一次性使用的, 可以在使用时以匿名对象的形式定义
object Users : Table<User>("user") {
    val id = int("id").primaryKey().bindTo { it.id }
    val userName = varchar("username").bindTo { it.userName }
    val age = int("age").bindTo { it.age }
}

// 实体
interface User : Entity<User> {
    companion object : Entity.Factory<User>()

    val id: Int
    var userName: String
	var age: Int
}

// 扩展属性(可选, 方便调用)
val Database.users get() = this.sequenceOf(Users)
```

### 绑定实体后使用 增删改查

```kotlin
// 可注入
val database = Database.connect("jdbc:mysql://localhost:3306/mytest", user = "root", password = "root")

// 实体 API 增
val user = User {
    userName = "法外狂徒张三"
    age = 26
}
database.users.add(user)

// 实体 API 删
val user = database.users.find { it.id eq 2 }
user?.delete()

// 实体 API 改
val user = User {
    id = 3
    userName = "法外狂徒男枪"
    age = 16
}
database.users.update(user)

// 实体 API 查
// 查询单个
val user = database.users.find { it.id eq 4 }
// 查询多个
val userList = database.users.filter { (Users.id eq 5) or (Users.id eq 6) }.toList()
```

### 实体 Entity/Json 转换

支持 Jackson, 但需要添加依赖 `ktorm-jackson`

依赖

```groovy
implementation 'com.fasterxml.jackson.module:jackson-module-kotlin'
implementation 'org.ktorm:ktorm-jackson:3.6.0'
```

- 自己注册实例

使用

```kotlin
// 获取 Jackson 的 ObjectMapper 对象
val objectMapper = jacksonObjectMapper().apply {
    // 注册一个 `ktorm-jackson` 里的 Module 对象, Module 是 Jackson 提供的抽象类
    registerModule(KtormModule())
}
val json = objectMapper.writeValueAsString(object)
```

- 或 直接使用 `ktorm-jackson` 内置对象 `sharedObjectMapper`

sharedObjectMapper 已经注册了 KtormModule

```kotlin
public val sharedObjectMapper: ObjectMapper = ObjectMapper()
    .registerModule(KtormModule())
    .registerModule(KotlinModule())
    .registerModule(JavaTimeModule())
```

使用

```kotlin
val json = sharedObjectMapper.writeValueAsString(object)
```

## 整合到 SpringBoot

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
        return Database.connectWithSpringSupport(
            dataSource = dataSource,
			// debug 级别可输出 sql 语句
            logger = ConsoleLogger(threshold = LogLevel.DEBUG),
        )
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

### 事务

- 整合前

整合前，可 `useTransaction` 设置隔离级别

```
database.useTransaction {
    // DML
}

// 要更改隔离级别的话 加上参数即可
database.useTransaction(isolation = TransactionIsolation.REPEATABLE_READ) {
    // DML
}
```

- 整合后

整合后，事务已经委托给 Spring 来处理，事务相关 使用 `@Transactional` 即可，且 `database.useTransaction{ }` 无效了

注意 测试时 `@Transactional` 不要在单元测试类内使用哦，事务全给你回滚。我刚从坑🕳里爬出来！

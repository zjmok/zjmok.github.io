---
layout: post
tags: Java Kotlin
---

# 解决 kotlin data class 没有无参构造、类和方法默认为 final 的问题

最近在用 kotlin 写 mybatis 时, 报了个 `MyBatisSystemException`, 之前用 Java 时好好的, 

便查了一下资料, 发现这是 kotlin 使用一些注解和反射的库时普遍遇到的问题, 

原因是 `data class` 是没有无参构造的, 导致利用反射通过无参构造实例化时出错

** 解决办法**: 
 
显然官方早就有了解决方案, [官方文档](https://kotlinlang.org/docs/no-arg-plugin.html)

通过 `noArg` 插件在 `data class` 上使用注解便可生成一个无参构造函数

同时官方也说了, 这个无参构造函数是合成的，不能直接从 `Java` 或 `Kotlin` 调用，但可以使用反射调用

## No-Arg 插件, 给 data class 生成无参构造

### gradle 项目中配置插件
   
1. 在任意位置声明一个注解类
```
annotation class NoArg
```

2. 在 `build.gradle` 添加插件, 版本号改成项目中 kotlin 对应的版本就行
```
plugins {
    id "org.jetbrains.kotlin.plugin.noarg" version "1.9.10"
}
```

3. 同时在 `build.gradle` 最外层添加如下内容
```
noArg {
    invokeInitializers = true
    annotation("com.my.NoArg") // 换成您的注解类的完整路径
}
```

4. 使用
```
@NoArg
data class User (
    val username: String,
)
```

### Maven 项目中配置插件

```
<plugin>
    <artifactId>kotlin-maven-plugin</artifactId>
    <groupId>org.jetbrains.kotlin</groupId>
    <version>${kotlin.version}</version>

    <configuration>
        <compilerPlugins>
            <!-- Or "jpa" for JPA support -->
            <plugin>no-arg</plugin>
        </compilerPlugins>

        <pluginOptions>
            <!-- 换成您的注解类的完整路径 -->
            <option>no-arg:annotation=com.my.Annotation</option>
            <!-- Call instance initializers in the synthetic constructor -->
            <!-- <option>no-arg:invokeInitializers=true</option> -->
        </pluginOptions>
    </configuration>

    <dependencies>
        <dependency>
            <groupId>org.jetbrains.kotlin</groupId>
            <artifactId>kotlin-maven-noarg</artifactId>
            <version>${kotlin.version}</version>
        </dependency>
    </dependencies>
</plugin>
```

## All-open 插件, 默认 open

有一些框架例如 `Spring AOP`, 要求 Java 类是 public 的 / kotlin 是 open 的 

(`kotlin-spring` 可以自动 all-open)

### gradle 项目中配置插件

1. 在任意位置声明一个注解类
```
annotation class AllOpen
```

2. 在 `build.gradle` 添加插件, 版本号改成项目中 kotlin 对应的版本就行
```
plugins {
    id "org.jetbrains.kotlin.plugin.allopen" version "1.9.10"
}
```

3. 同时在 `build.gradle` 最外层添加如下内容
```
allOpen {
    annotation("com.my.AllOpen") // 换成您的注解类的完整路径
}
```

4. 使用 (可继承, 即父类/接口/注解使用了 `@AllOpen`, 其子类也有效)
```
@AllOpen
data class User (
    val username: String,
)
```

### Maven 项目中配置插件

```
<plugin>
    <artifactId>kotlin-maven-plugin</artifactId>
    <groupId>org.jetbrains.kotlin</groupId>
    <version>${kotlin.version}</version>

    <configuration>
        <compilerPlugins>
            <!-- Or "spring" for the Spring support -->
            <plugin>all-open</plugin>
        </compilerPlugins>

        <pluginOptions>
            <!-- 换成您的注解类的完整路径 -->
            <option>all-open:annotation=com.my.Annotation</option>
        </pluginOptions>
    </configuration>

    <dependencies>
        <dependency>
            <groupId>org.jetbrains.kotlin</groupId>
            <artifactId>kotlin-maven-allopen</artifactId>
            <version>${kotlin.version}</version>
        </dependency>
    </dependencies>
</plugin>
```

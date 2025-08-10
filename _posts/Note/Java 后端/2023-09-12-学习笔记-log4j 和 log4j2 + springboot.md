---
layout: post
tags: Java
---

## log4j

log4j.properties

```
# Global logging configuration
# 设置日志输出级别以及输出目的地，可以设置多个输出目的地，开发环境下，日志级别要设置成DEBUG或者ERROR
# 前面写日志级别，逗号后面写输出目的地：我自己下面设置的目的地相对应，以逗号分开
# log4j.rootLogger = [level],appenderName1,appenderName2,…
log4j.rootLogger=DEBUG,CONSOLE,LOGFILE
########
#### 输出到控制台 ####
log4j.appender.CONSOLE=org.apache.log4j.ConsoleAppender
# 使用标准输出
log4j.appender.CONSOLE.Target=System.out
# 日志级别
log4j.appender.CONSOLE.Threshold=DEBUG
# 是否立即输出，默认值是 true
log4j.appender.CONSOLE.ImmediateFlush=true
# 设置编码方式
log4j.appender.CONSOLE.Encoding=UTF-8
# 日志输出布局
log4j.appender.CONSOLE.layout=org.apache.log4j.PatternLayout
# 如果日志输出布局为 PatternLayout 自定义级别，需要使用 ConversionPattern 指定输出格式
log4j.appender.CONSOLE.layout.ConversionPattern=%d{yyyy-MM-dd HH:mm:ss} %5p (%c:%L): %msg%n
########
#### 输出到文件 ####
log4j.appender.LOGFILE=org.apache.log4j.FileAppender
# 日志输出到文件，默认为true
log4j.appender.LOGFILE.Append=true
# 指定输出文件路径
# log4j.appender.LOGFILE.File = Z://logs/error.log
log4j.appender.LOGFILE.File=./logs/error_%d{yyyy-MM-dd}.log
# 日志级别
log4j.appender.LOGFILE.Threshold=ERROR
# 是否立即输出，默认值是 true
log4j.appender.LOGFILE.ImmediateFlush=true
# 设置编码方式
log4j.appender.LOGFILE.Encoding=UTF-8
# 日志输出布局
log4j.appender.LOGFILE.layout=org.apache.log4j.PatternLayout
# 如果日志输出布局为 PatternLayout 自定义级别，需要使用 ConversionPattern 指定输出格式
log4j.appender.LOGFILE.layout.ConversionPattern=%-d{yyyy/MM/dd HH:mm:ss.SSS} [%t:%r] %c#%M(%F:%L) [%p]: %msg%n
```

## log4j2

springboot 启动器的依赖

```

dependencies {
    // 依赖全局处理
    configurations.configureEach {
        resolutionStrategy {
            // log4j2 要求排除
            exclude group: 'org.springframework.boot', module: 'spring-boot-starter-logging'
//            exclude group: 'org.slf4j', module: 'slf4j-api'
            // logback-core 与 log4j-slf4j2-impl 冲突, 二选一
//            exclude group: 'org.apache.logging.log4j', module: 'log4j-slf4j2-impl'
            exclude group: 'ch.qos.logback', module: 'logback-classic'
            exclude group: 'ch.qos.logback', module: 'logback-core'
        }
    }

    implementation 'org.springframework.boot:spring-boot-starter-log4j2'
	// 其它依赖
	// ...
}
```

整合 springboot 后 application 配置

```
#日志配置 无特殊需求无需更改
logging:
  config: classpath:log4j2.xml
  level:
    root: INFO
    javax.activation: info
    org.apache.catalina: INFO
    org.apache.commons.beanutils.converters: INFO
    org.apache.coyote.http11.Http11Processor: INFO
    org.apache.http: INFO
    org.apache.tomcat: INFO
    org.springframework: INFO
    com.chinamobile.cmss.bdpaas.resource.monitor: DEBUG
```

log4j2.xml

```
<?xml version="1.0" encoding="UTF-8"?>
<Configuration>

    <properties>
        <property name="LOG_HOME">./logs</property>
    </properties>

    <!-- All < Trace < Debug < Info < Warn < Error < Fatal < OFF -->

    <Appenders>

        <!-- 控制台 标准输出 -->
        <Console name="Console" target="SYSTEM_OUT">
            <PatternLayout>
                <pattern>%d{HH:mm:ss.SSS} [%t:%r] %c#%M(%F:%L) [%p]: %msg%n</pattern>
            </PatternLayout>
        </Console>

        <!-- 文件日志 -->
        <!-- fileName 日志文件写入的文件, 达到条件后会归档到 filePattern -->
        <!-- filePattern 归档文件, `$i` 计数器, 归档时的计数 -->
        <RollingFile name="logFile"
                     fileName="${LOG_HOME}/error/${date:yyyy-MM}/day${date:dd}.log"
                     filePattern="${LOG_HOME}/error/%d{yyyy-MM}/day%d{dd}-%i.log">
            <PatternLayout>
                <pattern>%d{yyyy/MM/dd HH:mm:ss.SSS} [%t:%r] %c#%M(%F:%L) [%p]: %msg%n</pattern>
            </PatternLayout>
            <!-- 设置日志文件归档策略, fileName 归档到 filePattern -->
            <Policies>
                <!-- 满足 interval 个周期就会归档文件, 周期依赖 filePattern 中设置的最精确单位 -->
                <TimeBasedTriggeringPolicy interval="1"/>
                <!-- 满足达到指定 size 就会归档文件 -->
                <SizeBasedTriggeringPolicy size="64 MB"/>
            </Policies>
            <!--
            max 归档文件最大数量, 默认为 7 个, 超过上限后会被滚动覆盖
            (滚动覆盖: 假设 max=10, 当有 1 个新文件归档时, 文件 2 到 10 将会被重命名为 1 到 9, 新文件为 10),
            依赖于 filePattern 中的计数器 %i
            -->
            <DefaultRolloverStrategy max="10">
                <!-- 设置文件过期删除策略 -->
                <!-- maxDepth 目录深度 -->
                <Delete basePath="${LOG_HOME}" maxDepth="3">
                    <!--
                    以 bashPath 目录 开始查找 glob 文件
                    若 log 文件位于 logs/2023-09/day12_error.log
                    bashPath="logs" 的话, 那么 glob="*/*.log"
                    -->
                    <IfFileName glob="error/*/*.log"/>
                    <!-- 过期时间 -->
                    <IfLastModified age="7d"/>
                </Delete>
            </DefaultRolloverStrategy>
        </RollingFile>

        <RollingFile name="chargeRollingFile"
                     fileName="${LOG_HOME}/charge/${date:yyyy-MM}/day${date:dd}_error.log"
                     filePattern="${LOG_HOME}/charge/%d{yyyy-MM}/day%d{dd}_error-%i.log">
            <PatternLayout>
                <pattern>%d{yyyy/MM/dd HH:mm:ss.SSS} [%t:%r] %c#%M(%F:%L) [%p]: %msg%n</pattern>
            </PatternLayout>
            <Policies>
                <SizeBasedTriggeringPolicy size="16 MB"/>
            </Policies>
            <DefaultRolloverStrategy max="10"/>
        </RollingFile>

        <RollingFile name="testAutoDeleteRollingFile"
                     fileName="${LOG_HOME}/testAutoDelete/${date:yyyy-MM}/day${date:dd}.log"
                     filePattern="${LOG_HOME}/testAutoDelete/%d{yyyy-MM}/day%d{dd_HH-mm_ss}-%i.log">
            <PatternLayout>
                <pattern>%d{yyyy/MM/dd HH:mm:ss.SSS} [%t:%r] %c#%M(%F:%L) [%p]: %msg%n</pattern>
            </PatternLayout>
            <Policies>
                <TimeBasedTriggeringPolicy interval="1"/>
                <SizeBasedTriggeringPolicy size="2 KB"/>
            </Policies>
            <DefaultRolloverStrategy max="10">
                <Delete basePath="${LOG_HOME}" maxDepth="3">
                    <IfFileName glob="testAutoDelete/*/*.log"/>
                    <IfLastModified age="5s"/>
                </Delete>
            </DefaultRolloverStrategy>
        </RollingFile>

    </Appenders>

    <!--日志级别 -->
    <!-- all trace debug info warn error fatal off -->
    <Loggers>
        <!--
        过滤 logger name,
        可按前缀指定 (logger默认名称是类名全路径, LoggerFactory.getLogger(xxx) 传了 class 字节码对象 会转换为类名全路径),
        指定级别
        -->
        <!-- 自己项目 -->
        <logger name="com.example" level="all"/>
        <!-- spring -->
        <logger name="org.springframework" level="debug"/>
        <!-- 数据源 -->
        <logger name="com.zaxxer.hikari" level="Info"/>
        <logger name="druid.sql.statement" level="Info"/>
        <!-- mybatis -->
        <logger name="com.mybatis" level="debug"/>
        <!-- ktorm -->
        <logger name="Ktorm" level="debug"/>
        <logger name="org.ktorm" level="debug"/>

        <!-- 测试指定 name==charge 的 logger 单独指定输出策略 -->
        <!-- LoggerFactory.getLogger("charge") -->
        <!-- additivity 设置为 false (默认 true), 则这个 logger 不会将日志流反馈到 root 中 -->
        <logger name="charge" additivity="false" level="info">
            <!-- 这里不需要反馈到 root 中处理, ref 直接指定一个 RollingFile 的 Appender -->
            <appender-ref ref="chargeRollingFile" level="info"/>
        </logger>

        <!-- 测试过期自动删除 -->
        <logger name="testAutoDelete" additivity="false" level="info">
            <appender-ref ref="testAutoDeleteRollingFile" level="info"/>
        </logger>

        <root level="all">
            <appenderref ref="Console" level="all"/>
            <appenderref ref="logFile" level="error"/>
        </root>

    </Loggers>

</Configuration>
```

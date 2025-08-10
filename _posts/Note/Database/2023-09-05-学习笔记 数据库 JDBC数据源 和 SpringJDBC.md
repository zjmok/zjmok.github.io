---
layout: post
tags: 数据库
---

# 数据源

数据源，即数据源头，应用程序不需要关心底层实现，如何建立连接获取数据这些都交由数据源处理

Java 中 JDBC 只提供接口标准 DataSource, 由第三方自行实现

## 常用数据源库

- C3P0
...

- HikariCP 性能高, SpringBoot 默认数据源

- Druid 安全性高, 阿里出品

```
<!-- https://mvnrepository.com/artifact/com.alibaba/druid -->
<dependency>
	<groupId>com.alibaba</groupId>
	<artifactId>druid</artifactId>
	<version>1.2.18</version>
</dependency>
```

```
DruidDataSource dataSource = new DruidDataSource();
dataSource.setDriverClassName(drive);
dataSource.setUsername(username);
dataSource.setPassword(password);
dataSource.setUrl(url);

Connection connection = dataSource.getConnection();
// connection.setAutoCommit(false); // 开启事务, 即不自动提交
String sql = "select * from user where id=?";
PreparedStatement preparedStatement = connection.prepareStatement(sql);
preparedStatement.setInt(1, id); // 问号赋值
// preparedStatement.executeUpdate(); // SML 增删改
// connection.commit(); // 提交事务
ResultSet resultSet = preparedStatement.executeQuery();// SQL 查
while (resultSet.next()) {
	System.out.println("result=" + resultSet.getInt(1));
	System.out.println("result=" + resultSet.getString("username"));
}

connection.close();
```

## JDBC 配置文件抽取

`jdbc.properties`
```
jdbc.driver=com.mysql.cj.jdbc.Driver
jdbc.username=root
jdbc.password=root
jdbc.url=jdbc:mysql://localhost:3306/mytest
```

使用 `ResourceBundle` 读取配置文件
```
ResourceBundle bundle = ResourceBundle.getBundle("jdbc");
String driver = bundle.getString("jdbc.driver");
String username = bundle.getString("jdbc.username");
String password = bundle.getString("jdbc.password");
String url = bundle.getString("jdbc.url");
```

## 用 Spring 容器提供数据源

`applicationContext.xml`
```
<!-- spring 引入配置文件 -->
<context:property-placeholder location="classpath:jdbc.properties"/>

<!-- 注入数据源 -->
<!-- property 属性名根据 DruidDataSource 而定 -->
<bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource">
	<property name="driverClassName" value="${jdbc.driver}"/>
	<property name="username" value="${jdbc.username}"/>
	<property name="password" value="${jdbc.password}"/>
	<property name="url" value="${jdbc.url}"/>
</bean>
```

## SpringJDBC JDBCTemplate

JDBCTemplate 自动处理连接池和资源关闭, 所以开发者只需要关系调用 sql

步骤：
1. 导入 spring-jdbc 和 spring-tx 依赖
2. 创建数据库表和实体
3. 创建 JdbcTemplate 对象（需要创建数据源传入）
4. 执行数据库操作


- SML
  - update()

- SQL
  - queryForMap
  - queryForList
  - query
  - queryForObject

JDBCTemplate DQL
```
// 从容器其获取 dataSource
@Autowired
private DataSource dataSource;

@Test
public void testDataSource() throws SQLException {
	JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
	String sql = "select * from user";
	List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
	String json = new GsonBuilder()
			.setPrettyPrinting()
			.create()
			.toJson(list);
	System.out.println("json=" + json);
}
```

JDBCTemplate DML 开启事务
```
// 从容器其获取 dataSource
@Autowired
private DataSource dataSource;

@Test
public void testJDBCTemplate() {
	DataSourceTransactionManager manager = new DataSourceTransactionManager(dataSource);
	TransactionDefinition definition = new DefaultTransactionDefinition();
	TransactionStatus transaction = manager.getTransaction(definition); // 开启事务

	try {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		String sql = "INSERT INTO `user` (`id`, `username`, `password`) VALUES ('2', 'ZhangSan', 'qwer');";
		jdbcTemplate.update(sql);
		manager.commit(transaction); // 提交事务
		System.out.println("执行成功, 提交事务");
	} catch (DataAccessException e) {
		e.printStackTrace();
		manager.rollback(transaction); // 回滚
		System.out.println("执行失败, 回滚");
	}
}
```

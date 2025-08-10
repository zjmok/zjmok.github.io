---
layout: post
tags: 数据库 Mybatis
---

Mybatis

ORM 映射框架, 底层是 JDBC

---

集成步骤

1. 依赖 
2. 创建表
3. 创建实体
4. 编辑映射文件 `UserMaooer.xml`
5. 编辑核心文件 `SqlMapConfig.xml`
6. 测试

---

使用步骤

// 1. 加载核心配置文件
Resources.getResourceAsStream

// 2. 获得 sqlSession 工厂对象


// 3. 获得 sqlSession 对象


// 4. 执行 sql 语句


// 5. 操作返回对象
// do something

// 6. 关闭资源
sqlSession.close();

```
@Test
public void getAll() throws IOException {
	// 1. 加载核心配置文件
	InputStream resourceAsStream = Resources.getResourceAsStream("mybatis_config.xml");
	// 2. 获得 sqlSession 工厂对象
	SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);
	// 3. 获得 sqlSession 对象
	SqlSession sqlSession = sqlSessionFactory.openSession();
	// 4. 执行 sql 语句
	List<User> list = sqlSession.selectList("myMapper.getAll");
	// 5. 操作返回对象
	String json = new GsonBuilder()
			.setPrettyPrinting()
			.create()
			.toJson(list);
	System.out.println(json);
	// 6. 关闭资源
	sqlSession.close();
}
```

---

查询-xml

标签:

- insert
- delete
- update
- select

- id
- result
- resultMap

- association
- collection

多表查询-xml 配置方式

- 一对一: 使用 `<resultMap>` 做配置

- 一对多: 使用 `<resultMap>` + `<collection>` 做配置

- 多对多: 使用 `<resultMap>` + `<collection>` 做配置

---

查询-注解

- @Insert
- @Delete
- @Update
- @Select

- @Result 封装结果集, 只要实体类属性对应数据库字段就不用手动转换
- @Results 与 @Result 一起使用 封装多个结果集
- @One 一对一 结果集封装
- @Many 一对多 结果集封装

多表查询-注解

- @Results 代替 <resultMap> 标签

- @Result 代替 <id> <result> 标签

  - property 实体类属性名
  
  - column 数据库字段名
  
  - one 一对一 需要使用 @One
  
  - many 一对多 需要使用 @Many


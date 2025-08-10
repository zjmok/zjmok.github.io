---
layout: post
tags: Java SpringBoot
---

# SpringBoot 各层的含义和职责

`Controller (控制层，处理请求和响应) --> Service (服务层，业务逻辑) --> Repository (数据访问层，与数据库交互) --> DataBase (数据库层，MyBatis Ktorm Hibernate 等 ORM)`

Domain (领域层，或 Model，数据库映射对象类，业务模型)

Dto (数据传输对象，在 Controller 响应时返回给客户端)

---

在 Spring Boot 和一般的分层架构中，通常会将应用程序的代码分为多个层次，以便于管理和维护

### 1. Controller

**含义**：控制层，负责处理 HTTP 请求和响应。

**职责**：
- 接收和处理来自客户端的请求（如 GET、POST）。
- 调用服务层（Service）的业务逻辑。
- 将服务层的结果转换为适合客户端的响应格式（如 JSON）。
- 处理请求参数的验证和错误处理。

**示例**：
```java
@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        UserDto user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }
}
```

### 2. Service

**含义**：服务层，包含业务逻辑。

**职责**：
- 处理与业务相关的逻辑和规则。
- 调用数据访问层（Repository）进行数据操作。
- 进行事务管理（如需要）。
- 可以将多个 Repository 的操作组合成一个业务操作。

**示例**：
```java
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return new UserDto(user);
    }
}
```

### 3. Repository

**含义**：数据访问层，负责与数据库进行交互。

**职责**：
- 提供 CRUD（创建、读取、更新、删除）操作。
- 处理数据库查询，通常使用 ORM（如 JPA/Hibernate）来简化数据操作。
- 可以定义复杂的查询方法。

**示例**：
```java
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
```

使用 MyBatis、Ktorm 的项目可以省略 Repository，直接在 Service 调用 Mapper

- **MyBatis（Mapper 层）**：在 MyBatis 中，Mapper 接口通常是与数据库表相对应的，每个方法通常映射到一个 SQL 语句。Service 层可以直接调用这些 Mapper 方法来执行数据库操作
- **Repository**：对 Domain 对象的访问和持久化，通常基于 DAO。
- **DAO**：直接与数据库交互，执行具体的 CRUD 操作。

在一些应用中，Repository 和 DAO 的功能可能会重叠，特别是在简单的项目中，但在更复杂的系统中，它们的分离有助于提高可维护性和可测试性。

### 4. Domain（或 Model）

**含义**：领域层，定义业务模型。

**职责**：
- 表示应用程序的核心业务对象（如用户、订单等）。
- 包含与业务相关的属性和行为（方法）。
- 可以与数据库表一一对应（在使用 ORM 时）。

**示例**：
```java
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;

    // Getter 和 Setter
}
```

### 5. DTO（Data Transfer Object）

**含义**：数据传输对象，专门用于在不同层之间传输数据。

**职责**：
- 用于将数据从 Controller 传递到前端或从 Service 传递到 Controller。
- 通常只包含用于传输的属性，不包含业务逻辑。
- 可以在 DTO 中进行数据的格式化或验证。

**示例**：
```java
public class UserDto {
    private Long id;
    private String username;

    public UserDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
    }

    // Getter 和 Setter
}
```

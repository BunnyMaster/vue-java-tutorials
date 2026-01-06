在Java项目中，需要测试的主要有以下几类：

## 必须测试的（高优先级）

### 1. **业务逻辑层 (Service/Manager)**
```java
// 必须测试！包含核心业务逻辑
@Service
public class OrderService {
    public Order createOrder(OrderRequest request) {
        // 复杂的业务规则
        // 多步骤流程
        // 外部依赖协调
    }
}
```

### 2. **工具类/工具方法 (Utils/Helpers)**
```java
// 必须测试！被广泛复用
public class StringUtils {
    public static boolean isValidEmail(String email) {
        // 通用逻辑，任何错误都会影响多处
    }
}
```

### 3. **模型中的复杂方法 (Model复杂行为)**
```java
// 如果模型有复杂行为，需要测试
public class Order {
    // 简单getter/setter不测试
    
    public BigDecimal calculateTotal() {
        // 包含计算逻辑，需要测试
        return items.stream()
            .map(item -> item.getPrice().multiply(item.getQuantity()))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
```

### 4. **配置类 (Config)**
```java
// 关键配置需要测试
@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) {
        // 安全配置，需要验证是否正确
        return http.build();
    }
}
```

## 选择性测试的（中优先级）

### 5. **数据传输对象 (DTO/VO)**
```java
// 如果只有getter/setter，通常不单独测试
// 但如果包含转换逻辑，需要测试
public class UserDTO {
    public static UserDTO fromEntity(User user) {
        // 转换逻辑需要测试
        return new UserDTO(user.getName(), user.getEmail());
    }
}
```

### 6. **简单的配置类**
```java
// 如果只是简单的@Bean定义，通常通过集成测试覆盖
@Configuration
public class SimpleConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
```

## 通常不单独测试的（低优先级）

### 7. **纯数据模型 (POJO)**
```java
// 没有业务逻辑，只有getter/setter
@Entity
public class User {
    private Long id;
    private String name;
    // Lombok生成的getter/setter不测试
}
```

### 8. **简单的常量类**
```java
public class Constants {
    public static final String API_VERSION = "v1";
    // 常量定义不测试
}
```

## 测试策略建议

### 按类型划分的测试重点：
```java
// Helper/Utils → 单元测试全覆盖
// Service → 单元测试 + 集成测试  
// Controller → 集成测试/API测试
// Repository → 集成测试（用@TestRepository或内存数据库）
// Config → 集成测试验证配置生效
```

### 测试优先级矩阵：
```
高优先级：
- 包含业务规则的任何代码
- 被多个地方复用的代码
- 与外部系统交互的代码
- 安全相关的代码

中优先级：
- 数据转换逻辑
- 简单的流程控制

低优先级：
- 自动生成的代码
- 纯配置（无逻辑）
- 简单的getter/setter
```

## 实际项目中的测试实践

```java
// 项目结构示例
src/main/java/
├── model/           # 简单模型不单独测试
├── dto/            # 有转换逻辑的才测试
├── service/        # 必须测试（核心业务）
├── utils/          # 必须测试（通用工具）
├── config/         # 关键配置要测试
└── controller/     # 集成测试为主

src/test/java/
├── unit/           # 单元测试
│   ├── service/    # Service单元测试
│   ├── utils/      # Utils单元测试
│   └── helpers/    # Helpers单元测试
└── integration/    # 集成测试
    ├── config/     # 配置集成测试
    └── api/        # API端点测试
```

## 实用原则

1. **ROI原则**：测试投入要与风险/价值成正比
2. **核心原则**：业务核心逻辑必须测试
3. **复用原则**：被多处复用的代码必须测试
4. **边界原则**：系统边界（API、DB、外部服务）需要测试
5. **复杂原则**：复杂算法/逻辑必须测试

记住：**测试的是行为，而不是实现**。重点测试代码做了什么，而不是怎么做。
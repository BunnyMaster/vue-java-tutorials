## 清晰的分类和职责

### Controller参数

| 类型                 | 用途                     | 示例                | 推荐度 |
| -------------------- | ------------------------ | ------------------- | ------ |
| **Request / Form**   | 请求体（POST/PUT/PATCH） | `UserCreateRequest` | ⭐⭐⭐⭐⭐  |
| **Query / Criteria** | 查询参数（GET）          | `UserSearchQuery`   | ⭐⭐⭐⭐⭐  |
| **Command**          | CQRS模式中的命令         | `CreateUserCommand` | ⭐⭐⭐⭐   |
| **@RequestParam**    | 简单查询参数             | `String name`       | ⭐⭐⭐    |
| **@PathVariable**    | 路径参数                 | `Long id`           | ⭐⭐⭐⭐⭐  |
| **Pageable**         | 分页参数                 | `Pageable pageable` | ⭐⭐⭐⭐   |
| **DTO（特殊情况）**  | 内部API/管理后台         | `UserDTO`           | ⭐⭐     |

### Controller返回值

| 类型                   | 用途         | 示例                     | 推荐度 |
| ---------------------- | ------------ | ------------------------ | ------ |
| **VO / Response**      | 前端展示     | `UserVO`, `UserResponse` | ⭐⭐⭐⭐⭐  |
| **DTO**                | 业务数据传输 | `UserDTO`                | ⭐⭐⭐⭐   |
| **ResponseEntity**     | 完整HTTP响应 | `ResponseEntity<UserVO>` | ⭐⭐⭐⭐   |
| **Page / Slice**       | 分页数据     | `Page<UserVO>`           | ⭐⭐⭐⭐   |
| **Result / ApiResult** | 包装响应     | `Result<UserVO>`         | ⭐⭐⭐    |

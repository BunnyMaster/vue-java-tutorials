package com.spring.step2.security.service;

// @Service
// @RequiredArgsConstructor
// public class InMemoryUserDetailsService implements UserDetailsService {
//
//     private final PasswordEncoder passwordEncoder;
//
//     @Override
//     public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//         // 1. 这里应该根据username从数据库或其他存储中查询用户信息
//         // 以下是模拟数据，实际应用中应从数据库查询
//
//         // 2. 如果用户不存在，抛出UsernameNotFoundException
//         if (!"bunny".equalsIgnoreCase(username)) {
//             throw new UsernameNotFoundException("User not found: " + username);
//         }
//
//         // 3. 构建UserDetails对象返回
//         return User.builder()
//                 .username(username)  // 使用传入的用户名
//                 .password(passwordEncoder.encode("123456"))  // 密码应该已经加密存储，这里仅为示例
//                 .roles("USER")  // 角色会自动添加ROLE_前缀
//                 .authorities("read", "write")  // 添加具体权限
//                 .build();
//     }
// }
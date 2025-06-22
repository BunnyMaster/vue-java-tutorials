# Vue Router 路由管理与导航控制指南

## 一、路由导航核心方法

### 1. 编程式导航 API

| 方法      | 说明                  | 示例                             |
| --------- | --------------------- | -------------------------------- |
| `push`    | 添加新路由记录        | `this.$router.push('/about')`    |
| `replace` | 替换当前路由          | `this.$router.replace('/login')` |
| `go`      | 在历史记录中前进/后退 | `this.$router.go(-1)`            |
| `back`    | 后退一步              | `this.$router.back()`            |
| `forward` | 前进一步              | `this.$router.forward()`         |

### 2. 命名路由使用
```javascript
// 路由配置
{
  path: '/about',
  name: 'About',  // 命名路由
  component: About
}

// 导航使用
this.$router.push({ name: 'About' })
```

## 二、最佳实践建议

### 1. 导航封装方案
```javascript
// utils/navigation.js
export const navTo = (name, params = {}) => {
  router.push({ name, params })
}

// 组件中使用
import { navTo } from '@/utils/navigation'
methods: {
  goToAbout() {
    navTo('About')
  }
}
```

### 2. 导航守卫整合
```javascript
// 全局前置守卫
router.beforeEach((to, from, next) => {
  if (to.meta.requiresAuth && !isAuthenticated()) {
    next({ name: 'Login' })
  } else {
    next()
  }
})
```

### 3. 路由懒加载
```javascript
// 路由配置优化
const routes = [
  {
    path: '/about',
    component: () => import('@/views/About.vue')
  }
]
```

## 三、高级路由模式

### 1. 动态路由匹配
```javascript
// 路由配置
{
  path: '/user/:id',
  component: User
}

// 组件中获取参数
this.$route.params.id
```

### 2. 嵌套路由实践
```javascript
const routes = [
  {
    path: '/dashboard',
    component: Dashboard,
    children: [
      { path: 'profile', component: Profile },
      { path: 'settings', component: Settings }
    ]
  }
]
```

### 3. 路由过渡动画
```html
<transition name="fade" mode="out-in">
  <router-view></router-view>
</transition>

<style>
.fade-enter-active, .fade-leave-active {
  transition: opacity 0.3s;
}
.fade-enter, .fade-leave-to {
  opacity: 0;
}
</style>
```

## 四、常见问题解决方案

1. **路由重复导航错误**：
   ```javascript
   // 捕获重复导航错误
   this.$router.push('/path').catch(err => {
     if (err.name !== 'NavigationDuplicated') {
       console.error(err)
     }
   })
   ```

2. **页面刷新参数丢失**：
   ```javascript
   // 使用query代替params
   this.$router.push({ path: '/user', query: { id: 123 } })
   ```

3. **滚动行为控制**：
   ```javascript
   const router = new VueRouter({
     scrollBehavior(to, from, savedPosition) {
       return savedPosition || { x: 0, y: 0 }
     }
   })
   ```

## 五、与Vue3 Router对比

| 特性      | Vue2 Router        | Vue3 Router                   |
| --------- | ------------------ | ----------------------------- |
| 创建方式  | `new VueRouter()`  | `createRouter()`              |
| 历史模式  | `mode: 'history'`  | `history: createWebHistory()` |
| 路由匹配  | 基于path-to-regexp | 基于正则表达式                |
| 组合式API | 需要额外配置       | 原生支持                      |


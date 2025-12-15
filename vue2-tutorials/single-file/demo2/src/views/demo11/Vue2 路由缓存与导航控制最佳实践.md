# Vue2 路由缓存与导航控制最佳实践

## 一、Keep-Alive 路由缓存机制

### 1. 基本使用方式
```html
<!-- 缓存单个组件 -->
<keep-alive include="Home">
  <router-view/>
</keep-alive>

<!-- 缓存多个组件 -->
<keep-alive :include="['Home', 'About']">
  <router-view/>
</keep-alive>
```

### 2. 核心属性说明
| 属性      | 类型                | 说明                   |
| --------- | ------------------- | ---------------------- |
| `include` | String/Regexp/Array | 匹配的组件名将被缓存   |
| `exclude` | String/Regexp/Array | 匹配的组件名不会被缓存 |
| `max`     | Number              | 最大缓存组件数         |

## 二、组件生命周期变化

### 1. 缓存状态下的生命周期
```
首次加载：created → mounted → activated
离开组件：deactivated
再次进入：activated
```

### 2. 新增生命周期钩子
```javascript
export default {
  activated() {
    // 组件被激活时调用
    this.fetchData()
  },
  deactivated() {
    // 组件被停用时调用
    this.clearTimer()
  }
}
```

## 三、最佳实践建议

### 1. 缓存策略优化
```html
<!-- 动态决定是否缓存 -->
<keep-alive :include="cachedViews">
  <router-view :key="$route.fullPath"/>
</keep-alive>

<script>
computed: {
  cachedViews() {
    return this.$store.state.tagsView.cachedViews
  }
}
</script>
```

### 2. 数据刷新方案
```javascript
// 方案1：监听路由变化
watch: {
  '$route'(to, from) {
    if (to.name === 'Home') {
      this.refreshData()
    }
  }
}

// 方案2：使用activated钩子
activated() {
  this.loadData()
}
```

### 3. 内存管理技巧
```javascript
// 清除缓存数据
deactivated() {
  this.list = []
  this.pagination = { page: 1, size: 10 }
}
```

## 四、高级应用场景

### 1. 多级路由缓存
```javascript
// 父路由配置
{
  path: '/nested',
  component: Layout,
  children: [
    {
      path: 'child1',
      component: Child1,
      meta: { keepAlive: true }
    },
    {
      path: 'child2',
      component: Child2
    }
  ]
}

// 动态缓存
<keep-alive>
  <router-view v-if="$route.meta.keepAlive"/>
</keep-alive>
<router-view v-if="!$route.meta.keepAlive"/>
```

### 2. 滚动位置保持
```javascript
// router配置
scrollBehavior(to, from, savedPosition) {
  if (savedPosition) {
    return savedPosition
  } else if (to.meta.keepAlive) {
    return { x: 0, y: to.meta.scrollTop || 0 }
  }
  return { x: 0, y: 0 }
}

// 组件内记录位置
deactivated() {
  this.$route.meta.scrollTop = document.documentElement.scrollTop
}
```

### 3. 缓存状态管理
```javascript
// 动态移除缓存
this.$vnode.parent.componentInstance.cache = {}
this.$vnode.parent.componentInstance.keys = []
```

## 五、常见问题解决方案

1. **缓存不生效排查**：
   - 检查组件`name`是否与`include`匹配
   - 验证路由配置是否正确
   - 确保没有使用`v-if`干扰

2. **数据不更新问题**：
   ```javascript
   // 使用路由参数作为key强制更新
   <router-view :key="$route.fullPath"/>
   ```

3. **内存泄漏预防**：
   ```javascript
   deactivated() {
     // 清除定时器/事件监听
     clearInterval(this.timer)
     window.removeEventListener('resize', this.handleResize)
   }
   ```

## 六、与Vue3的区别

| 特性      | Vue2                    | Vue3                      |
| --------- | ----------------------- | ------------------------- |
| 缓存控制  | `include/exclude`       | 新增`max`属性             |
| 生命周期  | `activated/deactivated` | 需要导入`onActivated`钩子 |
| 路由缓存  | 需要`keep-alive`        | 内置路由组件缓存          |
| 组合式API | 不支持                  | 提供`onActivated`等钩子   |


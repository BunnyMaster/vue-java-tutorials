# Vue2 Transition 动画系统详解

## 一、基本动画实现

### 1. Transition 组件核心用法
```html
<Transition name="bunny" :appear="true">
  <div v-show="visibility">内容</div>
</Transition>
```

| 属性     | 说明                 | 示例值   |
| -------- | -------------------- | -------- |
| `name`   | 动画类名前缀         | "bunny"  |
| `appear` | 初始渲染是否应用动画 | true     |
| `mode`   | 过渡模式             | "out-in" |

### 2. 动画类名系统
```css
/* 进入动画 */
.bunny-enter-active { /* 激活阶段样式 */ }
.bunny-enter { /* 开始状态 */ }
.bunny-enter-to { /* 结束状态 */ }

/* 离开动画 */
.bunny-leave-active { /* 激活阶段样式 */ }
.bunny-leave { /* 开始状态 */ }
.bunny-leave-to { /* 结束状态 */ }
```

## 二、动画效果进阶

### 1. CSS 动画实现
```css
/* 关键帧动画 */
@keyframes slip {
  from { transform: translateX(-100%); }
  to { transform: translateX(0); }
}

.bunny-enter-active {
  animation: slip 0.5s ease-out;
}

.bunny-leave-active {
  animation: slip 0.5s ease-in reverse;
}
```

### 2. 过渡效果实现
```css
/* 过渡效果 */
.bunny-enter-active, .bunny-leave-active {
  transition: all 0.5s cubic-bezier(0.68, -0.55, 0.27, 1.55);
}

.bunny-enter, .bunny-leave-to {
  opacity: 0;
  transform: scale(0.8);
}
```

## 三、最佳实践指南

### 1. 性能优化建议
- 优先使用 `transform` 和 `opacity` 属性
- 避免动画期间触发布局重排
- 对移动设备减少动画持续时间

### 2. 动画调试技巧
```css
/* 调试用边框 */
.bunny-enter-active {
  outline: 2px solid red;
}
```

### 3. 响应式动画方案
```javascript
computed: {
  animationDuration() {
    return this.isMobile ? 300 : 500
  }
}
```

## 四、高级应用场景

### 1. 列表过渡
```html
<TransitionGroup name="list" tag="ul">
  <li v-for="item in items" :key="item.id">
    {{ item.text }}
  </li>
</TransitionGroup>

<style>
.list-move {
  transition: transform 0.5s ease;
}
</style>
```

### 2. 动态过渡
```html
<Transition :name="transitionName">
  <!-- ... -->
</Transition>

<script>
data() {
  return {
    transitionName: isMobile ? 'slide' : 'fade'
  }
}
</script>
```

### 3. JavaScript 钩子
```html
<Transition
  @before-enter="beforeEnter"
  @enter="enter"
  @after-enter="afterEnter"
>
  <!-- ... -->
</Transition>

<script>
methods: {
  beforeEnter(el) {
    el.style.opacity = 0
  },
  enter(el, done) {
    anime({
      targets: el,
      opacity: 1,
      duration: 500,
      complete: done
    })
  }
}
</script>
```

## 五、常见问题解决方案

1. **动画不生效排查**：
   - 检查类名前缀是否匹配
   - 确认元素初始状态是否正确
   - 验证CSS属性是否可动画

2. **闪烁问题处理**：
   ```css
   .v-enter {
     opacity: 0.01; /* 避免初始状态检测问题 */
   }
   ```

3. **多元素过渡**：
   ```html
   <Transition mode="out-in">
     <div :key="currentView">{{ currentView }}</div>
   </Transition>
   ```

## 六、与Vue3的对比

| 特性     | Vue2                   | Vue3             |
| -------- | ---------------------- | ---------------- |
| 类名前缀 | `v-` 或自定义          | 相同             |
| 过渡模式 | 支持 `in-out`/`out-in` | 新增 `in-out-in` |
| 性能     | 基于CSS                | 基于FLIP优化     |
| 新特性   | -                      | 过渡持久化       |


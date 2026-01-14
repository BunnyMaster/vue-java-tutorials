# Vue2 过渡动画与 Animate.css 整合指南

## 一、Transition 与 TransitionGroup 对比

| 特性     | Transition        | TransitionGroup |
| -------- | ----------------- | --------------- |
| 用途     | 单个元素/组件过渡 | 列表项过渡      |
| 要求     | 需要条件渲染      | 需要 `key` 属性 |
| 动画类   | 自动生成          | 可自定义类名    |
| 额外效果 | -                 | 自动处理定位    |

## 二、Animate.css 集成方案

### 1. 基本集成方法
```html
<TransitionGroup
  name="animate__animated animate__bounce"
  appear
  enter-active-class="animate__flipInX"
  leave-active-class="animate__flipOutX"
>
  <!-- 列表项 -->
</TransitionGroup>
```

### 2. 常用动画效果
| 进入效果               | 离开效果                 | 说明     |
| ---------------------- | ------------------------ | -------- |
| `animate__fadeIn`      | `animate__fadeOut`       | 淡入淡出 |
| `animate__zoomIn`      | `animate__zoomOut`       | 缩放效果 |
| `animate__slideInLeft` | `animate__slideOutRight` | 滑动效果 |
| `animate__flipInX`     | `animate__flipOutX`      | 3D翻转   |

## 三、自定义动画与库动画结合

### 1. 混合使用示例
```html
<!-- 自定义单个元素动画 -->
<Transition name="bunny">
  <h3 v-show="visibility">自定义动画</h3>
</Transition>

<!-- Animate.css 列表动画 -->
<TransitionGroup 
  enter-active-class="animate__animated animate__fadeInUp"
  leave-active-class="animate__animated animate__fadeOutDown"
>
  <li v-for="item in items" :key="item.id">{{ item.text }}</li>
</TransitionGroup>
```

### 2. 动画性能优化
```css
/* 启用GPU加速 */
.animate__animated {
  animation-duration: 0.5s;
  backface-visibility: hidden;
}

/* 限制动画影响范围 */
.bunny-enter-active, .bunny-leave-active {
  will-change: transform;
  isolation: isolate;
}
```

## 四、最佳实践建议

### 1. 动画选择原则
- **移动端**：使用较轻量的动画（如fade）
- **重要交互**：使用明显的反馈动画（如bounce）
- **连续操作**：保持动画风格一致

### 2. 时间控制技巧
```javascript
// 动态控制动画时长
data() {
  return {
    duration: window.innerWidth < 768 ? 300 : 500
  }
}
```

### 3. 无障碍访问
```css
/* 减少运动偏好设置 */
@media (prefers-reduced-motion: reduce) {
  .animate__animated,
  .bunny-enter-active,
  .bunny-leave-active {
    animation: none !important;
    transition: none !important;
  }
}
```

## 五、进阶应用场景

### 1. 路由过渡动画
```html
<Transition :name="transitionName" mode="out-in">
  <router-view></router-view>
</Transition>

<script>
export default {
  data() {
    return {
      transitionName: 'slide'
    }
  },
  watch: {
    '$route'(to, from) {
      this.transitionName = to.meta.transition || 'fade'
    }
  }
}
</script>
```

### 2. 状态驱动的动画
```javascript
computed: {
  animationClass() {
    return this.isError 
      ? 'animate__shakeX' 
      : 'animate__tada'
  }
}
```

### 3. 动画事件钩子
```html
<Transition
  @before-enter="beforeEnter"
  @after-enter="afterEnter"
>
  <div v-if="show">内容</div>
</Transition>

<script>
methods: {
  beforeEnter(el) {
    el.style.opacity = 0
  },
  afterEnter(el) {
    // 动画结束后的处理
  }
}
</script>
```

## 六、常见问题解决

1. **动画不连贯**：
   - 使用 `mode="out-in"` 确保顺序执行
   - 检查 `key` 属性是否唯一

2. **初始渲染无动画**：
   - 添加 `appear` 属性
   - 检查初始状态是否正确

3. **移动端性能差**：
   - 减少动画复杂度
   - 使用 `will-change` 提示浏览器优化


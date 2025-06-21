# Vue2 组件通信与事件处理深度指南

## 一、组件通信核心方式

### 1. 父子组件通信模式

#### Props 向下传递
```javascript
// 父组件
<Child :title="pageTitle" />

// 子组件
props: {
  title: String
}
```

#### Events 向上传递
```javascript
// 子组件
this.$emit('update', newValue)

// 父组件
<Child @update="handleUpdate" />
```

### 2. 引用直接访问
```javascript
// 父组件
<Child ref="childRef" />

methods: {
  callChildMethod() {
    this.$refs.childRef.childMethod()
  }
}
```

## 二、事件系统高级用法

### 1. 原生事件绑定
```html
<!-- 必须添加.native修饰符 -->
<StudentInfo @click.native="handleClick" />
```

### 2. 手动事件管理
```javascript
// 绑定事件（推荐在mounted钩子中）
mounted() {
  this.$refs.studenInfo.$on("getStudentName", this.getStudentName)
}

// 解绑事件（必须在beforeDestroy中清理）
beforeDestroy() {
  this.$refs.studenInfo.$off("getStudentName")
}
```

### 3. 事件参数传递
```javascript
// 子组件触发时可带多个参数
this.$emit('event-name', arg1, arg2, ...)

// 父组件接收所有参数
handler(arg1, arg2, ...rest) {
  // 处理参数
}
```

## 三、最佳实践建议

### 1. 事件命名规范
- 使用 kebab-case 命名（如 `student-updated`）
- 避免与原生事件重名（添加业务前缀）
- 语义化命名（如 `form-submitted`）

### 2. 安全通信模式
```javascript
// 添加事件存在性检查
if (this._events['custom-event']) {
  this.$emit('custom-event', data)
}

// 使用try-catch包裹可能出错的事件处理
try {
  this.$emit('critical-action', payload)
} catch (error) {
  console.error('事件处理失败:', error)
}
```

### 3. 性能优化方案
```javascript
// 防抖处理高频事件
methods: {
  handleInput: _.debounce(function() {
    this.$emit('input-changed', this.value)
  }, 300)
}
```

## 四、高级通信场景

### 1. 跨级组件通信
```javascript
// 祖先组件
provide() {
  return {
    appData: this.sharedData
  }
}

// 后代组件
inject: ['appData']
```

### 2. 动态事件处理器
```javascript
// 根据状态切换处理函数
<Child @custom-event="handlerMap[currentHandler]" />

data() {
  return {
    currentHandler: 'default',
    handlerMap: {
      default: this.handleDefault,
      special: this.handleSpecial
    }
  }
}
```

### 3. 事件总线模式
```javascript
// event-bus.js
import Vue from 'vue'
export default new Vue()

// 组件A
EventBus.$emit('data-updated', payload)

// 组件B
EventBus.$on('data-updated', handler)
```

## 五、常见问题解决方案

1. **事件未触发排查**：
   - 检查组件引用是否正确（`ref` 命名）
   - 确认事件名称完全匹配（大小写敏感）
   - 验证事件绑定时机（确保在 mounted 之后）

2. **内存泄漏预防**：
   ```javascript
   beforeDestroy() {
     // 清除所有自定义事件监听
     this.$off()
     // 清除事件总线监听
     EventBus.$off('data-updated', this.handler)
   }
   ```

3. **异步事件处理**：
   ```javascript
   // 返回Promise的事件处理
   async handleEvent() {
     try {
       await this.$nextTick()
       const result = await this.$emitAsync('async-event')
       // 处理结果
     } catch (error) {
       // 错误处理
     }
   }
   ```

## 六、与Vue3的对比

| 特性     | Vue2           | Vue3                 |
| -------- | -------------- | -------------------- |
| 事件定义 | 隐式声明       | `emits` 选项显式声明 |
| 原生事件 | 需要 `.native` | 自动识别             |
| 移除API  | -              | 移除 `$on`, `$off`   |
| 性能     | 基于观察者     | 基于Proxy的响应式    |


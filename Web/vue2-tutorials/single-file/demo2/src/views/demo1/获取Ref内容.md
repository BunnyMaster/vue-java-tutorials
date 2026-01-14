# Vue2 中的 ref 引用机制详解

## 一、ref 的基本概念

`ref` 是 Vue2 提供的一种特殊的属性，用于直接访问 DOM 元素或子组件实例。它比传统的 `document.getElementById` 等方式更适合在 Vue 生态中使用。

## 二、代码解析

```html
<template>
  <div id="app">
    <!-- 绑定到DOM元素 -->
    <h1 ref="title" v-text="message"></h1>
    <button @click="ShowDom">输出上方DOM元素</button>
    
    <!-- 绑定到子组件 -->
    <SchoolInfo ref="schoolInfoRef" />
  </div>
</template>

<script>
import SchoolInfo from "@/components/SchoolInfo.vue";

export default {
  name: "App",
  components: { SchoolInfo },
  data() {
    return { message: "学习Vue2" };
  },
  methods: {
    ShowDom() {
      // 访问DOM元素
      console.log(this.$refs.title); // 输出: <h1>学习Vue2</h1>
      
      // 访问子组件实例
      console.log(this.$refs.schoolInfoRef); // 输出: SchoolInfo组件实例
    }
  }
};
</script>
```

## 三、ref 的核心特性

### 1. 绑定目标类型

| 绑定目标  | 获取内容         | 使用场景                |
| --------- | ---------------- | ----------------------- |
| DOM元素   | 原生DOM节点      | 直接操作DOM             |
| 组件      | 组件实例         | 调用子组件方法/访问数据 |
| v-for元素 | DOM数组/组件数组 | 操作列表元素            |

### 2. 生命周期时机

- **创建**：在组件 `mounted` 钩子之后可用
- **更新**：响应式更新后自动同步
- **销毁**：组件销毁时自动解除引用

### 3. 访问方式

通过组件实例的 `$refs` 对象访问：
```javascript
this.$refs.refName
```

## 四、最佳实践

### 1. 命名规范
- 使用 camelCase 命名（如 `schoolInfoRef`）
- 避免使用保留字或Vue内置名称
- 保持命名语义化（如 `formRef`, `tableRef`）

### 2. 安全访问模式
```javascript
methods: {
  submitForm() {
    // 先检查是否存在
    if (this.$refs.formRef) {
      this.$refs.formRef.validate();
    }
  }
}
```

### 3. 动态ref（Vue 2.6+）
```html
<div v-for="item in list" :ref="`item_${item.id}`"></div>

<script>
methods: {
  getItemRef(id) {
    return this.$refs[`item_${id}`];
  }
}
</script>
```

## 五、高级用法

### 1. 组件通信
```javascript
// 父组件
this.$refs.childComp.doSomething();

// 子组件 SchoolInfo.vue
export default {
  methods: {
    doSomething() {
      // 子组件暴露的方法
    }
  }
}
```

### 2. 表单验证示例
```html
<template>
  <form ref="formRef">
    <input v-model="form.name" required>
    <button @click.prevent="validate">提交</button>
  </form>
</template>

<script>
export default {
  methods: {
    validate() {
      const form = this.$refs.formRef;
      if (form.checkValidity()) {
        // 表单验证通过
      }
    }
  }
}
</script>
```

### 3. 结合第三方库
```javascript
mounted() {
  // 使用ref初始化第三方库
  this.chart = new Chart(this.$refs.chartCanvas, {
    // 配置项
  });
},
beforeDestroy() {
  // 清理工作
  this.chart.destroy();
}
```

## 六、注意事项

1. **响应式限制**：
   - `$refs` 不是响应式的
   - 避免在模板或计算属性中使用

2. **执行时机**：
   - 在 `mounted` 之前访问会是 `undefined`
   - 更新后需要等待 `$nextTick` 获取最新引用

3. **过度使用问题**：
   - 优先考虑 props/events 通信
   - 避免形成紧密耦合的组件关系

4. **与 Vue3 的区别**：
   - Vue3 中 ref 需要从 `vue` 导入
   - 组合式 API 中使用 `ref()` 函数创建响应式引用

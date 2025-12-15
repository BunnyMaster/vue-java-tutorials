# Vue2 Props 属性传递机制详解

## 一、Props 的基本概念

Props 是 Vue 组件之间数据传递的主要方式，允许父组件向子组件传递数据。它具有明确的单向数据流特性（父组件 → 子组件）。

## 二、三种 Props 声明方式对比

### 1. 数组式声明（简单接收）
```javascript
props: ["name", "sex", "age"]
```
- **特点**：仅指定属性名
- **适用场景**：快速原型开发
- **限制**：无类型检查，无验证逻辑

### 2. 对象式声明（类型检查）
```javascript
props: {
  name: String,
  sex: String,
  age: Number
}
```
- **特点**：添加基础类型检查
- **优势**：在开发模式下会发出警告
- **不足**：无法设置必填/默认值

### 3. 配置对象声明（完整配置）
```javascript
props: {
  name: { 
    type: String, 
    required: true  // 必须传入
  },
  sex: { 
    type: String, 
    default: "男🚹"  // 默认值
  },
  age: { 
    type: Number, 
    required: true,
    validator: value => value >= 6 && value <= 60 // 自定义验证
  }
}
```
- **完整功能**：
  - 类型检查 (`type`)
  - 必填标记 (`required`)
  - 默认值 (`default`)
  - 自定义验证 (`validator`)

## 三、核心特性详解

### 1. 类型系统支持
Vue 支持以下原生构造函数作为类型：
- `String`
- `Number`
- `Boolean`
- `Array`
- `Object`
- `Date`
- `Function`
- `Symbol`

也可以使用自定义构造函数：
```javascript
props: {
  author: Person  // 检查是否是Person的实例
}
```

### 2. 默认值函数
当默认值是对象或数组时，必须使用工厂函数：
```javascript
props: {
  config: {
    type: Object,
    default: () => ({ pageSize: 10 })
  },
  list: {
    type: Array,
    default: () => []
  }
}
```

### 3. 自定义验证器
```javascript
age: {
  type: Number,
  validator: value => {
    return value >= 6 && value <= 60;
  }
}
```

## 四、最佳实践

### 1. 命名规范
- **Prop 名称**：使用 camelCase（JavaScript）
- **HTML 特性**：使用 kebab-case（DOM）
```html
<!-- 父组件模板 -->
<child-component user-name="张三"></child-component>

<!-- 子组件声明 -->
props: ['userName']
```

### 2. 单向数据流原则
- **禁止**子组件直接修改 prop
- **正确做法**：
  ```javascript
  // 子组件中使用data接收
  data() {
    return {
      localAge: this.age
    }
  }
  
  // 或使用计算属性
  computed: {
    normalizedAge() {
      return this.age + 1;
    }
  }
  ```

### 3. 类型定义技巧
```javascript
// 使用PropType增强类型提示
import { PropType } from 'vue';

props: {
  user: {
    type: Object as PropType<User>,
    required: true
  }
}
```

## 五、高级用法

### 1. 非Prop特性
未在props中声明的特性会自动绑定到组件的根元素上：
```html
<my-component data-test="123" class="extra"></my-component>
```

### 2. 同步修饰符
`.sync` 修饰符实现双向绑定（Vue 2.3+）：
```html
<!-- 父组件 -->
<child :title.sync="pageTitle"></child>

<!-- 等价于 -->
<child :title="pageTitle" @update:title="pageTitle = $event"></child>
```

### 3. v-model 整合
自定义组件实现 v-model：
```javascript
props: ['value'],
methods: {
  updateValue(newVal) {
    this.$emit('input', newVal);
  }
}
```

## 六、常见问题

1. **Prop未更新问题**：
   - 确保父组件数据是响应式的
   - 对象/数组需要确保是新的引用

2. **类型检查失败**：
   - 开发模式下会输出警告
   - 生产环境会静默失败

3. **性能优化**：
   - 避免传递大型对象
   - 必要时使用 `v-once` 冻结静态内容

## 七、与 Vue3 的区别

| 特性       | Vue2         | Vue3                    |
| ---------- | ------------ | ----------------------- |
| 默认值函数 | 必须使用函数 | 可以直接赋值            |
| 类型定义   | PropType     | 原生TypeScript支持      |
| 废弃语法   | .sync        | 移除，用v-model参数替代 |

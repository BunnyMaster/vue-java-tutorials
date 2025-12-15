# Vue2 数组变化监测原理与实战指南

## 一、核心原理

Vue2 通过重写数组的变异方法实现对数组变化的监测，这是其响应式系统的关键部分。当直接通过索引修改数组元素时（如 `arr[0] = newValue`），Vue 无法自动检测到这种变化。

## 二、代码示例解析

```html
<div id="app">
  <h1>列表更新演示</h1>
  <button @click="changeList">修改数组</button>
  <ul>
    <li v-for="(item,index) in list" :key="item.id">
      {{item.name}}---{{item.age}}---{{item.sex}}
    </li>
  </ul>
</div>

<script>
  const vm = new Vue({
    el: "#app",
    data: {
      list: [
        { id: "001", name: "Bunny-001", age: 19, sex: "男" },
        // ...其他初始数据
      ],
    },
    methods: {
      changeList() {
        // ❌ 无效的修改方式
        // this.list[0] = { id: "002", name: "Bunny-002", age: 16, sex: "男" };

        // ✅ 方式1：使用变异方法
        this.list.splice(0, 1, {
          id: "0021",
          name: "Bunny-0021",
          age: 16,
          sex: "男",
        });

        // ✅ 方式2：使用Vue.set/$set
        // Vue.$set(this.list, 0, {
        this.$set(this.list, 0, {
          id: "0021",
          name: "Bunny-0021",
          age: 16,
          sex: "男",
        });
      },
    },
  });
</script>
```

## 三、响应式数组操作方法

### 1. Vue 包装的变异方法

Vue 重写了以下数组方法，使其能触发视图更新：

- `push()` / `pop()`
- `shift()` / `unshift()`
- `splice()`
- `sort()` / `reverse()`

### 2. 特殊场景处理

| 场景     | 正确方法                        | 错误方法                |
| -------- | ------------------------------- | ----------------------- |
| 修改元素 | `Vue.set(arr, index, newValue)` | `arr[index] = newValue` |
| 添加元素 | `arr.splice(index, 0, newItem)` | `arr[index] = newItem`  |
| 删除元素 | `arr.splice(index, 1)`          | `delete arr[index]`     |

### 3. 注意事项

- 使用 `filter()`、`concat()` 等非变异方法时，需要用返回的新数组替换原数组
- 嵌套数组需要深度观测

## 四、原理深入

### 1. 实现机制

Vue 通过以下步骤实现数组响应式：

1. 拦截数组原型方法
2. 在方法执行后通知依赖更新
3. 对新增元素进行响应式处理

### 2. 源码关键部分

```javascript
// 简化版的数组响应式实现
const arrayProto = Array.prototype;
const arrayMethods = Object.create(arrayProto);

const methodsToPatch = [
  "push",
  "pop",
  "shift",
  "unshift",
  "splice",
  "sort",
  "reverse",
];

methodsToPatch.forEach(function (method) {
  const original = arrayProto[method];
  def(arrayMethods, method, function mutator(...args) {
    const result = original.apply(this, args);
    const ob = this.__ob__;
    let inserted;
    switch (method) {
      case "push":
      case "unshift":
        inserted = args;
        break;
      case "splice":
        inserted = args.slice(2);
        break;
    }
    if (inserted) ob.observeArray(inserted);
    ob.dep.notify(); // 通知更新
    return result;
  });
});
```

## 五、最佳实践

### 1. 性能优化

- 大数据量操作时使用 `Vue.nextTick` 批量更新
- 避免在模板中直接操作复杂数组计算

### 2. 代码规范

- 统一使用 `Vue.set` 或 `splice` 修改数组
- 为 `v-for` 设置合适的 `key`

### 3. 扩展方法

```javascript
// 安全的数组修改方法
function safeArrayUpdate(arr, index, newValue) {
  if (Array.isArray(arr)) {
    return Vue.set(arr, index, newValue);
  }
  throw new Error("Target is not an array");
}
```

## 六、常见问题解决方案

1. **为什么我的数组修改不生效？**

   - 检查是否使用了 Vue 能检测的变异方法
   - 确认没有直接修改数组长度（如 `arr.length = 0`）

2. **如何强制更新数组？**

   ```javascript
   // 方法1：使用空splice触发更新
   this.list.splice();

   // 方法2：使用Vue.set
   Vue.set(this.list, 0, this.list[0]);
   ```

3. **对象数组中的对象属性修改**
   - 直接修改对象属性可以触发更新（因为对象是响应式的）
   ```javascript
   // 这是有效的
   this.list[0].name = "new name";
   ```

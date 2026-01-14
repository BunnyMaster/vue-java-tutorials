# Vue 2 中样式动态绑定和使用文档

在 Vue 2 中，动态绑定样式是非常常见的需求，可以通过多种方式实现。以下是详细的文档说明：

## 一、对象语法

### 1. 基本对象语法

```html
<div :style="{ color: activeColor, fontSize: fontSize + 'px' }"></div>
```

```javascript
data() {
  return {
    activeColor: 'red',
    fontSize: 30
  }
}
```

### 2. 样式对象直接绑定

```html
<div :style="styleObject"></div>
```

```javascript
data() {
  return {
    styleObject: {
      color: 'red',
      fontSize: '13px'
    }
  }
}
```

## 二、数组语法

可以将多个样式对象应用到同一个元素上：

```html
<div :style="[baseStyles, overridingStyles]"></div>
```

```javascript
data() {
  return {
    baseStyles: {
      color: 'blue',
      fontSize: '14px'
    },
    overridingStyles: {
      color: 'red'
    }
  }
}
```

## 三、自动前缀

当使用需要浏览器前缀的 CSS 属性时（如 `transform`），Vue 会自动检测并添加适当的前缀。

```html
<div :style="{ transform: 'scale(' + scale + ')' }"></div>
```

## 四、多重值

可以为样式属性提供包含多个值的数组，常用于提供多个带前缀的值：

```html
<div :style="{ display: ['-webkit-box', '-ms-flexbox', 'flex'] }"></div>
```

## 五、绑定 class

### 1. 对象语法

```html
<div class="static" :class="{ active: isActive, 'text-danger': hasError }"></div>
```

```javascript
data() {
  return {
    isActive: true,
    hasError: false
  }
}
```

### 2. 数组语法

```html
<div :class="[activeClass, errorClass]"></div>
```

```javascript
data() {
  return {
    activeClass: 'active',
    errorClass: 'text-danger'
  }
}
```

### 3. 在组件上使用

当在自定义组件上使用 `class` 属性时，这些类将被添加到该组件的根元素上，且不会覆盖已有的类。

```html
<my-component class="baz boo" :class="{ active: isActive }"></my-component>
```

## 六、计算属性绑定

对于复杂的样式逻辑，推荐使用计算属性：

```html
<div :class="classObject"></div>
```

```javascript
computed: {
  classObject() {
    return {
      active: this.isActive && !this.error,
      'text-danger': this.error && this.error.type === 'fatal'
    }
  }
}
```

## 七、注意事项

1. **浏览器兼容性**：Vue 会自动添加浏览器前缀，但仍需注意某些 CSS 属性在不同浏览器中的支持情况。

2. **性能考虑**：频繁修改样式绑定可能会影响性能，特别是在大量元素上使用时。

3. **优先级**：`:style` 和 `:class` 绑定的样式会与普通样式合并，`:style` 的优先级高于内联样式。

4. **CSS 变量**：Vue 2.6+ 支持 CSS 变量绑定：

```html
<div :style="{'--color': themeColor}"></div>
```

## 八、最佳实践

1. 对于简单的样式切换，使用对象语法
2. 对于多个条件类，使用计算属性
3. 避免在模板中写复杂的样式逻辑
4. 考虑使用 CSS Modules 或 Scoped CSS 来管理组件样式

## 九、与 CSS 预处理器配合

Vue 支持与 Sass/SCSS、Less、Stylus 等预处理器一起使用：

```html
<style lang="scss">
  $primary-color: #42b983;
  .text {
    color: $primary-color;
  }
</style>
```

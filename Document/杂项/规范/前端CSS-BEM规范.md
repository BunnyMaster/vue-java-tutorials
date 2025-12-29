## 核心概念

*   **B** - **Block** （块）
*   **E** - **Element** （元素）
*   **M** - **Modifier** （修饰符）

### 具体用法

#### 1. Block （块）

*   **是什么**：一个功能或视觉上独立的、可复用的页面组件。
*   **特点**：块是独立的，它不依赖于页面上的其他元素。你可以把它放到任何地方，它都能正常工作。
*   **命名**：使用简洁的英文单词，多个单词用单个连字符 `-` 连接。
*   **示例**：`header`, `menu`, `search-form`, `user-profile`

```html
<!-- 一个独立的块 -->
<nav class="menu">...</nav>
<div class="search-form">...</div>
```

#### 2. Element （元素）

*   **是什么**：块的组成部分，不能脱离块而独立存在。
*   **特点**：元素在语义上属于其父块。你**不能**单独使用 `menu__item` 这个类，因为它脱离了 `menu` 块就没有意义。
*   **命名**：块名 + **双下划线 `__`** + 元素名。
*   **示例**：`menu__item`, `search-form__input`, `user-profile__avatar`

```html
<!-- 块内的元素 -->
<nav class="menu">
  <ul class="menu__list">
    <li class="menu__item">...</li>
    <li class="menu__item">...</li>
  </ul>
</nav>
```

#### 3. Modifier （修饰符）

*   **是什么**：代表块或元素的不同状态、版本或外观变化。
*   **特点**：修饰符**不能**单独使用，它必须附加在块或元素的类名上（通常会同时写基础类和修饰符类）。
*   **命名**：
    *   块或元素名 + **双连字符 `--`** + 修饰符名。
    *   修饰符名多个单词也用单连字符 `-` 连接。
*   **示例**：`menu--dark`, `button--disabled`, `menu__item--active`

```html
<!-- 块的修饰符 -->
<nav class="menu menu--dark">...</nav>

<!-- 元素的修饰符 -->
<li class="menu__item menu__item--active">...</li>
<button class="button button--large button--primary">提交</button>
```

## 三种符号的区分

这是 BEM 最容易混淆的地方，记住这个规则：

1.  **`__` （双下划线）**：
    *   **作用**：**连接块和元素**。
    *   **位置**：**只能出现在块名和元素名之间**。
    *   **解读**：`block__element`，表示“某个块下的某个元素”。
    *   **例子**：`.header__logo`, `.card__title`

2.  **`--` （双连字符）**：
    *   **作用**：**连接块/元素和修饰符**。
    *   **位置**：**只能出现在块名或元素名之后**。
    *   **解读**：`block--modifier` 或 `block__element--modifier`，表示“某个块/元素的某种状态或变体”。
    *   **例子**：`.button--primary`, `.menu__item--selected`

3.  **`-` （单连字符）**：
    *   **作用**：**连接一个名字内部的多个英文单词**（无论是块名、元素名还是修饰符名）。
    *   **位置**：在一个命名的内部。
    *   **解读**：仅仅是单词分隔符，没有结构上的意义。
    *   **例子**：
        *   块：`search-form` （搜索表单，两个单词）
        *   元素：`search-form__submit-button` （提交按钮，两个单词）
        *   修饰符：`search-form--full-width` （全宽度，两个单词）

## 示例

### 卡片用户

让我们用一个常见的卡片组件来串联所有规则：

```html
<!-- 块：card -->
<!-- 修饰符：card--highlighted -->
<article class="card card--highlighted">
  <!-- 元素：card__image -->
  <!-- 修饰符：card__image--rounded -->
  <img class="card__image card__image--rounded" src="..." alt="...">

  <!-- 元素：card__content -->
  <div class="card__content">
    <!-- 元素：card__title -->
    <h3 class="card__title">这是一个卡片标题</h3>
    <!-- 元素：card__description -->
    <p class="card__description">这里是卡片的描述文字...</p>
  </div>

  <!-- 元素：card__footer -->
  <div class="card__footer">
    <!-- 元素：card__button -->
    <!-- 修饰符：card__button--primary -->
    <button class="card__button card__button--primary">主要按钮</button>
    <button class="card__button">次要按钮</button>
  </div>
</article>
```

对应的 CSS：

```css
/* 块 */
.card { padding: 20px; border: 1px solid #ccc; }
/* 块的修饰符 */
.card--highlighted { border-color: gold; box-shadow: 0 2px 5px rgba(0,0,0,0.1); }

/* 元素 */
.card__image { width: 100%; }
.card__title { font-size: 1.5em; margin-bottom: 10px; }
.card__button { padding: 8px 16px; }

/* 元素的修饰符 */
.card__image--rounded { border-radius: 10px; }
.card__button--primary { background-color: blue; color: white; }
```

### 用户-卡片-表单

```html
<!-- user-info 模块 -->
<div class="user-info">
  <div class="user-info__header">...</div>
  
  <!-- 卡片区域 -->
  <div class="user-info__section">
    <h3 class="user-info__section-title">个人信息</h3>
    
    <!-- 独立的表单块 -->
    <form class="form form--inline">
      <div class="form__item">
        <label class="form__label">姓名</label>
        <input class="form__input" type="text">
      </div>
      <div class="form__item">
        <label class="form__label">邮箱</label>
        <input class="form__input form__input--error" type="email">
      </div>
      <button class="form__submit button button--primary">保存</button>
    </form>
  </div>
  
  <div class="user-info__footer">...</div>
</div>
```

**方案A：最推荐的方式 - 每个功能块都是独立的Block**

```html
<div class="user-info"> <!-- Block -->
  <div class="user-info__card"> <!-- user-info的元素 -->
    <form class="form"> <!-- 新的独立Block -->
      <div class="form__item">...</div> <!-- form的元素 -->
    </form>
  </div>
</div>
```

**说明**：

- `user-info` 是一个Block
- `user-info__card` 是 `user-info` 的一个元素（表示这个卡片是用户信息模块的一部分）
- `form` 是一个**新的独立Block**，它可以放在任何地方
- `form__item` 是 `form` 的元素

**方案B：如果表单确实是用户卡片特有的，可以这样**

```html
<div class="user-info">
  <div class="user-info__card">
    <form class="user-info__form"> <!-- 作为user-info的直接元素 -->
      <div class="user-info__form-item">...</div> <!-- 或者 user-info__form-item -->
    </form>
  </div>
</div>
```

**说明**：

- `user-info__form` 直接作为 `user-info` 的元素
- `user-info__form-item` 作为 `user-info__form` 的子元素（注意这里用了单连字符连接 `form-item`）

**方案C：使用混合（Mix）模式（高级但很强大）**

```html
<div class="user-info">
  <div class="user-info__card card"> <!-- 混合：既是元素又是独立块 -->
    <form class="card__form form"> <!-- 混合 -->
      <div class="form__item">...</div>
    </form>
  </div>
</div>
```

**说明**：

- `user-info__card card`：这个元素同时拥有两个类，它既是 `user-info` 的元素，也是一个独立的 `card` 块
- 这样你可以：
  - 用 `.user-info__card` 写用户信息模块特有的卡片样式
  - 用 `.card` 写通用的卡片样式
- 这是BEM中非常实用的模式，兼顾了上下文样式和可复用性

## 总结与最佳实践

*   **何时用 `__`**：当你需要定义**某个块内部的子部分**时。
*   **何时用 `--`**：当你需要定义**某个块或元素的特定状态、样式变体**时。
*   **何时用 `-`**：当你的类名**由多个英文单词组成**时，用它来连接这些单词。

**BEM 的核心优势**：
1.  **清晰**：只看类名就能知道 HTML 结构。
2.  **唯一性**：低概率的命名冲突，因为类名自带“命名空间”。
3.  **可维护性**：CSS 结构扁平，没有深度嵌套，便于查找和修改。
4.  **可复用性**：块是独立的，可以轻松移动到项目其他地方甚至其他项目。

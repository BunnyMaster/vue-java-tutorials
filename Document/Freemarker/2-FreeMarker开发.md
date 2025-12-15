## 方法

### 定义方法

#### 指令预览

```xml
<#assign x = "something">
${indexOf("met", x)}
${indexOf("foo", x)}
```

#### 生成结果

```
2
-1
```

#### 代码示例

**配置**

```java
@Configuration
public class FreemarkerConfig {

    @Bean
    public freemarker.template.Configuration customerFreemarkerConfig() {
        freemarker.template.Configuration cfg = new freemarker.template.Configuration(freemarker.template.Configuration.VERSION_2_3_34);
        // cfg.setClassForTemplateLoading(FramerWorkTest.class, "/freemarker");
        cfg.setClassForTemplateLoading(this.getClass(), "/freemarker");
        // cfg.setClassLoaderForTemplateLoading(getClass().getClassLoader(), "freemarker");

        cfg.setDefaultEncoding("UTF-8");
        cfg.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        cfg.setLogTemplateExceptions(true);
        cfg.setWrapUncheckedExceptions(true);
        // 当读取空循环变量时不回退到更高范围：
        cfg.setFallbackOnNullLoopVariable(false);
        // 适应 JDBC 返回值的方式；参见 Javadoc！
        cfg.setSQLDateAndTimeTimeZone(TimeZone.getDefault());

        Map<String, Object> sharedVariables = setSharedVariables();

        try {
            cfg.setSharedVariables(sharedVariables);
        } catch (TemplateModelException e) {
            throw new RuntimeException("Failed to set FreeMarker shared variables", e);
        }

        return cfg;
    }

    /**
     * 设置共享变量
     *
     * @return 共享变量
     */
    private Map<String, Object> setSharedVariables() {
        // 设置共享变量
        Map<String, Object> sharedVariables = new HashMap<>();
        sharedVariables.put("indexOf", new IndexOfMethod());

        return sharedVariables;
    }

}
```

**IndexOfMethod**

```java
/**
 * FreeMarker 方法
 * 使用方式：${indexOf("met", x)}
 */
public class IndexOfMethod implements TemplateMethodModelEx {

    @Override
    public Object exec(List arguments) throws TemplateModelException {
        // 检查参数数量
        if (arguments.size() != 2) {
            throw new TemplateModelException("indexOf method requires exactly 2 arguments: (substring, string)");
        }

        try {
            // 正确获取字符串参数
            String substring = getString(arguments.get(0));
            String mainString = getString(arguments.get(1));

            // 执行 indexOf 操作
            return mainString.indexOf(substring);

        } catch (ClassCastException e) {
            throw new TemplateModelException("Invalid argument types for indexOf method. Expected two strings.");
        }
    }

    /**
     * 安全地从 TemplateModel 中获取字符串值
     */
    private String getString(Object templateModel) throws TemplateModelException {
        return switch (templateModel) {
            case SimpleScalar simpleScalar -> simpleScalar.getAsString();
            case TemplateModel ignored ->
                    throw new TemplateModelException("Unsupported parameter type: " + templateModel.getClass().getName());
            case String s -> s;
            default ->
                    throw new TemplateModelException("Expected string parameter, got: " + templateModel.getClass().getName());
        };
    }
}
```

## 共享变量

> [!WARNING]
>
> 如果配置对象在多线程环境中使用，不要使用 `TemplateModel` 实现类来作为共享变量，因为它不是[线程安全](http://freemarker.foofun.cn/gloss.html#gloss.threadSafe)的！这也是基于 Servlet 应用程序的典型情形。

### 内置共享变量

| 名称                 | 类                                              | 描述         |
| :------------------- | :---------------------------------------------- | :----------- |
| `capture_output`     | `freemarker.template.utility.CaptureOutput`     | 捕获输出内容 |
| `compress`           | `freemarker.template.utility.StandardCompress`  | 压缩空白字符 |
| `html_escape`        | `freemarker.template.utility.HtmlEscape`        | HTML 转义    |
| `normalize_newlines` | `freemarker.template.utility.NormalizeNewlines` | 标准化换行符 |
| `xml_escape`         | `freemarker.template.utility.XmlEscape`         | XML 转义     |

### 自定义共享变量

使用 `setSharedVariable` 可以共享当前变量：

```java
Configuration cfg = new Configuration(Configuration.VERSION_2_3_22);
...
cfg.setSharedVariable("warp", new WarpDirective());
cfg.setSharedVariable("company", "Foo Inc.");

// 还可以设置多个共享变量
cfg.setSharedVariables(Map.of());
```

## 配置说明

> [!NOTE]
>
> 配置的优先级：Environment > Template > Configuration

### Configuration

这是最基础的配置，是可以被覆盖的：

```java
freemarker.template.Configuration configuration = new freemarker.template.Configuration(freemarker.template.Configuration.VERSION_2_3_34);
// cfg.setClassForTemplateLoading(FramerWorkTest.class, "/freemarker");
configuration.setClassForTemplateLoading(this.getClass(), "/freemarker");
// cfg.setDirectoryForTemplateLoading(new File("/xxx.ftl"));
// cfg.setClassLoaderForTemplateLoading(getClass().getClassLoader(), "freemarker");

configuration.setDefaultEncoding("UTF-8");
configuration.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
configuration.setTemplateExceptionHandler(freemarker.template.TemplateExceptionHandler.RETHROW_HANDLER);
configuration.setLogTemplateExceptions(true);
configuration.setWrapUncheckedExceptions(true);
configuration.setFallbackOnNullLoopVariable(false);
configuration.setSQLDateAndTimeTimeZone(TimeZone.getDefault());
configuration.setTemplateExceptionHandler(new FreemarkerTemplateExceptionHandler());
```

### Template

在 `Configuration` 或 `Template` 实例中的值也可以在单独调用 `Template.process` 方法后被覆盖。 

```java
Map<String, BigDecimal> map = Map.of("x", BigDecimal.valueOf(666));

try {
    Template template = configuration.getTemplate("test/math-operation-demo.ftl");
    Configuration configuration1 = template.getConfiguration();
    configuration1.setDefaultEncoding("UTF-8");
    configuration1.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
    Writer out = new OutputStreamWriter(System.out);
    template.process(map, out);
} catch (Exception e) {
    throw new RuntimeException(e);
}
```

### Environment

```java
Environment env = myTemplate.createProcessingEnvironment(root, out);
env.setLocale(java.util.Locale.ITALY);
env.setNumberFormat("0.####");
env.process(); 
```

### SpringBoot配置

## 模板加载器

### 模板加载方式

有三种主要的模板加载方式：

```java
void setDirectoryForTemplateLoading(File dir);
void setClassForTemplateLoading(Class cl, String prefix);
void setServletContextForTemplateLoading(Object servletContext, String path);
```

### 使用示例

```java
freemarker.template.Configuration cfg = new freemarker.template.Configuration(freemarker.template.Configuration.VERSION_2_3_34);

// 使用类路径加载
cfg.setClassForTemplateLoading(FramerWorkTest.class, "/freemarker");
cfg.setClassForTemplateLoading(this.getClass(), "/freemarker");
cfg.setClassLoaderForTemplateLoading(getClass().getClassLoader(), "freemarker");

// 使用文件系统加载
cfg.setDirectoryForTemplateLoading(new File("/path/to/templates"));
```

### 从其他资源加载模板

如果内置的类加载器都不适合使用，那么就需要编写自己的类加载器了。这个类需要实现 `freemarker.cache.TemplateLoader` 接口，然后将它传递给 `Configuration` 对象的 `setTemplateLoader(TemplateLoader loader)` 方法。可以阅读 API JavaDoc 文档获取更多信息。

如果模板需要通过 URL 访问其他模板，那么就不需要实现 `TemplateLoader` 接口了，可以选择子接口 `freemarker.cache.URLTemplateLoader` 来替代，只需实现 `URL getURL(String templateName)` 方法即可。

## 异常处理 TemplateException

### 自定义异常处理器

```java
/**
 * FreeMarker 模板异常处理器
 */
public class FreemarkerTemplateExceptionHandler implements freemarker.template.TemplateExceptionHandler {
    
    @Override
    public void handleTemplateException(TemplateException templateException, 
                                      freemarker.core.Environment environment, 
                                      Writer writer) throws TemplateException {
        try {
            writer.write("[ERROR: " + templateException.getMessage() + "]");
        } catch (IOException e) {
            throw new TemplateException("Failed to print error message. Cause: " + e, environment);
        }
    }
}
```

### 配置异常处理器

```java
Configuration cfg = new Configuration(Configuration.VERSION_2_3_34);
cfg.setTemplateExceptionHandler(new FreemarkerTemplateExceptionHandler());

// 或者使用内置的异常处理器
cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);      // 重新抛出异常
cfg.setTemplateExceptionHandler(TemplateExceptionHandler.DEBUG_HANDLER);        // 调试模式
cfg.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);   // HTML 调试模式
cfg.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);       // 忽略异常
```

### 异常处理配置选项

```java
// 设置是否记录模板异常
cfg.setLogTemplateExceptions(false);

// 设置是否包装未检查的异常
cfg.setWrapUncheckedExceptions(true);

// 设置当读取空循环变量时是否回退到更高范围
cfg.setFallbackOnNullLoopVariable(false);
```

## 多线程

在多线程运行环境中，`Configuration` 实例、`Template` 实例和数据模型应该是永远不能改变（只读）的对象。也就是说，创建和初始化它们（如使用 `set*...*` 方法）之后，就不能再修改它们了（比如不能再次调用 `set*...*` 方法）。这就允许我们在多线程环境中避免代价很大的同步锁问题。要小心 `Template` 实例；当使用了 `Configuration.getTemplate` 方法获得 `Template` 一个实例时，也许得到的是从模板缓存中缓存的实例，这些实例都已经被其他线程使用了，所以不要调用它们的 `set*...*` 方法。

## 高级指令

### macro、nested

#### macro

```xml
<#macro name param1 param2 ... paramN>
  ...
  <#nested loopvar1, loopvar2, ..., loopvarN>
  ...
  <#return>
  ...
</#macro>
```

- `name` 表示宏名称
- `param` 表示参数

**使用方式**

```xml
<#macro javaComment comment date author extra="">
    /**
    * ${comment}
    *
    * @author ${author}
    * @date ${date}
    * <#if extra?has_content>@description ${extra}</#if>
    */
</#macro>
```

如果这个文件在另一个文件夹中，需要引入文件：

```xml
<#-- 引入包含 javaComment 宏的模板 -->
<#include "common/common-comment.ftl">
```

**设定默认参数**

```html
<#macro button text type="primary" size="medium" disabled=false>
<button class="btn btn-${type} btn-${size}" <#if disabled>disabled</#if>>
    ${text}
</button>
</#macro>

<#-- 使用 -->
<@button text="确定" />
<@button text="取消" type="secondary" />
<@button text="禁用" disabled=true />
```

#### nested

相当于 Vue 的插槽：

```html
<#macro bordered_box title>
<div class="bordered-box">
    <h2>${title}</h2>
    <div class="content">
        <#nested>
    </div>
</div>
</#macro>
    
<#-- 使用 -->
<@bordered_box title="用户信息">
    <p>姓名：张三</p>
    <p>年龄：25</p>
</@bordered_box>
```

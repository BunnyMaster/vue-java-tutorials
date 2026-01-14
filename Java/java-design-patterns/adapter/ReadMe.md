## 适配器模式的关键组件

### 1. 目标接口 (`Target`)

- `TargetClient` 定义客户端期望的接口
- 包含`response()`方法，返回统一的 `AdapterInfoVO列表`

### 2. 源对象 (`Adaptee`)

- `CompanyClient`、`CreditClient`、`TaxClient` - 各自的数据源
- 实现了 `AdapterClient<T>` 接口，提供 getData()方法

### 3. 适配器 (`Adapter`)

- `CompanyTarget`、`CreditTarget`、`TaxTarget` - 实际的适配器类
- 持有源对象引用，将源对象的数据转换为目标接口所需格式

## 适配器模式实现流程

1. **客户端** - App类通过 TargetClient接口操作
2. **适配器** - 各个 `*Target` 类将不同的数据源`CompanyClient`/`CreditClient`/`TaxClient`适配到统一的 AdapterInfoVO格式
3. **数据转换** - 在适配过程中，将原始数据模型`CompanyEntity`/`CreditEntity`/`TaxEntity`转换为统一的目标格式

## 适配器模式的优点

- **解耦合** - 客户端无需了解各种数据源的具体实现
- **统一接口** - 不同的数据源通过适配器提供统一的访问方式
- **可扩展性** - 可以轻松添加新的数据源，只需实现相应的适配器

这种设计完美体现了适配器模式的核心思想：将一个类的接口转换为客户希望的另一个接口，使得原本由于接口不兼容而不能一起工作的类可以一起工作。
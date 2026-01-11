# 抽象工厂模式 (Abstract Factory Pattern)

## 概述

抽象工厂模式是一种创建型设计模式，它能创建一系列相关或相互依赖的对象，而无需指定它们具体的类。本项目演示了如何使用抽象工厂模式创建不同风格的家具产品系列。

## 设计模式结构

### 主要组件

- AbstractFactory: 抽象工厂类，定义了创建产品族的接口
- Chair 椅子产品接口
- Sofa: 沙发产品接口
- TeaTable 茶几产品接口

### 具体实现

#### 工厂实现

- ArtDecoFactory: 艺术装饰风格家具工厂
- ModernFactory: 现代风格家具工厂
- VictorianFactory: 维多利亚风格家具工厂

#### 产品实现

**椅子系列**:

- ArtDecoChair
- ModernChair
- VictorianChair

**沙发系列**:

- ArtDecoSofa
- ModernSofa
- VictorianSofa

**茶几系列**:

- ArtDecoTeaTable
- VictorianTeaTable

## 特性

- **产品族管理**: 同一工厂创建的椅子、沙发、茶几属于同一风格
- **枚举工厂**:
  通过 FactoryEnum
  提供工厂获取功能
- **统一接口**: 所有产品都有相同的方法接口（描述、价格、重量、颜色、材质）
- **易于扩展**: 可以轻松添加新风格的家具而不影响现有代码

## 使用方式

```java
// 方式1: 直接创建工厂
AbstractFactory factory = new ArtDecoFactory();
String products = factory.createProducts();

// 方式2: 通过枚举创建
AbstractFactory factory = FactoryEnum.getFactory(FactoryEnum.Modern);
String products = factory.createProducts();
```

## 适用场景

- 需要创建对象家族，且这些对象之间有相互依赖关系
- 需要在系统中切换不同风格的产品系列
- 确保创建的对象属于同一个产品族
- 客户端不需要知道创建对象的具体类，只需要其接口

## 优势

- **隔离具体类**: 客户端无需与具体类交互，只需使用抽象接口
- **保证产品一致性**: 同一族产品保持风格一致
- **易于切换产品族**: 通过更换工厂即可切换整个产品系列
- **符合开闭原则**: 添加新产品族无需修改现有代码
# demo_springboot_function

一个Spring Boot动态编译演示项目，展示如何在运行时动态编译Java代码并注册为Spring Bean。

## 项目简介

这个项目演示了如何使用liquor库在Spring Boot应用中实现动态编译功能。主要功能包括：

- 动态编译Java源码
- 动态注册Spring Bean
- 自动刷新Controller的RequestMapping

## 技术栈

- Spring Boot 2.7.18
- Java 8
- liquor 1.5.7（动态编译库）
- Lombok
- JUnit 5

## 快速开始

## 项目结构

```
src/main/java/demo/
├── DemoApp.java                 # Spring Boot启动类
├── DemoController.java          # 静态Controller示例
├── DemoService.java             # 业务服务类
├── DynamicBeanManager.java      # 动态Bean管理器（核心组件）
└── ControllerRegister.java      # 动态Controller注册器

src/main/resources/
├── application.yml              # 应用配置
└── codefile/
    └── HelloLiquor.txt          # 动态编译的Java源码示例
```

## 核心功能

### 动态Bean管理

`DynamicBeanManager`是核心组件，提供以下功能：

- **注册动态Bean**：`registerDynamicController(beanName, className, sourceCode)`
- **注销动态Bean**：`unregisterDynamicController(beanName)`
- **自动映射刷新**：动态Controller的RequestMapping会自动注册到Spring MVC

### 依赖注入支持

动态编译的类支持：
- 构造函数注入
- @Value属性注入
- @Autowired字段注入

### 示例代码

项目启动时会自动注册`HelloLiquor`动态Controller，源码位于`resources/codefile/HelloLiquor.txt`。

## 接口说明

### 静态Controller接口

| 路径 | 方法 | 说明 |
|------|------|------|
| `/` | GET | 返回Hello World |
| `/hello` | GET | 返回Hello World |

### 动态Controller接口

| 路径 | 方法 | 说明 |
|------|------|------|
| `/liquor` | GET | 返回Hello liquor |
| `/liquor/hello` | GET | 返回Hello liquor |
| `/liquor/getDate` | GET | 返回当前时间 |
| `/liquor/config` | GET | 返回服务配置信息 |

## 开发说明

### 动态编译流程

1. 准备Java源码字符串
2. 调用`DynamicBeanManager.registerDynamicController()`
3. 系统自动完成编译、注册Bean、刷新映射

### 注意事项

- 支持Spring的常用注解
- 动态Bean可以注入其他Spring Bean
- 重复注册会先注销旧的Bean再注册新的

## 测试

项目包含完整的集成测试，覆盖：
- 静态Controller测试
- 动态Controller测试
- Bean管理功能测试
- 重新注册功能测试

测试类：`src/test/java/demo/DemoAppTest.java`
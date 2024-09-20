### 1.2.9

* 调整 用 hash 方案替代 md5 方案。改进复用性能
* 添加 IEvaluator::compile() -> IExecutable 预编译支持
* 优化 反射执行性能，避免 Method 拷贝

### 1.2.7

* 修复 ScriptEvaluator import 前面有空隔时，会出错的问题
* 添加 CodeSpec::imports

### 1.2.6

* 添加 ScriptEvaluator import 导入语法支持（自动分离，加到新类的头部）
* 调整 main 函数名，避免冲突

### 1.2.4

* 优化 源顺编译顺序支持（按添加顺序）
* 优化 DynamicClassLoader 多类无序加载会出错的问题

### 1.2.0

* 添加 liquor-eval 模块

### 1.1.1

* 优化 预加载处理（类定义）

### 1.1.0

* 优化 接口体验
* 修复 多次构建必须先清空的问题（否则会异常）

### 1.0.3

* 同步 arthas 4.0.1 的代码
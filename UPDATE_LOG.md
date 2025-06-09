### 1.5.4

* 添加 DynamicCompiler:addOption 方法
* 修复 java -jar 下 lombok 动态编译失败的问题

### 1.5.3

* 添加 LiquorEvaluator:eval 内部自动参数绑定

### 1.5.2
* 增加 liquor-eval 对 jsr223 规范适配

### 1.4.0
* 优化 liquor-eval 编译类设计（执行时不再需要反射），提升性能（2到5倍） 

### 1.3.16

* 优化 liquor-eval 异常设计

### 1.3.15

* 优化 liquor-evel 改为 LRUCache 缓存 

### 1.3.14

* 优化 编译错误提示，包名改为包路径模式

### 1.3.13

* 添加 DynamicCompiler:compile 方法
* 添加 DynamicClassLoader:getClassBytes 方法
* 优化 编译错误提示，带上包名（可全类名露出）

### 1.3.12

* 优化 编译提醒添加类文件信息

### 1.3.11

* 修复 脚本里有新创的 Lambda 时，入口主方法可能会识别失败的问题

### 1.3.10

* 添加 Scripts:eval(String, Map) 方法
* 优化 LiquorEvaluator 编译的脚本允许显示抛出异常

### 1.3.9

* 添加 DynamicCompiler:addClassPath 方法
* 添加 DynamicCompiler:getStandardFileManager 方法

### 1.3.8

* 添加 评估器批量预编译支持（批量时，可大大提高编译速度。适合启动初始化）

### 1.3.7

* 修复 jar in jar 为空包时（没有类），会异常的问题

### 1.3.6

* 修复 多线程下评估编译锁的问题

### 1.3.5

* 优化 评估器的非缓存类加载器限次改为 10000

### 1.3.4

* 增加 非缓存模式
* 增加 估评器导入去重处理
* 修复 SharedNameTable 内存大涨的问题
* 优化 父类加载器传递细节，辅助非缓存模式

### 1.3.2

* 开放 Evaluator::printable 方法
* 修复 Exprs:eval(code, context) 在多参时会出错的问题
* 禁止 ParamSpec 修改值。避免后续影响 hashCode

### 1.3.1

* 合并 ScriptEvaluator,ExpressionEvaluator,AbstractEvaluator 为一个类：LiquorEvaluator
* 调整 parentClassLoader 的配置方式
* 增加 CodeSpec 字符串形式导入
* 增加 LiquorEvaluator 全局导入支持

### 1.3.0

* 新增 Exprs，Scripts 快捷工具类
* 优化 DynamicClassLoader 内存（删除副本，减少拷贝）
* 优化 AbstractEvaluator 线程安全
* 优化 ParamSpec 与 CodeSpec hashCode 算法
* 简化 CodeSpec 参数配置
* 简化 ExpressionEvaluator 与 ScriptEvaluator，合用公共代码

### 1.2.9

* 调整 用 hash 方案替代 md5 方案。改进复用性能
* 添加 IEvaluator::compile() -> IExecutable 预编译支持
* 添加 评估器快捷单例
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

<h1 align="center" style="text-align:center;">
Liquor
</h1>
<p align="center">
	<strong>Java 动态编译、表达式、脚本工具（兼容 jar in jar）</strong>
</p>
<p align="center">
    <a target="_blank" href="https://central.sonatype.com/search?q=org.noear%liquor">
        <img src="https://img.shields.io/maven-central/v/org.noear/liquor.svg?label=Maven%20Central" alt="Maven" />
    </a>
    <a target="_blank" href="LICENSE">
		<img src="https://img.shields.io/:License-Apache2-blue.svg" alt="Apache 2" />
	</a>
    <a target="_blank" href="https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html">
		<img src="https://img.shields.io/badge/JDK-8-green.svg" alt="jdk-8" />
	</a>
    <a target="_blank" href="https://www.oracle.com/java/technologies/javase/jdk11-archive-downloads.html">
		<img src="https://img.shields.io/badge/JDK-11-green.svg" alt="jdk-11" />
	</a>
    <a target="_blank" href="https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html">
		<img src="https://img.shields.io/badge/JDK-17-green.svg" alt="jdk-17" />
	</a>
    <a target="_blank" href="https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html">
		<img src="https://img.shields.io/badge/JDK-21-green.svg" alt="jdk-21" />
	</a>
    <a target="_blank" href="https://www.oracle.com/java/technologies/javase/jdk22-archive-downloads.html">
		<img src="https://img.shields.io/badge/JDK-22-green.svg" alt="jdk-22" />
	</a>
    <br />
    <a target="_blank" href='https://gitee.com/noear/liquor/stargazers'>
		<img src='https://gitee.com/noear/liquor/badge/star.svg' alt='gitee star'/>
	</a>
    <a target="_blank" href='https://github.com/noear/liquor/stargazers'>
		<img src="https://img.shields.io/github/stars/noear/liquor.svg?style=flat&logo=github" alt="github star"/>
	</a>
</p>

<br/>
<p align="center">
	<a href="https://jq.qq.com/?_wv=1027&k=kjB5JNiC">
	<img src="https://img.shields.io/badge/QQ交流群-22200020-orange"/></a>
</p>

<hr>


本工具的编译器代码主要源自 arthas。相关的知识点比较冷门，甚是珍贵。为了复用方便，特整理成一个小工具包进行长期维护。后来又增加了表达式、脚本支持。



| 工件             | 大小   | 功能                                   | 功能描述                             |
|----------------|------|--------------------------------------|----------------------------------|
| liquor         | 24KB | DynamicCompiler                      | 编译一个或多个类（可相互依赖，可多次不重复编译）         |
| liquor-eval    | 14KB | ExpressionEvaluator<br/>ScriptEvaluator | 评估一行表达式（可多次评估）<br/>评估一块脚本（可多次评估） |


引用依赖：

```xml
<dependency>
    <groupId>org.noear</groupId>
    <artifactId>liquor-eval</artifactId> <!-- or liquor -->
    <version>1.2.9</version>
</dependency>
```

示例参考：

* [liquor_demo_solon](liquor_demo_solon)
* [liquor_demo_springboot](liquor_demo_springboot)


性能表现（第三方性能测试，榜首）：

* [https://gitee.com/xiandafu/beetl/tree/master/express-benchmark](https://gitee.com/xiandafu/beetl/tree/master/express-benchmark)

## 编译器演示

可以有包名；可以导入类；跟平常写 java 类一样。

```java
public class DemoApp {
    public static void main(String[] args) throws Exception{
        //可以复用（不要，不断的新建）
        DynamicCompiler compiler = new DynamicCompiler();
        
        String className = "HelloWorld";
        String classCode = "import java.util.HashMap;\n\n"+
                "public class HelloWorld { " +
                "   public static void helloWorld() { " +
                "       System.out.println(\"Hello, world!\"); " +
                "   } " +
                "}";
        
        //添加源码（可多个）并 构建
        compiler.addSource(className, classCode).build();

        Class<?> clazz = compiler.getClassLoader().loadClass(className);
        clazz.getMethod("helloWorld").invoke(null);
    }
}
```

## 评估器演示


### 1) 表达式评估器（只能写一行代码）//内部会把表达式编译为一个静态函数

* 必须有结果返回
* 表达式中没有 return 时；评估器会自动添加 "return" 和 ";"
* 有 return 时；"return" 的左右必须要有空隔，且要添加 ";" 结尾
* 使用 CodeSpec::imports 导入表达式需要的类

```java
public class DemoApp {
    public static void main(String[] args) throws Exception {
        //可以复用（不要，不断的新建）
        ExpressionEvaluator evaluator = ExpressionEvaluator.getInstance();
        
        //基础
        System.out.println(evaluator.eval("1+1"));

        //进阶
        CodeSpec code1 = new CodeSpec("$0 + 22").parameters(Integer.class);
        System.out.println(evaluator.eval(code1, 1)); //=> 23

        CodeSpec code2 = new CodeSpec("aa + 22").parameters(new String[]{"aa"}, new Class[]{Integer.class});
        System.out.println(evaluator.eval(code2, 2)); //=> 24

        Map<String, Object> bindings3 = new HashMap<>();
        bindings3.put("bb", 3);
        System.out.println(evaluator.eval("bb + 22", bindings3)); //=>25

        System.out.println(evaluator.eval(new CodeSpec("Math.min(1,2)").imports(Math.class))); //=>1
    }
}
```

### 2) 脚本评估器 //内部会把脚本编译为一个静态函数

* 可以导入类；不能有包名；
* 使用内部类时不要加 "public" 修饰
* 使用 CodeSpec::imports 导入表达式需要的类；或者在代码里添加 "import" 语句

```java
public class DemoApp {
    public static void main(String[] args) throws Exception {
        //可以复用（不要，不断的新建）
        ScriptEvaluator evaluator = ScriptEvaluator.getInstance();
        
        //基础
        evaluator.eval("System.out.println(\"hello word\");");

        //进阶（如果有内部类，不要加 public）
        CodeSpec code1 = new CodeSpec("import java.util.HashMap;\n\n"+
                "    class Demo {\n" +
                "      public String hello(String word) {\n" +
                "        return word;\n" +
                "      }\n" +
                "    }\n" +
                "\n" +
                "    Demo demo = new Demo();\n" +
                "    return demo.hello(name);") //name 为外部参数
                .parameters(new String[]{"name"}, new Class[]{String.class})
                .returnType(String.class);
        System.out.println(evaluator.eval(code1, "noear")); //=>noear
    }
}
```

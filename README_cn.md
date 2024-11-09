
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
    <a target="_blank" href="https://www.oracle.com/java/technologies/javase/jdk23-archive-downloads.html">
		<img src="https://img.shields.io/badge/JDK-23-green.svg" alt="jdk-23" />
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


本工具的编译器代码主要源自 arthas。相关的知识点比较冷门，甚是珍贵。为了复用方便，特整理成一个小工具包进行长期维护。后来又增加了评估器，用于运行表达式和脚本。



| 工件             | 大小   | 功能                              | 功能描述     |
|----------------|------|---------------------------------|----------|
| liquor         | 24KB | DynamicCompiler                 | 编译类      |
| liquor-eval    | 14KB | LiquorEvaluator (Exprs,Scripts) | 运行表达式和脚本 |


引用依赖：

```xml
<dependency>
    <groupId>org.noear</groupId>
    <artifactId>liquor-eval</artifactId> <!-- or liquor -->
    <version>1.3.7</version>
</dependency>
```

性能表现（第三方性能测试，榜首）：

* [https://gitee.com/xiandafu/beetl/tree/master/express-benchmark](https://gitee.com/xiandafu/beetl/tree/master/express-benchmark)

## 编译器演示

可以有包名；可以导入类；可相互依赖；可多次不重复编译；跟平常写 java 类一样。

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

Liquor 评估器工具，是基于 Java 编译实现的。在“缓存覆盖”下，性能接近原始 Java 代码。但是，当有“无限多变化”的表达式时，缓存会失效，且会产生无限多的类，然后 OOM。

以表达式评估器为例：

* 使用“变量”替代常量，以减少编译 Exprs.eval(new CodeSpec("a+b+c"), context)。
  * 【推荐】效果，就像类与实例的关系
* 使用非缓存模式 Exprs.eval(new CodeSpec("1+2+3").cached(false))
  * 【不推荐】

### 1) 表达式评估器（只能写一行代码）

* 必须有结果返回
* 表达式中没有 ";" 时，会自动添加 "return" 和 ";"。否则要自己确保语句完整
* 使用 CodeSpec::imports 导入表达式需要的类

```java
public class DemoApp {
    public static void main(String[] args) throws Exception {
        //基础
        System.out.println(Exprs.eval("1+1"));

        //进阶
        CodeSpec code1 = new CodeSpec("aa + 22").parameters(new ParamSpec("aa", Integer.class));
        System.out.println(Exprs.eval(code1, 1)); //=> 23

        Map<String, Object> context2 = new HashMap<>();
        context2.put("bb", 3);
        System.out.println(Exprs.eval("bb + 22", context2)); //=>25

        System.out.println(Exprs.eval(new CodeSpec("Math.min(1,2)").imports(Math.class))); //=>1
    }
}
```

### 2) 脚本评估器

* 可以导入类或静态方法；不能有包名
* 使用内部类时不要加修饰（"public"、"static"）；内部类的字段和方法不支持 "static"
* 使用 CodeSpec::imports 导入表达式需要的类或静态方法；或者在代码里添加 "import" 语句

```java
public class DemoApp {
    public static void main(String[] args) throws Exception {
        //基础
        Scripts.eval("System.out.println(\"hello word\");");

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
                .parameters(new ParamSpec("name", String.class))
                .returnType(String.class);
        System.out.println(Scripts.eval(code1, "noear")); //=>noear
    }
}
```

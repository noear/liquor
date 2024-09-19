
<h1 align="center" style="text-align:center;">
Liquor for java
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


本工具的编译器代码主要源自 arthas。相关的知识点比较冷门，甚是珍贵。为了复用方便，特整理成一个小工具包进行长期维护。



| 工件（零依赖）     | 描述   | 大小     |
|-------------|------|--------|
| liquor      | 编译器  | 24KB   |
| liquor-eval | 评估器  | 10KB   |



## 编译器演示

```xml
<dependency>
    <groupId>org.noear</groupId>
    <artifactId>liquor</artifactId>
    <version>1.2.0</version>
</dependency>
```

```java
public class DemoApp {
    public static void main(String[] args) throws Exception{
        String className = "HelloWorld";
        String classCode = "public class HelloWorld { " +
                "   public static void helloWorld() { " +
                "       System.out.println(\"Hello, world!\"); " +
                "   } " +
                "}";

        DynamicCompiler compiler = new DynamicCompiler();
        //添加源码（可多个）并 构建
        compiler.addSource(className, classCode).build();

        Class<?> clazz = compiler.getClassLoader().loadClass(className);
        clazz.getMethod("helloWorld").invoke(null);
    }
}
```

## 评估器演示

```xml
<dependency>
    <groupId>org.noear</groupId>
    <artifactId>liquor-eval</artifactId>
    <version>1.2.0</version>
</dependency>
```

* 表达式评估器（只能写一行代码）

```java
public class DemoApp {
    public static void main(String[] args) throws Exception {
        ExpressionEvaluator expressionEvaluator = new ExpressionEvaluator();
        
        //基础
        System.out.println(expressionEvaluator.evaluate("1+1"));

        //进阶
        CodeSpec code1 = new CodeSpec("$0 + 22").parameters(Integer.class);
        System.out.println(expressionEvaluator.evaluate(code1, 2));
    }
}
```

* 脚本评估器

```java
public class DemoApp {
    public static void main(String[] args) throws Exception {
        ScriptEvaluator scriptEvaluator = new ScriptEvaluator();
        
        //基础
        scriptEvaluator.evaluate("System.out.println(\"hello word\");");

        //进阶
        CodeSpec code1 = new CodeSpec("class Demo {\n" +
                "      public String hello(String word) {\n" +
                "        return word;\n" +
                "      }\n" +
                "    }\n" +
                "\n" +
                "    Demo demo = new Demo();\n" +
                "    return demo.hello(name);") //name 为外部参数
                .parameters(new String[]{"name"}, new Class[]{String.class})
                .returnType(String.class);
        System.out.println(scriptEvaluator.evaluate(code1, "noear"));
    }
}
```

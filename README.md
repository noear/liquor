<h1 align="center" style="text-align:center;">
Liquor for java
</h1>
<p align="center">
	<strong>Java dynamic compiler, expression, scripting tool (jar in jar compatible)</strong>
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

The compiler code for this tool is mainly derived from arthas. Related knowledge is relatively unpopular, very precious. In order to reuse convenient, specially organized into a small toolkit for long-term maintenance.


| Artifact (zero dependencies) | Description | Size |
|------------------------------|-------------|------|
| liquor                       | compiler    | 24KB |
| liquor-eval                  | evaluator   | 10KB |

## Compiler demo

```xml
<dependency>
    <groupId>org.noear</groupId>
    <artifactId>liquor</artifactId>
    <version>1.2.2</version>
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
        //Add source code (more than one) and build
        compiler.addSource(className, classCode).build();

        Class<?> clazz = compiler.getClassLoader().loadClass(className);
        clazz.getMethod("helloWorld").invoke(null);
    }
}
```



## Evaluator demo

```xml
<dependency>
    <groupId>org.noear</groupId>
    <artifactId>liquor-eval</artifactId>
    <version>1.2.2</version>
</dependency>
```


* Expression Evaluator（You can only write one line of code）


```java
public class DemoApp {
    public static void main(String[] args) throws Exception {
        ExpressionEvaluator expressionEvaluator = new ExpressionEvaluator();
        
        //Basics
        System.out.println(expressionEvaluator.evaluate("1+1"));

        //Advanced
        CodeSpec code1 = new CodeSpec("$0 + 22").parameters(Integer.class);
        System.out.println(expressionEvaluator.evaluate(code1, 2)); //=> 24

        CodeSpec code2 = new CodeSpec("aa + 22").parameters(new String[]{"aa"}, new Class[]{Integer.class});
        System.out.println(expressionEvaluator.evaluate(code2, 2)); //=> 24
    }
}
```


* Script Evaluator

```java
public class DemoApp {
    public static void main(String[] args) throws Exception {
        ScriptEvaluator scriptEvaluator = new ScriptEvaluator();
        
        //Basics
        scriptEvaluator.evaluate("System.out.println(\"hello word\");");

        //Advanced
        CodeSpec code1 = new CodeSpec("class Demo {\n" +
                "      public String hello(String word) {\n" +
                "        return word;\n" +
                "      }\n" +
                "    }\n" +
                "\n" +
                "    Demo demo = new Demo();\n" +
                "    return demo.hello(name);") //name is an external parameter
                .parameters(new String[]{"name"}, new Class[]{String.class})
                .returnType(String.class);
        System.out.println(scriptEvaluator.evaluate(code1, "noear")); //=>noear
    }
}
```

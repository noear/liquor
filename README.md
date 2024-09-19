
[![Maven Central](https://img.shields.io/maven-central/v/org.noear/liquor.svg)](https://mvnrepository.com/search?q=g:org.noear%20AND%20liquor)
[![Apache 2.0](https://img.shields.io/:license-Apache2-blue.svg)](https://license.coscl.org.cn/Apache2/)
[![JDK-8+](https://img.shields.io/badge/JDK-8+-green.svg)](https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html)
[![QQ交流群](https://img.shields.io/badge/QQ交流群-22200020-orange)](https://jq.qq.com/?_wv=1027&k=kjB5JNiC)


# Liquor for java

Java dynamic compilation tool. (This tool is compatible with jar in jar)


The code of this tool mainly comes from Arthas. The relevant knowledge is relatively unpopular and precious, but it can only be used in appropriate scenes (it must not be abused). For the convenience of reuse, it is organized into a small toolkit for long-term maintenance.


Zero dependencies, 24KB of shipped packages.

| Packages (zero dependencies) | Description         | Size |
|------------------------------|------------|------|
| liquor                       | compiler   | 24kb |
| liquor-eval                  | evaluator        | 10kb |

## demo

* compiler-demo

```xml
<dependency>
    <groupId>org.noear</groupId>
    <artifactId>liquor</artifactId>
    <version>1.1.3</version>
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



* liquor-eval-demo

```xml
<dependency>
    <groupId>org.noear</groupId>
    <artifactId>liquor-eval</artifactId>
    <version>1.1.3</version>
</dependency>
```

```java
public class DemoApp {
    public static void main(String[] args) throws Exception{
        //脚本评估器
        ScriptEvaluator scriptEvaluator = new ScriptEvaluator();
        scriptEvaluator.evaluate("System.out.println(\"hello word\");");

        //表达式评估器（只能写一行代码）
        ExpressionEvaluator expressionEvaluator = new ExpressionEvaluator();
        System.out.println(expressionEvaluator.evaluate("1+1"));
    }
}
```
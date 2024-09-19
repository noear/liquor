
[![Maven Central](https://img.shields.io/maven-central/v/org.noear/liquor.svg)](https://mvnrepository.com/search?q=g:org.noear%20AND%20liquor)
[![Apache 2.0](https://img.shields.io/:license-Apache2-blue.svg)](https://license.coscl.org.cn/Apache2/)
[![JDK-8+](https://img.shields.io/badge/JDK-8+-green.svg)](https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html)
[![QQ交流群](https://img.shields.io/badge/QQ交流群-22200020-orange)](https://jq.qq.com/?_wv=1027&k=kjB5JNiC)


# Liquor for java

Java dynamic compilation tool. (This tool is compatible with jar in jar)


The compiler code for this tool is mainly derived from arthas. The relevant knowledge is relatively unpopular and precious, but it can only be used in appropriate scenes (it must not be abused). For the convenience of reuse, it is organized into a small toolkit for long-term maintenance.


Zero dependencies, 24KB of shipped packages.

| Packages (zero dependencies) | Description | Size |
|------------------------------|-------------|------|
| liquor                       | compiler    | 24KB |
| liquor-eval                  | evaluator   | 10KB |

## demo

* compiler-demo

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
    <version>1.2.0</version>
</dependency>
```


```java
//Script Evaluator
public class DemoApp {
    public static void main(String[] args) throws Exception {
        ScriptEvaluator scriptEvaluator = new ScriptEvaluator();
        
        //Basics
        scriptEvaluator.evaluate("System.out.println(\"hello word\");");

        //Advanced
        CodeSpec code1 = new CodeSpec("class Demo {\n" +
                "            public String hello(String word) {\n" +
                "                return word;\n" +
                "            }\n" +
                "        }\n" +
                "\n" +
                "        Demo demo = new Demo();\n" +
                "        return demo.hello(name);") //name 为外部参数
                .parameters(new String[]{"name"}, new Class[]{String.class})
                .returnType(String.class);
        System.out.println(scriptEvaluator.evaluate(code1, "noear"));
    }
}
```

```java
//Expression Evaluator（You can only write one line of code）
public class DemoApp {
    public static void main(String[] args) throws Exception {
        ExpressionEvaluator expressionEvaluator = new ExpressionEvaluator();
        
        //Basics
        System.out.println(expressionEvaluator.evaluate("1+1"));

        //Advanced
        CodeSpec codeSpec = new CodeSpec("$0 + 22").parameters(Integer.class);
        System.out.println(expressionEvaluator.evaluate(codeSpec, 2));
    }
}
```
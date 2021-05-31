
[![Maven Central](https://img.shields.io/maven-central/v/org.noear/liquor.svg)](https://mvnrepository.com/search?q=g:org.noear%20AND%20liquor)

` QQ交流群：22200020 `

# Liquor for java

Java dynamic compilation tool.


The code for this tool is primarily derived from Arthas. This part of the code is a bit of a no-no, so it's all very valuable, but it should only be used in appropriate situations (never overused). In order to facilitate reuse, it is specially arranged into a small tool kit for long-term maintenance.


### demo

```xml
<dependencies>
    <dependency>
        <groupId>org.noear</groupId>
        <artifactId>liquor</artifactId>
        <version>1.0.0</version>
    </dependency>
</dependencies>
```

```java
public class DemoApp {
    public static void main(String[] args) {
        String className = "com.demo.proxy.MyClass";
        String classCode = "package com.demo.proxy;\n" +
                "\n" +
                "public class MyClass {\n" +
                "\n" +
                "    public String say(String str){\n" +
                "        return \"hello\"+str;\n" +
                "    }\n" +
                "}";

        DynamicCompiler compiler = new DynamicCompiler();
        compiler.addSource(className, classCode);
        Map<String, Class<?>> classMap = compiler.build();

        System.out.println(classMap.get(className));
    }
}
```
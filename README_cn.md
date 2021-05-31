
[![Maven Central](https://img.shields.io/maven-central/v/org.noear/liquor.svg)](https://mvnrepository.com/search?q=g:org.noear%20AND%20liquor)

` QQ交流群：22200020 `

# Liquor for java

Java 动态编译小工具。


本工具的代码主要源自 arthas。这部份代码比较冷门，所有非常有价值，但也只能在合适的场景使用（切不可滥用）。为了方便复用，特整理成一个小工具包进行长期维护。


### 演示


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


[![Maven Central](https://img.shields.io/maven-central/v/org.noear/liquor.svg)](https://mvnrepository.com/search?q=g:org.noear%20AND%20liquor)
[![Apache 2.0](https://img.shields.io/:license-Apache2-blue.svg)](https://license.coscl.org.cn/Apache2/)
[![JDK-8+](https://img.shields.io/badge/JDK-8+-green.svg)](https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html)
[![QQ交流群](https://img.shields.io/badge/QQ交流群-22200020-orange)](https://jq.qq.com/?_wv=1027&k=kjB5JNiC)


# Liquor for java

Java 动态编译小工具。(此工具可兼容 jar in jar 的情况)


本工具的代码主要源自 arthas。相关的知识点比较冷门，甚是珍贵，但也只能用在合适的场景上（切不可滥用）。为了复用方便，特整理成一个小工具包进行长期维护。


零依赖，发布包仅 24kb。

### 演示


```xml
<dependencies>
    <dependency>
        <groupId>org.noear</groupId>
        <artifactId>liquor</artifactId>
        <version>1.0.4</version>
    </dependency>
</dependencies>
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

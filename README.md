# liquor

Java dynamic compilation tool

### demo

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
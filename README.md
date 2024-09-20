<h1 align="center" style="text-align:center;">
Liquor
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

The compiler code for this tool is mainly derived from arthas. Related knowledge is relatively unpopular, very precious. In order to reuse convenient, specially organized into a small toolkit for long-term maintenance. Expressions and scripting support were added later.

| Artifact             | Size | Features                                  | Functional Description                     |
|----------------------|------|-----------------------------------------|--------------------------|
| liquor               | 24KB | DynamicCompiler                         | Compile one or more classes (can depend on each other, can be compiled multiple times) |
| liquor-eval          | 14KB | ExpressionEvaluator<br>ScriptEvaluator  | Evaluate a one-line expression (multiple evaluations)<br/>Evaluate a script (can be evaluated multiple times) |


Reference dependency:

```xml
<dependency>
    <groupId>org.noear</groupId>
    <artifactId>liquor-eval</artifactId> <!-- or liquor -->
    <version>1.2.9</version>
</dependency>
```

Example reference:

* [liquor_demo_solon](liquor_demo_solon)
* [liquor_demo_springboot](liquor_demo_springboot)



## Compiler demo

You can have package names. You can import classes. Just like you would write a java class.

```java
public class DemoApp {
    public static void main(String[] args) throws Exception{
        // reusable (don't, keep creating)
        DynamicCompiler compiler = new DynamicCompiler();
        
        String className = "HelloWorld";
        String classCode = "import java.util.HashMap;\n\n"+
                "public class HelloWorld { " +
                "   public static void helloWorld() { " +
                "       System.out.println(\"Hello, world!\"); " +
                "   } " +
                "}";
        
        //Add source code (more than one) and build
        compiler.addSource(className, classCode).build();

        Class<?> clazz = compiler.getClassLoader().loadClass(className);
        clazz.getMethod("helloWorld").invoke(null);
    }
}
```



## Evaluator demo


### 1) Expression Evaluator（You can only write one line of code）// Internally compile the expression to a static function


* Something must be returned
* No ";" in the expression , the evaluator automatically adds "return" and ";". . Otherwise, make sure your statements are complete
* Use CodeSpec::imports to import the classes required by the expression


```java
public class DemoApp {
    public static void main(String[] args) throws Exception {
        // reusable (don't, keep creating)
        ExpressionEvaluator evaluator = ExpressionEvaluator.getInstance();
        
        //Basics
        System.out.println(evaluator.eval("1+1"));

        //Advanced
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


### 2) Script Evaluator // Internally compile the script to a static function

* Can import classes; It cannot have a package name
* Don't use "public" when using inner classes
* Using CodeSpec::imports to import the classes required by the expression; Or add an "import" statement to your code


```java
public class DemoApp {
    public static void main(String[] args) throws Exception {
        // reusable (don't, keep creating)
        ScriptEvaluator evaluator = ScriptEvaluator.getInstance();
        
        //Basics
        evaluator.eval("System.out.println(\"hello word\");");

        //Advanced (Don't add public if you have an inner class)
        CodeSpec code1 = new CodeSpec("import java.util.HashMap;\n\n"+
                "    class Demo {\n" +
                "      public String hello(String word) {\n" +
                "        return word;\n" +
                "      }\n" +
                "    }\n" +
                "\n" +
                "    Demo demo = new Demo();\n" +
                "    return demo.hello(name);") //name is an external parameter
                .parameters(new String[]{"name"}, new Class[]{String.class})
                .returnType(String.class);
        System.out.println(evaluator.eval(code1, "noear")); //=>noear
    }
}
```

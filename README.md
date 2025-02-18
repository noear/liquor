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

The compiler code for this tool is mainly derived from arthas. Related knowledge is relatively unpopular, very precious. In order to reuse convenient, specially organized into a small toolkit for long-term maintenance. Evaluators were added later to run expressions and scripts.

| Artifact             | Size | Features                                  | Functional Description         |
|----------------------|------|-----------------------------------------|--------------------------------|
| liquor               | 24KB | DynamicCompiler                         | Compiling classes              |
| liquor-eval          | 17KB | LiquorEvaluator (Exprs,Scripts)  | Evaluate expression and script |


Reference dependency:

```xml
<dependency>
    <groupId>org.noear</groupId>
    <artifactId>liquor-eval</artifactId> <!-- or liquor -->
    <version>1.3.11</version>
</dependency>
```


Performance (Third party performance test, champion):

* [https://gitee.com/xiandafu/beetl/tree/master/express-benchmark](https://gitee.com/xiandafu/beetl/tree/master/express-benchmark)


## Compiler demo

You can have package names. You can import classes. Interdependent; Can not repeat compilation; Just like you would write a java class.

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

Liquor evaluator tool, is based on Java compiler implementation. Under "cache overwriting", the performance is close to the original Java code. However, when there are "infinitely many variations" of expressions, the cache is invalidated and an infinite number of classes are generated, and then OOM.

Consider the expression evaluator:

* Use "variables" instead of constants to reduce compilation of Exprs.eval(new CodeSpec("a+b+c"), context).
  * [recommendation] effects, like class-to-instance relationships
* Use uncached mode Exprs.eval(new CodeSpec("1+2+3").cached(false))
  * [Not recommended]



### 1) Expression Evaluator（You can only write one line of code）


* Something must be returned
* There is no ";" in the expression , "return" and ";" are automatically added. . Otherwise, make sure your statements are complete
* Use CodeSpec::imports to import the classes required by the expression


```java
public class DemoApp {
    public static void main(String[] args) throws Exception {
        //Basics
        System.out.println(Exprs.eval("1+1"));

        //Advanced
        CodeSpec code1 = new CodeSpec("aa + 22").parameters(new ParamSpec("aa", Integer.class));
        System.out.println(Exprs.eval(code1, 1)); //=> 23
        
        Map<String, Object> context2 = new HashMap<>();
        context2.put("bb", 3);
        System.out.println(Exprs.eval("bb + 22", context2)); //=>25

        System.out.println(Exprs.eval(new CodeSpec("Math.min(1,2)").imports(Math.class))); //=>1
    }
}
```


### 2) Script Evaluator

* You can import classes or static methods. No package name
* Inner classes are used without decoration ("public", "static"). Fields and methods of inner classes do not support "static"
* Using CodeSpec::imports to import classes or static methods required by an expression Or add an "import" statement to your code


```java
public class DemoApp {
    public static void main(String[] args) throws Exception {
        //Basics
        Scripts.eval("System.out.println(\"hello word\");");

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
                .parameters(new ParamSpec("name", String.class))
                .returnType(String.class);
        System.out.println(Scripts.eval(code1, "noear")); //=>noear
    }
}
```

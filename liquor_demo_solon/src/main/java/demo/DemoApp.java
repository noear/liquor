package demo;

import org.noear.liquor.DynamicCompiler;
import org.noear.solon.Solon;
import org.noear.solon.core.util.ResourceUtil;

/**
 * @author noear 2021/5/31 created
 */
public class DemoApp {
    public static void main(String[] args) throws Exception {
        Solon.start(DemoApp.class, args);

        System.out.println("------------------ test0.无第三方包引入（纯代码） ------------------");
        compiler_test0();

        System.out.println("------------------ test1.无第三方包引入（读取文件）------------------");
        compiler_test1();

        System.out.println("------------------ test2.有第三方包引入(验证 spring-boot-maven-plugin 打包是否运行正常) ------------------");
        compiler_test2();
    }

    public static void compiler_test0() throws Exception {
        String className = "HelloWorld";
        String classCode = "public class HelloWorld { " +
                "   public static void helloWorld() { " +
                "       System.out.println(\"Hello, world!\"); " +
                "   } " +
                "}";

        DynamicCompiler compiler = new DynamicCompiler();
        //添加源码（可多个）并构建
        compiler.addSource(className, classCode).build();

        Class<?> clazz = compiler.getClassLoader().loadClass(className);
        clazz.getMethod("helloWorld").invoke(null);
    }

    public static void compiler_test1() throws Exception {
        String className = "com.demo.proxy.MyClass";
        String classCode = ResourceUtil.getResourceAsString("codefile/MyClass1.txt", null);

        System.out.println(classCode);
        DynamicCompiler compiler = new DynamicCompiler();
        compiler.addSource(className, classCode).build();

        Object instance = compiler.getClassLoader().loadClass(className);
        System.out.println(instance);
    }

    public static void compiler_test2() throws Exception {
        String className = "com.demo.proxy.MyClass";
        String classCode = ResourceUtil.getResourceAsString("codefile/MyClass2.txt", null);

        System.out.println(classCode);
        DynamicCompiler compiler = new DynamicCompiler();
        compiler.addSource(className, classCode).build();

        Object instance = compiler.getClassLoader().loadClass(className);
        System.out.println(instance);
    }
}
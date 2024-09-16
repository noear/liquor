package demo;

import org.noear.liquor.DynamicCompiler;
import org.noear.solon.Solon;
import org.noear.solon.Utils;

import java.io.IOException;

/**
 * @author noear 2021/5/31 created
 */
public class DemoApp {
    public static void main(String[] args) throws Exception {

        Solon.start(DemoApp.class, args);

        System.out.println("------------------ test1.无第三方包引入 ------------------");
        compiler_test1();

        System.out.println("------------------ test2.有第三方包引入(验证打包后是否运行正常) ------------------");
        compiler_test2();
    }

    public static void compiler_test1() throws Exception {
        String className = "com.demo.proxy.MyClass";
        String classCode = Utils.getResourceAsString("codefile/MyClass1.txt", null);

        System.out.println(classCode);
        DynamicCompiler compiler = new DynamicCompiler();
        compiler.addSource(className, classCode).build();

        Object instance = compiler.getClassLoader().loadClass(className);
        System.out.println(instance);
    }

    public static void compiler_test2() throws Exception {
        String className = "com.demo.proxy.MyClass";
        String classCode = Utils.getResourceAsString("codefile/MyClass2.txt", null);

        System.out.println(classCode);
        DynamicCompiler compiler = new DynamicCompiler();
        compiler.addSource(className, classCode).build();

        Object instance = compiler.getClassLoader().loadClass(className);
        System.out.println(instance);
    }
}

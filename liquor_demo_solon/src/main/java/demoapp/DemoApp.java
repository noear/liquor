package demoapp;

import org.noear.liquor.DynamicCompiler;
import org.noear.solon.Solon;
import org.noear.solon.Utils;

/**
 * @author noear 2021/5/31 created
 */
public class DemoApp {
    public static void main(String[] args) throws Exception{

        Solon.start(DemoApp.class, args);

        System.out.println("------------------ test1.无第三方包引入 ------------------");
        compiler_test1();

        System.out.println("------------------ test2.有第三方包引入(验证 spring-boot-maven-plugin 打包是否运行正常) ------------------");
        compiler_test2();
    }

    public static void compiler_test1() {
        String fullName = "com.demo.proxy.MyClass";
        String code = Utils.getResourceAsString("codefile/MyClass1.txt", null);

        System.out.println(code);
        DynamicCompiler de = new DynamicCompiler();
        de.addSource(fullName, code);
        Object instance = de.build().get(fullName);
        System.out.println(instance);
    }

    public static void compiler_test2()  {
        String fullName = "com.demo.proxy.MyClass";
        String code = Utils.getResourceAsString("codefile/MyClass2.txt", null);

        System.out.println(code);
        DynamicCompiler de = new DynamicCompiler();
        de.addSource(fullName, code);
        Object instance = de.build().get(fullName);
        System.out.println(instance);
    }
}

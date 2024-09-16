package demo;

import org.noear.liquor.DynamicCompiler;

import java.util.Map;

/**
 * @author noear 2021/5/31 created
 */
public class DemoApp2 {
    public static void main(String[] args) throws Exception{
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
}

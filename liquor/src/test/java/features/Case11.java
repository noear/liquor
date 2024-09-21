package features;

import org.junit.jupiter.api.Test;
import org.noear.liquor.DynamicCompiler;

/**
 * @author noear 2022/5/6 created
 */
public class Case11 {
    @Test
    public void test() throws Exception {
        DynamicCompiler compiler = new DynamicCompiler();

        String className = "com.demo.AClass";
        String classCode = "package com.demo;\n" +
                "\n" +
                "public class AClass {\n" +
                "\n" +
                "    public String say(String str){\n" +
                "        return \"hello\" + str;\n" +
                "    }\n" +
                "}";

        compiler.addSource(className, classCode).build();

        System.out.println("--> " + compiler.getClassLoader().size());
        Class<?> test1Class = compiler.getClassLoader().loadClass(className);
        System.out.println("--> " + test1Class.getName());

        // 增加源码进行编译 TODO 有bug
        className = "com.demo.BClass";
        classCode = "package com.demo;\n" +
                "\n" +
                "public class BClass {\n" +
                "\n" +
                "    public String say(String str){\n" +
                "        return \"hello\" + str;\n" +
                "    }\n" +
                "}";

        compiler.addSource(className, classCode).build();

        Class<?> test2Class = compiler.getClassLoader().loadClass(className);
        System.out.println("--> " + compiler.getClassLoader().size());

        Object tmp1 = test1Class.newInstance();
        Object tmp2 = test2Class.newInstance();
        System.out.println(tmp1.toString());
        System.out.println(tmp2.toString());
    }
}
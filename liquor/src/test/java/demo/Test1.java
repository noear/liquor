package demo;

import org.noear.liquor.DynamicCompiler;

import java.util.Map;

/**
 * @author noear 2022/5/6 created
 */
public class Test1 {
    public static void main(String[] args) throws Throwable{
        DynamicCompiler compiler = new DynamicCompiler();

        Map<String, Class<?>> classMap;

        String className = "com.demo.AClass";
        String classCode = "package com.demo;\n" +
                "\n" +
                "public class AClass {\n" +
                "\n" +
                "    public String say(String str){\n" +
                "        return \"hello\" + str;\n" +
                "    }\n" +
                "}";

        compiler.addSource(className, classCode);
        classMap = compiler.build();
        System.out.println("--> " + classMap.size());
        Class<?> test1Class = classMap.get(className);

        compiler.clear();

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

        compiler.addSource(className, classCode);
        classMap = compiler.build();

        System.out.println("--> " + classMap.size());

        Object tmp = test1Class.newInstance();
        System.out.println(tmp.toString());
    }
}

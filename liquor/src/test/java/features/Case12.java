package features;

import org.junit.jupiter.api.Test;
import org.noear.liquor.DynamicCompiler;

/**
 * @author noear 2024/9/20 created
 */
public class Case12 {
    @Test
    public void test() throws Exception{
        DynamicCompiler compiler = new DynamicCompiler();

        compiler.addSource("com.demo.UserDo", "package com.demo;\n" +
                "import java.util.HashMap;\n\n"+
                "public class UserDo{\n" +
                "    private String name;\n" +
                "\n" +
                "    public String getName() {\n" +
                "        return name;\n" +
                "    }\n" +
                "    \n" +
                "    public UserDo(String name) {\n" +
                "        this.name = name;\n" +
                "    }\n" +
                "}");

        compiler.addSource("com.demo.IUserService", "package com.demo;\n" +
                "public interface IUserService {\n" +
                "    UserDo getUser(String name);\n" +
                "}");

        compiler.addSource("com.demo.UserService", "package com.demo;\n" +
                "public class UserService implements IUserService {\n" +
                "    @Override\n" +
                "    public UserDo getUser(String name) {\n" +
                "        return new UserDo(name);\n" +
                "    }\n" +
                "}");

        compiler.build();

        Class<?> clz = compiler.getClassLoader().loadClass("com.demo.UserService");
        Object obj = clz.newInstance();

        System.out.println(obj);
        System.out.println(obj.getClass());

        Object objUser = clz.getMethods()[0].invoke(obj, "noear");
        System.out.println(objUser);
        System.out.println(objUser.getClass());
    }
}

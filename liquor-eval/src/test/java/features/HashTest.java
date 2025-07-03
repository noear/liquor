package features;

import org.junit.jupiter.api.Test;
import org.noear.liquor.eval.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author noear 2024/9/20 created
 */
public class HashTest {
    @Test
    public void case1() throws Exception {
        System.out.println(new CodeSpec("1+1").hashCode());
        System.out.println(new CodeSpec("1+1").hashCode());
        System.out.println("-------------");
        assert new CodeSpec("1+1").hashCode() == new CodeSpec("1+1").hashCode();

        System.out.println(new CodeSpec("1+1").parameters(new ParamSpec("name", String.class)).hashCode());
        System.out.println(new CodeSpec("1+1").parameters(new ParamSpec("name", String.class)).hashCode());
        System.out.println("-------------");
        assert new CodeSpec("1+1").parameters(new ParamSpec("name", String.class)).hashCode() == new CodeSpec("1+1").parameters(new ParamSpec("name", String.class)).hashCode();

        System.out.println(new ParamSpec("name", String.class).hashCode());
        System.out.println(new ParamSpec("name", String.class).hashCode());
        assert new ParamSpec("name", String.class).hashCode() == new ParamSpec("name", String.class).hashCode();
    }

    @Test
    public void case2() throws Exception {
        Map<String, Object> context = new HashMap<>();
        context.put("a", 1);
        context.put("b", 2);
        context.put("c", 3L);

        CodeSpec codeSpec1 = new CodeSpec("a+b+c");
        codeSpec1.parameters(context);


        context = new HashMap<>();
        context.put("b", 2);
        context.put("a", 1);
        context.put("c", 3L);

        CodeSpec codeSpec2 = new CodeSpec("a+b+c");
        codeSpec2.parameters(context);


        context = new HashMap<>();
        context.put("a", 1);
        context.put("c", 3L);
        context.put("b", 2);

        CodeSpec codeSpec3 = new CodeSpec("a+b+c");
        codeSpec3.parameters(context);

        assert codeSpec1.equals(codeSpec2);
        assert codeSpec1.equals(codeSpec3);
    }

    @Test
    public void case3() throws Exception {
        Map<String, Object> context = new HashMap<>();
        context.put("a", 1);
        context.put("b", 2);
        context.put("c", 3L);

        Execable execable1 = Exprs.compile(new CodeSpec("a+b+c").parameters(context));


        context = new HashMap<>();
        context.put("b", 2);
        context.put("a", 1);
        context.put("c", 3L);


        Execable execable2 = Exprs.compile(new CodeSpec("a+b+c").parameters(context));


        context = new HashMap<>();
        context.put("a", 1);
        context.put("c", 3L);
        context.put("b", 2);


        Execable execable3 = Exprs.compile(new CodeSpec("a+b+c").parameters(context));

        assert execable3 == execable1;
        assert execable3 == execable2;
    }
}
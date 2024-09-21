package features;

import org.junit.jupiter.api.Test;
import org.noear.liquor.eval.*;

/**
 * @author noear
 * @since 1.1
 */
public class Case21 {
    @Test
    public void test() throws Exception {
        Script.eval("System.out.println(\"hello word\");");
        Script.eval("System.out.println(\"hello word\");"); //cached
        Script.eval("System.out.println(\"hello word---x\");");

        Script.compile("System.out.println(\"hello word1\");").exec();

        //不推荐
        IExecutable executable1 = Script.compile("System.out.println(\"hello word2\");");
        executable1.exec();

        //////////////////////////

        System.out.println(Expr.eval("1+1"));
        System.out.println(Expr.eval("1+1")); //cached
        System.out.println(Expr.eval("1+2"));

        System.out.println(Expr.compile("1+22222").exec());

        //不推荐
        IExecutable executable2 = Expr.compile("1+1");
        System.out.println(executable2.exec());
    }
}
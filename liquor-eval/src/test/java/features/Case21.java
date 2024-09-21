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
        Scripts.eval("System.out.println(\"hello word\");");
        Scripts.eval("System.out.println(\"hello word\");"); //cached
        Scripts.eval("System.out.println(\"hello word---x\");");

        Scripts.compile("System.out.println(\"hello word1\");").exec();

        //不推荐
        IExecutable executable1 = Scripts.compile("System.out.println(\"hello word2\");");
        executable1.exec();

        //////////////////////////

        System.out.println(Expressions.eval("1+1"));
        System.out.println(Expressions.eval("1+1")); //cached
        System.out.println(Expressions.eval("1+2"));

        System.out.println(Expressions.compile("1+22222").exec());

        //不推荐
        IExecutable executable2 = Expressions.compile("1+1");
        System.out.println(executable2.exec());
    }
}
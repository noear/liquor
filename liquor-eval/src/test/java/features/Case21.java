package features;

import org.junit.jupiter.api.Test;
import org.noear.liquor.eval.*;

import java.util.List;

/**
 * @author noear
 * @since 1.1
 */
public class Case21 {
    @Test
    public void test() throws Exception {
        //测试全局导入功能
        LiquorEvaluator tmp = ((LiquorEvaluator)LiquorEvaluator.getInstance());
        tmp.globalImports(List.class);
        tmp.globalImports("java.util.*");

        /////////

        Scripts.eval("System.out.println(\"hello word\");");
        Scripts.eval("System.out.println(\"hello word\");"); //cached
        Scripts.eval("System.out.println(\"hello word---x\");");

        Scripts.compile("System.out.println(\"hello word1\");").exec();

        Execable execable1 = Scripts.compile("System.out.println(\"hello word2\");");
        execable1.exec();

        //////////////////////////

        System.out.println(Exprs.eval("1+1"));
        System.out.println(Exprs.eval("1+1")); //cached
        System.out.println(Exprs.eval("1+2"));

        System.out.println(Exprs.compile("1+22222").exec());

        Execable execable2 = Exprs.compile("1+1");
        System.out.println(execable2.exec());
    }
}
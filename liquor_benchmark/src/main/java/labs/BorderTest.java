package labs;

import com.googlecode.aviator.AviatorEvaluator;
import org.codehaus.janino.ExpressionEvaluator;
import org.noear.liquor.eval.CodeSpec;
import org.noear.liquor.eval.Exprs;
import org.noear.liquor.eval.LiquorEvaluator;

/**
 * @author noear 2024/9/23 created
 */
public class BorderTest {
    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 2000; i++) {
            //System.out.println(AviatorEvaluator.execute("1-1+" + i));
            //System.out.println(Exprs.eval(new CodeSpec("1-1+" + i).cached(false)));
            LiquorEvaluator evaluator = new LiquorEvaluator(null);

            System.out.println(evaluator.eval(new CodeSpec("1-1+" + i).returnType(Object.class)));
        }

        System.in.read();
    }
}

package benchmark;


import com.googlecode.aviator.AviatorEvaluator;
import org.noear.liquor.eval.Exprs;

public class Case1 {
    public static void main(String[] args) throws Exception {
        expr_janino_11(10000);
        expr_aviator_11(10000);
        expr_liquor_11(10000);

        expr_janino_11(10000);
        expr_aviator_11(10000);
        expr_liquor_11(10000);
    }

    public static void expr_janino_11(int count) throws Exception {
        long start = System.currentTimeMillis();


        //不能复用
        org.codehaus.janino.ExpressionEvaluator evaluator;
        for (int i = 0; i < count; i++) {
            evaluator = new org.codehaus.janino.ExpressionEvaluator();
            evaluator.cook("1+1");
            evaluator.evaluate();

            evaluator = new org.codehaus.janino.ExpressionEvaluator();
            evaluator.cook("1+2");
            evaluator.evaluate();

            evaluator = new org.codehaus.janino.ExpressionEvaluator();
            evaluator.cook("1+3");
            evaluator.evaluate();

            evaluator = new org.codehaus.janino.ExpressionEvaluator();
            evaluator.cook("1+4");
            evaluator.evaluate();

            evaluator = new org.codehaus.janino.ExpressionEvaluator();
            evaluator.cook("1+5");
            evaluator.evaluate();
        }

        System.out.println("janino::" + (System.currentTimeMillis() - start));
    }

    public static void expr_aviator_11(int count) throws Exception {
        long start = System.currentTimeMillis();

        //可以复用
        for (int i = 0; i < count; i++) {
            AviatorEvaluator.execute("1+1");
            AviatorEvaluator.execute("1+2");
            AviatorEvaluator.execute("1+3");
            AviatorEvaluator.execute("1+4");
            AviatorEvaluator.execute("1+5");
        }

        System.out.println("aviator::" + (System.currentTimeMillis() - start));
    }

    public static void expr_liquor_11(int count) throws Exception {
        long start = System.currentTimeMillis();

        //可以复用
        for (int i = 0; i < count; i++) {
            Exprs.eval("1+1");
            Exprs.eval("1+2");
            Exprs.eval("1+3");
            Exprs.eval("1+4");
            Exprs.eval("1+5");
        }

        System.out.println("liquor::" + (System.currentTimeMillis() - start));
    }
}
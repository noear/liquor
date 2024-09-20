package benchmark;



public class Case1 {
    public static void main(String[] args) throws Exception {
        expr_janino_11(10000);
        expr_liquor_11(10000);

        expr_janino_11(10000);
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

    public static void expr_liquor_11(int count) throws Exception {
        long start = System.currentTimeMillis();

        //可以复用
        org.noear.liquor.eval.ExpressionEvaluator evaluator = new org.noear.liquor.eval.ExpressionEvaluator();
        for (int i = 0; i < count; i++) {
            evaluator.eval("1+1");
            evaluator.eval("1+2");
            evaluator.eval("1+3");
            evaluator.eval("1+4");
            evaluator.eval("1+5");
        }

        System.out.println("liquor::" + (System.currentTimeMillis() - start));
    }
}
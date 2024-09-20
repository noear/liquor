
特点比较：

|   | liquor        | janino    | aviator |
|---|---------------|-----------|---------|
| 1 | 可以复用编译器       | 每次要新建     | 全局单例    | 
| 2 | 新建编译器，成本高     | 新建编译器，成本低 |   全局单例      | 

实现以下效果；liquor 比 janino 快20多倍，与 aviator 相关。

```java
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

expr_liquor_11(10000);
```
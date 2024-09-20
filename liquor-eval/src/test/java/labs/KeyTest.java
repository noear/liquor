package labs;

import org.noear.liquor.eval.CodeSpec;

/**
 * @author noear 2024/9/20 created
 */
public class KeyTest {
    public static void main(String[] args) {
        System.out.println(new CodeSpec("1+1").hashCode());
        System.out.println(new CodeSpec("1+1").hashCode());
        System.out.println(new CodeSpec("1+1").hashCode());
        System.out.println(new CodeSpec("1+1").hashCode());
        System.out.println(new CodeSpec("1+1").hashCode());
        System.out.println(new CodeSpec("1+2").hashCode());
        System.out.println(new CodeSpec("1+2").hashCode());
        System.out.println(new CodeSpec("1+2").hashCode());
        System.out.println(new CodeSpec("1+2").hashCode());
        System.out.println(new CodeSpec("1+2").hashCode());
        System.out.println(new CodeSpec("1+2").hashCode());

        System.out.println("1+1".hashCode());
        System.out.println("1+1".hashCode());
        System.out.println("1+2".hashCode());
        System.out.println("1+2".hashCode());
    }
}

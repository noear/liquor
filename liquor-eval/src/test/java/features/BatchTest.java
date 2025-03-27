package features;

import org.junit.jupiter.api.Test;
import org.noear.liquor.eval.CodeSpec;
import org.noear.liquor.eval.Execable;
import org.noear.liquor.eval.Exprs;
import org.noear.liquor.eval.Scripts;

import java.util.*;

/**
 * @author noear 2025/1/25 created
 */
public class BatchTest {
    @Test
    public void case1() throws Exception {
        Map<CodeSpec, Execable> execableMap = Exprs.compile(Arrays.asList(
                new CodeSpec("1+1"),
                new CodeSpec("2+2")));

        List<Integer> valList = new ArrayList<>();
        for (Map.Entry<CodeSpec, Execable> entry : execableMap.entrySet()) {
            Integer val = (Integer) entry.getValue().exec();
            valList.add(val);
            System.out.println(val);
        }

        assert valList.contains(2);
        assert valList.contains(4);
    }

    @Test
    public void case2() throws Exception {
        Map<CodeSpec, Execable> execableMap = Scripts.compile(Arrays.asList(
                new CodeSpec("return 1+1;").returnType(Integer.class),
                new CodeSpec("return 2+2;").returnType(Integer.class)));

        List<Integer> valList = new ArrayList<>();
        for (Map.Entry<CodeSpec, Execable> entry : execableMap.entrySet()) {
            Integer val = (Integer) entry.getValue().exec();
            valList.add(val);
            System.out.println(val);
        }

        assert valList.contains(2);
        assert valList.contains(4);
    }
}

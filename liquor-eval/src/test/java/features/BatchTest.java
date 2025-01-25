package features;

import org.junit.jupiter.api.Test;
import org.noear.liquor.eval.CodeSpec;
import org.noear.liquor.eval.Execable;
import org.noear.liquor.eval.Exprs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author noear 2025/1/25 created
 */
public class BatchTest {
    @Test
    public void case1() throws Exception {
        Map<CodeSpec, Execable> execableMap = Exprs.compile(Arrays.asList(new CodeSpec("1+1"), new CodeSpec("2+2")));

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

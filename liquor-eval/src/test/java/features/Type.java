package features;

import org.junit.jupiter.api.Test;
import org.noear.liquor.eval.Scripts;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author noear 2025/8/1 created
 */
public class Type {
    @Test
    public void case1() throws Exception {
        Map<String, Object> argsMap = new HashMap<>();
        argsMap.put("a", Arrays.asList(1,2,3));
        argsMap.put("b", Arrays.asList(1,2,3).iterator());
        argsMap.put("c",0);


        Scripts.eval("c=1+2;", argsMap);
    }
}

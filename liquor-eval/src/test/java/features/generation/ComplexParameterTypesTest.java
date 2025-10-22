package features.generation;

import org.junit.jupiter.api.Test;
import org.noear.liquor.eval.CodeSpec;
import org.noear.liquor.eval.Exprs;
import org.noear.liquor.eval.ParamSpec;
import org.noear.liquor.eval.Scripts;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 复杂参数类型测试
 * 测试嵌入类、临时类、匿名类、函数式接口等复杂类型
 */
public class ComplexParameterTypesTest {

    // 静态嵌套类
    public static class StaticNestedClass {
        private String value;

        public StaticNestedClass(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public String process(String prefix) {
            return prefix + ":" + value;
        }
    }

    // 内部类（非静态）
    public class InnerClass {
        private int number;

        public InnerClass(int number) {
            this.number = number;
        }

        public int getNumber() {
            return number;
        }

        public int multiply(int factor) {
            return number * factor;
        }
    }

    // 接口实现
    public interface Processor {
        String process(String input);
    }

    // 抽象类实现
    public static abstract class AbstractProcessor {
        public abstract String transform(String input);

        public String addPrefix(String input) {
            return "PREFIX_" + input;
        }
    }

    @Test
    void testStaticNestedClass() {
        StaticNestedClass nested = new StaticNestedClass("test_value");

        CodeSpec spec = new CodeSpec("nested.getValue().toUpperCase()")
                .imports(StaticNestedClass.class)
                .parameters(new ParamSpec("nested", StaticNestedClass.class))
                .returnType(String.class);

        Map<String, Object> context = new HashMap<>();
        context.put("nested", nested);

        Object result = Scripts.eval(spec, context);
        assertEquals("TEST_VALUE", result);
    }

    @Test
    void testInnerClass() {
        // 内部类需要外部类实例
        InnerClass inner = this.new InnerClass(42);

        CodeSpec spec = new CodeSpec("inner.multiply(2)")
                .imports(ComplexParameterTypesTest.class) // 导入外部类
                .parameters(new ParamSpec("inner", InnerClass.class))
                .returnType(Integer.class);

        Map<String, Object> context = new HashMap<>();
        context.put("inner", inner);

        Object result = Scripts.eval(spec, context);
        assertEquals(84, result);
    }

    @Test
    void testAnonymousClass() {
        // 匿名类实例
        Processor anonymousProcessor = new Processor() {
            @Override
            public String process(String input) {
                return input.toUpperCase() + "_PROCESSED";
            }
        };

        CodeSpec spec = new CodeSpec("processor.process(\"hello\")")
                .imports(Processor.class)
                .parameters(new ParamSpec("processor", Processor.class))
                .returnType(String.class);

        Map<String, Object> context = new HashMap<>();
        context.put("processor", anonymousProcessor);

        Object result = Scripts.eval(spec, context);
        assertEquals("HELLO_PROCESSED", result);
    }

    @Test
    void testLambdaExpression() {
        // Lambda 表达式（函数式接口）
        Function<String, Integer> stringLength = s -> s.length();

        CodeSpec spec = new CodeSpec("function.apply(\"hello world\")")
                .imports(Function.class)
                .parameters(new ParamSpec("function", Function.class))
                .returnType(Object.class);

        Map<String, Object> context = new HashMap<>();
        context.put("function", stringLength);

        Object result = Exprs.eval(spec, context);
        assertEquals(11, result);
    }

    @Test
    void testAbstractClassImplementation() {
        // 抽象类的匿名实现
        AbstractProcessor abstractImpl = new AbstractProcessor() {
            @Override
            public String transform(String input) {
                return input.toLowerCase();
            }
        };

        CodeSpec spec = new CodeSpec("processor.transform(processor.addPrefix(\"TEST\"))")
                .imports(AbstractProcessor.class)
                .parameters(new ParamSpec("processor", AbstractProcessor.class))
                .returnType(String.class);

        Map<String, Object> context = new HashMap<>();
        context.put("processor", abstractImpl);

        Object result = Scripts.eval(spec, context);
        assertEquals("PREFIX_TEST", ((String) result).toUpperCase());
    }

    public static class LocalCalculator {
        private int base;

        public LocalCalculator(int base) {
            this.base = base;
        }

        public int calculate(int multiplier) {
            return base * multiplier;
        }
    }

    @Test
    void testLocalClass() {
        // 方法内定义的局部类


        LocalCalculator localCalc = new LocalCalculator(10);

        CodeSpec spec = new CodeSpec("calculator.calculate(5)")
                .parameters(new ParamSpec("calculator", LocalCalculator.class))
                .returnType(Integer.class);

        Map<String, Object> context = new HashMap<>();
        context.put("calculator", localCalc);

        Object result = Exprs.eval(spec, context);
        assertEquals(50, result);
    }

    @Test
    void testGenericTypes() {
        // 泛型集合
        List<String> stringList = Arrays.asList("a", "bb", "ccc");
        Map<Integer, String> numberMap = new HashMap<>();
        numberMap.put(1, "one");
        numberMap.put(2, "two");

        CodeSpec spec = new CodeSpec("int totalLength = 0;\n" +
                "            for (Object s : stringList) {\n" +
                "                totalLength += ((String)s).length();\n" +
                "            }\n" +
                "            totalLength += ((String)numberMap.get(1)).length();\n" +
                "            totalLength += ((String)numberMap.get(2)).length();\n" +
                "            return totalLength;")
                .imports(List.class, Map.class)
                .parameters(
                        new ParamSpec("stringList", List.class),
                        new ParamSpec("numberMap", Map.class)
                )
                .returnType(Integer.class);

        Map<String, Object> context = new HashMap<>();
        context.put("stringList", stringList);
        context.put("numberMap", numberMap);

        Object result = Scripts.eval(spec, context);
        assertEquals(12, result);
    }

    @Test
    void testConcurrentAtomicTypes() {
        // 并发原子类型
        AtomicInteger atomicInt = new AtomicInteger(100);

        CodeSpec spec = new CodeSpec("atomic.getAndAdd(50)")
                .imports(AtomicInteger.class)
                .parameters(new ParamSpec("atomic", AtomicInteger.class))
                .returnType(Integer.class);

        Map<String, Object> context = new HashMap<>();
        context.put("atomic", atomicInt);

        Object result = Scripts.eval(spec, context);
        assertEquals(100, result); // getAndAdd 返回旧值
        assertEquals(150, atomicInt.get()); // 但实际值已经增加
    }

    // 枚举类型
    public static enum Status {
        PENDING, PROCESSING, COMPLETED, FAILED
    }

    @Test
    void testEnumTypes() {


        CodeSpec spec = new CodeSpec("status.name() + \"_\" + status.ordinal()")
                .imports(ComplexParameterTypesTest.class) // 包含枚举的类
                .parameters(new ParamSpec("status", Status.class))
                .returnType(String.class);

        Map<String, Object> context = new HashMap<>();
        context.put("status", Status.PROCESSING);

        Object result = Exprs.eval(spec, context);
        assertEquals("PROCESSING_1", result);
    }

    @Test
    void testArrayOfCustomObjects() {
        // 自定义对象数组
        StaticNestedClass[] objects = {
                new StaticNestedClass("first"),
                new StaticNestedClass("second"),
                new StaticNestedClass("third")
        };

        CodeSpec spec = new CodeSpec("StringBuilder sb = new StringBuilder();\n" +
                "            for (int i = 0; i < objects.length; i++) {\n" +
                "                if (i > 0) sb.append(\",\");\n" +
                "                sb.append(objects[i].getValue());\n" +
                "            }\n" +
                "            return sb.toString();")
                .imports(StaticNestedClass.class, StringBuilder.class)
                .parameters(new ParamSpec("objects", StaticNestedClass[].class))
                .returnType(String.class);

        Map<String, Object> context = new HashMap<>();
        context.put("objects", objects);

        Object result = Scripts.eval(spec, context);
        assertEquals("first,second,third", result);
    }

    // 复杂的嵌套结构
   public static class Department {
        private String name;
        private List<Employee> employees;

        public Department(String name, List<Employee> employees) {
            this.name = name;
            this.employees = employees;
        }

        public String getName() {
            return name;
        }

        public List<Employee> getEmployees() {
            return employees;
        }
    }

   public static class Employee {
        private String name;
        private int salary;

        public Employee(String name, int salary) {
            this.name = name;
            this.salary = salary;
        }

        public String getName() {
            return name;
        }

        public int getSalary() {
            return salary;
        }
    }

    @Test
    void testComplexNestedStructures() {


        List<Employee> employees = Arrays.asList(
                new Employee("Alice", 50000),
                new Employee("Bob", 60000),
                new Employee("Charlie", 70000)
        );

        Department dept = new Department("Engineering", employees);

        CodeSpec spec = new CodeSpec("int totalSalary = 0;\n" +
                "            int employeeCount = department.getEmployees().size();\n" +
                "            for (Employee emp : department.getEmployees()) {\n" +
                "                totalSalary += emp.getSalary();\n" +
                "            }\n" +
                "            return department.getName() + \": \" + employeeCount + \" employees, total salary: \" + totalSalary;")
                .parameters(
                        new ParamSpec("department", Department.class),
                        new ParamSpec("Employee", Employee.class) // 用于类型声明
                )
                .imports(Employee.class)
                .returnType(String.class);

        Map<String, Object> context = new HashMap<>();
        context.put("department", dept);

        Object result = Exprs.eval(spec, context);
        assertEquals("Engineering: 3 employees, total salary: 180000", result);
    }

    @Test
    void testStreamAPIWithComplexTypes() {
        // 使用 Stream API 处理复杂类型
        List<StaticNestedClass> items = Arrays.asList(
                new StaticNestedClass("apple"),
                new StaticNestedClass("banana"),
                new StaticNestedClass("cherry")
        );

        CodeSpec spec = new CodeSpec("items.stream()\n" +
                "                .map(item -> (((StaticNestedClass)item).getValue()).toUpperCase())\n" +
                "                .sorted()\n" +
                "                .collect(java.util.stream.Collectors.joining(\", \"))")
                .imports(StaticNestedClass.class, java.util.stream.Collectors.class)
                .parameters(new ParamSpec("items", List.class))
                .returnType(String.class);

        Map<String, Object> context = new HashMap<>();
        context.put("items", items);

        Object result = Scripts.eval(spec, context);
        assertEquals("APPLE, BANANA, CHERRY", result);
    }

    @Test
    void testOptionalTypes() {
        // Optional 类型处理
        Optional<String> optionalValue = Optional.of("present_value");
        Optional<String> emptyOptional = Optional.empty();

        CodeSpec spec = new CodeSpec("Object result1 = value1.isPresent() ? value1.get() : \"default1\";\n" +
                "            Object result2 = value2.isPresent() ? value2.get() : \"default2\";\n" +
                "            return result1 + \"|\" + result2;")
                .imports(Optional.class)
                .parameters(
                        new ParamSpec("value1", Optional.class),
                        new ParamSpec("value2", Optional.class)
                )
                .returnType(String.class);

        Map<String, Object> context = new HashMap<>();
        context.put("value1", optionalValue);
        context.put("value2", emptyOptional);

        Object result = Scripts.eval(spec, context);
        assertEquals("present_value|default2", result);
    }

    // 通过公共接口访问私有内部类
    public static interface PublicInterface {
        String getData();
    }

    // 私有内部类实现公共接口
    public static class PrivateClass implements PublicInterface {
        private String data;

        public PrivateClass(String data) {
            this.data = data;
        }

        @Override
        public String getData() {
            return "Private: " + data;
        }
    }

    @Test
    void testPrivateInnerClassThroughPublicInterface() {


        PublicInterface instance = new PrivateClass("secret");

        CodeSpec spec = new CodeSpec("instance.getData()")
                .imports(PublicInterface.class)
                .parameters(new ParamSpec("instance", PublicInterface.class))
                .returnType(String.class);

        Map<String, Object> context = new HashMap<>();
        context.put("instance", instance);

        Object result = Scripts.eval(spec, context);
        assertEquals("Private: secret", result);
    }

    // 包含泛型方法的类
    public static class GenericMethodClass {
        public <T> T process(T input) {
            return input;
        }

        public <T> List<T> createList(T... items) {
            return Arrays.asList(items);
        }
    }

    @Test
    void testClassWithGenericMethods() {
        GenericMethodClass processor = new GenericMethodClass();

        CodeSpec spec = new CodeSpec("  String strResult = processor.process(\"test_string\");\n" +
                "            List<Integer> listResult = processor.createList(1, 2, 3);\n" +
                "            return strResult + \"_\" + listResult.size();")
                .imports(GenericMethodClass.class, List.class)
                .parameters(new ParamSpec("processor", GenericMethodClass.class))
                .returnType(String.class);

        Map<String, Object> context = new HashMap<>();
        context.put("processor", processor);

        Object result = Scripts.eval(spec, context);
        assertEquals("test_string_3", result);
    }
}
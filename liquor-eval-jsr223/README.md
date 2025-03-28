
### 简单示例

引入依赖包

```xml
<dependency>
    <groupId>org.noear</groupId>
    <artifactId>liquor-eval-jsr223</artifactId>
    <version>1.5.0</version>
</dependency>
```

编写单元测试

```java
@Test
public void case1() {
    ScriptEngineManager sem = new ScriptEngineManager();
    ScriptEngine scriptEngine = scriptEngineManager.getEngineByName("liquor"); //或 "java"

    scriptEngine.eval("System.out.println(\"Hello world!\");");
}
```
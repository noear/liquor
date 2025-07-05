package demo;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Spring Boot API集成测试类
 * 测试静态控制器、动态控制器和动态Bean管理功能
 *
 * @author phanes
 */
@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DemoAppTest {

    @Value("${local.server.port}")
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private DynamicBeanManager dynamicBeanManager;

    /**
     * 测试应用启动和动态Bean注册
     */
    @Test
    @Order(1)
    public void testApplicationStartupAndDynamicBeanRegistration() {
        log.info("开始测试应用启动和动态Bean注册...");

        // 验证应用上下文正常启动
        assertThat(applicationContext).isNotNull();

        // 验证动态Bean管理器正常工作
        assertThat(dynamicBeanManager).isNotNull();

        // 验证动态Bean已注册
        assertThat(dynamicBeanManager.getRegisteredBeans()).isNotEmpty();
        assertThat(dynamicBeanManager.getRegisteredBeans()).containsKey("helloLiquorController");

        log.info("应用启动和动态Bean注册测试通过");
    }

    /**
     * 测试静态控制器 - 根路径默认参数
     */
    @Test
    @Order(2)
    public void testIndexControllerRootWithDefaultParam() {
        log.info("测试静态控制器根路径默认参数...");

        ResponseEntity<String> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Hello World!");

        log.info("静态控制器根路径默认参数测试通过");
    }

    /**
     * 测试静态控制器 - hello路径默认参数
     */
    @Test
    @Order(3)
    public void testIndexControllerHelloWithDefaultParam() {
        log.info("测试静态控制器hello路径默认参数...");

        ResponseEntity<String> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/hello", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Hello World!");

        log.info("静态控制器hello路径默认参数测试通过");
    }

    /**
     * 测试静态控制器 - 自定义参数
     */
    @Test
    @Order(4)
    public void testIndexControllerWithCustomParam() {
        log.info("测试静态控制器自定义参数...");

        ResponseEntity<String> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/?name=SpringBoot", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Hello SpringBoot!");

        // 测试hello路径的自定义参数
        response = restTemplate.getForEntity(
                "http://localhost:" + port + "/hello?name=Test", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Hello Test!");

        log.info("静态控制器自定义参数测试通过");
    }

    /**
     * 测试动态控制器 - liquor路径默认参数
     */
    @Test
    @Order(5)
    public void testDynamicControllerLiquorWithDefaultParam() {
        log.info("测试动态控制器liquor路径默认参数...");

        ResponseEntity<String> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/liquor", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Hello liquor!");

        log.info("动态控制器liquor路径默认参数测试通过");
    }

    /**
     * 测试动态控制器 - liquor/hello路径默认参数
     */
    @Test
    @Order(6)
    public void testDynamicControllerLiquorHelloWithDefaultParam() {
        log.info("测试动态控制器liquor/hello路径默认参数...");

        ResponseEntity<String> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/liquor/hello", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Hello liquor!");

        log.info("动态控制器liquor/hello路径默认参数测试通过");
    }

    /**
     * 测试动态控制器 - 自定义参数
     */
    @Test
    @Order(7)
    public void testDynamicControllerWithCustomParam() {
        log.info("测试动态控制器自定义参数...");

        ResponseEntity<String> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/liquor?name=DynamicTest", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Hello DynamicTest!");

        // 测试liquor/hello路径的自定义参数
        response = restTemplate.getForEntity(
                "http://localhost:" + port + "/liquor/hello?name=Compiler", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Hello Compiler!");

        log.info("动态控制器自定义参数测试通过");
    }

    /**
     * 测试动态控制器 - 时间获取接口
     */
    @Test
    @Order(8)
    public void testDynamicControllerGetDate() {
        log.info("测试动态控制器时间获取接口...");

        ResponseEntity<String> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/liquor/getDate", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isNotEmpty();

        // 验证返回的时间格式是否为有效的LocalDateTime字符串
        try {
            LocalDateTime.parse(response.getBody());
            log.info("时间格式验证成功: {}", response.getBody());
        } catch (DateTimeParseException e) {
            throw new AssertionError("返回的时间格式无效: " + response.getBody(), e);
        }

        log.info("动态控制器时间获取接口测试通过");
    }

    /**
     * 测试动态控制器 - 配置信息获取接口
     */
    @Test
    @Order(9)
    public void testDynamicControllerGetConfig() {
        log.info("测试动态控制器配置信息获取接口...");

        ResponseEntity<String> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/liquor/config", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isNotEmpty();

        // 验证返回内容包含配置信息
        assertThat(response.getBody()).contains("服务配置信息");
        assertThat(response.getBody()).contains("端口: 0");
        assertThat(response.getBody()).contains("上下文路径: /");
        assertThat(response.getBody()).contains("日志级别: debug");

        log.info("动态控制器配置信息获取接口测试通过: {}", response.getBody());
    }

    /**
     * 测试动态Bean管理功能
     */
    @Test
    @Order(10)
    public void testDynamicBeanManagement() {
        log.info("测试动态Bean管理功能...");

        // 验证动态Bean注册信息
        Map<String, DynamicBeanManager.DynamicBeanInfo> registeredBeans = dynamicBeanManager.getRegisteredBeans();
        assertThat(registeredBeans).hasSize(1);

        DynamicBeanManager.DynamicBeanInfo beanInfo = registeredBeans.get("helloLiquorController");
        assertThat(beanInfo).isNotNull();
        assertThat(beanInfo.getBeanName()).isEqualTo("helloLiquorController");
        assertThat(beanInfo.getClassName()).isEqualTo("demo.HelloLiquor");
        assertThat(beanInfo.getClazz()).isNotNull();
        assertThat(beanInfo.getRegistrationTime()).isNotNull();

        // 验证动态Bean在Spring容器中存在
        assertThat(applicationContext.containsBean("helloLiquorController")).isTrue();

        // 验证动态编译的类可以被加载
        Object dynamicBean = applicationContext.getBean("helloLiquorController");
        assertThat(dynamicBean).isNotNull();
        assertThat(dynamicBean.getClass().getName()).isEqualTo("demo.HelloLiquor");

        log.info("动态Bean管理功能测试通过 - Bean: {}, 类: {}, 注册时间: {}",
                beanInfo.getBeanName(),
                beanInfo.getClassName(),
                beanInfo.getRegistrationTime());
    }

    /**
     * 测试动态控制器重新注册功能
     */
    @Test
    @Order(11)
    public void testDynamicControllerReRegistration() throws Exception {
        log.info("测试动态控制器重新注册功能...");

        dynamicBeanManager.unregisterDynamicController("helloLiquorController");

        // 读取修改后的HelloLiquor.txt内容
        String updatedClassCode = "            package demo;\n" +
                "\n" +
                "            import org.springframework.beans.factory.annotation.Autowired;\n" +
                "            import org.springframework.web.bind.annotation.GetMapping;\n" +
                "            import org.springframework.web.bind.annotation.RequestParam;\n" +
                "            import org.springframework.web.bind.annotation.ResponseBody;\n" +
                "\n" +
                "            public class HelloLiquor {\n" +
                "\n" +
                "                @Autowired\n" +
                "                private DemoService demoService;\n" +
                "\n" +
                "                @ResponseBody\n" +
                "                @GetMapping(\"/liquor/profile\")\n" +
                "                public String getUserProfile(\n" +
                "                    @RequestParam(value = \"name\", defaultValue = \"访客\") String name,\n" +
                "                    @RequestParam(value = \"age\", defaultValue = \"18\") Integer age\n" +
                "                ) {\n" +
                "                    String greeting = demoService.sayHello(name);\n" +
                "                    return String.format(\"%s 年龄: %d岁, 服务信息: %s\",\n" +
                "                        greeting, age, demoService.getServiceInfo());\n" +
                "                }\n" +
                "\n" +
                "            }";

        // 重新注册动态控制器
        dynamicBeanManager.registerDynamicController("helloLiquorController", "demo.HelloLiquor", updatedClassCode);

        // 验证重新注册成功
        Map<String, DynamicBeanManager.DynamicBeanInfo> registeredBeans = dynamicBeanManager.getRegisteredBeans();
        assertThat(registeredBeans).hasSize(1);
        assertThat(registeredBeans).containsKey("helloLiquorController");

        // 验证新的Bean可以正常工作
        Object dynamicBean = applicationContext.getBean("helloLiquorController");
        assertThat(dynamicBean).isNotNull();
        assertThat(dynamicBean.getClass().getName()).isEqualTo("demo.HelloLiquor");

        log.info("动态控制器重新注册功能测试通过");
    }

    /**
     * 测试新增的profile接口 - 默认参数
     */
    @Test
    @Order(12)
    public void testNewProfileInterfaceWithDefaultParams() {
        log.info("测试新增的profile接口默认参数...");

        ResponseEntity<String> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/liquor/profile", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).contains("Hello 访客!");
        assertThat(response.getBody()).contains("年龄: 18岁");
        assertThat(response.getBody()).contains("服务信息: DemoService - 动态Bean依赖注入测试服务");

        log.info("新增profile接口默认参数测试通过: {}", response.getBody());
    }

    /**
     * 测试新增的profile接口 - 自定义参数
     */
    @Test
    @Order(13)
    public void testNewProfileInterfaceWithCustomParams() {
        log.info("测试新增的profile接口自定义参数...");

        ResponseEntity<String> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/liquor/profile?name=张三&age=25", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).contains("Hello 张三!");
        assertThat(response.getBody()).contains("年龄: 25岁");
        assertThat(response.getBody()).contains("服务信息: DemoService - 动态Bean依赖注入测试服务");

        // 测试部分参数自定义
        response = restTemplate.getForEntity(
                "http://localhost:" + port + "/liquor/profile?name=李四", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("Hello 李四!");
        assertThat(response.getBody()).contains("年龄: 18岁"); // 使用默认年龄

        log.info("新增profile接口自定义参数测试通过");
    }

}

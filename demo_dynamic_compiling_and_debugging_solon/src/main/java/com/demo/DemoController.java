package com.demo;

import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.Mapping;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
@Mapping("/demo")
public class DemoController {
    @Inject
    DemoDynamicJavaService demoLiquorService;

    @Mapping("/hello")
    public String hello() {
        return "Hello, World!";
    }

    @Mapping("/testDynamicJava")
    public String test() throws Exception {
        // 获取请求报文参数
        Map<String,Object> params=new HashMap<>();
        Class<?> clazz = demoLiquorService.loadClass("com.demo.script.DynamicDemoScript");
        Object instance = clazz.getDeclaredConstructor().newInstance();
        return clazz.getMethod("hello", Map.class).invoke(instance, params).toString();
    }
}

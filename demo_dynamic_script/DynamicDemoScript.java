package com.demo.script;

import java.util.Map;

//此脚本由 demo_dynamic_compiling_and_debugging_solon 动态加载，且是可调试的
public class DynamicDemoScript {
    public String hello(Map<String, Object> params) {
        System.out.println("Hello, Dynamic Script!1");
        System.out.println("Hello, Dynamic Script!2");
        return "Hello, Dynamic Script!5";
    }
}

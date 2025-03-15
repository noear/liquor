package com.demo;

import org.noear.solon.Solon;
import org.noear.solon.annotation.SolonMain;
import org.noear.solon.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SolonMain
public class DemoApp {
    public static void main(String[] args) {
        Solon.start(DemoApp.class, args, app -> {
            //如果打开根地址，跳到演示地址（方便）
            app.get("/", ctx -> ctx.redirect("/demo/testDynamicJava"));
        });
    }
}

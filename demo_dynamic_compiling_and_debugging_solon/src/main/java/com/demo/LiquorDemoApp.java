package com.demo;

import org.noear.solon.Solon;
import org.noear.solon.annotation.SolonMain;
import org.noear.solon.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SolonMain
public class LiquorDemoApp {
    public static void main(String[] args) {
        Solon.start(LiquorDemoApp.class, args, app -> {
            app.get("/", ctx -> ctx.redirect("/demo/testDynamicJava"));
        });
    }
}

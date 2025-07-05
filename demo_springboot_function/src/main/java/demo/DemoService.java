package demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 演示服务类
 * 用于测试动态编译类的构造函数依赖注入功能
 *
 * @author phanes
 */
@Slf4j
@Service
public class DemoService {

    public String sayHello(String name) {
        String message = String.format("Hello %s!", name);
        log.info("DemoService.sayHello 调用: {}", message);
        return message;
    }

    public String getServiceInfo() {
        return "DemoService - 动态Bean依赖注入测试服务";
    }

}

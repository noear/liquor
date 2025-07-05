package demo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * 动态Controller注册器
 * 使用Spring原生Bean注册机制，完全集成到Spring生命周期中
 *
 * @author phanes
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ControllerRegister implements ApplicationRunner {

    private final DynamicBeanManager dynamicBeanManager;
    private final ResourceLoader resourceLoader;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        this.registerHelloLiquorController();
    }

    /**
     * 注册HelloLiquor控制器
     */
    public void registerHelloLiquorController() {
        try {
            // 读取资源文件
            Resource resource = resourceLoader.getResource("classpath:codefile/HelloLiquor.txt");
            String classCode;
            try (InputStreamReader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)) {
                classCode = FileCopyUtils.copyToString(reader);
            }

            // 编译并注册动态Controller
            dynamicBeanManager.registerDynamicController("helloLiquorController", "demo.HelloLiquor", classCode);

            log.info("动态Controller注册成功: HelloLiquor");
        } catch (IOException e) {
            log.error("读取HelloLiquor.txt文件失败", e);
            throw new RuntimeException("动态Controller注册失败", e);
        } catch (Exception e) {
            log.error("动态Controller注册失败", e);
            throw new RuntimeException("动态Controller注册失败", e);
        }
    }

}

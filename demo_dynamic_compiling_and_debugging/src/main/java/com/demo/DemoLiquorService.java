package com.demo;

import lombok.extern.slf4j.Slf4j;
import org.noear.liquor.DynamicCompiler;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Init;
import org.noear.solon.scheduling.annotation.Scheduled;

import java.io.IOException;
import java.nio.file.*;
import java.util.stream.Stream;


@Slf4j
@Component
public class DemoLiquorService {
    private final DynamicCompiler compiler = new DynamicCompiler();
    private final String directoryPath;

    public DemoLiquorService() {
        //这个相对位置，只适合在开发时以 liquor 工程为基准
        directoryPath = System.getProperty("user.dir") + "/demo_dynamic_compiling_and_debugging/dynamic/";
    }

    //获取类
    public Class<?> loadClass(String className) throws Exception {
        return compiler.getClassLoader().loadClass(className);
    }

    //初始化
    @Init
    public void init() {
        loadAndCompileFiles();
    }

    //定时刷新
    @Scheduled(fixedDelay = 10)
    public void update() {
        try {
            WatchService watchService = FileSystems.getDefault().newWatchService();
            Path path = Paths.get(directoryPath);
            path.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);
            WatchKey key = watchService.take();
            log.info("File watcher is active and waiting for events...");

            for (WatchEvent<?> event : key.pollEvents()) {
                if (event.kind() == StandardWatchEventKinds.ENTRY_MODIFY) {
                    log.info("File modified: " + event.context());
                    loadAndCompileFiles();
                }
            }
        } catch (Exception e) {
            log.error("File watcher interrupted", e);
        }
    }

    private void loadAndCompileFiles() {
        try {
            // 清除之前的类定义
            compiler.reset();
            try (Stream<Path> paths = Files.walk(Paths.get(directoryPath))) {
                paths.filter(Files::isRegularFile)
                        .filter(path -> path.toString().endsWith(".java"))
                        .forEach(path -> {
                            try {
                                String classCode = new String(Files.readAllBytes(path));
                                String className = "com.a." + path.getFileName().toString().replace(".java", "");
                                compiler.addSource(className, classCode);
                            } catch (IOException e) {
                                log.error("Failed to read file: " + path, e);
                            }
                        });
                compiler.compile();
            }
        } catch (Exception e) {
            log.error("Failed to load and compile files", e);
        }
    }
}
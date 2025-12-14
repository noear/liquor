package demo;

import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 动态Bean信息
 */
@Getter
public class DynamicBeanInfo {
    private final String beanName;
    private final String className;
    private final Class<?> clazz;
    private final LocalDateTime registrationTime;

    public DynamicBeanInfo(String beanName, String className, Class<?> clazz) {
        this.beanName = beanName;
        this.className = className;
        this.clazz = clazz;
        this.registrationTime = LocalDateTime.now();
    }
}
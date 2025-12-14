package demo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.noear.liquor.DynamicCompiler;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 动态Bean管理器
 * 提供动态编译类的Spring Bean注册、生命周期管理和RequestMapping自动刷新功能
 *
 * @author phanes
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DynamicBeanManager {

    private final ConfigurableApplicationContext applicationContext;
    private final RequestMappingHandlerMapping requestMappingHandlerMapping;

    /**
     * 已注册的动态Bean缓存
     */
    @Getter
    private final Map<String, DynamicBeanInfo> registeredBeans = new ConcurrentHashMap<>();

    /**
     * 注册动态编译的Controller类到Spring容器
     *
     * @param beanName   Bean名称
     * @param className  类名
     * @param sourceCode 源代码
     * @throws Exception 注册过程中的异常
     */
    public void registerDynamicController(String beanName, String className, String sourceCode) throws Exception {
        try {
            log.info("开始注册动态Controller: {} -> {}", beanName, className);

            // 检查是否已存在，如果存在则先注销
            if (registeredBeans.containsKey(beanName)) {
                unregisterDynamicController(beanName);
            }

            // 编译动态类
            Class<?> clazz = compileDynamicClass(className, sourceCode);

            // 注册为Spring Bean
            registerSpringBean(beanName, clazz);

            // 刷新RequestMapping映射
            refreshRequestMappings(beanName);

            // 记录注册信息
            DynamicBeanInfo beanInfo = new DynamicBeanInfo(beanName, className, clazz);
            registeredBeans.put(beanName, beanInfo);

            log.info("动态Controller注册成功: {}", beanName);

        } catch (Exception e) {
            log.error("注册动态Controller失败: {} -> {}", beanName, className, e);
            // 清理可能的部分注册状态
            cleanupFailedRegistration(beanName);
            throw e;
        }
    }

    /**
     * 注销动态Controller
     *
     * @param beanName Bean名称
     */
    public void unregisterDynamicController(String beanName) {
        DynamicBeanInfo beanInfo = registeredBeans.remove(beanName);
        if (beanInfo != null) {
            log.info("开始注销动态Controller: {} (类名: {})", beanName, beanInfo.getClassName());
            try {
                // 注销RequestMapping映射
                unregisterRequestMappings(beanName);

                // 使用DefaultListableBeanFactory进行完整的Bean清理
                DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();

                // 完整清理Bean实例和定义
                if (beanFactory.containsBeanDefinition(beanName)) {
                    // 如果Bean实例已创建，先销毁实例
                    if (beanFactory.containsSingleton(beanName)) {
                        beanFactory.destroySingleton(beanName);
                        log.info("Bean实例 {} 已被销毁", beanName);
                    }

                    // 移除Bean定义
                    beanFactory.removeBeanDefinition(beanName);
                    log.info("Bean定义 {} 已从Spring容器中移除", beanName);
                } else {
                    log.warn("Bean定义 {} 不存在，跳过移除", beanName);
                }

                // 验证清理状态
                validateCleanupStatus(beanName, beanFactory);

                log.info("动态Controller注销成功: {}", beanName);
            } catch (ClassCastException e) {
                log.error("ApplicationContext类型不支持动态Bean管理: {}", beanName, e);
            } catch (IllegalStateException e) {
                log.error("Spring容器状态异常，无法注销Bean: {}", beanName, e);
            } catch (Exception e) {
                log.error("注销动态Controller失败: {}", beanName, e);
            }
        } else {
            log.warn("尝试注销不存在的动态Controller: {}", beanName);
        }
    }

    /**
     * 编译动态类
     */
    private Class<?> compileDynamicClass(String className, String sourceCode) throws Exception {
        DynamicCompiler compiler = new DynamicCompiler();
        compiler.addSource(className, sourceCode).build();
        return compiler.getClassLoader().loadClass(className);
    }

    /**
     * 使用BeanDefinition方式注册Bean（支持构造函数注入）
     */
    private void registerSpringBean(String beanName, Class<?> clazz) {
        if (!(applicationContext instanceof GenericApplicationContext)) {
            throw new IllegalStateException("ApplicationContext必须是GenericApplicationContext类型才能动态注册Bean");
        }

        GenericApplicationContext context = (GenericApplicationContext) applicationContext;

        // 创建BeanDefinition
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(clazz);
        beanDefinition.setScope(BeanDefinition.SCOPE_SINGLETON);
        beanDefinition.setRole(BeanDefinition.ROLE_APPLICATION);
        beanDefinition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_CONSTRUCTOR);

        // 注册BeanDefinition
        context.registerBeanDefinition(beanName, beanDefinition);

        log.debug("Spring Bean注册成功: {} -> {}", beanName, clazz.getName());
    }

    /**
     * 刷新RequestMapping映射
     */
    private void refreshRequestMappings(String beanName) {
        try {
            Object bean = applicationContext.getBean(beanName);
            Class<?> beanClass = bean.getClass();

            // 遍历所有方法，为标注了@RequestMapping的方法创建映射
            Method[] methods = beanClass.getDeclaredMethods();
            int registeredCount = 0;

            for (Method method : methods) {
                RequestMapping requestMapping = AnnotatedElementUtils.findMergedAnnotation(method, RequestMapping.class);
                if (requestMapping != null) {
                    // 创建RequestMappingInfo
                    RequestMappingInfo mappingInfo = createRequestMappingInfo(requestMapping);

                    // 注册映射
                    requestMappingHandlerMapping.registerMapping(mappingInfo, bean, method);

                    registeredCount++;
                    log.debug("注册RequestMapping: {} -> {}", mappingInfo.getPathPatternsCondition(), method.getName());
                }
            }

            log.info("RequestMapping映射刷新成功: {} ({}个方法)", beanName, registeredCount);
        } catch (Exception e) {
            log.error("刷新RequestMapping映射失败: {}", beanName, e);
        }
    }

    /**
     * 创建RequestMappingInfo
     */
    private RequestMappingInfo createRequestMappingInfo(RequestMapping requestMapping) {
        // 获取配置信息
        RequestMappingInfo.BuilderConfiguration config = requestMappingHandlerMapping.getBuilderConfiguration();

        // 创建RequestMappingInfo
        RequestMappingInfo.Builder builder = RequestMappingInfo.paths(requestMapping.value())
                .methods(requestMapping.method())
                .params(requestMapping.params())
                .headers(requestMapping.headers())
                .consumes(requestMapping.consumes())
                .produces(requestMapping.produces())
                .options(config);

        return builder.build();
    }

    /**
     * 注销RequestMapping映射
     */
    private void unregisterRequestMappings(String beanName) {
        try {
            Object bean = applicationContext.getBean(beanName);
            Class<?> beanClass = bean.getClass();

            // 遍历所有方法，注销RequestMapping映射
            Method[] methods = beanClass.getDeclaredMethods();
            int unregisteredCount = 0;

            for (Method method : methods) {
                RequestMapping requestMapping = AnnotatedElementUtils.findMergedAnnotation(method, RequestMapping.class);
                if (requestMapping != null) {
                    // 创建RequestMappingInfo（用于注销）
                    RequestMappingInfo mappingInfo = createRequestMappingInfo(requestMapping);

                    // 注销映射
                    requestMappingHandlerMapping.unregisterMapping(mappingInfo);

                    unregisteredCount++;
                    log.debug("注销RequestMapping: {} -> {}", mappingInfo.getPathPatternsCondition(), method.getName());
                }
            }

            log.info("RequestMapping映射注销成功: {} ({}个方法)", beanName, unregisteredCount);
        } catch (Exception e) {
            log.debug("注销RequestMapping映射时出现异常: {}", beanName, e);
        }
    }

    /**
     * 验证Bean清理状态
     */
    private void validateCleanupStatus(String beanName, DefaultListableBeanFactory beanFactory) {
        boolean definitionExists = beanFactory.containsBeanDefinition(beanName);
        boolean singletonExists = beanFactory.containsSingleton(beanName);

        if (definitionExists || singletonExists) {
            log.warn("Bean清理验证失败: {} (定义存在: {}, 实例存在: {})",
                    beanName, definitionExists, singletonExists);
        } else {
            log.debug("Bean清理验证成功: {} 已完全清理", beanName);
        }
    }

    /**
     * 清理失败注册的残留状态
     */
    private void cleanupFailedRegistration(String beanName) {
        try {
            registeredBeans.remove(beanName);
        } catch (Exception e) {
            log.debug("清理失败注册状态时出现异常: {}", beanName, e);
        }
    }
}
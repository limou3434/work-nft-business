package cn.com.edtechhub.nftbase.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Spring 上下文描述类，方便手动操作 Bean TODO：想给这个工具类换个名字
 *
 * @author limou3434
 */
@Component
public class SpringContextHolder implements ApplicationContextAware {

    /**
     * Spring 应用上下文
     */
    private static ApplicationContext applicationContext;

    /**
     * 设置当前 Spring 上下文到静态成员变量中
     *
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextHolder.applicationContext = applicationContext;
    }

    /**
     * 从 Spring 上下文中获取对应名称的 Bean
     *
     * @param name
     * @return
     */
    public static Object getBean(String name) {
        return applicationContext.getBean(name);
    }

    /**
     * 从 Spring 上下文中获取对应类名的 Bean
     *
     * @param name
     * @param <T>
     * @return
     */
    public static <T> T getBean(Class<T> name) {
        return applicationContext.getBean(name);
    }

}

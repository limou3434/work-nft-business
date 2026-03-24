package cn.com.edtechhub.nftrpc;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RPC 配置，其中 @EnableDubbo 触发 Dubbo 的自动配置：
 * - 读取项目中的 Dubbo 配置（如 application.yaml 里的 dubbo.registry.address、dubbo.protocol.name 等）
 * - 初始化 Dubbo 核心组件（注册中心客户端、服务暴露器、服务引用代理器）
 * - 扫描 @DubboService 注解的服务并暴露，处理 @DubboReference 注解的引用
 *
 * @author limou3434
 */
@Configuration // 声明这是一个 Spring 配置类，内部将提前注册好各种需要使用到的 Bean
@EnableDubbo // 启用 Dubbo 的自动配置，并且内部自动导入 Dubbo 需要的核心 Bean
public class RpcConfiguration {

    /**
     * 在 Dubbo 核心 Bean 之外，额外注册自定义的切面 Bean，用于对 Dubbo 服务调用做统一增强
     *
     * @return
     */
    @Bean
    public FacadeAspect facadeAspect() {
        return new FacadeAspect();
    }

}

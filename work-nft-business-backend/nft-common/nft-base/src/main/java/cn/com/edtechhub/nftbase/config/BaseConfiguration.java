package cn.com.edtechhub.nftbase.config;

import cn.com.edtechhub.nftbase.utils.SpringContextHolder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * nft-turbo-base 的基本配置类
 *
 * @author limou3434
 */
@Configuration
public class BaseConfiguration {

    @Bean
    public SpringContextHolder springContextHolder() {
        return new SpringContextHolder();
    }

}

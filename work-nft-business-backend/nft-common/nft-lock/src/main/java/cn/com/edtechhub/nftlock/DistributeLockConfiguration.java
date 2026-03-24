package cn.com.edtechhub.nftlock;

import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * TODO：这个类的名字又是怎么回事？？怎么不延续其他类的写法...
 * @author limou3434
 */
@Configuration
public class DistributeLockConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public DistributeLockAspect distributeLockAspect(RedissonClient redisson){
        return new DistributeLockAspect(redisson);
    }

}

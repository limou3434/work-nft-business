package cn.com.edtechhub.nftuser.infrastructure;

// import cn.hollis.nft.turbo.api.chain.service.ChainFacadeService;
// import org.apache.dubbo.config.annotation.DubboReference;
// import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
// import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 提前在这里配置好多个 Dubbo 服务，后续本模块需要使用则直接使用 @Autowired 即可快速获取，不必写过长的 @DubboReference
 *
 * @author limou3434
 */
@Configuration
public class UserDubboConfiguration {

    // TODO：等待解开注释
//    @DubboReference(version = "1.0.0")
//    private ChainFacadeService chainFacadeService;
//
//    @Bean
//    @ConditionalOnMissingBean(name = "chainFacadeService")
//    public ChainFacadeService chainFacadeService() {
//        return chainFacadeService;
//    }

}

package cn.com.edtechhub.nftinventory.infrastructure;

import org.springframework.context.annotation.Configuration;

/**
 * 提前在这里配置好多个 Dubbo 服务，后续本模块需要使用则直接使用 @Autowired 即可快速获取，不必写过长的 @DubboReference
 *
 * @author limou3434
 */
@Configuration
public class InventoryDubboConfiguration {

    // 暂时没有使用服务

}

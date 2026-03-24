package cn.com.edtechhub.nftcache;

import com.alicp.jetcache.anno.config.EnableMethodCache;
import org.springframework.context.annotation.Configuration;

/**
 * 缓存配置，利用 JetCache 可以统一缓存的特效统一操作缓存中间件
 * 这里简单说下 JetCache 的常见使用方法：
 * - @Cached —— 查/增
 * - @CacheUpdate —— 修改
 * - @CacheInvalidate —— 删除
 *
 * @author limou3434
 */
@Configuration
@EnableMethodCache(basePackages = "cn.com.edtechhub") // 扫描项目中所有被 @Cacheable 标记的地方
public class CacheConfiguration {
}

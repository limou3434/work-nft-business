package cn.com.edtechhub.nftbase.utils;

import com.google.common.base.CaseFormat;

/**
 * 和 SpringContextHolder 搭配使用，这个工具可以快速拼接出
 *
 * @author limou3434
 */
public class BeanNameUtils {

    /**
     * 把一个策略名结合服务名转换成 beanName（如 WEN_CHANG ，ChainService -> wenChangChainService）
     *
     * @param strategyName
     * @param serviceName
     * @return
     */
    public static String getBeanName(String strategyName, String serviceName) {
        // TODO：可以在这里加上一些检测步骤，让代码更加健壮一些

        // 这里使用了 Google Guava 工具类
        return CaseFormat
                .UPPER_UNDERSCORE // 指定原格式为 “大写下划线”
                .converterTo(CaseFormat.LOWER_CAMEL) // 指定目标格式为 “小写驼峰”
                .convert(strategyName) + serviceName // 执行格式转换，并且拼接一个固定服务名
        ;
    }

}

package cn.com.edtechhub.nftbase.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 人名币转化
 *
 * @author limou3434
 */
public class MoneyUtils {

    // TODO：可以提供元转角和角转元的

    /**
     * 元转分
     *
     * @param number
     * @return
     */
    public static Long yuanToCent(BigDecimal number) {
        // 这里需要注意，并不是所有币种的元转分都是乘 100 的，比如日元就不是，所以，在考虑多币种情况下，需要使用一个单独的 Money 类来屏蔽掉这些币种的差异。
        return number.multiply(new BigDecimal("100")).longValue();
    }

    /**
     * 分转元
     *
     * @param number
     * @return
     */
    public static BigDecimal centToYuan(Long number) {
        if (number == null) {
            return null;
        }
        return new BigDecimal(number.toString()).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
    }
}

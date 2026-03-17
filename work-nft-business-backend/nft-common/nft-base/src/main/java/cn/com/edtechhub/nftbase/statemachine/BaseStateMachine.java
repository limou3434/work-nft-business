package cn.com.edtechhub.nftbase.statemachine;

import cn.com.edtechhub.nftbase.exception.BizErrorCode;
import cn.com.edtechhub.nftbase.exception.BizException;
import com.google.common.base.Joiner;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * 状态机抽象类
 *
 * @author limou3434
 */
public class BaseStateMachine<STATE, EVENT> implements StateMachine<STATE, EVENT> {

    /**
     * 状态转移规则存储容器
     * Key 存储 “旧状态_事件” 拼接的字符串 -> Value 泛型，代表转移后的目标状态
     * 示例：Key: "UNPAID_PAY" -> Value: PAID，表示 “未支付_支付” 转移为 “已支付”
     */
    private final Map<String, STATE> stateTransitions = Maps.newHashMap();

    /**
     * 添加状态转移规则，每条规则都是 “旧状态 + 事件 -> 新状态”，这些状态会存储在 stateTransitions 中
     *
     * @param origin 旧状态
     * @param event  事件
     * @param target 新状态
     */
    protected void putTransition(STATE origin, EVENT event, STATE target) {
        // 根据用户自定义的规则，插入一条状态转移规则
        stateTransitions.put(
                Joiner // Guava 库的字符串拼接工具
                        .on("_") // 指定拼接符为 “_”
                        .join(origin, event), // 拼接字符串
                target
        );
    }

    /**
     * 状态转移方法
     *
     * @param origin 旧状态
     * @param event  事件
     * @return 新状态
     */
    @Override
    public STATE transition(STATE origin, EVENT event) {
        // 根据用户的旧状态和事件来取出状态转移规则所对应的新状态
        STATE target = stateTransitions.get(Joiner.on("_").join(origin, event));

        // 如果规则中没有对应的新状态，则抛出异常
        if (target == null) {
            throw new BizException("origin: " + origin + ", event: " + event, BizErrorCode.STATE_MACHINE_TRANSITION_FAILED);
        }

        // 返回新状态
        return target;
    }

}

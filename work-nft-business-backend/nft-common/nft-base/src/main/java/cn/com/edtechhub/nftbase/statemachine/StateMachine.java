package cn.com.edtechhub.nftbase.statemachine;

/**
 * 作为状态机必须要实现的行为，一个状态机通常包含以下几个要素：
 * - 状态（States）：代表系统可能处于的各种状态，例如 "已下单"、"已支付"、"已发货" 等
 * - 事件（Events）：触发状态转换的事件，例如 "下单"、"支付"、"发货" 等
 * - 转换（Transitions）：定义状态之间的转换规则，即在某个事件发生时，系统从一个状态转换到另一个状态的规则
 * - 动作（Actions）：在状态转换发生时执行的操作或行为
 * 状态机的实现，有很多种方式，可以用一些状态机的框架，如 Spring StateMachine，也可以用状态模式，也可以自己封装一个工具类都行
 * 我们之所以需要一个状态机，是因为我们需要避免在不严格控制下发生错误的状态转换，导致业务逻辑出错
 *
 * @author limou3434
 */
public interface StateMachine<STATE, EVENT> {

    /**
     * 状态机转换方法
     *
     * @param origin 旧状态
     * @param event  事件
     * @return 新状态
     */
    STATE transition(STATE origin, EVENT event);

}


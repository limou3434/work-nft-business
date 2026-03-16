package cn.com.edtechhub.nftbase.utils;

import cn.com.edtechhub.nftbase.exception.BizErrorCode;
import cn.com.edtechhub.nftbase.exception.BizException;
import cn.com.edtechhub.nftbase.exception.RemoteCallErrorCode;
import cn.com.edtechhub.nftbase.exception.RemoteCallException;
import com.alibaba.fastjson2.JSON;
import com.google.common.collect.ImmutableSet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.util.StopWatch;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Function;

/**
 * 远程调用的包装工具，核心作用不是直接发送请求，而是对远程调用的过程进行标准化包装（主要针对 Dubbo 中的微服务远程调用接口）：
 * - 包含耗时统计、请求 / 响应日志打印
 * - 响应结果合法性校验（成功状态 / 响应码）
 * - 异常统一处理
 * 最终返回远程调用的结果或抛出标准化异常
 * 这里给一个使用的例子：
 * OrderResponse orderResponse = RemoteCallWrapper.call(
 * req -> orderFacadeService.create(req),
 * orderCreateRequest,
 * "createOrder"
 * );
 *
 * @author limou3434
 */
@Slf4j
public class RemoteCallWrapper {

    /**
     * 用于校验 “响应布尔能否成功” 的方法名集合，后续会通过反射调用该方法判断响应布尔
     */
    private static ImmutableSet<String> SUCCESS_CHECK_METHOD = ImmutableSet.of("isSuccess", "isSucceeded", "getSuccess");

    /**
     * 用于校验 “响应编码能否获取” 的方法名集合，后续会通过反射调用该方法获取响应编码
     */
    private static ImmutableSet<String> SUCCESS_CODE_METHOD = ImmutableSet.of("getResponseCode");

    /**
     * 合法的响应编码集合，只要响应编码在这个集合中，就进一步认为调用成功
     */
    private static ImmutableSet<String> SUCCESS_CODE = ImmutableSet.of("SUCCESS", "DUPLICATE", "DUPLICATED_REQUEST");

    /**
     * 封装远程调用的全流程
     *
     * @param function          实际的远程调用逻辑（比如调用 HTTP 接口、RPC）
     * @param request           远程调用的请求参数
     * @param requestName       日志中标识请求的名称（如"OrderCreateRequest"）
     * @param checkResponse     是否需要校验响应状态（isSuccess 等方法）
     * @param checkResponseCode 是否需要校验响应码值（判断响应码是否在合法集合中）
     * @param <T>               请求远程调用参数的类型
     * @param <R>               远程调用返回结果的类型
     * @return 远程调用的结果
     */
    public static <T, R> R call(Function<T, R> function, T request, String requestName, boolean checkResponse, boolean checkResponseCode) {
        // 初始化 Spring StopWatch，用于精准统计远程调用耗时
        StopWatch stopWatch = new StopWatch();

        // 声明响应对象，存储远程调用的返回结果
        R response = null;

        // 封装的过程
        try {
            // 统计执行远程调用所需要使用的时间
            stopWatch.start(); // 开始计时
            response = function.apply(request);
            stopWatch.stop(); // 结束计时

            // 是否需要检查响应的“响应布尔”，如果需要则进行检查
            if (checkResponse) {
                Assert.notNull(response, RemoteCallErrorCode.REMOTE_CALL_RESPONSE_IS_NULL.name()); // 断言响应为非空，否则抛出 IllegalArgumentException 异常
                if (!isResponseValid(response)) {
                    String message = String.format(
                            "远程调用 %s 结果中的 “响应布尔” 无效，请求为 %s, 响应为 %s",
                            requestName,
                            JSON.toJSONString(request),
                            JSON.toJSONString(response)
                    );
                    throw new RemoteCallException(message, RemoteCallErrorCode.REMOTE_CALL_RESPONSE_IS_FAILED);
                }
            }

            // 是否需要检查响应的“响应编码”，如果需要则进行检查
            if (checkResponseCode) {
                Assert.notNull(response, RemoteCallErrorCode.REMOTE_CALL_RESPONSE_IS_NULL.name()); // 断言响应为非空，否则抛出 IllegalArgumentException 异常
                if (!isResponseCodeValid(response)) {
                    String message = String.format(
                            "远程调用 %s 结果中的 “响应编码” 无效，请求为 %s, 响应为 %s",
                            requestName,
                            JSON.toJSONString(request),
                            JSON.toJSONString(response)
                    );
                    throw new RemoteCallException(message, RemoteCallErrorCode.REMOTE_CALL_RESPONSE_IS_FAILED);
                }
            }
        } catch (IllegalAccessException e) { // 断言出错时会抛出 IllegalAccessException
            throw new BizException(String.format(
                    "有断言失言: %s", e.getMessage()),
                    BizErrorCode.WAITING_FOR_DEFINITION
            );
        } catch (InvocationTargetException e) { // 反射调用方法出错时会抛出 InvocationTargetException
            throw new BizException(
                    String.format("有反射方法失败: %s", e.getMessage()),
                    BizErrorCode.WAITING_FOR_DEFINITION
            );
        } catch (Throwable e) {
            throw new BizException("触发兜底机制，抛出了未预料到的异常", BizErrorCode.WAITING_FOR_DEFINITION);
        } finally {
            if (log.isInfoEnabled()) {
                log.info("## [调用名称}: {}, ## [总共耗时]: {}ms, ## [请求报文]: {}, ## [响应报文]: {}",
                        requestName,
                        stopWatch.getTotalTimeMillis(),
                        JSON.toJSONString(request),
                        JSON.toJSONString(response)
                );
            }
        }

        // 最后还有把远程调用结果做一次返回
        return response;
    }
    public static <T, R> R call(Function<T, R> function, T request, String requestName, boolean checkResponse) {
        return call(function, request, requestName, checkResponse, false);
    }
    public static <T, R> R call(Function<T, R> function, T request, String requestName) {
        return call(function, request, requestName, true, false);
    }
    public static <T, R> R call(Function<T, R> function, T request, boolean checkResponse, boolean checkResponseCode) {
        return call(function, request, request.getClass().getSimpleName(), checkResponse, checkResponseCode);
    }
    public static <T, R> R call(Function<T, R> function, T request, boolean checkResponse) {
        return call(function, request, request.getClass().getSimpleName(), checkResponse, false);
    }
    public static <T, R> R call(Function<T, R> function, T request) {
        return call(function, request, request.getClass().getSimpleName(), true, false);
    }

    /**
     * 检查响应的“响应布尔”
     *
     * @param response
     * @return
     * @param <R>
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    private static <R> boolean isResponseValid(R response) throws IllegalAccessException, InvocationTargetException {
        // 声明变量存储要找的方法
        Method successMethod = null;

        // 获取响应对象的所有公有方法（getMethods() 返回所有 public 方法，包括父类）
        Method[] methods = response.getClass().getMethods();

        // 遍历方法，匹配预设的 “成功状态方法名”（isSuccess/isSucceeded/getSuccess）
        for (Method method : methods) {
            String methodName = method.getName();
            if (SUCCESS_CHECK_METHOD.contains(methodName)) {
                successMethod = method;
                break;
            }
        }

        // 如果没找到任何成功状态方法，默认认为响应有效（兼容无状态的响应对象）
        if (successMethod == null) {
            return true;
        }

        // 反射调用找到的方法
        return (Boolean) successMethod.invoke(response); // 语法 Object result = 方法对象.invoke(目标对象, 方法参数 1, 方法参数 2...);
    }

    /**
     * 检查响应的“响应编码”
     *
     * @param response
     * @return
     * @param <R>
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    private static <R> boolean isResponseCodeValid(R response) throws IllegalAccessException, InvocationTargetException {
        // 声明变量存储要找的方法
        Method successMethod = null;

        // 遍历方法，匹配预设的 “成功状态方法名”（isSuccess/isSucceeded/getSuccess）
        Method[] methods = response.getClass().getMethods();
        for (Method method : methods) {
            String methodName = method.getName();
            if (SUCCESS_CODE_METHOD.contains(methodName)) {
                successMethod = method;
                break;
            }
        }

        // 如果没找到任何成功状态方法，默认认为响应有效（兼容无状态的响应对象）
        if (successMethod == null) {
            return true;
        }

        // 反射调用找到的方法
        return SUCCESS_CODE.contains(successMethod.invoke(response)); // 语法 Object result = 方法对象.invoke(目标对象, 方法参数 1, 方法参数 2...);
    }

}

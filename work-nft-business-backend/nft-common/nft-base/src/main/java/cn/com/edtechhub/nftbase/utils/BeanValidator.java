package cn.com.edtechhub.nftbase.utils;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;
import org.hibernate.validator.HibernateValidator;

import java.util.Set;

/**
 * 基于 JSR 380（Bean Validation）规范 + Hibernate Validator 实现的参数校验工具，用法如下：
 * // 规则 1：用户名不能为空，长度 2-10
 * // @NotBlank(message = "用户名不能为空")
 * // @Size(min = 2, max = 10, message = "用户名长度必须在 2-10 个字符之间")
 * // private String username;
 * // 规则 2：手机号格式必须正确
 * // @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式错误（需以 13/14/15/17/18/19 开头）")
 * // private String phone;
 * // 规则 3：密码不能为空，且长度 ≥ 6
 * // @NotBlank(message = "密码不能为空")
 * // @Size(min = 6, message = "密码长度不能少于6位")
 * // private String password;
 * // 如果字段是私有的必须保证有 getter 让校验器检测到
 *
 * @author limou3434
 */
public class BeanValidator {

    /**
     * 初始化并且获取到唯一的静态校验器
     */
    private static final Validator validator = Validation
            .byProvider(HibernateValidator.class) // 指定使用 Hibernate Validator 作为校验器实现
            .configure() // 开启配置模式
            .failFast(true) // 设置为快速失败模式，发现第一个错误就停止校验
            .buildValidatorFactory() // 构建校验器工厂
            .getValidator();  // 获取全局唯一的 Validator 实例

    /**
     * 使用校验器来做校验
     *
     * @param object object
     * @param groups groups
     */
    public static void validateObject(Object object, Class<?>... groups) throws ValidationException {
        // 传入待校验对象 + 校验分组（groups 可选参数），返回所有校验失败的结果，这里被校验的对象中，会被 Hibernate Validator 内部解析出使用的注解，然后会根据注解上书写的规则进行校验
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object, groups);

        // 判断是否有校验失败的结果，只取第一个失败结果的错误信息，因为 failFast(true) 保证只有一个
        if (constraintViolations.stream().findFirst().isPresent()) {
            throw new ValidationException(constraintViolations
                    .stream()
                    .findFirst()
                    .get()
                    .getMessage()
            );
        }
    }

}

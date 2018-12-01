package com.lian.miaosha_7.validator;

/**
 * @Author:L1ANN
 * @Description:
 * @Date:Created in 下午4:10 2018/10/28
 * @Modified By:
 */

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {IsMobileValidator.class})
public @interface IsMobile {
    /**
     * true：参数不为空
     * false：参数可以为空
     */
    boolean required() default true;

    /**
     * 校验失败时返回的字符串
     */
    String message() default "手机号码格式有误";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

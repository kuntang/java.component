package com.alipay.springmvc.udf.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Created by tangkun.tk on 2016/8/30.
 * 自定义一个验证参数不为空的注解 @NotEmpty
 */
@Documented
@Target({ElementType.FIELD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NotEmptyValidator.class)
public @interface NotEmpty {
    String message() default "not_empty";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

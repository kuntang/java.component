package com.alipay.springmvc.udf.annotation;



import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Created by tangkun.tk on 2016/8/30.
 */
public class NotEmptyValidator implements ConstraintValidator<NotEmpty,String>{

    @Override
    public void initialize(NotEmpty notEmpty) {

    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return !StringUtils.isEmpty(s);
    }
}

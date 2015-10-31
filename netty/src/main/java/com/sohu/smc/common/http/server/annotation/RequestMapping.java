package com.sohu.smc.common.http.server.annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created with IntelliJ IDEA.
 * User: tangkun
 * Date: 12-9-18
 * Time: 上午11:02
 * To change this template use File | Settings | File Templates.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.TYPE})
public @interface RequestMapping {

    String[] value() default {};
   ContentType contentType() default ContentType.TEXT_PLAIN;

}

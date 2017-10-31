package cn.com.lanlyc.base.persistence;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 映射为主键
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PrimaryKeyMapper {
    
    /** 数据库中的别名 */
    String value() default "";
    
    /** 数据库中的类型 */
    Class<?> type() default java.lang.Object.class;
}

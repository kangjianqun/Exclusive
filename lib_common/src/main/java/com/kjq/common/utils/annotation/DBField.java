package com.kjq.common.utils.annotation;




import com.kjq.common.utils.data.Constant;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 代表数据库字段名注解
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DBField {
    /**
     * 字段名
     * @return 字段名
     */
    String columnName() default "";

    String type() default Constant.DB.T_STRING;//字段类型

    /**
     * 得到值的类型
     * @return 值的类型
     */
    Class valueType() default String.class;
    Class observableFieldType()default String.class;
}

package com.kjq.common.ui.designs.title.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>标题注解</p>
 *
 * 将注解放到Activity 或者Fragment 类名上，在加载中会自动注入属性
 * @author 康建群 948182974---->>>2018/9/10 15:04
 * @version 1.1.2
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AnnTitle {
    /**
     *  标题的文本内容
     *  将注解绑定到类上面，用于自动得到内容
     * @return 文本
     */
    String titleTxt() default "";

    /**
     * 是否显示标题右边的内容
     * @return 是否
     */
    boolean isShowRight() default false;
}

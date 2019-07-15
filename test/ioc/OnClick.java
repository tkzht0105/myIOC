package com.test.ioc;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 *
 * @Target(ElementType.FIELD) 代表Annotation的位置 FIELD属性  TYPE 类上 CONSTRUCTOR构造函数上
 *
 */
//@Target(ElementType.FIELD) 代表Annotation的位置 FIELD属性  TYPE 类上 CONSTRUCTOR构造函数上
@Target(ElementType.METHOD)
//@Retention(RetentionPolicy.RUNTIME)  什么时候生效 CLASS 编译时  RUNTIME 运行时 SOURCE 编码资源
@Retention(RetentionPolicy.RUNTIME)
public @interface OnClick {
    //--> @ViewById(R.id.xxx);
    int[] value();
}

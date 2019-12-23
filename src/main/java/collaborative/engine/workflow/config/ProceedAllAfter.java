package collaborative.engine.workflow.config;

import collaborative.engine.workflow.Work;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author XyParaCrim
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ProceedAllAfter {

    Class<? extends Work>[] value() default {};

    String[] names() default {};
}

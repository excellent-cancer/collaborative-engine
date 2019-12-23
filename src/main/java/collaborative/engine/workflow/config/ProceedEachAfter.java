package collaborative.engine.workflow.config;

import collaborative.engine.workflow.Work;
import collaborative.engine.workflow.WorkSlot;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.WeakHashMap;

/**
 * @author XyParaCrim
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ProceedEachAfter {

    Class<? extends Work>[] value() default {};

    Class<? extends Work.WorkSlot<? extends Work>>[] slots() default {};
}

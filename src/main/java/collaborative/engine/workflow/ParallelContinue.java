package collaborative.engine.workflow;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author XyParaCrim
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ParallelContinue {
    Class<? extends Work>[] value() default {};

    Class<? extends Work.WorkSlot<? extends Work>>[] name() default {};
}

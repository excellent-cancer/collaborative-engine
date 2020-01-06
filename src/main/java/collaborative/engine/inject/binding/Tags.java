package collaborative.engine.inject.binding;

import java.lang.annotation.Annotation;
import java.util.Objects;

public final class Tags {

    // Sign injection point

    public interface PointedTag {

    }

    private static class DefaultPointedTag implements PointedTag {
    }

    private static class NamedPointedTag implements PointedTag {
        private final String name;

        public NamedPointedTag(String name) {
            this.name = name;
        }
    }

    private static class AnnotatedPointedTag implements PointedTag {
        private final Annotation annotation;

        public AnnotatedPointedTag(Annotation annotation) {
            this.annotation = annotation;
        }
    }

    private static final PointedTag DEFAULT_POINTED_TAG = new DefaultPointedTag();

    public static PointedTag defaultTag() {
        return DEFAULT_POINTED_TAG;
    }

    public static PointedTag namedTag(String name) {
        return new NamedPointedTag(Objects.requireNonNull(name));
    }

    public static PointedTag annotatedTag(Annotation annotation) {
        return new AnnotatedPointedTag(Objects.requireNonNull(annotation));
    }

    // Sign injection type

    public interface SourceTag {

        boolean isSingleton();
    }

    public static class InstanceTag implements SourceTag {

        private final Object instance;

        private InstanceTag(Object instance) {
            this.instance = instance;
        }

        @Override
        public boolean isSingleton() {
            return true;
        }
    }

    public static class ConstructorTag implements SourceTag {

        private final Class<?> implementation;
        private final boolean isSingleton;

        private ConstructorTag(Class<?> implementation, boolean isSingleton) {
            this.implementation = implementation;
            this.isSingleton = isSingleton;
        }

        @Override
        public boolean isSingleton() {
            return false;
        }
    }

    public static SourceTag constructorTag(boolean isSingleton) {
        return new ConstructorTag(null, isSingleton);
    }

    public static SourceTag constructorTag(Class<?> implementation, boolean isSingleton) {
        return new ConstructorTag(implementation, isSingleton);
    }

    public static SourceTag instanceTag(Object instance) {
        return new InstanceTag(instance);
    }
}

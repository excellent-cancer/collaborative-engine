package collaborative.engine.inject.module;

import collaborative.engine.inject.Binder;
import collaborative.engine.inject.Injector;
import collaborative.engine.inject.binding.Binding;
import collaborative.engine.inject.binding.Bindings;
import collaborative.engine.inject.binding.Key;

import java.lang.annotation.Annotation;
import java.util.*;

public class ModuleBinder implements Binder {

    private final List<ModuleOptional<?>> moduleOptionalList;

    public ModuleBinder() {
        this.moduleOptionalList = new LinkedList<>();
    }

    @Override
    public <V> ModuleOptional<V> bind(Class<V> type) {
        Objects.requireNonNull(type);
        ModuleOptional<V> moduleOptional = new ModuleOptional<>(type);
        moduleOptionalList.add(moduleOptional);
        return moduleOptional;
    }

    public Injector newInjector() {
        return new ModuleInjector(moduleOptionalList);
    }

    private static class ModuleInjector implements Injector {

        private final Map<Key<?>, Binding> bindingMap;

        public ModuleInjector(List<ModuleOptional<?>> moduleOptionalList) {
            Map<Key<?>, Binding> defaults = new HashMap<>();
            for (ModuleOptional<?> moduleOptional : moduleOptionalList) {
                Binding binding = Bindings.of(
                        moduleOptional.type,
                        moduleOptional.pointedTag,
                        moduleOptional.sourceTag
                );
                if (defaults.containsKey(binding.key())) {
                    // TODO
                    throw new RuntimeException();
                } else {
                    defaults.put(binding.key(), binding);
                }
            }
            this.bindingMap = Collections.unmodifiableMap(defaults);
        }

        @Override
        public <T> T instance(Class<T> type) {
            return (T) bindingMap.get(Key.get(type)).supplier().get();
        }

        @Override
        public <T> T instance(Class<T> type, String name) {
            return (T) bindingMap.get(Key.get(type, name)).supplier().get();
        }

        @Override
        public <T> T instance(Class<T> type, Annotation annotation) {
            return (T) bindingMap.get(Key.get(type, annotation)).supplier().get();
        }
    }
}

package collaborative.engine.inject.module;

import collaborative.engine.inject.Injector;
import collaborative.engine.inject.binding.Binding;
import collaborative.engine.inject.binding.Bindings;
import collaborative.engine.inject.binding.Key;
import collaborative.engine.inject.binding.Keys;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class ModuleInjector implements Injector {

    private final Map<Key<?>, Binding<?>> bindingMap;

    public ModuleInjector(List<ModuleOptional<?>> moduleOptionalList) {
        Map<Key<?>, Binding<?>> defaults = new HashMap<>();
        for (ModuleOptional<?> moduleOptional : moduleOptionalList) {
            Binding<?> binding = Bindings.of(
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
        return instance(Keys.get(type));
    }

    @Override
    public <T> T instance(Class<T> type, String name) {
        return instance(Keys.get(type, name));
    }

    @Override
    public <T> T instance(Class<T> type, Annotation annotation) {
        return instance(Keys.get(type, annotation));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T instance(Key<T> key) {
        return (T) bindingMap.getOrDefault(key, Bindings.nullable()).instance();
    }
}

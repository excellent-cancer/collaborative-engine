package collaborative.engine.inject.module;

import collaborative.engine.inject.Binder;
import collaborative.engine.inject.Injector;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

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
}

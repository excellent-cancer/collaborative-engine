package collaborative.engine.inject;

import collaborative.engine.inject.module.ModuleInjectRuler;

import java.util.Objects;
import java.util.function.Consumer;

public final class InjectSupport {

    public static Injector newInjector(InjectRuler<? extends RuleCollector<?>> ruler) {
        return null;
    }

    public static Injector newInjectorByModule(Consumer<ModuleInjectRuler> configurate) {
        Objects.requireNonNull(configurate);

        return null;
    }

}

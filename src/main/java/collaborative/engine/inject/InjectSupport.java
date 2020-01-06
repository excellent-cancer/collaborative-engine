package collaborative.engine.inject;

import collaborative.engine.inject.module.ModuleBinder;

import java.util.Objects;
import java.util.function.Consumer;

public final class InjectSupport {

    public static Injector newInjectorByModule(Consumer<ModuleBinder> configurate) {
        Objects.requireNonNull(configurate);
        ModuleBinder moduleBinder = new ModuleBinder();
        configurate.accept(moduleBinder);
        return moduleBinder.newInjector();
    }

    public static Injector newInjectorByModule(Consumer<ModuleBinder> configurate1, Consumer<ModuleBinder> configurate2) {
        Objects.requireNonNull(configurate1);
        Objects.requireNonNull(configurate2);

        ModuleBinder moduleBinder = new ModuleBinder();
        configurate1.accept(moduleBinder);
        configurate2.accept(moduleBinder);

        return moduleBinder.newInjector();
    }

    public static Injector newInjectorByModule(Consumer<ModuleBinder> configurate1, Consumer<ModuleBinder> configurate2, Consumer<ModuleBinder>... others) {
        Objects.requireNonNull(configurate1);
        Objects.requireNonNull(configurate2);

        ModuleBinder moduleBinder = new ModuleBinder();
        configurate1.accept(moduleBinder);
        configurate2.accept(moduleBinder);

        if (others.length > 0) {
            for (Consumer<ModuleBinder> configurate : others) {
                Objects.requireNonNull(configurate);
                configurate.accept(moduleBinder);
            }
        }
        return moduleBinder.newInjector();
    }

}

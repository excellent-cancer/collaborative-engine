package collaborative.engine.inject;

import collaborative.engine.inject.module.ModuleBinder;

import java.util.Objects;
import java.util.function.Consumer;


/**
 * 仿照Guice写的一个类似的依赖注入框架。这个类提供所有的依赖注入的功能。
 *
 * @author XyParaCrim
 */
public final class InjectSupport {

    public static Injector createModuleInjector(Consumer<Binder> configurate) {
        Objects.requireNonNull(configurate);
        ModuleBinder moduleBinder = new ModuleBinder();
        configurate.accept(moduleBinder);
        return moduleBinder.newInjector();
    }

    public static Injector createModuleInjector(Consumer<Binder> configurate1, Consumer<Binder> configurate2) {
        Objects.requireNonNull(configurate1);
        Objects.requireNonNull(configurate2);

        ModuleBinder moduleBinder = new ModuleBinder();
        configurate1.accept(moduleBinder);
        configurate2.accept(moduleBinder);

        return moduleBinder.newInjector();
    }

    public static Injector createModuleInjector(Consumer<Binder> configurate1, Consumer<Binder> configurate2, Consumer<Binder>... others) {
        Objects.requireNonNull(configurate1);
        Objects.requireNonNull(configurate2);

        ModuleBinder moduleBinder = new ModuleBinder();
        configurate1.accept(moduleBinder);
        configurate2.accept(moduleBinder);

        if (others.length > 0) {
            for (Consumer<Binder> configurate : others) {
                Objects.requireNonNull(configurate);
                configurate.accept(moduleBinder);
            }
        }
        return moduleBinder.newInjector();
    }
}

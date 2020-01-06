package collaborative.engine.inject.binding;

import java.util.function.Supplier;

public abstract class Binding {

    public abstract Key<?> key();

    public abstract <T> Supplier<T> supplier();

}

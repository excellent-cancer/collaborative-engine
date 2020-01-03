package collaborative.engine.inject;

import pact.annotation.NotNull;

@SuppressWarnings("unused")
public interface RuleCollector<T> {

    RuleCollector<T> asInstance(@NotNull T instance);

    RuleCollector<T> asDefault();

    RuleCollector<T> of(@NotNull Class<? extends T> implementation);

    RuleCollector<T> factoryMode();
}

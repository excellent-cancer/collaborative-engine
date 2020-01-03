package collaborative.engine.inject.module;

import collaborative.engine.inject.InjectRuler;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class ModuleInjectRuler implements InjectRuler<ModuleInjectRuleCollector<?>> {

    private final List<Rule> rules;

    public ModuleInjectRuler() {
        this.rules = new LinkedList<>();
    }

    @Override
    public <V> ModuleInjectRuleCollector<?> rule(Class<V> type) {
        Objects.requireNonNull(type);
        return new ModuleInjectRuleCollector<>(RuleKey.of(type), rules);
    }
}

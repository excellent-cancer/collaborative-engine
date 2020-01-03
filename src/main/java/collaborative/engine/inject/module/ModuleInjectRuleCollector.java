package collaborative.engine.inject.module;

import collaborative.engine.inject.RuleCollector;
import pact.annotation.NotNull;

import java.util.List;

@SuppressWarnings("unused")
public class ModuleInjectRuleCollector<T> implements RuleCollector<T> {

    private final List<Rule> rules;
    private final RuleKey<T> ruleKey;
    private final Rule rule;

    public ModuleInjectRuleCollector(RuleKey<T> ruleKey, List<Rule> rules) {
        this.rules = rules;
        this.ruleKey = ruleKey;
        this.rule = new Rule<>(ruleKey);

    }

    @Override
    public RuleCollector<T> asInstance(@NotNull T instance) {
        return null;
    }

    @Override
    public RuleCollector<T> asDefault() {
        return null;
    }

    @Override
    public RuleCollector<T> of(@NotNull Class<? extends T> implementation) {
        return null;
    }

    @Override
    public RuleCollector<T> factoryMode() {
        return null;
    }
}

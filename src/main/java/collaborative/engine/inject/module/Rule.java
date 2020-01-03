package collaborative.engine.inject.module;

class Rule<T> {

    private final RuleKey<T> ruleKey;

    public Rule(RuleKey<T> ruleKey) {
        this.ruleKey = ruleKey;
    }
}

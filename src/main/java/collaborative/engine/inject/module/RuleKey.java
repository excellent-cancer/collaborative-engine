package collaborative.engine.inject.module;

class RuleKey<T> {

    private final Class<T> type;

    public RuleKey(Class<T> type) {
        this.type = type;
    }

    static <T> RuleKey<T> of(Class<T> type) {
        return new RuleKey<>(type);
    }
}

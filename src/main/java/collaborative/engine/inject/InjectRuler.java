package collaborative.engine.inject;

public interface InjectRuler<T extends RuleCollector<?>> {

    <V> T rule(Class<V> type);

}

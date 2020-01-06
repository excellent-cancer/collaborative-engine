package collaborative.engine.inject.module;

import collaborative.engine.inject.BindingOptional;
import collaborative.engine.inject.binding.Tags;
import pact.annotation.NotNull;

import java.lang.annotation.Annotation;

import static collaborative.engine.inject.BindingOptional.PointedOptional;

@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class ModuleOptional<T> implements BindingOptional<T>, PointedOptional {

    final Class<T> type;
    Tags.SourceTag sourceTag;
    Tags.PointedTag pointedTag;

    public ModuleOptional(Class<T> type) {
        this.type = type;
        this.asSingleton().defaulted();
    }

    // Binding options

    @Override
    public PointedOptional asSingleton() {
        this.sourceTag = Tags.constructorTag(true);
        return this;
    }

    @Override
    public PointedOptional asSingleton(@NotNull T instance) {
        this.sourceTag = Tags.instanceTag(instance);
        return this;
    }

    @Override
    public PointedOptional asSingleton(@NotNull Class<? extends T> implementation) {
        this.sourceTag = Tags.constructorTag(implementation, true);
        return this;
    }

    @Override
    public PointedOptional asFactory() {
        this.sourceTag = Tags.constructorTag(false);
        return this;
    }

    @Override
    public PointedOptional asFactory(@NotNull Class<? extends T> implementation) {
        this.sourceTag = Tags.constructorTag(implementation, false);
        return this;
    }

    // Pointed options

    @Override
    public void defaulted() {
        this.pointedTag = Tags.defaultTag();
    }

    @Override
    public void named(@NotNull String name) {
        this.pointedTag = Tags.namedTag(name);
    }

    @Override
    public void annotated(Annotation annotation) {
        this.pointedTag = Tags.annotatedTag(annotation);
    }
}

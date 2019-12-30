package collaborative.engine;

import collaborative.engine.workflow.Work;
import collaborative.engine.workflow.config.Proceed;
import collaborative.engine.workflow.config.ProceedAllAfter;
import collaborative.engine.workflow.config.ProceedEachAfter;
import pact.support.ReflectSupport;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class CarcinogenFactor {

    public final CollaborativeCarcinogen carcinogen;

    private final List<Method> startWork = new LinkedList<>();

    private final Map<Class<? extends Work>, List<Method>> defaultSlotsMap = new HashMap<>();

    private final Map<Class<? extends Work.WorkSlot<? extends Work>>, List<Method>> nameSlotsMap = new HashMap<>();

    public CarcinogenFactor(CollaborativeCarcinogen carcinogen) {
        this.carcinogen = carcinogen;
        ReflectSupport.walkMethod(carcinogen, CarcinogenFactor::legalMethod, method -> {
            ReflectSupport.ifAnnotated(method, Proceed.class, this::resolveProceed);
            ReflectSupport.ifAnnotated(method, ProceedAllAfter.class, this::resolveProceedAllAfter);
            ReflectSupport.ifAnnotated(method, ProceedEachAfter.class, this::resolveProceedEachAfter);
        });
    }

    public List<Work> defaultWork(Class<? extends Work> workClass) {
        List<Method> methods = defaultSlotsMap.getOrDefault(workClass, Collections.emptyList());
        return methods.stream().map(this::newWork).collect(Collectors.toList());
    }

    public List<Work> startWork() {
        return startWork.stream().map(this::newWork).collect(Collectors.toList());
    }

    private void resolveProceed(Proceed proceed, Method method) {
        startWork.add(method);
    }

    private void resolveProceedEachAfter(ProceedEachAfter proceedEachAfter, Method method) {
        for (Class<? extends Work> defaultSlot : proceedEachAfter.value()) {
            List<Method> methods = defaultSlotsMap.computeIfAbsent(defaultSlot, k -> new LinkedList<>());
            methods.add(method);
        }

        for (Class<? extends Work.WorkSlot<? extends Work>> nameSlot : proceedEachAfter.slots()) {
            List<Method> methods = nameSlotsMap.computeIfAbsent(nameSlot, k -> new LinkedList<>());
            methods.add(method);
        }
    }

    private void resolveProceedAllAfter(ProceedAllAfter proceedAllAfter, Method method) {
        // throw new UnsupportedOperationException();
    }

    private Work newWork(Method method) {
        Work work = null;
        if (method != null) {
            try {
                work = (Work) method.invoke(carcinogen);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }

        return work;
    }

    public static CarcinogenFactor analysis(CollaborativeCarcinogen carcinogen) {
        return new CarcinogenFactor(carcinogen);
    }

    private static boolean legalMethod(Method method) {
        return method.getReturnType().isAssignableFrom(Work.class);
    }

}

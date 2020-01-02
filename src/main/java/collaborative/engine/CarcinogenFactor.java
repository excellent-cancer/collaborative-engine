package collaborative.engine;

import collaborative.engine.workflow.Work;
import collaborative.engine.workflow.config.Proceed;
import collaborative.engine.workflow.config.ProceedAllAfter;
import collaborative.engine.workflow.config.ProceedEachAfter;
import pact.support.ReflectSupport;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static pact.support.ReflectSupport.walkMethod;

public class CarcinogenFactor {

    public final CollaborativeCarcinogen carcinogen;

    private final List<Method> startWork;

    private final Map<Class<? extends Work>, List<Method>> defaultSlotsMap;

    private final Map<Class<? extends Work.WorkSlot<? extends Work>>, List<Method>> nameSlotsMap;

    private CarcinogenFactor(CollaborativeCarcinogen carcinogen) {
        this.carcinogen = carcinogen;
        this.startWork = new LinkedList<>();
        this.defaultSlotsMap = new HashMap<>();
        this.nameSlotsMap = new HashMap<>();
        walkMethod(carcinogen, CarcinogenFactor::legalMethod, method -> {
            ReflectSupport.ifAnnotated(method, Proceed.class, this::resolveProceed);
            ReflectSupport.ifAnnotated(method, ProceedAllAfter.class, this::resolveProceedAllAfter);
            ReflectSupport.ifAnnotated(method, ProceedEachAfter.class, this::resolveProceedEachAfter);
        });
    }

    // Resolve annotation methods

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

    private Work newWorkInstance(Method method) {
        try {
            return (Work) method.invoke(carcinogen);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean legalMethod(Method method) {
        return method.getReturnType().isAssignableFrom(Work.class);
    }

    // Public methods

    public void handleStartProceedWork(Consumer<Work> handler) {
        Objects.requireNonNull(handler);
        for (Method method : startWork) {
            handler.accept(newWorkInstance(method));
        }
    }

    public void handleDefaultProceedWork(Class<? extends Work> workClass, Consumer<Work> handler) {
        Objects.requireNonNull(handler);
        for (Method method : defaultSlotsMap.getOrDefault(workClass, Collections.emptyList())) {
            handler.accept(newWorkInstance(method));
        }
    }

    public void handleSlotProceedWork(Class<? extends Work.WorkSlot<? extends Work>> workSlot, Consumer<Work> handler) {
        Objects.requireNonNull(handler);
        for (Method method : nameSlotsMap.getOrDefault(workSlot, Collections.emptyList())) {
            handler.accept(newWorkInstance(method));
        }
    }

    public List<Work> startProceedWork() {
        return startWork.stream().map(this::newWorkInstance).collect(Collectors.toUnmodifiableList());
    }

    public List<Work> defaultProceedWork(Class<? extends Work> workClass) {
        return defaultSlotsMap.containsKey(workClass) ?
                defaultSlotsMap.get(workClass).stream().map(this::newWorkInstance).collect(Collectors.toUnmodifiableList()) :
                Collections.emptyList();
    }

    public List<Work> slotProceedWork(Class<? extends Work.WorkSlot<? extends Work>> workSlot) {
        return nameSlotsMap.containsKey(workSlot) ?
                nameSlotsMap.get(workSlot).stream().map(this::newWorkInstance).collect(Collectors.toUnmodifiableList()) :
                Collections.emptyList();
    }

    public static CarcinogenFactor analysis(CollaborativeCarcinogen carcinogen) {
        return new CarcinogenFactor(carcinogen);
    }
}

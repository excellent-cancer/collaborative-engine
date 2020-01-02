package collaborative.engine.workflow.run;

import collaborative.engine.workflow.DispatcherWork;
import collaborative.engine.workflow.WorkProcessing;
import collaborative.engine.workflow.Workflow;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

public class DispatcherWorkExecutor implements ExecutorService {

    private final ExecutorService executorService = Executors.newFixedThreadPool(1);

    private final ConcurrentLinkedQueue<DispatcherWork> dispatcherWorks = new ConcurrentLinkedQueue<>();

    public void submit(DispatcherWork work, Workflow workflow, WorkProcessing processing) {
        submit(() -> work.proceed(processing, workflow));
    }

/*    private final class Dispatcher implements Runnable {

        @Override
        public void run() {

        }
    }*/

    @Override
    public void shutdown() {
        executorService.shutdown();
    }

    @NotNull
    @Override
    public List<Runnable> shutdownNow() {
        return executorService.shutdownNow();
    }

    @Override
    public boolean isShutdown() {
        return executorService.isShutdown();
    }

    @Override
    public boolean isTerminated() {
        return executorService.isTerminated();
    }

    @Override
    public boolean awaitTermination(long timeout, @NotNull TimeUnit unit) throws InterruptedException {
        return executorService.awaitTermination(timeout, unit);
    }

    @NotNull
    @Override
    public <T> Future<T> submit(@NotNull Callable<T> task) {
        return executorService.submit(task);
    }

    @NotNull
    @Override
    public <T> Future<T> submit(@NotNull Runnable task, T result) {
        return executorService.submit(task, result);
    }

    @NotNull
    @Override
    public Future<?> submit(@NotNull Runnable task) {
        return executorService.submit(task);
    }

    @NotNull
    @Override
    public <T> List<Future<T>> invokeAll(@NotNull Collection<? extends Callable<T>> tasks) throws InterruptedException {
        return executorService.invokeAll(tasks);
    }

    @NotNull
    @Override
    public <T> List<Future<T>> invokeAll(@NotNull Collection<? extends Callable<T>> tasks, long timeout, @NotNull TimeUnit unit) throws InterruptedException {
        return executorService.invokeAll(tasks, timeout, unit);
    }

    @NotNull
    @Override
    public <T> T invokeAny(@NotNull Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
        return executorService.invokeAny(tasks);
    }

    @Override
    public <T> T invokeAny(@NotNull Collection<? extends Callable<T>> tasks, long timeout, @NotNull TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return executorService.invokeAny(tasks, timeout, unit);
    }

    @Override
    public void execute(@NotNull Runnable command) {
        executorService.execute(command);
    }
}

package collaborative.engine;

import collaborative.engine.disposable.Disposable;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

public abstract class DisposableService implements Disposable {

    protected final ServiceLifecycle lifecycle = new ServiceLifecycle();

    protected static class ServiceLifecycle {
        enum State {
            INITIALIZED,
            STARTED,
            CLOSED,
            TERMINATING;
        }

        private static AtomicReferenceFieldUpdater<ServiceLifecycle, State> STATE_UPDATER =
                AtomicReferenceFieldUpdater.newUpdater(ServiceLifecycle.class, State.class, "state");

        private volatile State state = State.INITIALIZED;

        public boolean isStarted() {
            return state == State.STARTED;
        }

        public boolean isClose() {
            return state == State.CLOSED;
        }

        public boolean isTerminaling() {
            return state == State.TERMINATING;
        }

        public boolean tryStarted() {
            for ( ; ; ) {
                State hereState = state;
                if (hereState == State.INITIALIZED) {
                    if (STATE_UPDATER.compareAndSet(this, hereState, State.STARTED)) {
                        return true;
                    }
                    continue;
                }

                if (hereState == State.STARTED) {
                    return false;
                }

                throw new IllegalStateException("Can't make state of Service started when it's not initialized");
            }
        }

        public boolean tryTerminaling() {
            for ( ; ; ) {
                State hereState = state;
                if (hereState == State.INITIALIZED || hereState == State.TERMINATING) {
                    return false;
                }

                if (hereState == State.CLOSED) {
                    throw new IllegalStateException("Can't make state of Service terminaling when it's closed");
                }

                if (hereState == State.STARTED && STATE_UPDATER.compareAndSet(this, hereState, State.TERMINATING)) {
                    return true;
                }
            }
        }

        public boolean tryClosed() {
            for ( ; ; ) {
                State hereState = state;
                if (hereState == State.INITIALIZED || hereState == State.CLOSED) {
                    return false;
                }

                if (STATE_UPDATER.compareAndSet(this, state, State.CLOSED)) {
                    return true;
                }
            }
        }
    }
}

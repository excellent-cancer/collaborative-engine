package collaborative.engine;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

public class ServiceLifecycle {

    enum State {
        INITIALIZED,
        STARTED,
        CLOSED,
        TERMINATING;
    }

    private static final AtomicReferenceFieldUpdater<ServiceLifecycle, State> STATE_UPDATER =
            AtomicReferenceFieldUpdater.newUpdater(ServiceLifecycle.class, State.class, "state");

    private volatile State state = State.INITIALIZED;

    public boolean isStarted() {
        return state == State.STARTED;
    }

    public boolean isClose() {
        return state == State.CLOSED;
    }

    public boolean isTerminating() {
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

    public boolean tryTerminating() {
        for ( ; ; ) {
            State hereState = state;
            if (hereState == State.INITIALIZED || hereState == State.TERMINATING) {
                return false;
            }

            if (hereState == State.CLOSED) {
                throw new IllegalStateException("Can't make state of Service terminating when it's closed");
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

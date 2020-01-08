package collaborative.engine.core.command;

import collaborative.engine.core.Collaboratory;

import java.util.concurrent.Callable;

public class StatusCommand implements Callable<Void> {

    public StatusCommand(Collaboratory collaboratory) {
    }

    @Override
    public Void call() throws Exception {
        return null;
    }
}

package collaborative.engine.core.command;

import collaborative.engine.core.Collaboratory;

import java.util.concurrent.Callable;

public class HistoryCommand implements Callable<Void> {

    public HistoryCommand(Collaboratory collaboratory) {
    }

    @Override
    public Void call() throws Exception {
        return null;
    }
}

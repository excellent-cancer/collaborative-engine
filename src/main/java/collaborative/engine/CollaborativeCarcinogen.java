package collaborative.engine;

import collaborative.engine.inject.Binder;
import collaborative.engine.parameterize.ParameterTable;
import collaborative.engine.service.ServiceBinder;
import pact.annotation.NotNull;

public interface CollaborativeCarcinogen extends Carcinogen {

    void configurateModules(@NotNull Binder moduleBinder);

    void configurateServices(@NotNull ServiceBinder serviceBinder);

    void configurateParameterTable(@NotNull ParameterTable parameterTable);
}

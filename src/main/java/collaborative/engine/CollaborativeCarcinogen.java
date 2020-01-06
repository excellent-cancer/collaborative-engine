package collaborative.engine;

import collaborative.engine.inject.module.ModuleBinder;
import collaborative.engine.parameterize.ParameterTable;
import collaborative.engine.service.ServiceBinder;
import pact.annotation.NotNull;

public interface CollaborativeCarcinogen extends Carcinogen {

    void configurateModules(@NotNull ModuleBinder moduleBinder);

    void configurateServices(@NotNull ServiceBinder serviceBinder);

    void configurateParameterTable(@NotNull ParameterTable parameterTable);
}

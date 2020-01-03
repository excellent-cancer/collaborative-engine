package collaborative.engine;

import collaborative.engine.parameterize.ParameterTable;
import collaborative.engine.service.ServiceBinder;
import pact.annotation.NotNull;

public interface CollaborativeCarcinogen extends Carcinogen {

    void configurateParameterTable(@NotNull ParameterTable parameterTable);

    void configurateServices(@NotNull ServiceBinder serviceBinder);
}

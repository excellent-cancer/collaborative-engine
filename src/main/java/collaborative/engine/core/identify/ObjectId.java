package collaborative.engine.core.identify;

import java.io.File;

public interface ObjectId {

    File location();

    File groupLocation();

    void used();

    ObjectId unmodifiable();
}
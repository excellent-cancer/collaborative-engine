package collaborative.engine.core.errors;

import java.io.File;
import java.io.IOException;

public class WorkSiteNotFoundException extends IOException {

    public WorkSiteNotFoundException(File location) {
        super("workSite not found: " + location);
    }
}

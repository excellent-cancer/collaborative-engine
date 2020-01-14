package collaborative.engine.core;

import collaborative.engine.core.identify.Identifier;
import collaborative.engine.core.identify.SimpleIdentifier;

public class ContentSystem {

    public static Identifier newIdentifier() {
        return new SimpleIdentifier();
    }

}

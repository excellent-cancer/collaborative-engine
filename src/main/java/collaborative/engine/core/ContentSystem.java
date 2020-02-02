package collaborative.engine.core;

import collaborative.engine.core.identify.Identifier;
import collaborative.engine.core.identify.SimpleIdentifier;

/**
 * 管理所有的依赖关系
 *
 * @author XyParaCrim
 */
public class ContentSystem {

    public Identifier newIdentifier() {
        return new SimpleIdentifier();
    }

}

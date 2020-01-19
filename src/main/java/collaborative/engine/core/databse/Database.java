package collaborative.engine.core.databse;

import collaborative.engine.core.identify.Identifier;
import collaborative.engine.core.identify.ObjectId;
import pact.support.CollectionsSupport;

import java.io.File;
import java.util.Set;

/**
 * @author XyParaCrim
 * @temp 是否需要一个database接口或者store接口
 */
public interface Database extends AutoCloseable {

    @Override
    default void close() throws Exception {
        // TODO
    }

    // Working with data

    boolean contains(ObjectId objectId);

    default InsertResult insert(File file, InsertOption... options) {
        return insert(file, CollectionsSupport.asSet(options));
    }

    InsertResult insert(File file, Set<InsertOption> options);

    default RemoveResult remove(ObjectId objectId, RemoveOption... options) {
        return remove(objectId, CollectionsSupport.asSet(options));
    }

    RemoveResult remove(ObjectId objectId, Set<RemoveOption> options);

    // Misc

    File location();

    Identifier identifier();
}

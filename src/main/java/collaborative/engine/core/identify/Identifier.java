package collaborative.engine.core.identify;


/**
 * Id生成器，它的实现需要满足一下几个目标：
 * 1.从此生成器生成的Id实例，可以控制自身的生命周期并且可以与所生产的生成器通讯
 * 2.希望可以设置自定义的缓存策略
 * 3.提供通用的缓存API
 *
 * @author XyParaCrim
 */
public interface Identifier {

    ObjectId newId();

    // boolean contains(ObjectId objectId);

    // void add(ObjectId objectId);

    ObjectId toObjectId(String name);
}

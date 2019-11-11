package collaborative.engine.content;

public interface ContentProvider {

    ContentInfo contentInfo();

    interface ContentInfo {
        int rows();

        int columns();
    }
}

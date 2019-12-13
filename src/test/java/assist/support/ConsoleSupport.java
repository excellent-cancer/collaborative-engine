package assist.support;

public final class ConsoleSupport {

    private static ThreadLocal<Integer> lastOutputLength = ThreadLocal.withInitial(() -> 0);

    public static void rollback() {
        int size = lastOutputLength.get();
        lastOutputLength.set(0);
        System.out.println("\b".repeat(Math.max(0, size)));
    }

    public static void rollback(String message) {
        rollback();
        println(message);
    }

    public static void println(String message) {
        System.out.println(message);
        lastOutputLength.set(message.length());
    }

    public static void printExceptionRoutinely(Exception e, String preMessage) {
        println(preMessage);
        e.printStackTrace();
    }
}

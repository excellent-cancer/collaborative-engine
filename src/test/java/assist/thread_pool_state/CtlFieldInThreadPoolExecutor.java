package assist.thread_pool_state;

import java.io.IOException;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 简单地记录{@link ThreadPoolExecutor}里的字段ctl.用于控制线程池
 * 的状态.
 *
 * @author XyParaCrim
 */
public class CtlFieldInThreadPoolExecutor {

    private static final int COUNT_BITS = Integer.SIZE - 3;
    // 00011111111111111111111111111111
    private static final int COUNT_MASK = (1 << COUNT_BITS) - 1;

    // 在最高三位记录一下几个状态，其依次增大
    // 11100000000000000000000000000000
    private static final int RUNNING = -1 << COUNT_BITS;
    // 00000000000000000000000000000000
    private static final int SHUTDOWN = 0 << COUNT_BITS;
    // 00100000000000000000000000000000
    private static final int STOP = 1 << COUNT_BITS;
    // 01000000000000000000000000000000
    private static final int TIDYING = 2 << COUNT_BITS;
    // 01100000000000000000000000000000
    private static final int TERMINATED = 3 << COUNT_BITS;

    private static int runStateOf(int c) {
        return c & ~COUNT_MASK;
    }

    private static int workerCountOf(int c) {
        return c & COUNT_MASK;
    }

    private static int ctlOf(int rs, int wc) {
        return rs | wc;
    }


    public static void main(String[] args) throws IOException {
        recordStateWithCtl();
        howToKeepaliveWithCheap();
        service.submit(() -> System.out.println("sdas"));
        service.submit(() -> System.out.println("sdas"));
        service.submit(() -> System.out.println("sdas"));

        while (true) {
            if (System.in.read() > 0) {
                count.countDown();
                // service.submit(() -> System.out.println("sdas"));
            }
        }
    }

    public static void recordStateWithCtl() {
        // 假设ctl就是ThreadPoolExecutor实例中的ctl字段
        AtomicInteger ctl;

        // 初始运行态: 11100000000000000000000000000000
        // 高三位记录状态
        ctl = new AtomicInteger(
                ctlOf(RUNNING, 0)
        );
        System.out.println(Integer.toBinaryString(ctl.get()));

        // 获取当前work数量，即11100000000000000000000000000000 & 00010000000000000000000000000000
        // 即取低位的数值，低位表示worker数量
        int works = workerCountOf(ctl.get());
        System.out.println(works);
        System.out.println(workerCountOf(11));
    }

    final static ExecutorService service = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(1));

    // final static ExecutorService service = Executors.newSingleThreadExecutor();
    final static CountDownLatch count = new CountDownLatch(1);

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(count::countDown));
    }

    public static void howToKeepaliveWithCheap() {
        service.submit(() -> {
            try {
                System.out.println("aaa");
                count.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }
}

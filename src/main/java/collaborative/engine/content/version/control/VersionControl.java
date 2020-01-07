package collaborative.engine.content.version.control;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;

public class VersionControl {


    private volatile Edition head;

    private volatile Edition tail;

    private static class Edition {
        volatile Edition prev;

        volatile Edition next;
    }

    public void commit() {
        Edition newEdition = new Edition();

        for (Edition currHead = head, currWalk = currHead, currPrev; ; ) {
            // 向前探寻两次
            if ((currPrev = currWalk.prev) != null &&
                    (currPrev = (currWalk = currPrev).prev) != null) {
                // 因为head节点已经有prev节点了
                // 但是不确定head字段是否被修改了
                // 若未修改，则很可能其他线程在修改，则取此时探测到的currPrev继续
                // 反之，取新的head继续操作
                currWalk = currHead != (currHead = head) ? currHead : currPrev;
            } else {
                NEXT.set(newEdition, currWalk);
                if (PREV.compareAndSet(currWalk, null, newEdition)) {
                    if (currWalk != currHead) {
                        HEAD.weakCompareAndSet(this, head, newEdition);
                    }
                    return;
                }
            }
        }
    }

    private Edition lastest() {
        for (Edition currHead, currWalk, currPrev; ; ) {

            currHead = currWalk = head;

            while ((currPrev = currWalk.prev) != null &&
                    (currPrev = (currWalk = currPrev).prev) != null) {
                currHead = currHead != (currHead = head) ? currHead : currPrev;
            }

            if (currWalk == currHead || HEAD.weakCompareAndSet(this, currHead, currWalk)) {
                return currWalk;
            }
        }
    }

    private Edition start() {
        // currTail 表示当前tail字段
        // currWalk 通过探测的真正的tail节点
        // currNext 临时变量，记录next节点
        for (Edition currTail, currWalk, currNext; ; ) {

            currTail = currWalk = tail;

            while ((currNext = currWalk.next) != null &&
                    (currNext = (currWalk = currNext).next) != null) {
                currTail = currTail != (currTail = tail) ? currTail : currNext;
            }

            if (currWalk == currTail || TAIL.weakCompareAndSet(this, currTail, currWalk)) {
                return currWalk;
            }
        }
    }

    private static final VarHandle HEAD;
    private static final VarHandle TAIL;

    private static final VarHandle PREV;
    private static final VarHandle NEXT;

    static {
        try {
            MethodHandles.Lookup lookup = MethodHandles.lookup();

            HEAD = lookup.findVarHandle(VersionControl.class, "head", Edition.class);
            TAIL = lookup.findVarHandle(VersionControl.class, "tail", Edition.class);

            PREV = lookup.findVarHandle(Edition.class, "prev", Edition.class);
            NEXT = lookup.findVarHandle(Edition.class, "next", Edition.class);
        } catch (ReflectiveOperationException e) {
            throw new ExceptionInInitializerError(e);
        }
    }
}

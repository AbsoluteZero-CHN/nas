package cn.noload.nas.service.concurrent;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class AtomicTest {

    private final static int LOOP = 100000;

    public static void main(String[] args) throws Exception {
        atomicInteger();
    }

    private static void atomicInteger() throws InterruptedException {
        AtomicInteger atomicInteger = new AtomicInteger();
        CountDownLatch count = new CountDownLatch(LOOP);
        for(int i = 0; i < LOOP; i++) {
            new Thread(() -> {
                atomicInteger.addAndGet(1);
                count.countDown();
            }).start();
        }
        count.await();
        System.out.println(atomicInteger.get());
    }
}

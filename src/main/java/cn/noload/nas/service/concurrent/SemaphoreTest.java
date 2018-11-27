package cn.noload.nas.service.concurrent;


import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.stream.IntStream;

// 同时执行的线程数控制
public class SemaphoreTest {
    private static final int THREAD_COUNT = 30;
    private static final Semaphore semaphore = new Semaphore(10);

    public static void main(String[] args) {
        IntStream.range(0, THREAD_COUNT).forEach((i) -> {
            new Thread(() -> {
                try {
                    semaphore.acquire();
                    System.out.println("save data");
                    Thread.sleep(1000);
                    semaphore.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        });
    }
}

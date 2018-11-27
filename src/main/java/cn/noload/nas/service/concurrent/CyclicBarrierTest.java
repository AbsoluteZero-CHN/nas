package cn.noload.nas.service.concurrent;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

// 同步屏障
public class CyclicBarrierTest {
    public static void main(String[] args) throws Exception {
        base();
//        more();
    }

    static void base() {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(2);
        new Thread(() -> {
            try {
                cyclicBarrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
            // 等所有线程都准备好了, 才会执行之后的代码
            System.out.println(1);
        }).start();
        try {
            cyclicBarrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
        // 等所有线程都准备好了, 才会执行之后的代码
        System.out.println(2);
    }

    static void more() {
        // 所有线程准备好以后, 优先执行构造器中的线程
        CyclicBarrier cyclicBarrier = new CyclicBarrier(2, new Sys());
        new Thread(() -> {
            try {
                cyclicBarrier.await();
                System.out.println(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        }).start();

        try {
            cyclicBarrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
        System.out.println(2);
    }

    static class Sys implements Runnable {

        @Override
        public void run() {
            System.out.println(3);
        }
    }
}

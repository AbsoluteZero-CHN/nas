package cn.noload.nas.service.concurrent;

import java.util.stream.IntStream;

public class Synchronized {

    public static void main(String[] args) {
        Lock lock0 = new Lock();
        Lock lock1 = new Lock();
        Lock lock2 = new Lock();
        Lock lock3 = new Lock();
        Lock lock4 = new Lock();
        Lock lock5 = new Lock();
        IntStream.range(0, 10).forEach(i -> {
            switch (i % 5) {
                case 0: new Thread(new Run(lock0), "Thread-" + i).start(); break;
                case 1: new Thread(new Run(lock1), "Thread-" + i).start(); break;
                case 2: new Thread(new Run(lock2), "Thread-" + i).start(); break;
                case 3: new Thread(new Run(lock3), "Thread-" + i).start(); break;
                case 4: new Thread(new Run(lock4), "Thread-" + i).start(); break;
                case 5: new Thread(new Run(lock5), "Thread-" + i).start(); break;
            }
        });
    }

    static class Run implements Runnable {

        private final Lock lock;

        public Run(Lock lock) {
            this.lock = lock;
        }

        @Override
        public void run() {
            lock.call();
        }
    }

    static class Lock {
        public void call() {
            synchronized (this) {
                try {
                    System.out.println(Thread.currentThread().getName());
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

package cn.noload.nas.service.concurrent;


import java.util.concurrent.Exchanger;
import java.util.concurrent.Executors;

// 线程间交换数据
public class ExchangerTest {
    private static final Exchanger<String> exchanger = new Exchanger();

    public static void main(String[] args) {
        new Thread(() -> {
            String a = "银行流水A";
            try {
                exchanger.exchange(a);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            String b = "银行流水B";
            try {
                String a = exchanger.exchange("");
                System.out.println("A和B数据是否一致：" + a.equals(b) + "，A录入的是：" + a + "，B录入是：" + b);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}

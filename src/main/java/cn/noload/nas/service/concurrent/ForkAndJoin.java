package cn.noload.nas.service.concurrent;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;

// 把大任务分成若干个小任务并行执行 (Fork
// 把若干的小任务结果返回, 以整合成大任务的结果 (Join
public class ForkAndJoin {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ForkJoinPool pool = new ForkJoinPool();
        CountTask task = new CountTask(1, 100000);
        Future<Integer> result = pool.submit(task);
        System.out.println(result.get());
    }
}
class CountTask extends RecursiveTask<Integer> {

    private final static int THRESHOLD = 2;
    private int start;
    private int end;

    public CountTask(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    protected Integer compute() {
        int sum = 0;
        boolean needCompute = (end - start) <= THRESHOLD;
        if(needCompute) {
            for(int i = start; i <= end; i++) {
                sum += i;
            }
        } else {
            int middle = (start + end) / 2;
            CountTask leftTask = new CountTask(start, middle);
            CountTask rightTask = new CountTask(middle + 1, end);
            leftTask.fork();
            rightTask.fork();
            sum = leftTask.join() + rightTask.join();
        }
        return sum;
    }
}

package demo;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author noear 2022/5/31 created
 */
public class RunAsyncTest {
    @Test
    public void test() throws Exception{
        ExecutorService executorService  = Executors.newCachedThreadPool();
        for (int i = 0; i < 100; i++) {
            int i2 = i;
            executorService.submit(() -> {
                try {
                    Thread.sleep(1000);
                }catch (Throwable e){

                }
                System.out.println(Thread.currentThread().getName() +":: " +i2);
            });
        }

        Thread.currentThread().join();
    }
}

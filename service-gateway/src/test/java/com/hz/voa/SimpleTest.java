package com.hz.voa;

import cn.hutool.http.HttpUtil;
import lombok.SneakyThrows;
import org.junit.Test;

import java.util.concurrent.*;

/**
 * 
 * @author rhb
 * @date 2026/1/5 16:31
 **/
public class SimpleTest {

    @Test
    @SneakyThrows
    public void qpsFlowType(){
        ThreadPoolExecutor tpe = new ThreadPoolExecutor(2, 2, 0, TimeUnit.SECONDS, new ArrayBlockingQueue<>(10), new ThreadPoolExecutor.CallerRunsPolicy());
        CompletionService<String> cs = new ExecutorCompletionService<>(tpe);
        for (int i =0 ; i<10; i++){
            cs.submit(()->{
                String body = HttpUtil.get("http://localhost:10000/order/mock?param=hello");
                return body;
            });
        }
        for (int i = 0; i<10 ; i++){
            String s = cs.take().get();
            System.out.println(s);
        }
        tpe.shutdown();
    }

}

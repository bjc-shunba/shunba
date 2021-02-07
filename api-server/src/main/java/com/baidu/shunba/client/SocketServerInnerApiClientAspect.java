package com.baidu.shunba.client;

import com.baidu.shunba.annotation.ApiModelDetails;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 注解拦截对socket的请求, 使用线程池推送数据到socket
 */
@Aspect
@Component
@Slf4j
public class SocketServerInnerApiClientAspect {
    /**
     * 推送socket线程池
     */
    private final ExecutorService es = Executors.newFixedThreadPool(10);

    @Pointcut(value = "execution(* com.baidu.shunba.client.SocketServerInnerApiClient.*(..))")
    public void execute() {
    }

    @Around(value = "execute()")
    public void around(ProceedingJoinPoint joinPoint) {
        es.submit(new SocketMessageSender(joinPoint));
    }

    private class SocketMessageSender implements Runnable {
        private ProceedingJoinPoint joinPoint;

        public SocketMessageSender(ProceedingJoinPoint joinPoint) {
            this.joinPoint = joinPoint;
        }

        /**
         * 获取方法功能名称
         *
         * @param method
         * @return
         */
        private String getMethodDetails(Method method) {
            ApiModelDetails annotation = method.getAnnotation(ApiModelDetails.class);

            if (annotation == null) {
                return method.getName();
            }

            return annotation.function();
        }

        private String getArgs() {
            Object[] args = joinPoint.getArgs();

            if (args.length == 0) {
                return "";
            }

            return new Gson().toJson(args[0]);
        }

        @Override
        public void run() {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            String methodDetails = getMethodDetails(signature.getMethod());
            String args = getArgs();

            log.info(" ======== 启动 {}, 入参: {}", methodDetails, args);

            try {
                Object result = joinPoint.proceed();
                log.info(" ======== 执行 {}, 入参: [{}] 成功: {}", methodDetails, args, result);
            } catch (Throwable throwable) {
                log.error(" ======== 执行 {}, 入参: [{}] 失败, 失败原因: {}", methodDetails, args, throwable);
            }
        }
    }
}

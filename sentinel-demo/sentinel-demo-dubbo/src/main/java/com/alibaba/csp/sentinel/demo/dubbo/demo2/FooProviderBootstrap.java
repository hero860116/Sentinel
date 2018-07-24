package com.alibaba.csp.sentinel.demo.dubbo.demo2;

import com.alibaba.csp.sentinel.init.InitExecutor;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Please add the following VM arguments:
 * <pre>
 * -Djava.net.preferIPv4Stack=true
 * -Dcsp.sentinel.api.port=8720
 * -Dproject.name=dubbo-provider-demo
 * </pre>
 *
 * @author Eric Zhao
 */
public class FooProviderBootstrap {

    public static void main(String[] args) {
        InitExecutor.doInit();

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(ProviderConfiguration.class);
        context.refresh();

        System.out.println("Service provider is ready");
    }
}

package com.alibaba.csp.sentinel.demo.dubbo.consumer;

import com.alibaba.csp.sentinel.demo.dubbo.FooService;
import com.alibaba.dubbo.config.annotation.Reference;

/**
 * @author Eric Zhao
 */
public class FooServiceConsumer {

    @Reference(url = "dubbo://127.0.0.1:25758", timeout = 3000)
    private FooService fooService;

    public String sayHello(String name) {
        return fooService.sayHello(name);
    }

    public String doAnother() {
        return fooService.doAnother();
    }
}

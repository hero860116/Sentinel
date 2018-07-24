package com.alibaba.csp.sentinel.demo.dubbo.demo2;

import java.time.LocalDateTime;

import com.alibaba.csp.sentinel.demo.dubbo.FooService;
import com.alibaba.dubbo.config.annotation.Service;

/**
 * @author Eric Zhao
 */
@Service
public class FooServiceImpl implements FooService {

    @Override
    public String sayHello(String name) {
        return String.format("Hello, %s at %s", name, LocalDateTime.now());
    }

    @Override
    public String doAnother() {
        return LocalDateTime.now().toString();
    }
}

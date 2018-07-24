# Sentinel Dubbo Demo

Sentinel 提供了与 Dubbo 整合的模块 - Sentinel Dubbo Adapter，
主要包括针对 Service Provider 和 Service Consumer 实现的 Filter。使用时用户只需引入以下模块（以 Maven 为例）：

```xml
<dependency>
    <groupId>com.alibaba.csp</groupId>
    <artifactId>sentinel-dubbo-adapter</artifactId>
    <version>x.y.z</version>
</dependency>
```

引入此依赖后，Dubbo 会默认启动 Sentinel 支持。我们可以创建一个示例服务来看一下流量控制的生效情况，
具体代码请见 [sentinel-demo-dubbo](https://github.com/alibaba/Sentinel/tree/master/sentinel-demo/sentinel-demo-dubbo)。

假设我们已经定义了某个服务接口 `com.alibaba.csp.sentinel.demo.dubbo.FooService`，其中有一个方法 `sayHello(java.lang.String)`。我们希望给 Provider 端 `sayHello` 方法限制最大调用 QPS 为 10，则可以在 Provider 端进行如下设置：

```java
private static final String RES_KEY = "com.alibaba.csp.sentinel.demo.dubbo.FooService:sayHello(java.lang.String)";

private static void initFlowRule() {
    FlowRule flowRule = new FlowRule();
    flowRule.setResource(RES_KEY); // 资源名为 接口全限定名:方法签名
    flowRule.setCount(10); // 最大 QPS 为 10
    flowRule.setGrade(RuleConstant.FLOW_GRADE_QPS); // QPS 模式
    flowRule.setLimitApp("default");
    FlowRuleManager.loadRules(Collections.singletonList(flowRule)); // 设定限流规则
}
```

同时给 Consumer 端限制最大调用 QPS 为 12：

```java
private static final String RES_KEY = "com.alibaba.csp.sentinel.demo.dubbo.FooService:sayHello(java.lang.String)";

private static void initFlowRule() {
    FlowRule flowRule = new FlowRule();
    flowRule.setResource(RES_KEY); // 资源名为 接口全限定名:方法签名
    flowRule.setCount(12); // 最大 QPS 为 12
    flowRule.setGrade(RuleConstant.FLOW_GRADE_QPS); // QPS 模式
    flowRule.setLimitApp("default");
    FlowRuleManager.loadRules(Collections.singletonList(flowRule)); // 设定限流规则
}
```

测试的时候先启动 Provider 端，然后在 Consumer 端通过服务引用调用 15 次 sayHello 方法：

```java
// 注意：本机启动的时候要加 -Djava.net.preferIPv4Stack=true 参数，否则可能会报错
for (int i = 0; i < 15; i++) {
    try {
        String message = service.sayHello("Eric");
        // 调用成功
        System.out.println("Passed: " + message);
    } catch (SentinelRpcException ex) {
        // SentinelRpcException 代表请求被阻止 (Sentinel BlockException 的包装)
        System.out.println("Blocked");
    } catch (Exception ex) {
        // 其它异常，如 Dubbo 的 RpcException
        ex.printStackTrace();
    }
}
```

可以发现 15 次调用中有 10 次成功，5 次失败。其中 Provider 端有 5 次调用被限流，
在 Consumer 端有 3 次调用被限流。Consumer 端的调用结果：

```
Passed: Hello, Eric at 2018-07-24T09:06:53.244
Passed: Hello, Eric at 2018-07-24T09:06:53.256
Passed: Hello, Eric at 2018-07-24T09:06:53.258
Passed: Hello, Eric at 2018-07-24T09:06:53.260
Passed: Hello, Eric at 2018-07-24T09:06:53.261
Passed: Hello, Eric at 2018-07-24T09:06:53.263
Passed: Hello, Eric at 2018-07-24T09:06:53.264
Passed: Hello, Eric at 2018-07-24T09:06:53.266
Passed: Hello, Eric at 2018-07-24T09:06:53.267
Passed: Hello, Eric at 2018-07-24T09:06:53.269
com.alibaba.dubbo.rpc.RpcException: Failed to invoke remote method: sayHello, provider: dubbo://127.0.0.1:25758/com.alibaba.csp.sentinel.demo.dubbo.FooService?application=demo-consumer&dubbo=2.6.2&interface=com.alibaba.csp.sentinel.demo.dubbo.FooService&methods=sayHello&pid=34692&register.ip=xxx&side=consumer&timestamp=1532394402856, cause: com.alibaba.csp.sentinel.slots.block.SentinelRpcException: com.alibaba.csp.sentinel.slots.block.flow.FlowException
com.alibaba.csp.sentinel.slots.block.SentinelRpcException: com.alibaba.csp.sentinel.slots.block.flow.FlowException
Caused by: com.alibaba.csp.sentinel.slots.block.flow.FlowException

	at com.alibaba.dubbo.rpc.protocol.dubbo.DubboInvoker.doInvoke(DubboInvoker.java:100)
	at com.alibaba.dubbo.rpc.protocol.AbstractInvoker.invoke(AbstractInvoker.java:148)
	at com.alibaba.csp.sentinel.adapter.dubbo.SentinelDubboConsumerFilter.invoke(SentinelDubboConsumerFilter.java:39)
	at com.alibaba.dubbo.rpc.protocol.ProtocolFilterWrapper$1.invoke(ProtocolFilterWrapper.java:72)
	at com.alibaba.dubbo.monitor.support.MonitorFilter.invoke(MonitorFilter.java:75)
	at com.alibaba.dubbo.rpc.protocol.ProtocolFilterWrapper$1.invoke(ProtocolFilterWrapper.java:72)
	at com.alibaba.dubbo.rpc.protocol.dubbo.filter.FutureFilter.invoke(FutureFilter.java:54)
	at com.alibaba.dubbo.rpc.protocol.ProtocolFilterWrapper$1.invoke(ProtocolFilterWrapper.java:72)
	at com.alibaba.dubbo.rpc.filter.ConsumerContextFilter.invoke(ConsumerContextFilter.java:48)
	at com.alibaba.dubbo.rpc.protocol.ProtocolFilterWrapper$1.invoke(ProtocolFilterWrapper.java:72)
	at com.alibaba.dubbo.rpc.listener.ListenerInvokerWrapper.invoke(ListenerInvokerWrapper.java:77)
	at com.alibaba.dubbo.rpc.proxy.InvokerInvocationHandler.invoke(InvokerInvocationHandler.java:52)
	at com.alibaba.dubbo.common.bytecode.proxy0.sayHello(proxy0.java)
	at com.alibaba.csp.sentinel.demo.dubbo.consumer.FooServiceConsumer.sayHello(FooServiceConsumer.java:15)
	at com.alibaba.csp.sentinel.demo.dubbo.consumer.FooConsumerBootstrap.main(FooConsumerBootstrap.java:33)
Caused by: com.alibaba.dubbo.remoting.RemotingException: com.alibaba.csp.sentinel.slots.block.SentinelRpcException: com.alibaba.csp.sentinel.slots.block.flow.FlowException
com.alibaba.csp.sentinel.slots.block.SentinelRpcException: com.alibaba.csp.sentinel.slots.block.flow.FlowException
Caused by: com.alibaba.csp.sentinel.slots.block.flow.FlowException

	at com.alibaba.dubbo.remoting.exchange.support.DefaultFuture.returnFromResponse(DefaultFuture.java:222)
	at com.alibaba.dubbo.remoting.exchange.support.DefaultFuture.get(DefaultFuture.java:139)
	at com.alibaba.dubbo.remoting.exchange.support.DefaultFuture.get(DefaultFuture.java:112)
	at com.alibaba.dubbo.rpc.protocol.dubbo.DubboInvoker.doInvoke(DubboInvoker.java:95)
	... 14 more
com.alibaba.dubbo.rpc.RpcException: Failed to invoke remote method: sayHello, provider: dubbo://127.0.0.1:25758/com.alibaba.csp.sentinel.demo.dubbo.FooService?application=demo-consumer&dubbo=2.6.2&interface=com.alibaba.csp.sentinel.demo.dubbo.FooService&methods=sayHello&pid=34692&register.ip=xxx&side=consumer&timestamp=1532394402856, cause: com.alibaba.csp.sentinel.slots.block.SentinelRpcException: com.alibaba.csp.sentinel.slots.block.flow.FlowException
com.alibaba.csp.sentinel.slots.block.SentinelRpcException: com.alibaba.csp.sentinel.slots.block.flow.FlowException
Caused by: com.alibaba.csp.sentinel.slots.block.flow.FlowException

	at com.alibaba.dubbo.rpc.protocol.dubbo.DubboInvoker.doInvoke(DubboInvoker.java:100)
	at com.alibaba.dubbo.rpc.protocol.AbstractInvoker.invoke(AbstractInvoker.java:148)
	at com.alibaba.csp.sentinel.adapter.dubbo.SentinelDubboConsumerFilter.invoke(SentinelDubboConsumerFilter.java:39)
	at com.alibaba.dubbo.rpc.protocol.ProtocolFilterWrapper$1.invoke(ProtocolFilterWrapper.java:72)
	at com.alibaba.dubbo.monitor.support.MonitorFilter.invoke(MonitorFilter.java:75)
	at com.alibaba.dubbo.rpc.protocol.ProtocolFilterWrapper$1.invoke(ProtocolFilterWrapper.java:72)
	at com.alibaba.dubbo.rpc.protocol.dubbo.filter.FutureFilter.invoke(FutureFilter.java:54)
	at com.alibaba.dubbo.rpc.protocol.ProtocolFilterWrapper$1.invoke(ProtocolFilterWrapper.java:72)
	at com.alibaba.dubbo.rpc.filter.ConsumerContextFilter.invoke(ConsumerContextFilter.java:48)
	at com.alibaba.dubbo.rpc.protocol.ProtocolFilterWrapper$1.invoke(ProtocolFilterWrapper.java:72)
	at com.alibaba.dubbo.rpc.listener.ListenerInvokerWrapper.invoke(ListenerInvokerWrapper.java:77)
	at com.alibaba.dubbo.rpc.proxy.InvokerInvocationHandler.invoke(InvokerInvocationHandler.java:52)
	at com.alibaba.dubbo.common.bytecode.proxy0.sayHello(proxy0.java)
	at com.alibaba.csp.sentinel.demo.dubbo.consumer.FooServiceConsumer.sayHello(FooServiceConsumer.java:15)
	at com.alibaba.csp.sentinel.demo.dubbo.consumer.FooConsumerBootstrap.main(FooConsumerBootstrap.java:33)
Caused by: com.alibaba.dubbo.remoting.RemotingException: com.alibaba.csp.sentinel.slots.block.SentinelRpcException: com.alibaba.csp.sentinel.slots.block.flow.FlowException
com.alibaba.csp.sentinel.slots.block.SentinelRpcException: com.alibaba.csp.sentinel.slots.block.flow.FlowException
Caused by: com.alibaba.csp.sentinel.slots.block.flow.FlowException

	at com.alibaba.dubbo.remoting.exchange.support.DefaultFuture.returnFromResponse(DefaultFuture.java:222)
	at com.alibaba.dubbo.remoting.exchange.support.DefaultFuture.get(DefaultFuture.java:139)
	at com.alibaba.dubbo.remoting.exchange.support.DefaultFuture.get(DefaultFuture.java:112)
	at com.alibaba.dubbo.rpc.protocol.dubbo.DubboInvoker.doInvoke(DubboInvoker.java:95)
	... 14 more
Blocked
Blocked
Blocked
```

可以看到前 10 次的调用是成功的。第 11 和 12 次调用，Provider 端调用已触发限流，而 Consumer 端还没有触发限流，
因此抛了 Dubbo 的 `RpcException`，我们可以看到对应的 cause 是 `FlowException`，即服务端被限流。
第 13 到 15 次调用，Consumer 端调用也触发限流了，因此直接抛了 `SentinelRpcException`，输出 `Blocked`。

我们也可以在 Sentinel 的日志中看到相关的限流情况。拦截日志统一记录在 `~/logs/csp/sentinel-block.log` 中，
比如本次测试产生的限流（第一行是 Provider 端的限流记录，第二行是 Consumer 端的限流记录）：

```
2018-07-24 09:06:53|1|com.alibaba.csp.sentinel.demo.dubbo.FooService:sayHello(java.lang.String),FlowException,default,|2,0
2018-07-24 09:06:53|1|com.alibaba.csp.sentinel.demo.dubbo.FooService:sayHello(java.lang.String),FlowException,default,|3,0
```

Sentinel 还提供 API 用于获取实时的监控信息，对应文档见[此处](https://github.com/alibaba/Sentinel/wiki/%E5%AE%9E%E6%97%B6%E7%9B%91%E6%8E%A7)。
为了便于使用，Sentinel 还提供了一个控制台用于配置规则、查看监控、机器发现等功能。我们只需要按照 [Sentinel 控制台文档](https://github.com/alibaba/Sentinel/wiki/%E6%8E%A7%E5%88%B6%E5%8F%B0) 启动控制台，然后给对应的应用程序添加相应参数并启动即可。比如本文中 Service Provider 示例的启动参数：

```bash
-Djava.net.preferIPv4Stack=true -Dcsp.sentinel.api.port=8720 -Dcsp.sentinel.dashboard.server=localhost:8080 -Dproject.name=dubbo-provider-demo
```

这样在启动 Service Provider 示例以后，就可以在 Sentinel 控制台中找到我们的应用了。

若不希望开启 Sentinel Dubbo Adapter 中的某个 Filter，可以手动关闭对应的 Filter，比如：

```java
@Bean
public ConsumerConfig consumerConfig() {
    ConsumerConfig consumerConfig = new ConsumerConfig();
    consumerConfig.setFilter("-sentinel.dubbo.consumer.filter");
    return consumerConfig;
}
```
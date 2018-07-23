# Sentinel: Sentinel of Your Application

[![Travis Build Status](https://travis-ci.org/alibaba/Sentinel.svg?branch=master)](https://travis-ci.org/alibaba/Sentinel)
[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)
[![Gitter](https://badges.gitter.im/alibaba/Sentinel.svg)](https://gitter.im/alibaba/Sentinel)

## Documentation

See the [Wiki](https://github.com/alibaba/Sentinel/wiki) for full documentation, examples, operational details and other information.

See the [Javadoc](https://github.com/alibaba/Sentinel/tree/master/doc) for the API.

See [中文Readme](https://github.com/alibaba/Sentinel/wiki/%E4%BB%8B%E7%BB%8D) for readme

## What Does It Do?

With the popularity of distributed systems, the stability between services is becoming more important than ever before. Sentinel takes "flow" as breakthrough point, and works on multiple fields including flow control, concurrency, circuit breaking, load protection, to protect service stability

Sentinel has following features:

* **Rich applicable scenarios**
Sentinel has been wildly used in Alibaba, and has covered almost all the core-scenarios in Double -11 Shopping Festivals in the past 10 years. Lots of scenario, such as “Miaosha”, which needs to limit burst flow traffic to meet the system capacity; message peaks and valley fills; degrade un reliable downstream applications, etc.

* **Integrated monitor module**
Sentinel also provides the most real-time monitoring function, you can see the runtime information of a single machine in real-time, and the summary runtime info of a cluster less than  500 nodes

* **Easy extension point**
Sentinel provides easy-to-use extension points that allow you to quickly customize your logic by implementing extension points. For example, custom rule management, adapting data sources, and so on.

## "Hello world"
Below is a simple demo that guides new users to use Sentinel in just 3 steps. It also shows how to monitor this demo using the dashboard.

### 1.Download Library
**Note:** Sentinel requires Java 6 or later.

If your application is build in maven, just add following code in pom.xml

```xml
<dependency>
    <groupId>com.alibaba.csp</groupId>
    <artifactId>sentinel-core</artifactId>
    <version>x.y.z</version>
</dependency>
```

If not, you can download JAR in [maven](https://mvnrepository.com/artifact/com.alibaba)


### 2.Define Resource
Wrap code snippet via sentinel api: `SphU.entry("RESOURCENAME")` and `entry.exit()`. In below example, it is `System.out.println("hello world");`

```java
Entry entry = null;

try {   
  entry = SphU.entry("HelloWorld");
  
  // BIZ logic being protected
  System.out.println("hello world");
} catch (BlockException e) {
  // handle block logic
} finally {
  // make sure that the exit() logic is called
  if (entry != null) {
    entry.exit();
  }
}
```

After above 2 steps, the code modification is done.  

### 3.Define Rules
If we want to limit the access times of resource, we can define rules. Below code define a rule that limit access to the reource to 20 times per second to the maximum. 
```java
List<FlowRule> rules = new ArrayList<FlowRule>();
FlowRule rule = new FlowRule();
rule.setResource("hello world");
// set limit qps to 20
rule.setCount(20);
rules.add(rule);
FlowRuleManager.loadRules(rules);
```


### 4. Check the Result

After running the demo for a while, you can see following records in `[user dir]\csp\logs\${appName}-metrics.log.xxx`
```
|--timestamp-|------date time----|--resource-|p |block|s |e|rt
1529998904000|2018-06-26 15:41:44|hello world|20|0    |20|0|0
1529998905000|2018-06-26 15:41:45|hello world|20|5579 |20|0|728
1529998906000|2018-06-26 15:41:46|hello world|20|15698|20|0|0
1529998907000|2018-06-26 15:41:47|hello world|20|19262|20|0|0
1529998908000|2018-06-26 15:41:48|hello world|20|19502|20|0|0
1529998909000|2018-06-26 15:41:49|hello world|20|18386|20|0|0

p for incoming reqeust, block for intercepted by rules, success for success handled, e for exception, rt for average response time(ms)

```
This shows that the demo can print "hello world" 20 times per second

More examples and information can be found in the [How To Use](https://github.com/alibaba/Sentinel/wiki/How-to-Use) section.

How it works can be found in [How it works](https://github.com/alibaba/Sentinel/wiki/How-it-works)

Samples can be found in the [demo](https://github.com/alibaba/Sentinel/tree/master/sentinel-demo) module.

### 5.Start Dashboard
Sentinel also provides a simple dashboad, which can monitor the cliets, and configure the rules in real time.

More details please refer to: [Dashboard](https://github.com/alibaba/Sentinel/wiki/%E6%8E%A7%E5%88%B6%E5%8F%B0)

## Trouble Shooting and Logs

Sentinel will generate logs for trouble shooting. All the infos can be found in  [logs](https://github.com/alibaba/Sentinel/wiki/Logs)

## Bugs and Feedback

For bugs, questions and discussions please use the [GitHub Issues](https://github.com/alibaba/sentinel/issues)

Contact us: sentinel@linux.alibaba.com


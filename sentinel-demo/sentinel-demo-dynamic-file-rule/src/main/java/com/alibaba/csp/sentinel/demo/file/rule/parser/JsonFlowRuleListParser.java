package com.alibaba.csp.sentinel.demo.file.rule.parser;

import java.util.List;

import com.alibaba.csp.sentinel.datasource.ConfigParser;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

/**
 * A {@link ConfigParser} parses Json String to {@code List<FlowRule>}.
 *
 * @author Carpenter Lee
 */
public class JsonFlowRuleListParser implements ConfigParser<String, List<FlowRule>> {
    @Override
    public List<FlowRule> parse(String source) {
        return JSON.parseObject(source, new TypeReference<List<FlowRule>>() {});
    }
}

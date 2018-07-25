package com.alibaba.csp.sentinel.demo.file.rule.parser;

import java.util.List;

import com.alibaba.csp.sentinel.datasource.ConfigParser;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

/**
 * A {@link ConfigParser} parses Json String to {@code List<DegradeRule>}.
 *
 * @author Carpenter Lee
 */
public class JsonDegradeRuleListParser implements ConfigParser<String, List<DegradeRule>> {
    @Override
    public List<DegradeRule> parse(String source) {
        return JSON.parseObject(source, new TypeReference<List<DegradeRule>>() {});
    }
}

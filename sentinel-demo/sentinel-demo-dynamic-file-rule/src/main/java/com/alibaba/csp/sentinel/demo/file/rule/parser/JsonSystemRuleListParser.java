package com.alibaba.csp.sentinel.demo.file.rule.parser;

import java.util.List;

import com.alibaba.csp.sentinel.datasource.ConfigParser;
import com.alibaba.csp.sentinel.slots.system.SystemRule;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

/**
 * A {@link ConfigParser} parses Json String to {@code List<SystemRule>}.
 *
 * @author Carpenter Lee
 */
public class JsonSystemRuleListParser implements ConfigParser<String, List<SystemRule>> {
    @Override
    public List<SystemRule> parse(String source) {
        return JSON.parseObject(source, new TypeReference<List<SystemRule>>() {});
    }
}

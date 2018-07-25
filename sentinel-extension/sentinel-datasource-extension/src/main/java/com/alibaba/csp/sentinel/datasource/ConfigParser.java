package com.alibaba.csp.sentinel.datasource;

/**
 * Parse config from source data type S to target data type T.
 *
 * @author leyou
 */
public interface ConfigParser<S, T> {
    /**
     * Parse {@code source} to the target format.
     *
     * @param source the source.
     * @return the target.
     */
    T parse(S source);
}

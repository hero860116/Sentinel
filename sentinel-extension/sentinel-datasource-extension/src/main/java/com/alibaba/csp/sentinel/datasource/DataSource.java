package com.alibaba.csp.sentinel.datasource;

import com.alibaba.csp.sentinel.property.SentinelProperty;

/**
 * This class is responsible for getting configs.
 *
 * @param <S> source data type
 * @param <T> target data type
 * @author leyou
 */
public interface DataSource<S, T> {

    /**
     * Load data data source as the target type.
     *
     * @return the target data.
     * @throws Exception
     */
    T loadConfig() throws Exception;

    /**
     * Read original data from the data source.
     *
     * @return the original data.
     * @throws Exception
     */
    S readSource() throws Exception;

    /**
     * Get {@link SentinelProperty} of the data source.
     *
     * @return the property.
     */
    SentinelProperty<T> getProperty();

    /**
     * Write the {@code values} to the data source.
     *
     * @param values
     * @throws Exception
     */
    void writeDataSource(T values) throws Exception;

    /**
     * Close the data source.
     *
     * @throws Exception
     */
    void close() throws Exception;
}

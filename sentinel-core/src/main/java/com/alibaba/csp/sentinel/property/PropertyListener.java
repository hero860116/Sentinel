package com.alibaba.csp.sentinel.property;

/**
 * This class holds callback method when {@link SentinelProperty#updateValue(Object)} need inform the listener
 *
 * @author jialiang.linjl
 */
public interface PropertyListener<T> {

    /**
     * Callback method when {@link SentinelProperty#updateValue(Object)} need inform the listener.
     *
     * @param value updated value.
     */
    void configUpdate(T value);

    /**
     * The first time of the {@code value}'s load.
     *
     * @param value the value loaded.
     */
    void configLoad(T value);
}

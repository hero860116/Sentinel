package com.alibaba.csp.sentinel.property;

/**
 * <p>
 * This class holds current value of the config, and is responsible for informing all {@link PropertyListener}s
 * added on this when the config is updated.
 * </p>
 * <p>
 * Note that not every {@link #updateValue(Object newValue)} invocation should inform the listeners, only when
 * {@code newValue} is not Equals to the old value, informing is needed.
 * </p>
 *
 * @param <T> the target type.
 * @author Carpenter Lee
 */
public interface SentinelProperty<T> {

    /**
     * <p>
     * Add a {@link PropertyListener} to this {@link SentinelProperty}. After the listener is added,
     * {@link #updateValue(Object)} will inform the listener if needed.
     * </p>
     * <p>
     * This method can invoke multi times to add more than one listeners.
     * </p>
     *
     * @param listener listener to add.
     */
    void addListener(PropertyListener<T> listener);

    /**
     * Remove the {@link PropertyListener} on this. After removing, {@link #updateValue(Object)}
     * will not inform the listener.
     *
     * @param listener the listener to remove.
     */
    void removeListener(PropertyListener<T> listener);

    /**
     * Update the {@code newValue} as the current value of this property and inform all {@link PropertyListener}s
     * added on this only when new {@code newValue} is not Equals to the old value.
     *
     * @param newValue the new value.
     */
    void updateValue(T newValue);
}

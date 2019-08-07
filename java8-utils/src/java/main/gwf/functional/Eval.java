/*
 * Copyright (c) 2019 BTS-IT, Inc. All Rights Reserved.
 * The source code for this program is not published or otherwise divested of 
 * its trade secrets, irrespective of what has been deposited with the U.S. 
 * Copyright Office.
 */
package gwf.functional;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Description: Eval defers the execution of a supplier until <code>get()</code> is called the first time and then caches the result so that the supplier is only executed once.
 * <br>
 * @author gforbis created at Jul 10, 2019
 */
public class Eval<T> implements Supplier<T> {
    /**
     * The supplier delegate that populates the cache.
     */
    private final Supplier<T> delegate;

    /**
     * The result. If null, the supplier delegate will be called to refresh the cached
     * value. If the supplier returns null, the cached value will be an
     * Optional.empty();
     */
    private Optional<T> deferred = null;

    /**
     * Private constructor to use the static defer method for clarity.
     * @param supplier
     */
    private Eval(Supplier<T> supplier) {
        this.delegate = supplier;
    }

    /**
     * The first time this is called, caches the result from the provided Supplier.
     * Afterward returns the cached result. Should be thread-safe.
     */
    @Override
    public T get() {
        if (null == deferred) {
            synchronized (Eval.class) {
                if (null == deferred) {
                    deferred = Optional.ofNullable(delegate.get());
                }
            }
        }
        return deferred.orElse(null);
    }
    
    /**
     * Invalidates the cache and updates it from the underlying supplier on the next get.
     */
    private Eval<T> invalidate() {
        deferred = null;
        return this;
    }

    /**
     * A Supplier that lazily evaluates the supplier the first time the value is
     * requested, and thereafter returns the cached value. Detects if the supplier is an
     * instance of Eval and returns the provided instance instead of wrapping it.<br>
     * <br>
     * Re-deferring an Eval will invalidate the cache.
     * 
     * @param supplier
     * @return
     */
    public static <T> Supplier<T> defer(Supplier<T> supplier) {
        if (Eval.class.isAssignableFrom(supplier.getClass())) {
            return ((Eval<T>)supplier).invalidate();
        } else {
            return new Eval<T>(Objects.requireNonNull(supplier));
        }
    }
}
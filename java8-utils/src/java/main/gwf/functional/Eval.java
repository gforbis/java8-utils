/*
 * Copyright (c) 2019 BTS-IT, Inc. All Rights Reserved.
 * The source code for this program is not published or otherwise divested of 
 * its trade secrets, irrespective of what has been deposited with the U.S. 
 * Copyright Office.
 */
package gwf.functional;

import java.util.Objects;
import java.util.OptionalLong;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Description: Eval defers the execution of a supplier until <code>get()</code> is called the first time and then caches the result so that the supplier is only executed once.
 * <br>
 * @author gforbis created at Jul 10, 2019
 */
public class Eval<T> implements Supplier<T> {
    /**
     * The original supplier
     */
    private final Supplier<T> supplier;
    /**
     * The number of milliseconds to cache the value (forever if empty)
     */
    private final OptionalLong duration;
    /**
     * Logic to invalidate cache. Very fast if no duration.
     */
    private Predicate<Eval<T>> isInvalid = e -> true; // always invalid before the first execution
    /**
     * null or cached value
     */
    private T deferred = null;

    private Eval(Supplier<T> supplier) {
        this.supplier = supplier;
        this.duration = OptionalLong.empty();
    }
    
    private Eval(Supplier<T> supplier, long duration) {
        this.supplier = supplier;
        this.duration = OptionalLong.of(duration);
    }
    
    @Override
    public T get() {
        if (isInvalid.test(this)) {
            deferred = supplier.get();
            if (duration.isPresent()) {
                final long expiry = System.currentTimeMillis() + duration.getAsLong();
                isInvalid = e -> System.currentTimeMillis() >  expiry;
            } else {
                isInvalid = e -> false; // Cache never invalidates
            }
        }
        return deferred;
    }

    /**
     * A Supplier that lazily evaluates the supplier the first time the value is requested, and thereafter returns the cached value.
     * @param supplier
     * @return
     */
    public static <T> Supplier<T> defer(Supplier<T> supplier) {
        return new Eval<T>(Objects.requireNonNull(supplier));
    }

    /**
     * A Supplier that lazily evaluates the supplier any time the cached value is invalid. If the duration is less than 1, the provided supplier is returned to perform an uncached lookup.
     * @param supplier
     * @param durationMillis
     * @return
     */
    public static <T> Supplier<T> defer(Supplier<T> supplier, long durationMillis) {
        if (durationMillis > 0) {
            return new Eval<T>(Objects.requireNonNull(supplier), durationMillis);
        } else {
            return Objects.requireNonNull(supplier);
        }
    }
}

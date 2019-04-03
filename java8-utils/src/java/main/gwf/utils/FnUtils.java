/*
 * Copyright (c) 2019 BTS-IT, Inc. All Rights Reserved. The source code for this program
 * is not published or otherwise divested of its trade secrets, irrespective of what has
 * been deposited with the U.S. Copyright Office.
 */
package gwf.utils;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class FnUtils {
    private FnUtils() {
        throw new RuntimeException("Do not instantiate");
    }

    public static <T, U> Predicate<U> applyLeft(BiPredicate<T, U> predicate, T t) {
        return u -> predicate.test(t, u);
    }

    public static <T, U> Predicate<T> applyRight(BiPredicate<T, U> predicate, U u) {
        return t -> predicate.test(t, u);
    }

    public static <T, U> Consumer<U> applyLeft(BiConsumer<T, U> consumer, T t) {
        return u -> consumer.accept(t, u);
    }

    public static <T, U> Consumer<T> applyRight(BiConsumer<T, U> consumer, U u) {
        return t -> consumer.accept(t, u);
    }

    public static <T, U, R> Function<U, R> applyLeft(BiFunction<T, U, R> function, T t) {
        return u -> function.apply(t, u);
    }

    public static <T, U, R> Function<T, R> applyRight(BiFunction<T, U, R> function, U u) {
        return t -> function.apply(t, u);
    }
}
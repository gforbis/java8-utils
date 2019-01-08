package gwf.utils;

import java.util.function.BiFunction;
import java.util.function.Function;

public class FunctionUtils {
    public static <T, U, V> Function<T, Function<U, V>> curryFirst(BiFunction<T, U, V> bifunction) {
        return T -> U -> bifunction.apply(T, U);
    }

    public static <T, U, V> Function<U, Function<T, V>> currySecond(
            BiFunction<T, U, V> bifunction) {
        return U -> T -> bifunction.apply(T, U);
    }
}

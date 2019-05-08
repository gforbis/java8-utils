package gwf.utils.functions;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 
 * Class FnUtils<br>
 * Description: This class FnUtils is a utility class used for currying functions
 * <br>
 * @author gforbis created at May 8, 2019
 */
public class FnUtils {
    private FnUtils() {
        throw new RuntimeException("Do not instantiate");
    }

    /**
     * @param <T> the type of the first input to the function
     * @param <U> the type of the second input to the function
     * @param predicate a predicate function
     * @param t a value to be partially applied
     * @return <b>Predicate&lt;U&gt;</b>
     */
    public static <T, U> Predicate<U> applyFirst(BiPredicate<T, U> predicate, T t) {
        return u -> predicate.test(t, u);
    }

    /**
     * @param <T> the type of the first input to the function
     * @param <U> the type of the second input to the function
     * @param predicate a predicate function
     * @param u a value to be partially applied
     * @return <b>Predicate&lt;T&gt;</b>
     */
    public static <T, U> Predicate<T> applySecond(BiPredicate<T, U> predicate, U u) {
        return t -> predicate.test(t, u);
    }

    /**
     * @param <T> the type of the first input to the function
     * @param <U> the type of the second input to the function
     * @param consumer a consumer function
     * @param t a value to be partially applied
     * @return <b>Consumer&lt;U&gt;</b>
     */
    public static <T, U> Consumer<U> applyFirst(BiConsumer<T, U> consumer, T t) {
        return u -> consumer.accept(t, u);
    }

    /**
     * @param <T> the type of the first input to the function
     * @param <U> the type of the second input to the function
     * @param consumer a consumer function
     * @param u a value to be partially applied
     * @return <b>Consumer&lt;T&gt;</b>
     */
    public static <T, U> Consumer<T> applySecond(BiConsumer<T, U> consumer, U u) {
        return t -> consumer.accept(t, u);
    }

    /**
     * @param <T> the type of the first input to the function
     * @param <U> the type of the second input to the function
     * @param <R> the return type of the function
     * @param function
     * @param t a value to be partially applied
     * @return <b>Function&lt;U,R&gt;</b>
     */
    public static <T, U, R> Function<U, R> applyFirst(BiFunction<T, U, R> function, T t) {
        return u -> function.apply(t, u);
    }

    /**
     * @param <T> the type of the first input to the function
     * @param <U> the type of the second input to the function
     * @param <R> the return type of the function
     * @param function
     * @param u a value to be partially applied
     * @return <b>Function&lt;T,R&gt;</b>
     */
    public static <T, U, R> Function<T, R> applySecond(BiFunction<T, U, R> function, U u) {
        return t -> function.apply(t, u);
    }
}
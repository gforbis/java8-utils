package gwf.utils.functions;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Class FunctionUtils<br>
 * Description: This class FunctionUtils is an alternative take on FnUtils. It is more
 * of a thought-experiment as the syntax is more verbose without adding clarity. <br>
 * 
 * @author gforbis created at May 8, 2019
 */
public class FunctionUtils {
    /**
     * Usage Function&lt;T,V&gt; fn = FunctionUtils.curryFirst(BiFunction&lt;T,U,V&gt;
     * function).apply(T t);
     * 
     * @param <T> the type of the first input to the function
     * @param <U> the type of the second input to the function
     * @param <V> the return type of the function
     * @param function
     * @return <b>Function&lt;T,Function&lt;U,V&gt;&gt;</b>
     */
    public static <T, U, V> Function<T, Function<U, V>> curryFirst(BiFunction<T, U, V> function) {
        return T -> U -> function.apply(T, U);
    }

    /**
     * Usage Function&lt;U,V&gt; fn = FunctionUtils.currySecond(BiFunction&lt;T,U,V&gt;
     * function).apply(U u);
     * 
     * @param <T> the type of the first input to the function
     * @param <U> the type of the second input to the function
     * @param <V> the return type of the function
     * @param function
     * @return <b>Function&lt;U,Function&lt;T,V&gt;&gt;</b>
     */
    public static <T, U, V> Function<U, Function<T, V>> currySecond(BiFunction<T, U, V> function) {
        return U -> T -> function.apply(T, U);
    }
}

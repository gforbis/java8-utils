package gwf.utils;

import static org.junit.jupiter.api.Assertions.*;

import java.util.function.BiFunction;
import java.util.function.Function;

import org.junit.jupiter.api.Test;

import gwf.utils.FunctionUtils;

class FunctionUtilsTest {

    @Test
    void testCurryFirst() {
        BiFunction<Integer,Integer,Integer> uncurried = (A, B) -> A + B;
        Function<Integer, Integer> curried = FunctionUtils.curryFirst(uncurried).apply(Integer.valueOf(5));
        assertTrue(Integer.valueOf(8).equals(curried.apply(Integer.valueOf(3))));
    }

    @Test
    void testCurrySecond() {
        BiFunction<Integer,Integer,Integer> uncurried = (A, B) -> A + B;
        Function<Integer, Integer> curried = FunctionUtils.currySecond(uncurried).apply(Integer.valueOf(3));
        assertTrue(Integer.valueOf(8).equals(curried.apply(Integer.valueOf(5))));
    }

}

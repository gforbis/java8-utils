package gwf.utils;

import static org.junit.jupiter.api.Assertions.*;

import java.util.function.BiFunction;
import java.util.function.Function;

import org.junit.jupiter.api.Test;

import gwf.utils.functions.FunctionUtils;

class FunctionUtilsTest {
    private static final Integer FIRST = 5;
    private static final Integer SECOND = 3;
    private static final Integer SUM = 8;
    private static final BiFunction<Integer,Integer,Integer> FN_SUM = (A,B) -> A + B;

    @Test
    void testCurryFirst() {
       Function<Integer, Integer> curried = FunctionUtils.curryFirst(FN_SUM).apply(FIRST);
        assertTrue(SUM.equals(curried.apply(Integer.valueOf(SECOND))));
    }

    @Test
    void testCurrySecond() {
        Function<Integer, Integer> curried = FunctionUtils.currySecond(FN_SUM).apply(SECOND);
        assertTrue(SUM.equals(curried.apply(FIRST)));
    }

}

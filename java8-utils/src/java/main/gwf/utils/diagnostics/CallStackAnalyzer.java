/*
 * Copyright (c) 2019 BTS-IT, Inc. All Rights Reserved.
 * The source code for this program is not published or otherwise divested of 
 * its trade secrets, irrespective of what has been deposited with the U.S. 
 * Copyright Office.
 */
package gwf.utils.diagnostics;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CallStackAnalyzer {
    private static final Predicate<StackTraceElement> isNotAnonymous = e -> e.getClassName().indexOf('$') == -1 && e.getMethodName().indexOf('$') == -1;
    private final Predicate<StackTraceElement> fnMatchClass ;
    private final int limit;
    private final Function<StackTraceElement, String> fnFormat;
    private final String join;
    
    private CallStackAnalyzer(Predicate<StackTraceElement> matchClass, int limit, String join, Function<StackTraceElement, String> fnFormat) {
        fnMatchClass = matchClass.and(e -> !e.getClassName().equals(this.getClass().getCanonicalName())); // Filter me out
        this.limit = limit;
        this.join = join;
        this.fnFormat = fnFormat;
    }
    
    public void sendTo(Consumer<String> fnProcess) {
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();
        String callStack = Arrays.stream(stack)
                .filter(isNotAnonymous.and(fnMatchClass))
                .limit(limit)
                .map(fnFormat)
                .collect(Collectors.joining(join));
        fnProcess.accept(callStack);
    }
    
    public static Builder builder() {
        return new BuilderImpl();
    }
    
    public static interface Builder {
        /**
         * Only log classes whose canonical names match. Typical something like<br><code>s->s.startsWith("net.bts") || s.startsWith("com.ra")</code><br><br>
         * <i>Default: s -> true</i>
         * @param match
         * @return
         */
        Builder filter(Predicate<String> match);
        /**
         * The maximum number of matching stack entries to log.<br><br>
         * <i>Default: Integer.MAX_VALUE</i>
         * @param limit
         * @return
         */
        Builder limit(int limit);
        /**
         * A string with which to join each matching element.<br><br>
         * <i>Default: <code>"\n"</code></i>
         * @param join
         * @return
         */
        Builder joinWith(String join);
        /**
         * A formatter that is applied to each stack element.<br><br>
         * <i>Default: <code>e -> e.getClassName() + "." + e.getMethodName() + "(...)[" + e.getLineNumber() + "]"</code></i>
         * @param fnFormat
         * @return
         */
        Builder formatWith(Function<StackTraceElement, String> fnFormat);
        CallStackAnalyzer build();
    }
    
    private static class BuilderImpl implements Builder {
        private Predicate<String> fnMatchClass = s -> true; // Default to all entries
        private int limit = Integer.MAX_VALUE; // Default to no limit
        private String join = "\n  ";
        private Function<StackTraceElement, String> fnFormat = e -> e.getClassName() + "." + e.getMethodName() + "(...)[" + e.getLineNumber() + "]";
        
        public Builder filter(Predicate<String> match) {
            fnMatchClass = Objects.requireNonNull(match);
            return this;
        }
        @Override
        public Builder limit(int limit) {
            if (limit < 1) {
                throw new IllegalArgumentException("limit must be a positive number");
            }
            this.limit = limit;
            return this;
        }
        @Override
        public Builder joinWith(String join) {
            this.join = Objects.requireNonNull(join);
            return this;
        }
        @Override
        public Builder formatWith(Function<StackTraceElement,String> fnFormat) {
            this.fnFormat = fnFormat;
            return this;
        }
        @Override
        public CallStackAnalyzer build() {
            return new CallStackAnalyzer(ste -> fnMatchClass.test(ste.getClassName()), limit, join, fnFormat);
        }
    }
}

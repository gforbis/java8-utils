package gwf.utils.diagnostics;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class Perf {
    private final Map<String,Long> runTime = new HashMap<>();
    private final Map<String,Integer> runCount = new HashMap<>();
    private final Consumer<String> startListener;
    private final Consumer<String> stopListener;
    private long minDuration = 1;

    private Perf(Consumer<String> startListener, Consumer<String> stopListener) {
    	this.startListener = startListener;
    	this.stopListener = stopListener;
    }
    
    public void start() {
        StackTraceElement e = Thread.currentThread().getStackTrace()[2];
        String id = e.getClassName() + "->"+e.getMethodName();
        start(id);
    }
    
    public void start(String id) {
        Long current = runTime.put(id, System.currentTimeMillis());
        if (null != current) {
            startListener.accept("Monitor " + id + " was restarted without a stop event");
        }
    }
    
    public void stop() {
        StackTraceElement e = Thread.currentThread().getStackTrace()[2];
        String id = e.getClassName() + "->"+e.getMethodName();
        stop(id);
    }
    
    public void stop(String id) {
        long stop = System.currentTimeMillis();
        Long start = runTime.get(id);
        Integer count = runCount.get(id);
        count = null == count ? 0 : count;
        runCount.put(id, ++count);
        if (null == start) {
            stopListener.accept("Monitor " + id + " was stopped without a start event");
        } else {
            long duration = stop - start.longValue();
            if (duration >= minDuration) {
                StringBuffer sb = new StringBuffer();
                sb.append(id);
                if (count > 1) {
                    sb.append(" run ");
                    sb.append(count);
                }
                sb.append(" completed in ");
                sb.append(stop - start);
                sb.append(" ms");
                stopListener.accept(sb.toString());            
            }
        }
    }
}

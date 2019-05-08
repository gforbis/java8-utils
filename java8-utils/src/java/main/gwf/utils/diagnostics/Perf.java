package gwf.utils.diagnostics;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Perf {
    private static final Logger LOG = LogManager.getLogger(Perf.class);
    private static final Perf instance = new Perf();
    private final Map<String,Long> runTime = new HashMap<>();
    private final Map<String,Integer> runCount = new HashMap<>();
    private long minDuration = 1;

    private Perf() {
        
    }
    
    public static void start() {
        StackTraceElement e = Thread.currentThread().getStackTrace()[2];
        String id = e.getClassName() + "->"+e.getMethodName();
        start(id);
    }
    
    public static void start(String id) {
        Long current = instance.runTime.put(id, System.currentTimeMillis());
        if (null != current) {
            LOG.info("Monitor " + id + " was restarted without a stop event");
        }
    }
    
    public static void stop() {
        StackTraceElement e = Thread.currentThread().getStackTrace()[2];
        String id = e.getClassName() + "->"+e.getMethodName();
        stop(id);
    }
    
    public static void stop(String id) {
        long stop = System.currentTimeMillis();
        Long start = instance.runTime.get(id);
        Integer count = instance.runCount.get(id);
        count = null == count ? 0 : count;
        instance.runCount.put(id, ++count);
        if (null == start) {
            LOG.info("Monitor " + id + " was stopped without a start event");
        } else {
            long duration = stop - start.longValue();
            if (duration >= instance.minDuration) {
                StringBuffer sb = new StringBuffer();
                sb.append(id);
                if (count > 1) {
                    sb.append(" run ");
                    sb.append(count);
                }
                sb.append(" completed in ");
                sb.append(stop - start);
                sb.append(" ms");
                LOG.info(sb.toString());            
            }
        }
    }
}

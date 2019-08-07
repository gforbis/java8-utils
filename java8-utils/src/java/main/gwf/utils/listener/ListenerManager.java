package gwf.utils.listener;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gwf.utils.diagnostics.CallStackAnalyzer;

/**
 * Listener Manager provides a centralized place to handle Listeners.
 * <br>
 * @param T The type of the event id <em>(typically String or an enum)</em>
 * @param E The type of Consumer listeners being managed
 * @author gforbis created at Jun 20, 2019
 */
public class ListenerManager<T, E> {
    private final static Logger LOG = LogManager.getLogger(ListenerManager.class);
    private final Map<T, List<Consumer<E>>> eventListeners = new HashMap<>();
    private final Consumer<T> eventLogger;
    
    /**
     * The object upon which event handlers operate.
     * @NotNull
     */
    private final E controller;
    
    /**
     * Constructor.
     * 
     * @param controller
     * @throws NullPointerException if controller is <code>null</code>
     */
    public ListenerManager(final E controller) {
        this.controller = Objects.requireNonNull(controller);
        eventLogger = null;
    }
    
    /**
     * Constructor.
     * 
     * @param controller
     * @param logger a Listener&lt;eventType&gt; which runs just before the listeners to that event
     * @throws NullPointerException if controller is <code>null</code>
     */
    public ListenerManager(final E controller, final Consumer<T> logger) {
        this.controller = Objects.requireNonNull(controller);
        eventLogger = logger;
    }

    /**
     * Adds a listener for the given event.
     * @param event
     * @param listener
     * @throws NullPointerException if either event or listener is <code>null</code>
     */
    public void add(final T event, Consumer<E>listener) {
        getListeners(Objects.requireNonNull(event)).add(Objects.requireNonNull(listener));
    }
    
    /**
     * Removes a listener from any and all events.
     * @param listener
     */
    public void remove(final Consumer<E> listener) {
        eventListeners.values().forEach(listenerList -> listenerList.remove(listener));
        eventListeners.entrySet().removeIf(e -> e.getValue().isEmpty());
    }
    
    /**
     * Removes a listener from the specified event.
     * @param event
     * @param listener
     */
    public void remove(final T event, final Consumer<E> listener) {
        List<Consumer<E>> ll = eventListeners.get(event);
        if (null != ll) {
            ll.remove(listener);
            if (ll.isEmpty()) {
                eventListeners.remove(event);
            }
        }
    }
    
    /**
     * For each listener of the given event, invokes the handler with the
     * {@link #controller controller}.
     * 
     * @param event
     * @throws NullPointerException if event is <code>null</code>
     */
    public void notify(final T event) {
        Exception ex = null;
        if (null != eventLogger) {
            try {
                eventLogger.accept(event);
            } catch (Exception e) {
                ex = null == ex ? e : ex;
                LOG.error("Event logger exception", e);
            }
        }
        for (Consumer<E> listener : getListeners(Objects.requireNonNull(event))) {
            try {
                listener.accept(controller);
            } catch (Exception e) {
                ex = null == ex ? e : ex;
                LOG.error("Event handler exception", e);
            }
        }
        if(null != ex) {
            if (ex instanceof RuntimeException) {
                throw (RuntimeException) ex;
            } else {
                throw new RuntimeException(ex);
            }
        }
    }
    
    /**
     * Immediately removes all listeners; 
     */
    public void dispose() {
        eventListeners.clear();
    }
    
    /**
     * @param event
     * @return
     */
    private List<Consumer<E>> getListeners(final T event) {
        if (! eventListeners.containsKey(event)) {
            eventListeners.put(event, new ArrayList<>());
        }
        return eventListeners.get(event);
    }
    
    private static final Predicate<StackTraceElement> fnNotAnonymous = e -> e.getClassName().indexOf('$') == -1 && e.getMethodName().indexOf('$') == -1;
    
    /**
     * A Logger which looks at the method 
     * @param eventClass
     * @return
     */
    public static <T> Consumer<T> methodNameLogger(Class<T> eventClass) {
        int index = 3; // 1 is the lambda, 2 is Consumer.accept, 3 is ListenerManger.notify
        return event -> {
            PrintStream ps = null;
            String eventName = String.valueOf(event);
            if (eventName.charAt(0) == '*') {
                eventName = eventName.substring(1);
                ps = System.err;
            } else {
                ps = System.out;
            }
            StackTraceElement e = Arrays.stream(Thread.currentThread().getStackTrace()).skip(index).filter(fnNotAnonymous).findFirst().orElse(null);
            String result = null;
            if (null == e) {
                result = "Event: " + eventName + " -> origin error";
            } else {
                result = "Event: " + eventName + " -> " + e.getClassName() + "." + e.getMethodName() + "[" + e.getLineNumber() + "]";
            }
            ps.println(result);
        };
    }
    
    public static <T> Consumer<T> stackLogger(Class<T> eventClass, CallStackAnalyzer csa) {
        return event -> {
            PrintStream ps = null;
            String eventName = String.valueOf(event);
            if (eventName.charAt(0) == '*') {
                eventName = eventName.substring(1);
                ps = System.err;
            } else {
                ps = System.out;
            }
            ps.println("Event: " + eventName);
            csa.sendTo(ps::println);
        };
    }
    
    public static <T> Consumer<T> eventNameLogger(Class<T> eventClass) {
        return event -> {
            PrintStream ps = null;
            String eventName = String.valueOf(event);
            if (eventName.charAt(0) == '*') {
                eventName = eventName.substring(1);
                ps = System.err;
            } else {
                ps = System.out;
            }
            ps.println("Event: " + eventName);
        };
    }
}

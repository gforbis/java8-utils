package gwf.utils.listener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Class ListenerManager<br>
 * Description: This class ListenerManager is a container for managing Listeners. It
 * contains a final, non-null object to be passed to listeners when an event occurs.<br>
 * <br>
 * 
 * @param <T> The key type for identifying listeners. Typically an <code>enum</code> or
 * <code>String</code>
 * @param <U> The value type that listeners accept in order to handle an event
 * @author gforbis created at May 8, 2019
 */
public class ListenerManager<T, U> {
    private static final Logger LOG = LogManager.getLogger(ListenerManager.class);

    private Map<T, List<Listener<U>>> listeners;

    private final U eventObject;

    public ListenerManager(U eventObject) {
        this.eventObject = Objects.requireNonNull(eventObject);
    }

    public void notify(T event) {
        RuntimeException ex = null;
        for (Listener<U> listener : getListeners(event)) {
            try {
                listener.handle(eventObject);
            }
            catch (Exception e) {
                LOG.error("Event handling exception: " + e.toString(), e);
                if (null == ex) {
                    if (e instanceof RuntimeException) {
                        ex = (RuntimeException) e;
                    }
                    else {
                        ex = new RuntimeException(e);
                    }
                }
            }
        }
        if (null != ex) {
            throw ex;
        }
    }

    public void addListener(T event, Listener<U> listener) {
        getListeners(event).add(listener);
    }

    public void removeListener(T event, Listener<U> listener) {
        getListeners(event).remove(listener);
    }

    public void removeListener(Listener<U> listener) {
        for (T key : listeners.keySet()) {
            removeListener(key, listener);
        }
    }

    private List<Listener<U>> getListeners(T event) {
        List<Listener<U>> eventListeners = listeners.get(Objects.requireNonNull(event));
        if (null == eventListeners) {
            eventListeners = new ArrayList<>();
            listeners.put(event, eventListeners);
        }
        return eventListeners;
    }
}

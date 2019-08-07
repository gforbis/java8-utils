package gwf.utils.listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

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
    private final Map<T, List<Consumer<U>>> listeners = new HashMap<>();

    private final U eventObject;

    public ListenerManager(U eventObject) {
        this.eventObject = Objects.requireNonNull(eventObject);
    }

    public void notify(T event) {
        RuntimeException ex = null;
        for (Consumer<U> listener : getListeners(event)) {
            try {
                listener.accept(eventObject);
            }
            catch (Exception e) {
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

    public void addListener(T event, Consumer<U> listener) {
        getListeners(event).add(listener);
    }

    public void removeListener(T event, Consumer<U> listener) {
        getListeners(event).remove(listener);
    }

    public void removeListener(Consumer<U> listener) {
        for (T key : listeners.keySet()) {
            removeListener(key, listener);
        }
    }

    private List<Consumer<U>> getListeners(T event) {
        List<Consumer<U>> eventListeners = listeners.get(Objects.requireNonNull(event));
        if (null == eventListeners) {
            eventListeners = new ArrayList<>();
            listeners.put(event, eventListeners);
        }
        return eventListeners;
    }
}

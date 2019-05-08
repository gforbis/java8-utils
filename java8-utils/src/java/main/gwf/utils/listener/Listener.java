package gwf.utils.listener;

/**
 * 
 * Class Listener<br>
 * <br>
 * @param <T> The type of the object required to handle an event
 * @author gforbis created at May 8, 2019
 */
public interface Listener<T> {
    /**
     * Process an event using the provided eventObject
     * @param eventObject
     */
    void handle(T eventObject);
}

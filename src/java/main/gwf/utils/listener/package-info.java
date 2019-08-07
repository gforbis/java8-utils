/**
 * Package gwf.utils.listener<br>
 * Description: This package is for a very simple listener mechanism.<br>
 * <br>
 * Anticipated usage would be for a ListenerManager to contain a reference to the
 * containing object, which is then passed to listeners when an event occurs.<br>
 * <br>
 * Example:<br>
 * 
 * <pre>
 * public class MyDataObject implements IData{
 *    private final ListenerManager&lt;String,IData&gt; lm = new ListenerManager(this);
 *    public save(){
 *      lm.notify("beforeSave");
 *      boolean result = // do save
 *      if (result) {
 *          lm.notify("saveSuccess");
 *      } else {
 *          lm.notify("saveFailure");
 *      }
 *    }
 *    public addListener(String event, Consumer&lt;IData&gt; listener) {
 *        lm.addListener(event, listener);
 *    }
 * }
 * 
 * public class MyDataObjectHandler {
 *     private final Consumer&lt;IData&gt; beforeSaveListener = data -> {};
 *     private final Consumer&lt;IData&gt; saveSuccessListener = data -> {};
 *     private final Consumer&lt;IData&gt; saveFailureListener = data -> {};
 *     
 *     public MyDataObjectHandler(IData dataObject) {
 *         dataObject.addListener("beforeSave", beforeSaveListener);
 *         dataObject.addListener("saveSuccess", saveSuccessListener);
 *         dataObject.addListener("saveFailure", saveFailureListener);
 *     }
 * }
 * </pre>
 * 
 * <br>
 * 
 * @author gforbis created at Apr 19, 2019
 */
package gwf.utils.listener;
/*
 * DialogRunnableFactory.java
 *
 * Research In Motion Limited proprietary and confidential
 * Copyright Research In Motion Limited, 2011-2011
 */

package blackberry.ui.dialog.nav;

import java.util.Vector;

import net.rim.device.api.script.Scriptable;
import net.rim.device.api.script.ScriptableFunction;

import blackberry.core.ApplicationEventHandler;
import blackberry.core.EventService;

import blackberry.ui.dialog.nav.datetime.DateTimeDialog;
import blackberry.ui.dialog.nav.IWebWorksDialog;
import blackberry.ui.dialog.nav.select.SelectDialog;

public class DialogRunnableFactory {
    
    public static Runnable getDateTimeRunnable(String type, String value, String min, String max, ScriptableFunction callback, Object thiz) {
        IWebWorksDialog d = new DateTimeDialog(type, value, min, max);
        return new DialogRunnable(d, callback, thiz);
    }
    
    public static Runnable getSelectRunnable(boolean allowMultiple, String[] labels, boolean[] enabled, boolean[] selected, int[] types, ScriptableFunction callback, Object thiz) {
        IWebWorksDialog d = new SelectDialog(allowMultiple, labels, enabled, selected, types);
        return new DialogRunnable(d, callback, thiz);
    }
    
    private static class DialogRunnable implements Runnable {
        private IWebWorksDialog _dialog;        
        private ScriptableFunction _callback;
        private Object _context;
            
        /**
         * Constructs a <code>DialogRunnable</code> object.
         * 
         * @param dialog
         *            The dialog
         * @param callback
         *            The callback
         * @param context
         *            The context in which the callback executes (its "this" object)
         */
        DialogRunnable(IWebWorksDialog dialog, ScriptableFunction callback, Object context) {
            _dialog = dialog;
            _callback = callback;
            _context = context;
        }
        

        /**
         * Run the dialog.
         * 
         * @see java.lang.Runnable#run()
         */
        public void run() { 
            if(_dialog.show()) {
                Object dialogValue = _dialog.getSelectedValue();
                final Object[] cbArgs;
                
                //we'll accept Vector-type dialog return values for arrays
                //otherwise get object's string as all ecma primitives will return a valid string representation of themselves
                if (dialogValue instanceof Vector) {
                    Vector v = (Vector)dialogValue;
                    Object[] retVal = new Object[v.size()];
                    v.copyInto(retVal);
                    cbArgs = new Object[] { retVal };
                } else {
                    cbArgs = new Object[] { dialogValue.toString() };
                }
                
                ThreadSafeCallback tscb = new ThreadSafeCallback(_callback);
                EventService.getInstance().addHandler( 103, tscb);
                EventService.getInstance().fireEvent(103, cbArgs, true);
                EventService.getInstance().removeHandler(103, tscb);
            }
        }
    }
    
    private static class ThreadSafeCallback implements ApplicationEventHandler {
        final ScriptableFunction _cb;
        
        ThreadSafeCallback(ScriptableFunction callback) {
            _cb = callback;
        }
        
        public boolean handlePreEvent( int eventID, Object[] args ) {
             try {
                _cb.invoke(null, args);
            } catch (Exception e) {
                throw new RuntimeException("Invoke callback failed: " + e.getMessage());
            }
            return true;
        }

        public void handleEvent( int eventID, Object[] args ) {
             try {
                _cb.invoke(null, args);
            } catch (Exception e) {
                throw new RuntimeException("Invoke callback failed: " + e.getMessage());
            }
        }
    }
}



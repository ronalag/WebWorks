/*
 * DialogRunnableFactory.java
 *
 * Research In Motion Limited proprietary and confidential
 * Copyright Research In Motion Limited, 2011-2011
 */

package blackberry.ui.dialog.nav;

import java.util.Vector;

import net.rim.device.api.script.Scriptable;
import net.rim.device.api.script.ScriptableImpl;
import net.rim.device.api.script.ScriptableFunction;

import blackberry.common.util.json4j.JSONObject;

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
                final Object retVal;
                
                //we'll accept Vector-type dialog return values for arrays
                //otherwise get object's string as all ecma primitives will return a valid string representation of themselves
                if (dialogValue instanceof Vector) {
                    retVal = vectorToScriptable((Vector)dialogValue);
                } else {
                    retVal = dialogValue.toString();
                }
                
                try {
                    _callback.invoke(_context, new Object[] { retVal });
                } catch (Exception e) {
                    throw new RuntimeException("Invoke callback failed: " + e.getMessage());
                }
            }
        }
        
        private Scriptable vectorToScriptable(Vector vec) {
            ScriptableImpl s = new ScriptableImpl();
            
            for(int i = 0; i < vec.size(); i++) {
                s.putElement(i, vec.elementAt(i));
            }
            
            return s;
        }
    }
}



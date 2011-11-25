/*
 * DialogRunnableFactory.java
 *
 * Research In Motion Limited proprietary and confidential
 * Copyright Research In Motion Limited, 2011-2011
 */

package blackberry.ui.dialog.nav;

import net.rim.device.api.script.Scriptable;
import net.rim.device.api.script.ScriptableFunction;

import blackberry.common.util.json4j.JSONObject;

import blackberry.ui.dialog.nav.DateTimeDialog;
import blackberry.ui.dialog.nav.IWebWorksDialog;

public class DialogRunnableFactory {
    
    public static Runnable getDateTimeRunnable(String type, String value, String min, String max, ScriptableFunction callback, Object thiz) {
        IWebWorksDialog d = new DateTimeDialog(type, value, min, max);
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
                final String retVal = _dialog.getSelectedValue();
                
                try {
                    _callback.invoke(_context, new Object[] { retVal });
                } catch (Exception e) {
                    throw new RuntimeException("Invoke callback failed: " + e.getMessage());
                }
            }
        }
    }
}



/*
 * DateTimeAsync.java
 *
 * Research In Motion Limited proprietary and confidential
 * Copyright Research In Motion Limited, 2011-2011
 */

package blackberry.ui.dialog;

import blackberry.core.FunctionSignature;
import blackberry.core.ScriptableFunctionBase;

import net.rim.device.api.script.ScriptEngine;
import net.rim.device.api.script.Scriptable;
import net.rim.device.api.script.ScriptableFunction;
import net.rim.device.api.ui.UiApplication;

import blackberry.common.util.json4j.JSONObject;

import blackberry.ui.dialog.DateTimeDialog;
import blackberry.ui.dialog.IWebWorksDialog;

public class DateTimeAsyncFunction extends ScriptableFunctionBase {
    
    public static final String NAME = "dateTimeAsync";
    
    public static final int TYPE_DATE = 0;
    public static final int TYPE_MONTH = 1;
    public static final int TYPE_TIME = 2;
    public static final int TYPE_DATETIME = 3;
    public static final int TYPE_DATETIMELOCAL = 4;
    
    private ScriptEngine _scriptEngine;
    
    public DateTimeAsyncFunction(ScriptEngine se) {
        _scriptEngine = se;
    }

    /**
     * @see blackberry.core.ScriptableFunctionBase#execute(Object, Object[])
     */
    public Object execute(Object thiz, Object[] args) throws Exception {
        String typeStr = (String) args[0];
        Scriptable options = (Scriptable) args[1];
        String callback = (String) args[2] ;
        
        int type = getTypeForStringArg(typeStr);
        String value = (String)options.getField("value");
        String min = (String)options.getField("min");
        String max = (String)options.getField("max");
        String stepStr = (String)options.getField("step");
        double step = "".equals(stepStr) ? 0 : Double.parseDouble(stepStr);
        
        // create dialog
        DateTimeDialog d = new DateTimeDialog(type, value, min, max, step);
        DialogRunner currentDialog = new DialogRunner(d, _scriptEngine, callback);

        // queue
        UiApplication.getUiApplication().invokeLater(currentDialog);

        // return value
        return Scriptable.UNDEFINED;
    }
    
    private int getTypeForStringArg(String typeStr) {
         if("date".equals(typeStr)) {
            return TYPE_DATE;
         } else if("month".equals(typeStr)) {
            return TYPE_MONTH;
         } else if("time".equals(typeStr)) {
            return TYPE_TIME; 
         } else if("datetime".equals(typeStr)) {
            return TYPE_DATETIME; 
         } else if("local".equals(typeStr)) {
            return TYPE_DATETIMELOCAL; 
         } else {
            throw new IllegalArgumentException("Invalid date type.");
         }
    }
    
    /**
     * @see blackberry.core.ScriptableFunctionBase#getFunctionSignatures()
     */
    protected FunctionSignature[] getFunctionSignatures() {
        FunctionSignature fs = new FunctionSignature(3);
        // type
        fs.addParam(String.class, true);
        // options
        fs.addParam(Scriptable.class, true);
        //callback
        fs.addParam(String.class, true);
        
        return new FunctionSignature[] { fs };
    }

    private static class DialogRunner implements Runnable {
        private IWebWorksDialog _dialog;
        private ScriptEngine _se;
        private String _callback;
            
        /**
         * Constructs a <code>DialogRunner</code> object.
         * 
         * @param dialog
         *            The dialog
         * @param callback
         *            The onSelect callback
         */
        DialogRunner(IWebWorksDialog dialog, ScriptEngine se, String callback) {
            _dialog = dialog;
            _se = se;
            _callback = callback;
        }
        

        /**
         * Run the dialog.
         * 
         * @see java.lang.Runnable#run()
         */
        public void run() { 
            if(_dialog.show()) {
                final String newDate = _dialog.getSelectedValue();
                
                try {
                    _se.executeScript(_callback + "(\"" + newDate + "\");", null);
                } catch (Exception e) {
                    throw new RuntimeException("Invoke callback failed: " + e.getMessage());
                }
            }
        }
    }
}

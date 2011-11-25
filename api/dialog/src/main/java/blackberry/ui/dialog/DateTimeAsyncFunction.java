/*
 * DateTimeAsync.java
 *
 * Research In Motion Limited proprietary and confidential
 * Copyright Research In Motion Limited, 2011-2011
 */

package blackberry.ui.dialog;

import blackberry.core.FunctionSignature;
import blackberry.core.ScriptableFunctionBase;

import net.rim.device.api.script.Scriptable;
import net.rim.device.api.script.ScriptableFunction;
import net.rim.device.api.ui.UiApplication;

import blackberry.ui.dialog.nav.DialogRunnableFactory;

public class DateTimeAsyncFunction extends ScriptableFunctionBase {
    
    public static final String NAME = "dateTimeAsync";
    
    public static final int TYPE_DATE = 0;
    public static final int TYPE_MONTH = 1;
    public static final int TYPE_TIME = 2;
    public static final int TYPE_DATETIME = 3;
    public static final int TYPE_DATETIMELOCAL = 4;

    /**
     * @see blackberry.core.ScriptableFunctionBase#execute(Object, Object[])
     */
    public Object execute(Object thiz, Object[] args) throws Exception {
        String type = (String) args[0];
        Scriptable options = (Scriptable) args[1];
        ScriptableFunction callback = (ScriptableFunction) args[2] ;
        
        String value = (String)options.getField("value");
        String min = (String)options.getField("min");
        String max = (String)options.getField("max");
        
        Runnable dr = DialogRunnableFactory.getDateTimeRunnable(type, value, min, max, callback, thiz);
        
        // queue
        UiApplication.getUiApplication().invokeLater(dr);

        // return value
        return Scriptable.UNDEFINED;
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
        fs.addParam(ScriptableFunction.class, true);
        
        return new FunctionSignature[] { fs };
    }
}

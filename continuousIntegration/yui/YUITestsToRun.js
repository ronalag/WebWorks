(function() {
    var framework = YUI.framework;
    try {

        //on blackberry
        framework.device = "Blackberry";
        framework.yuiTestsToRunArray = [

        YUI.framework.testDomain +"/yui/blackberry/bb_utils_yui.js",
        //YUI.framework.testDomain + "\yui\blackberry_dev/bb_pim_category_yui_dev.js",
		
        //YUI.framework.testDomain +"/yui/blackberry/bb_utils_yui_bindings.js",
        // TODO: add your test suites here
        ];
    } catch (e) {
    }

})();
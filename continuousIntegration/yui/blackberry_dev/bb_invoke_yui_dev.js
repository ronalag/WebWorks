(function() {
    var framework = YUI.framework;
    framework.setupFramework(generateTestCaseCallback);

    //We're passing in the Y parameter and expecting back an array of test cases
    function generateTestCaseCallback(Y) {
        var testCases = new Array();
        var Assert = Y.Assert;

        testCases["suiteName"] = "blackberry.invoke TestSuite DEV";

        testCases[0] = new Y.Test.Case({
            name: "blackberry.invoke DEV Main Tests",

            "Invoke JavaArgs should construct" : function () {
                Assert.areEqual('net_rim_bb_memo_app', (new blackberry.invoke.JavaArguments('net_rim_bb_memo_app').uri) , "test JavaArguments constructor 1");
            },
            "Invoke Search Argument should construct" : function () {
                Assert.areEqual("t1", (new blackberry.invoke.SearchArguments("t1","t2")).text , "test SearchArguments constructor 1");
                Assert.areEqual("t2", (new blackberry.invoke.SearchArguments("t1","t2")).name , "test SearcArguments constructor 2");
            },
            "Invoke Camera Argument should construct": function () {
                Assert.areEqual(0, (new blackberry.invoke.CameraArguments()).view , "test CameraArguments constructor 1");
            },
        });

        return testCases;
    }

})();
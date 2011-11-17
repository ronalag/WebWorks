(function() {
    var framework = YUI.framework;
    framework.setupFramework(generateTestCaseCallback);
	
	
    //We're passing in the Y parameter and expecting back an array of test cases
    function generateTestCaseCallback(Y) {
        var testCases = new Array();
        var Assert = Y.Assert;

        testCases["suiteName"] = "blackberry HTTP Error Codes TestSuite DEV";

        testCases[0] = new Y.Test.Case({
            name: "blackberry HTTP Error Codes DEV Main Tests",
			
			    //create test cases
			"HTTP Code Test should return 200 successful " : function () {
				var request = makeXHRCall("http://webworks/blackberry/system/hasDataCoverage");
				Y.Assert.areEqual("200", request.status, "suppose to return 200 for successful");
				Y.Assert.areEqual(true, JSON.parse(request.responseText).data["hasDataCoverage"], "true for response text"); 
			},

	
			"HTTP Code Test (dummy featureID) should return 404 not found ": function () {
				var request = makeXHRCall("http://webworks/sdgsklgkjskldgjskldgjsdklgjskldgjkskldgjsdlgjls/asd");
				Y.Assert.areEqual("404", request.status, "suppose to return 404 for forbidden"); 
			},
	
			"HTTP Code Test (not whitelisted) should return 404 not found ": function () {
				var request = makeXHRCall("http://webworks/blackberry/invoke/");
				Y.Assert.areEqual("404", request.status, "suppose to return 404 for forbidden");  
			},
	
			"Request HTTP Code Test (unexistant featureID) should return 404 not found ": function () {
				var request = makeXHRCall("http://webworks/blackberry/..  ... / .. /");
				Y.Assert.areEqual("404", request.status, "suppose to return 404 for bad request");  
			},
	
	
			" HTTP Code Test, false response text (method doesnt exist) should return 501": function () {
				var request = makeXHRCall("http://webworks/blackberry/system/sdgsklgkjskldgjskldgjs");
				Y.Assert.areEqual("501", request.status, "suppose to return 501 for not implemeneted");
			},
    
			"HTTP Code Test (not implemeneted) should return 501": function () {
				var request = makeXHRCall("http://webworks/blackberry/invoke/invoke?appType=camera://");
				Y.Assert.areEqual("501", request.status, "suppose to return 501 for not implemented");
			},
			
		});

        return testCases;
    }

})();
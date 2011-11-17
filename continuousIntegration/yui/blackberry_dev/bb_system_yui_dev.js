(function() {
    var framework = YUI.framework;
    framework.setupFramework(generateTestCaseCallback);
	
	
    //We're passing in the Y parameter and expecting back an array of test cases
    function generateTestCaseCallback(Y) {
        var testCases = new Array();
        var Assert = Y.Assert;

        testCases["suiteName"] = "blackberry.system TestSuite DEV";

        testCases[0] = new Y.Test.Case({
            name: "blackberry.system DEV Main Tests",
			
           "has Capability Sure Type should return false": function () {
            var request = makeXHRCall("http://webworks/blackberry/system/hasCapability?capability=input.keyboard.issuretype");
				Y.Assert.areEqual("200", request.status, "suppose to return 200 for successful");
				Y.Assert.areEqual(false, JSON.parse(request.responseText).data["hasCapability"], "suppose to return false for response text on default");
			},

			"has Capability Touch Input should return true": function () {
				var request = makeXHRCall("http://webworks/blackberry/system/hasCapability?capability=input.touch");
				Y.Assert.areEqual("200", request.status, "suppose to return 200 for successful");
			Y.Assert.areEqual(true, JSON.parse(request.responseText).data["hasCapability"], "suppose to return true for response text on default");
			},

			"has Capability Audio Capture should return true": function () {
				var request = makeXHRCall("http://webworks/blackberry/system/hasCapability?capability=media.audio.capture");
				Y.Assert.areEqual("200", request.status, "suppose to return 200 for successful");
				Y.Assert.areEqual(true, JSON.parse(request.responseText).data["hasCapability"], "suppose to return true for response text on default");
			},

			"has Capability Video Capture should return true": function () {
				var request = makeXHRCall("http://webworks/blackberry/system/hasCapability?capability=media.video.capture");
				Y.Assert.areEqual("200", request.status, "suppose to return 200 for successful");
				Y.Assert.areEqual(true, JSON.parse(request.responseText).data["hasCapability"], "suppose to return true for response text on default");
			},
			"has Capability Media Recording should return true" : function () {
				var request = makeXHRCall("http://webworks/blackberry/system/hasCapability?capability=media.recording");
				Y.Assert.areEqual("200", request.status, "suppose to return 200 for successful");
				Y.Assert.areEqual(true, JSON.parse(request.responseText).data["hasCapability"], "suppose to return true for response text on default");
			},   
			"has Capability GPS should return true": function () {
				var request = makeXHRCall("http://webworks/blackberry/system/hasCapability?capability=location.gps");
				Y.Assert.areEqual("200", request.status, "suppose to return 200 for successful");
				Y.Assert.areEqual(true, JSON.parse(request.responseText).data["hasCapability"], "suppose to return true for response text on default");
			},

			"has Capability Maps should return true": function () {
				var request = makeXHRCall("http://webworks/blackberry/system/hasCapability?capability=location.maps");
				Y.Assert.areEqual("200", request.status, "suppose to return 200 for successful");
				Y.Assert.areEqual(true, JSON.parse(request.responseText).data["hasCapability"], "suppose to return true for response text on default");
			},
			"has Capability Memory Card should return false": function () {
				var request = makeXHRCall("http://webworks/blackberry/system/hasCapability?capability=storage.memorycard");
				Y.Assert.areEqual("200", request.status, "suppose to return 200 for successful");
				Y.Assert.areEqual(false, JSON.parse(request.responseText).data["hasCapability"], "suppose to return false for response text on default");
			},
			"has Capability Bluetooth should return true": function () {
				var request = makeXHRCall("http://webworks/blackberry/system/hasCapability?capability=network.bluetooth");
				Y.Assert.areEqual("200", request.status, "suppose to return 200 for successful");
				Y.Assert.areEqual(true, JSON.parse(request.responseText).data["hasCapability"], "suppose to return false for response text on default");
			},
			"has Capability WLAN should return true": function () {
				var request = makeXHRCall("http://webworks/blackberry/system/hasCapability?capability=network.wlan");
				Y.Assert.areEqual("200", request.status, "suppose to return 200 for successful");
				Y.Assert.areEqual(true, JSON.parse(request.responseText).data["hasCapability"], "suppose to return false for response text on default");
			},
			"has Capability WLAN should return true": function () {
				var request = makeXHRCall("http://webworks/blackberry/system/hasCapability?capability=network.3gpp");
				Y.Assert.areEqual("200", request.status, "suppose to return 200 for successful");
				Y.Assert.areEqual(true, JSON.parse(request.responseText).data["hasCapability"], "suppose to return false for response text on default");
			},
			"has Capability CDMA should return false": function () {
				var request = makeXHRCall("http://webworks/blackberry/system/hasCapability?capability=network.cdma");
				Y.Assert.areEqual("200", request.status, "suppose to return 200 for successful");
				Y.Assert.areEqual(false, JSON.parse(request.responseText).data["hasCapability"], "suppose to return false for response text on default");
			},
			"has Capability Iden": function () {
				var request = makeXHRCall("http://webworks/blackberry/system/hasCapability?capability=network.iden");
				Y.Assert.areEqual("200", request.status, "suppose to return 200 for successful");
				Y.Assert.areEqual(false, JSON.parse(request.responseText).data["hasCapability"], "suppose to return false for response text on default");
			},
    
    
    //hasDataCoverage
			"has Data Coverage should return true" : function () {
				var request = makeXHRCall("http://webworks/blackberry/system/hasDataCoverage");
				Y.Assert.areEqual("200", request.status, "suppose to return 200 for successful");
				Y.Assert.areEqual(true, JSON.parse(request.responseText).data["hasDataCoverage"], "suppose to return true for response text on default");
			},
    
    //hasPermission
			"hasPermission should return True (blackberry.system)": function () {
				var request = makeXHRCall("http://webworks/blackberry/system/hasPermission?module=blackberry.system");
				Y.Assert.areEqual("200", request.status, "suppose to return 200 for successful"); 
				Y.Assert.areEqual("0", JSON.parse(request.responseText).data["hasPermission"], "suppose to return true for response text on default");
			},
    
		"hasPermission should return False (blackberry.app)": function () {
            var request = makeXHRCall("http://webworks/blackberry/system/hasPermission?module=blackberry.app");
            Y.Assert.areEqual("200", request.status, "suppose to return 200 for successful"); 
			Y.Assert.areEqual("0", JSON.parse(request.responseText).data["hasPermission"], "suppose to return false for response text on default");
        },
    
    //isMassStorage
			"isMassStorageActive should return False on default": function () {
				var request = makeXHRCall("http://webworks/blackberry/system/isMassStorageActive");
				Y.Assert.areEqual("200", request.status, "suppose to return 200 for successful"); 
				Y.Assert.areEqual(false, JSON.parse(request.responseText).data["isMassStorageActive"], "suppose to return false for response text on default");
			},
	
		});

        return testCases;
    }

})();
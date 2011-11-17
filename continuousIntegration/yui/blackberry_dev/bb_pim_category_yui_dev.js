(function() {
    var framework = YUI.framework;
    framework.setupFramework(generateTestCaseCallback);

    //We're passing in the Y parameter and expecting back an array of test cases
    function generateTestCaseCallback(Y) {
        var testCases = new Array();
        var Assert = Y.Assert;

        testCases["suiteName"] = "blackberry.pim.categorty TestSuite DEV";

        testCases[0] = new Y.Test.Case({
            name: "blackberry.pim.category DEV Main Tests",
			
            "pim getCategoreies should return a string array" : function () {	
				var cats = blackberry.pim.category.getCategories();
				Assert.isArray(cats);
            },
			
			"pim addCategory should add a category" : function () {	
			    blackberry.pim.category.addCategory("cat1");
				var cats1 = blackberry.pim.category.getCategories();
				
				var found = false;
				for ( var i = 0; i < cats1.length; i++)
				{
					if(cats[i] == "cat1"){
					found = true;
					break
					}
				}
				Assert.isTrue(found);
            },
			
			
			"pim addCategory should delete a category" : function () {	
			    blackberry.pim.category.deleteCategory("cat1");
				var cats1 = blackberry.pim.category.getCategories();
				
				var found = false;
				for ( var i = 0; i < cats1.length; i++)
				{
					if(cats[i] == "cat1"){
					found = true;
					break
					}
				}
				Assert.isFalse(found);
            },
			
        });

        return testCases;
    }

})();
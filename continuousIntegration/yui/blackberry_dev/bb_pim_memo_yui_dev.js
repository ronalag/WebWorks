(function() {
    var framework = YUI.framework;
    framework.setupFramework(generateTestCaseCallback);

    //We're passing in the Y parameter and expecting back an array of test cases
    function generateTestCaseCallback(Y) {
        var testCases = new Array();
        var Assert = Y.Assert;

        testCases["suiteName"] = "blackberry.pim.Memo TestSuite DEV";

        testCases[0] = new Y.Test.Case({
            name: "blackberry.pim.Memo DEV Main Tests",
			
			"pim Memo constructor should construct" : function () {	
			
				var newMemo = new blackberry.pim.Memo(); 
				newMemo.title = "Memo title 1";
				newMemo.note = "Memo title 1";
				
                Assert.areEqual("Memo title 1", newTask.title , "test pim memo constrcutor 1");
				Assert.areEqual("Memo title 1", newTask.note , "test pim memo constrcutor 2");
            },
			
			"pim Memo save/find should work" : function () {	
				    
					//save 10 Memos
					for (i = 0; i<10;i++)
					{
					var newMemo = new blackberry.pim.Memo(); 
					newMemo.title = "Memo Title number " + i; 
					newMemo.note = "Memo Note number "+i;   
					newMemo.save(); 
					}	
					
					var findvar = blackberry.pim.Memo.find();
					
					Assert.areEqual(10, findvar.length, "test length of the found Memo array");
					Assert.areEqual("Memo Title number 0" , findvar[0].title, "test element of Memo");
					Assert.isArray(findvar[0].categories);
            },
			
			"pim Memo remove should work" : function () {	

					//save one memo
					var newMemo = new blackberry.pim.Memo(); 
					newMemo.title = "Memo Dummy Title" ; 
					newMemo.note = "Memo Dummy Note";   
					newMemo.save(); 	
					
					var findvar = blackberry.pim.Memo.find();
					
					//remove all Tasks
					for(i=0;i< findvar.length;i++){
						findvar[i].remove();
					} 
					
					var findvar2 = blackberry.pim.Memo.find();
					
					Assert.areEqual(0, findvar2.length, "test removing of all tasks");
            },

			
			
        });

        return testCases;
    }

})();
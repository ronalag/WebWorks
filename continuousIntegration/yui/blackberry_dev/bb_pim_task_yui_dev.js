(function() {
    var framework = YUI.framework;
    framework.setupFramework(generateTestCaseCallback);

    //We're passing in the Y parameter and expecting back an array of test cases
    function generateTestCaseCallback(Y) {
        var testCases = new Array();
        var Assert = Y.Assert;

        testCases["suiteName"] = "blackberry.pim.Task TestSuite DEV";

        testCases[0] = new Y.Test.Case({
            name: "blackberry.pim.task DEV Main Tests",
			
            "pim Task constants should construct" : function () {	
                Assert.areEqual(0, blackberry.pim.Task.PRIORITY_HIGH , "test pim task constants 1");
				Assert.areEqual(1, blackberry.pim.Task.PRIORITY_NORMAL , "test pim task constants 2");
				Assert.areEqual(2, blackberry.pim.Task.PRIORITY_LOW , "test pim task constants 3");
				Assert.areEqual(0, blackberry.pim.Task.NOT_STARTED , "test pim task constants 4");
				Assert.areEqual(1, blackberry.pim.Task.IN_PROGRESS , "test pim task constants 5");
				Assert.areEqual(2, blackberry.pim.Task.COMPLETED , "test pim task constants 6");
				Assert.areEqual(3, blackberry.pim.Task.WAITING , "test pim task constants 7");
            },
			
			"pim Task constructor should construct" : function () {	
			
				var newTask = new blackberry.pim.Task(); 
				newTask.summary = "Get Groceries"; 
				newTask.note = "Pick up eggs, and Milk";  
				newTask.priority = blackberry.pim.Task.PRIORITY_HIGH;  
			
                Assert.areEqual("Get Groceries", newTask.summary , "test pim task constrcutor 1");
				Assert.areEqual("Pick up eggs, and Milk", newTask.note , "test pim task constrcutor 2");
				Assert.areEqual(blackberry.pim.Task.PRIORITY_HIGH, newTask.priority , "test pim task constrcutor 3");
            },
			
			"pim Task save/find should work" : function () {	
				    
					//save 10 tasks
					for (i = 0; i<10;i++)
					{
					var newTask = new blackberry.pim.Task(); 
					newTask.summary = "Get Groceries" + i; 
					newTask.note = "Pick up eggs, and Milk"+i;  
					newTask.priority = blackberry.pim.Task.PRIORITY_HIGH;  
					newTask.save(); 
					}	
					
					var findvar = blackberry.pim.Task.find();
					
					Assert.areEqual(10, findvar.length, "test length of the found task array");
					Assert.areEqual(blackberry.pim.Task.PRIORITY_HIGH, findvar[5].priority, "test element of task");
            },
			
			"pim Task remove should work" : function () {	

					//save one task
					var newTask = new blackberry.pim.Task(); 
					newTask.summary = "Get Groceries dummy " ; 
					newTask.note = "Pick up eggs, and Milk dummy";  
					newTask.priority = blackberry.pim.Task.PRIORITY_HIGH;  
					newTask.save(); 	
					
					var findvar = blackberry.pim.Task.find();
					
					//remove all Tasks
					for(i=0;i< findvar.length;i++){
						findvar[i].remove();
					} 
					
					var findvar2 = blackberry.pim.Task.find();
					
					Assert.areEqual(0, findvar2.length, "test removing of all tasks");
            },

			
			
        });

        return testCases;
    }

})();
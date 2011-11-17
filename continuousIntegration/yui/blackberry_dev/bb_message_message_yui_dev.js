(function() {
    var framework = YUI.framework;
    framework.setupFramework(generateTestCaseCallback);

    //We're passing in the Y parameter and expecting back an array of test cases
    function generateTestCaseCallback(Y) {
        var testCases = new Array();
        var Assert = Y.Assert;

        testCases["suiteName"] = "blackberry.message.Message TestSuite DEV";

        testCases[0] = new Y.Test.Case({
            name: "blackberry.message.Message DEV Main Tests",
			
			"Message constants should construct" : function () {	
                Assert.areEqual(-1, blackberry.message.Message.STATUS_UNKNOWN , "test message constants 1");
				Assert.areEqual(0, blackberry.message.Message.STATUS_SAVED , "test message constants 2");
				Assert.areEqual(1, blackberry.message.Message.STATUS_DRAFT , "test message constants 3");
				Assert.areEqual(2, blackberry.message.Message.STATUS_SENT , "test message constants 4");
				Assert.areEqual(3, blackberry.message.Message.STATUS_ERROR_OCCURED , "test message constants 5");
				Assert.areEqual(0, blackberry.message.Message.PRIORITY_HIGH , "test message constants 6");
				Assert.areEqual(1, blackberry.message.Message.PRIORITY_MEDIUM , "test message constants 7");
				Assert.areEqual(2, blackberry.message.Message.PRIORITY_LOW , "test message constants 8");
				Assert.areEqual(0, blackberry.message.Message.FOLDER_INBOX , "test message constants 9");
				Assert.areEqual(1, blackberry.message.Message.FOLDER_SENT , "test message constants 10");
				Assert.areEqual(2, blackberry.message.Message.FOLDER_DRAFT , "test message constants 11");
				Assert.areEqual(3, blackberry.message.Message.FOLDER_OUTBOX , "test message constants 12");
				Assert.areEqual(4, blackberry.message.Message.FOLDER_DELETED , "test message constants 13");
				Assert.areEqual(5, blackberry.message.Message.FOLDER_OTHER , "test message constants 14");
				
            },
			
			
			"Message constructor should construct" : function () {	
			
				var message = new blackberry.message.Message();
				message.toRecipients = "noone@blackberryWidgets.com";
				message.subject = "Hello";
				message.body = "World";
				
                Assert.areEqual("noone@blackberryWidgets.com", message.toRecipients , "test message constrcutor 1");
				Assert.areEqual("Hello", message.subject , "test message constrcutor 2");
				Assert.areEqual("World", message.body , "test message constrcutor 3");
            },
			
			"Message save/send/find should work" : function () {	
				    
					var findmsg = blackberry.message.Message.find ();
					//remove all message first
					for(i=0;i< findmsg.length;i++){
						findmsg[i].remove();
					} 
					
					Assert.areEqual(0, findvar2.length, "test removing of all tasks");
					
					//save 10 Messages
					for (i = 0; i<10;i++)
					{
					var message = new blackberry.message.Message(); 
					message.toRecipients = "noone@blackberryWidgets.com " + i;
					message.subject = "Hello " + i;
					message.body = "World " + i;  
					message.save(); 
					}	
					
					var filter = new blackberry.find.FilterExpression("folder", "==", blackberry.message.Message.FOLDER_DRAFT, false);
					var findvar = blackberry.pim.Memo.find(filter);
					
					Assert.areEqual(10, findvar.length, "test length of the found draft message array");
					Assert.areEqual("World 0" , findvar[0].body, "test body element of Message");

					
					
					//send10 Messages
					for (i = 0; i<10;i++)
					{
					var message = new blackberry.message.Message(); 
					message.toRecipients = "snoone@blackberryWidgets.com " + i;
					message.subject = "sHello " + i;
					message.body = "sWorld " + i;  
					message.send); 
					}	
					
					var filter = new blackberry.find.FilterExpression("folder", "==", blackberry.message.Message.FOLDER_SENT, false);
					var findvar = blackberry.pim.Memo.find(filter);
					
					Assert.areEqual(10, findvar.length, "test length of the found sent message array");
					Assert.areEqual("sWorld 0" , findvar[0].body, "test body element of Message");
					
					
					
            },
			

			
			
        });

        return testCases;
    }

})();
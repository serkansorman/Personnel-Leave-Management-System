



function sendMessage(receiverEmail){
	
	
	if (document.getElementById("title").value == "" || document.getElementById("content").value == ""){
		toastr.error("Please fill out these fields");
	}
	else{
		
		var message = {
				sender: authenticatedUser.email,
				receiver: receiverEmail,
				title: document.getElementById("title").value,
				content: document.getElementById("content").value
		}
		
		
		$.ajax({
	        url: 'rest/message/sendMessage',
	        type: 'POST',
	        dataType: 'json',
	        contentType: 'application/json',
	        data: JSON.stringify(message),
	        success: function () {
			    	toastr.success("Message Sent to "+receiverEmail);
	        },
	    });
	}
	
	
		
}




function getMessages(){
	
	showHide();	
	
	$("#messagesTable > tbody").html("");

	document.getElementById('messagesTableDiv').className="show";
	
	
	$.ajax({
        url: 'rest/message/getAllMessages',
        type: 'POST',
        dataType: 'json',
        contentType: 'application/json',
        data: authenticatedUser.email,
        success: function (messages) {
        	$.each(messages, function(key, message) {
        		
            	$("#messagesTable").append(
            			"<tr><td>" + message.sender + "</td><td>" + 
            			message.title + "</td><td>" +
            			message.content + "</td><td>" +  
            			'<button id="' + message.id + '" class="btn btn-danger">Remove</button>' + "</td><td>" + 
            			'<button id="' + message.id+message.sender + '" class="btn btn-primary">Reply</button>' + "</td><td  style=' width: 6%;'>" +
            			
            			"</td></tr>");
            	

	    	    document.getElementById(message.id).addEventListener("click", function() {
	    	    	removeMessage(message.id);
	    	    }, false);
	    	    
	    	    document.getElementById(message.id+message.sender).addEventListener("click", function() {
	    	    	document.getElementById('messagesTableDiv').className="hide";
	    	    	document.getElementById('messageForm').className="show";
	    	    	
	    	    	 document.getElementById("submit").addEventListener("click", function() {
	 	    	    	sendMessage(message.sender);
	 	    	    }, false);
	    	    }, false);
	    	    
	    	   
            	
        	});	
        }
    });
		
}

function removeMessage(messageID){
	
	$.ajax({
	    url: "rest/message/deleteMessage",
	    type: "POST",
	    mimeType: "application/json",
	    contentType: "application/json",
	    data: messageID.toString(),
	    success: function() {
	    	getMessages();
	    	toastr.success("Message was deleted succesfully");

	    },
	    error: function(jqXHR, textStatus, errorThrown) {
	
	    }
	  });
	
}
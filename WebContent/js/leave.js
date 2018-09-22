function showLeaveRequest(){
	
	showHide();
	
	var leavesUrl;
	var sendType;
	var isAdmin = $.inArray("admin",authenticatedUser.roles ); // -1 değilse rolü admindir.
	
	// Kullanıcı adminse method tipi ve pathi değiştirilir.
	if(isAdmin != -1){
		leavesUrl = "rest/leave/getLeaveRequestsAdmin";
		sendType = "GET";
	} 
		
	else{ // Kullanıcı proje yöneticisi
		leavesUrl = "rest/leave/getLeaveRequestsPM";
		sendType = "POST";
	}
		
	$("#leaveRequestTable > tbody").html("");
	document.getElementById('leaveRequestTableDiv').className="show";
	
	 $.ajax({
	    type: sendType,
	    url: leavesUrl,
	    contentType: "application/json",
	    mimeType: "application/json",
	    data: authenticatedUser.email,
	    success: function(leaves) {
	    $.each(leaves, function(key, leave) {
	      	
	    	var historyButtonID = leave.owner.email+","+leave.id;
	    	$("#leaveRequestTable").append("<tr><td>" + leave.owner.name + 
	    			"</td><td>" + leave.owner.email + "</td><td>" + 
	    			leave.beginDate + "</td><td>" + 
	    			leave.endDate + "</td><td>" +
	    			leave.owner.totalLeaveDays + "</td><td style=' width: 10%;'>" + 
		    	'<button id="' + historyButtonID + '" class="btn btn-primary">Leave History</button>' + "</td><td  style=' width: 6%;'>" + 
   	    		'<button id="' + "a"+leave.id + '" class="btn btn-success" >Accept</button>' + "</td><td style=' width: 7%;'>" + 
   	    		'<button id="' + "d"+leave.id + '" class="btn btn-danger" >Decline</button>' +"</td></tr>");
	    	
	    	 document.getElementById("a"+leave.id).addEventListener("click", function() {
	    		 	/* Kullanıcının rolü proje yöneticisiyse ve izni onaylarsa 
	    		 	admine iletilmek üzere statusu on Admin yapılır*/
	    		 	if(isAdmin == -1)
	    	    		respondLeaveRequest("a"+leave.id,"on Admin");
	    		 	else{// Admine gelen izin onaylanırsa izin kabul edilir.
	    	    		respondLeaveRequest("a"+leave.id,"Accepted");
						decreaseUserLeaveDays(leave);
	    		 	}
	    	  }, false);
	    	 
	    	 //Admin veya proje yöneticisinin izin isteiğini reddetmesi
	    	 document.getElementById("d"+leave.id).addEventListener("click", function() {
	    	    	respondLeaveRequest("d"+leave.id,"Refused");
	    	 }, false);
	    	 
	    	 
	    	//İzin isteği yollayan personellerin izin geçmişinin görüntülenmesi
	    	 document.getElementById(historyButtonID).addEventListener("click", function() {
	    		 	var email = historyButtonID.split(',')[0];
	    		 	getPersonnelLeaveHistory(email);
	    	 }, false);
	   	    		
	      });    
	    },
	    error: function() {
	      console.log("getAllUsers error");
	    }
	  });
}



function showLeaveHistory(){
	
	showHide();
	
	$("#ownLeaveTable > tbody").html("");

	document.getElementById('ownLeaveTableDiv').className="show";
	
	
	  $.ajax({
		  url: "rest/leave/getLeaves",
		    type: "POST",
		    mimeType: "application/json",
		    contentType: "application/json",
		    data: authenticatedUser.email,
		    success: function(leaves){
		    $.each(leaves, function(key, leave) {
		    	
		    	var cancelButton;
		    	
		    	if(leave.status == "on Project Manager" || leave.status == "on Admin" )
		    		cancelButton = "</td><td>" + '<button id="' + leave.id + '"onclick="'+"cancelLeave("+leave.id+")"+'" class="btn btn-danger">Cancel</button></td>';
		    	else
		    		cancelButton = "</td><td> </td>"
		    	cancelButton += "</tr>";
		    	
		    	
		    	$("#ownLeaveTable").append(
		    			"<tr><td>"  + leave.beginDate 
		    			+ "</td><td>" + leave.endDate 
		    			+ "</td><td>" + leave.status
		    			+ cancelButton);
		    
		      });    
		    },
		    error: function() {
		      console.log("getAllUsers error");
		    }
		  }); 
}




function addLeave() {
	
	var status;
	var isEmployee = $.inArray("employee",authenticatedUser.roles);
	var workDays = getWorkDays();
	
	if (workDays != -100) {
		// Personelin izin hakkı ile aldığı izin karşılaştırılır.
		if(workDays <= authenticatedUser.totalLeaveDays){
			
			//Personel izin alırsa izin isteği proje yöneticisne yollanır.
			if(isEmployee != -1)
				status = "on Project Manager";
			else//Proje yöneticisi izin alırsa istek direkt olarak admine gider
				status = "on Admin";
			
			
		    var leave = {
		   	
		        owner: {
		        	email: authenticatedUser.email
		        },
		        beginDate: document.getElementById("txtFrom").value,
		        endDate: document.getElementById("txtTo").value,
		        status: status,
		        workDays: workDays
		    }

		    $.ajax({
		        url: 'rest/leave/addLeave',
		        type: 'POST',
		        dataType: 'json',
		        contentType: 'application/json',
		        data: JSON.stringify(leave),
		        success: function () {
		        	if(isEmployee != -1){
		        		toastr.success("Number of Work Days: "+workDays+"\nPlease wait response from "+authenticatedUser.projectManager);
		        	}else{
		        		toastr.success("Leave request sent successfully, wait response from Admin");
		        	}
					
		        	
		        	hideDatePicker();
		        },
		    });
		}else{
			toastr.error("You do not have enough leave days.");
		}
	} else {
		toastr.warning("Tarih aralığı giriniz.")
	}
	
	

	
    
}




function respondLeaveRequest(id,status){
	

	var leaveResponse = {
			sender: authenticatedUser.email,
			leaveID: id.substring(1,id.length),
			status: status
	}
	
    $.ajax({
        url: 'rest/leave/respondLeaveRequest',
        type: 'POST',
        dataType: 'json',
        contentType: 'application/json',
        data: JSON.stringify(leaveResponse),
        success: function () {
        	
        	if(status == "on Admin" ){
        		//alert("Leave request sent to Admin");
        		toastr.success("Leave request sent to Admin");
        	}else if(status == "Accepted"){
        		//alert("Leave request is Accepted");
        		toastr.success("Leave request is Accepted");
        	}else{
        		//alert("Leave request is Refused");
        		toastr.warning("Leave request is Refused");
        	}
        	showLeaveRequest(); // Listeyi güncelle

        	
        },
    });
}


function getPersonnelLeaveHistory(email){
	
	showHide();
	
	$("#leaveHistoryTable > tbody").html("");
	$("#personnelEmail").text(email);

	document.getElementById('leaveHistoryTableDiv').className="show";
	
	$.ajax({
        url: 'rest/leave/getLeaves',
        type: 'POST',
        dataType: 'json',
        contentType: 'application/json',
        data: email,
        success: function (leaves) {
        	$.each(leaves, function(key, leave) {
        		
        		hideLeaveRequests();
				
        		//İşlem olarak tamamlanmış izinleri historyde göster.
        		if(leave.status == "Refused" || leave.status == "Accepted")
            	$("#leaveHistoryTable").append(
            			"<tr><td>" + leave.beginDate + "</td><td>" + 
    	    			leave.endDate + "</td><td>" +
    	    			leave.status + "</td><td>" + "</td></tr>");
            	
        	});	
        }
    });
}

function hideLeaveRequests(){
	document.getElementById("leaveRequestTableDiv").className="hide";

}


function cancelLeave(leaveID){
	
	
	$.ajax({
	    url: "rest/leave/cancelLeave",
	    type: "POST",
	    mimeType: "application/json",
	    contentType: "application/json",
	    data: leaveID.toString(),
	    success: function() {
	    	//alert("Leave was cancelled succesfully");
	    	toastr.success("Leave was cancelled succesfully");
	    	showLeaveHistory();
	    },
	    error: function(jqXHR, textStatus, errorThrown) {
	
	    }
	  });
}



function showAddLeave(){
	showHide();
	
	document.getElementById('leaveDaysForm').className="show";
	
	$.ajax({
	    type: "GET",
	    url: 'rest/user/getAllUsers',
	    contentType: "application/json",
	    mimeType: "application/json",
	    success: function(users) {
	      $.each(users, function(key, value) {
	    	  if(authenticatedUser.email != value.email){ //Giriş yapan admin personellerde gözükmez.
		    	    $("#selectPersonEmail").append("<option>"+value.email+"</option>");
	    	  }
	      });
	    },
	    error: function() {
	      console.log("getAllUsers error");
	    }
	  });

}


function addLeaveAdmin(){
	
	var email = $('#selectPersonEmail').val();
	var leaveDays =  $('#inputLeaveDays').val();
	
	var user = {
			email: email,
			totalLeaveDays: leaveDays
	}
	
	 $.ajax({
	        url: 'rest/user/addUserLeaveDays',
	        type: 'POST',
	        dataType: 'json',
	        contentType: 'application/json',
	        data: JSON.stringify(user),
	        success: function () {
			    	//alert(leaveDays+" leave days added to "+user.email);
			    	toastr.success(leaveDays+" leave days added to "+user.email);
			    	document.getElementById('leaveDaysForm').className="hide";

	        },
	    });
}

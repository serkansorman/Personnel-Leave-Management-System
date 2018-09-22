$("#navbarLinks > li").hide(); // Navbardaki butonları başlangıçta gizle
var authenticatedUser;
var beginDate;
var endDate;
authenticateUser();
getDatePicker();
function authenticateUser() {
	
	  $.get("LoginServlet", function() {
	    $.ajax({
	      type: "POST",
	      url: 'rest/user/getAuthenticatedUser',
	      contentType: "application/json",
	      mimeType: "application/json",
	      success: function(data) {
	        authenticatedUser = data;
	        //Rollere göre nav bar düzenlenir.
	        $.each(authenticatedUser.roles, function(key, value) {
	          	if(value == "admin"){
	                $("#nav_leave_requests").show();
	                $("#nav_project_managers").show();
	                $("#nav_show_personnel").show();
	                $("#nav_add_leave_days").show();
	                $("#nav_add_new_user").show();
	                $("#nav_leave_messages").show();

	          	}
	          	else if(value == "projectManager"){
	          	  $("#nav_leave_requests").show();
	              $("#nav_project_members").show();
	              $("#nav_add_leave").show();
	              $("#nav_leave_messages").show();
	          	}
	          	else{
	          	  $("#nav_history").show();
	              $("#nav_add_leave").show();
	          	}
	        });
	      }
	    });
	  });
}
function logout() {
	
	  $.get("LogoutServlet", function() {
		  window.location.replace("main.html");
	  });
}


function showAllPersonnel(){
	
	showHide();
	
	$("#personnelTable > tbody").html("");

	document.getElementById('personnelTableDiv').className="show";
	
	 $.ajax({
	    type: "GET",
	    url: 'rest/user/getAllUsers',
	    contentType: "application/json",
	    mimeType: "application/json",
	    success: function(users) {
	      $.each(users, function(key, value) {
	    	  //Sadece personelleri göster
	    	  if(authenticatedUser.email != value.email && value.projectManager != "none"){ 		    	    $("#personnelTable").append("<tr><td>" + value.name + "</td><td>" + 
	   	    		value.email + "</td><td>" +  
	   	    		value.department + "</td><td>" + 
	   	    		value.projectManager + "</td><td>" + 
	   	    		value.totalLeaveDays +"</td><td>" + 
	   	    		'<button id="' + value.email + '" class="btn btn-danger">Remove</button>' + "</td></tr>");
	   	    		
	   	    		//Hangi personel silinecekse onun emaili buttonun idsinden get edilir.
		    	    document.getElementById(value.email).addEventListener("click", function() {
		    	    	deletePersonnel(value.email);
		    	    }, false);
	    	  }
	      });
	    },
	    error: function() {
	      console.log("getAllUsers error");
	    }
	  });
}

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
	
	
function showDatePicker() {
	showHide();
	
	document.getElementById('datepicker').className="show";
	document.getElementById('sendLeaveRequest').className="btn btn-primary show";
}
function hideDatePicker(){
	
	document.getElementById('datepicker').className="hide";
	document.getElementById('sendLeaveRequest').className="btn btn-primary hide";
}


function showProfile(){
	
	showHide();
	
	document.getElementById('profile').className="show";
	
	 $("#user_name_title").append(authenticatedUser.name);
	 $("#user_name").append(authenticatedUser.name);
	 $("#user_email").append(authenticatedUser.email);
	 $("#user_department").append(authenticatedUser.department);
	 $("#project_manager_email").append(authenticatedUser.projectManager);
	 $("#total_leave_days").append(authenticatedUser.totalLeaveDays);
	 
	 //Userin rolleri profilde belirtilir.
	 for (var index = 0; index < authenticatedUser.roles.length; ++index) {
		 $("#user_role_title").append(authenticatedUser.roles[index]+" ");
		 $("#user_role").append(authenticatedUser.roles[index]+" ");
	 }
	
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

function getDatePicker(){
	
	$(document).ready(function() {
	    jQuery("#txtFrom").datepicker({
	        dateFormat: 'mm/dd/yy',
	        changeMonth: true,
	        changeYear: true,
 	        minDate:0, 
	        onClose: function( selectedDate ) {
	        	jQuery( "#txtTo" ).datepicker( "option", "minDate", selectedDate );
	        }
	    });
	    jQuery("#txtTo").datepicker({
	        dateFormat: 'mm/dd/yy',
	        changeMonth: true,
	        changeYear: true,
	        onClose: function( selectedDate ) {
	        	jQuery( "#txtFrom" ).datepicker( "option", "maxDate", selectedDate );
	        }
	    });
	});
	
}


function getWorkDays() {
	
	if($("#txtFrom").val() && $("#txtTo").val()){
		var startDate = new Date(document.getElementById("txtFrom").value);
		var endDate = new Date(document.getElementById("txtTo").value);
	    var count = 0;
	    var curDate = startDate;
	    
	    
	    while (curDate <= endDate) {
	        var dayOfWeek = curDate.getDay();
	        if(!((dayOfWeek == 6) || (dayOfWeek == 0)))
	           count++;
	        curDate.setDate(curDate.getDate() + 1);
	    }
	    return count;
		
	}else{
		return -100;
	}
	
	
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
		        		//alert("Number of Work Days: "+workDays+"\nPlease wait response from "+authenticatedUser.projectManager);
		        		toastr.warning("Number of Work Days: "+workDays+"\nPlease wait response from "+authenticatedUser.projectManager);
		        	}else{
		        		//alert("Leave request sent successfully, wait response from Admin");
		        		toastr.success("Leave request sent successfully, wait response from Admin");
		        	}
					
		        	
		        	hideDatePicker();
		        },
		    });
		}else{
			//alert("You do not have enough leave days.");
			toastr.error("You do not have enough leave days.");
		}
	} else {
		toastr.warning("Tarih aralığı giriniz.")
	}
	
	

	
    
}

function deletePersonnel(email){
	
	
	  $.ajax({
	    url: "rest/user/deleteUser",
	    type: "POST",
	    mimeType: "application/json",
	    contentType: "application/json",
	    data: email,
	    success: function() {
	    	//alert(email + " was deleted succesfully");
	    	toastr.success(email + " was deleted succesfully");
	    	showAllPersonnel(); // Personel listesi ekranda güncellenr
	    },
	    error: function(jqXHR, textStatus, errorThrown) {
	
	    }
	  });
		
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


function decreaseUserLeaveDays(leave){
	

	 $.ajax({
	        url: 'rest/user/decreaseUserLeaveDays',
	        type: 'POST',
	        dataType: 'json',
	        contentType: 'application/json',
	        data: JSON.stringify(leave),
	        success: function () {
			     console.log("User: "+leave.owner.email+" leave days decreased");	
	        },
	    });
}

function addNewUserPageShow(){
	showHide();
	
	document.getElementById("addNewUserForm").className="show";
}


function addNewUser(){
	var email = $('#email').val();
	var fullName =  $('#fullName').val();
	var projectManager = $('#projectManager').val();
	var totalLeaveDays =  $('#totalLeaveDays').val();
	var department = $('#department').val();
	var password = $('#password').val();
	
	var projectManagerCheckBox = document.getElementById("projectManagerCheckBox").checked;
	var employeeCheckBox = document.getElementById("employeeCheckBox").checked;
	var adminCheckBox = document.getElementById("adminCheckBox").checked;
	
	var flag = true;
	
	var roles = [];
	
	if(projectManagerCheckBox==true && employeeCheckBox==true && adminCheckBox==true){
		roles[0]="projectManager";
		roles[1]="admin";
		roles[2]="employee";
	}
	else if(projectManagerCheckBox==true && employeeCheckBox==true ){
		roles[0]="projectManager";
		roles[1]="employee";
	}
	else if(employeeCheckBox==true && adminCheckBox==true){
		roles[0]="admin";
		roles[1]="employee";
	}
	else if(projectManagerCheckBox==true && adminCheckBox==true){
		roles[0]="admin";
		roles[1]="projectManager";
	}
	else if(projectManagerCheckBox==true){
		roles[0]="projectManager";
	}
	else if(employeeCheckBox==true){
		roles[0]="employee";
	}
	else if(adminCheckBox==true){
		roles[0]="admin";
	}
	else{
    	//alert(" Please select at least one role");
    	toastr.warning(" Please select at least one role");
    	flag = false;

	}
	
	

	if(flag==true){
		var user = {
				email: email,
				department: department,
				projectManager: projectManager,
				name: fullName,
				roles:roles,
				totalLeaveDays:totalLeaveDays,
				password:password
		}
		
		 $.ajax({
		        url: 'rest/user/addNewUser',
		        type: 'POST',
		        dataType: 'json',
		        contentType: 'application/json',
		        data: JSON.stringify(user),
		        success: function () {
				    	//alert(fullName+" succesfully added");
				    	toastr.success(fullName+" succesfully added");
				    	document.getElementById('leaveDaysForm').className="hide";

		        },
		    });
	}
	

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


function getProjectManagers(){
	
	showHide();
	
	$("#pmanagerTable > tbody").html("");

	document.getElementById('pmanagerTableDiv').className="show";
	
	 $.ajax({
	    type: "GET",
	    url: 'rest/user/getAllUsers',
	    contentType: "application/json",
	    mimeType: "application/json",
	    success: function(users) {
	      $.each(users, function(key, value) {
	    	  //Sadece proje yöneticilerini göster
	    	  if($.inArray("projectManager",value.roles ) != -1){ 
		    	    $("#pmanagerTable").append("<tr><td>" + value.name + "</td><td>" + 
	   	    		value.email + "</td><td>" +  
	   	    		value.department + "</td><td>" + 
	   	    		'<button id="' + "r"+value.email + '" class="btn btn-danger">Remove</button>' + "</td><td>" +
	   	    		'<button id="' + "c"+value.email + '" class="btn btn-secondary">Contact</button>' + 
	   	    		"</td></tr>");
	   	    		
	   	    		//Hangi personel silinecekse onun emaili buttonun idsinden get edilir.
		    	    document.getElementById("r"+value.email).addEventListener("click", function() {
		    	    	deletePersonnel(value.email);
		    	    }, false);
	   	    		
	   	    	
		    	    document.getElementById("c"+value.email).addEventListener("click", function() {
		    	    	document.getElementById('pmanagerTableDiv').className="hide";
		    	    	document.getElementById('messageForm').className="show";


		    	    }, false);
		    	    
		    	    
		    	    document.getElementById("submit").addEventListener("click", function() {
		    	    	sendMessage(value.email);
		    	    }, false);
	    	 }
	      });
	    },
	    error: function() {
	      console.log("getAllUsers error");
	    }
	  });
}



function sendMessage(receiverEmail){
	
	
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

function showProjectMembers(){
	
	showHide();
	
	$("#employersTable > tbody").html("");

	document.getElementById('employersTableDiv').className="show";
	
	
	  $.ajax({
		  url: "rest/user/getEmployeersOfProjectManager",
		    type: "POST",
		    mimeType: "application/json",
		    contentType: "application/json",
		    data: authenticatedUser.email,
		    success: function(employeers){
		    $.each(employeers, function(key, employee) {
		    	
	           var cancelButton;
		    	
		    	
	           cancelButton = "</td><td>" + '"<button id="' + employee.email + '"onclick="'+'">Cancel</button></td>';
		       cancelButton += "</tr>";
		    	

		       $("#employersTable").append(
		    			"<tr><td>"  + employee.name
		    			+ "</td><td>" + employee.email 
		    			+ "</td><td>" + employee.department
		    			+ "</td><td>" + employee.totalLeaveDays
		    			+"</td><td>" + '<button id="' + employee.email + '" class="btn btn-danger">Remove</button>' + "</td></tr>");
			    document.getElementById(employee.email).addEventListener("click", function() {
		    		 removeEmployeeFromProject(employee.email);
		    	}, true);
		    
		      });    
		    },
		    error: function() {
		      console.log("getAllUsers error");
		    }
		  }); 
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
function removeEmployeeFromProject(deletedEmployeeEmail){
	
	
	$.ajax({
	    url: "rest/user/removeEmployeeFromProject",
	    type: "POST",
	    mimeType: "application/json",
	    contentType: "application/json",
	    data: deletedEmployeeEmail.toString(),
	    success: function() {
 	    	showProjectMembers();	    	
	    	toastr.success(deletedEmployeeEmail+" was removed from project succesfully");
	    	
	    },
	    error: function(jqXHR, textStatus, errorThrown) {
	
	    }
	  });
}

function showUpdateUserPage(leaveID){
	showHide();
	document.getElementById("updateUserForm").className="show";
	
}


function updateUser(){
		
	var oldpassword = $('#oldpassword').val();
	var newpassword =  $('#newpassword').val();

	
	var user = {
			email: authenticatedUser.email,
			password: newpassword
	}
	if(oldpassword==authenticatedUser.password && newpassword!=oldpassword){
		
		 $.ajax({
		        url: 'rest/user/updateUser',
		        type: 'POST',
		        dataType: 'json',
		        contentType: 'application/json',
		        data: JSON.stringify(user),
		        success: function () {
				    	toastr.success("Your password is successfully changed");

		        },
		    });
	}
	else if(newpassword==oldpassword){
		toastr.error("Nothing changed");

	}
	else{
		toastr.error("Wrong old password");
	}

}

function showHide(){
	document.getElementById('personnelTableDiv').className="hide";

	document.getElementById('leaveRequestTableDiv').className="hide";

	document.getElementById('datepicker').className="hide";
	document.getElementById('sendLeaveRequest').className="btn btn-primary hide";

	document.getElementById('profile').className="hide";

	document.getElementById('ownLeaveTableDiv').className="hide";

	document.getElementById('leaveHistoryTableDiv').className="hide";

	document.getElementById("addNewUserForm").className="hide";

	document.getElementById('leaveDaysForm').className="hide";

	document.getElementById('pmanagerTableDiv').className="hide";

	document.getElementById('messageForm').className="hide";

	document.getElementById('employersTableDiv').className="hide";

	document.getElementById('messagesTableDiv').className="hide";
	
	
}
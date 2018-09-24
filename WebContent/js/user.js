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
	   	    		'<button id="' + "c"+value.email + '" class="btn btn-primary">Contact</button>' + 
	   	    		"</td></tr>");
	   	    		
	   	    		//Hangi personel silinecekse onun emaili buttonun idsinden get edilir.
		    	    document.getElementById("r"+value.email).addEventListener("click", function() {
		    	    	deletePersonnel(value.email);
		    	    }, false);
	   	    		
	   	    	
		    	    document.getElementById("c"+value.email).addEventListener("click", function() {
		    	    	document.getElementById('pmanagerTableDiv').className="hide";
		    	    	document.getElementById('messageForm').className="show";
		    	    	
		    	    	 document.getElementById("submit").addEventListener("click", function() {
				    	    	sendMessage(value.email);
				    	 }, false);

		    	    }, false);
		    	    
		    	    
		    	   
	    	 }
	      });
	    },
	    error: function() {
	      console.log("getAllUsers error");
	    }
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
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
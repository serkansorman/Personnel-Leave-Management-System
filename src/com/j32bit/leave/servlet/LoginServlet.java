package com.j32bit.leave.servlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.j32bit.leave.bean.User;
import com.j32bit.leave.service.ServiceFacade;


public class LoginServlet extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		
		System.out.println("Entered Servlet");
		String email = request.getUserPrincipal().getName();
		
		User authenticatedUser = null;
		try {
			
			authenticatedUser = ServiceFacade.getInstance().getUserDAO().getUser(email);
			HttpSession session = request.getSession();
			session.setAttribute("LOGIN_USER", authenticatedUser);
			response.sendRedirect("/LeaveManagement/main.html");
			
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
	}
	
}

package org.collin.dashboard.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CollinServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final String S_REDIRECT_URL = "http://demo.condast.com/TeamZodiac/embed.html";
	private static final String S_PATH = "path";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//RequestDispatcher dispatcher = getServletContext()
		//	      .getRequestDispatcher("http://www.condast.com");
		//	    dispatcher.forward(req, resp);
		String path = req.getParameter(S_PATH); 
		resp.sendRedirect( path );
	}

	
}

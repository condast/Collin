package org.collin.dashboard.servlet;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Alias: base url +"/' + ponte
 * @author Kees
 *
 */
public class CollinServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final String S_RESPONSE = 
			"<html>\r\n" + 
			"\t<head>\r\n" + 
			"\t\t<script language=\"javascript\">\r\n" +
			"\t\t\tfunction redirect(){\r\n" + 
			"\t\t\t\t var newWin = window.open('$(PATH)',toolbar='no',menubar='no',location='no','_blank');\r\n" +
			"\t\t\t }\r\n" + 
			"\t\t\twindow.onLoad = redirect;\r\n" + 
			"\t\t</script>\r\n" + 
			"\t</head>\r\n\t" + 
			"<body onload=\"redirect()\">\r\n" + 
			"</body>\r\n</html>";
	
	private static final String S_PATH = "path";
	
	private int counter;
	
	public CollinServlet() {
		super();
		counter = 0;
	}



	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		//response.addHeader("Refresh", "5");
		String path = req.getParameter(S_PATH);
		path += "?counter=" + counter;
		String result = S_RESPONSE;
		result = result.replaceAll("\\$\\(PATH\\)", path);
		Writer writer = response.getWriter();
		writer.append(result);
		//response.sendRedirect( response.encodeRedirectURL( path ));
		//RequestDispatcher dispatcher = req.getRequestDispatcher(path);
		//dispatcher.forward(req, response);
		writer.flush();
		writer.close();
		counter++;
	}

	
}

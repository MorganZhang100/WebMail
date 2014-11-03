package cs601.webmail.page;

import cs601.webmail.module.UserModule;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HomePage extends Page {
	public HomePage(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}


    @Override
    public void headerMore() {
        out.println("<link href=\"http://localhost:8081/css/home.css\" rel=\"stylesheet\">");
    }

	@Override
	public void body() {
        UserModule user = new UserModule();
        user.currentUser(request);

        out.println(user.getLoginName());
        out.println("<button id='logout'>Log Out</button><br><script src=\"http://localhost:8081/js/logout.js\"></script>");
        out.println("<div id=\"up_big_containter\"> " +
                    "   <div class=\"container\">\n" +
                    "       <div id=\"up_containter\">\n" +
                    "       </div>" +
                    "   </div>" +
                    "</div>");
	}
}

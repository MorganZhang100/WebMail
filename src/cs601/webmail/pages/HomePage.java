package cs601.webmail.pages;

import cs601.webmail.managers.UserManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HomePage extends Page {
	public HomePage(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}


    @Override
    public void headerMore() {
        out.println("<link href=\"http://localhost:8081/css/login.css\" rel=\"stylesheet\">");
    }

	@Override
	public void body() {
        UserManager user = new UserManager();
        user.currentUser(request);

        out.println(user.getLoginName());
        out.println("<button id='logout'>Log Out</button><script src=\"http://localhost:8081/js/logout.js\"></script>");
	}
}

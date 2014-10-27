package cs601.webmail.pages;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UseRegistration extends Page {
	public UseRegistration(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}

	@Override
	public void body() {

        out.println("This is the news page");
	}
}

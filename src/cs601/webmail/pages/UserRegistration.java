package cs601.webmail.pages;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserRegistration extends Page {
	public UserRegistration(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}

    @Override
    public void headerMore() {
        out.println("<link href=\"http://localhost:8081/css/login.css\" rel=\"stylesheet\">");
    }

	@Override
	public void body() {

        out.println("<div class=\"container\">\n" +
                "\n" +
                "      <h2 class=\"form-signin-heading text-center\">Create Your Account</h2>\n" +
                "\n" +
                "      <div class=\"card card-signin\">\n" +
                "        <form class=\"form-signin\" role=\"form\">\n" +
                "          <div class=\"form-group\">\n" +
                "            <label for=\"loginName\">Login Name</label>\n" +
                "            <input type=\"text\" class=\"form-control\" id=\"loginName\" placeholder=\"Your Login Name\">\n" +
                "          </div>" +
                "          <div class=\"form-group\">\n" +
                "            <label for=\"Password\">Password</label>\n" +
                "            <input type=\"password\" class=\"form-control\" id=\"Password\" placeholder=\"Your Password\">\n" +
                "          </div>" +
                "          <button class=\"btn btn-lg btn-primary btn-block\" type=\"submit\">Register</button>\n" +
                "\n" +
                "        </form>\n" +
                "      </div>\n" +
                "\n" +
                "      <p class=\"text-center\">\n" +
                "        <a href=\"/\">back to login page</a>\n" +
                "      </p>\n" +
                "\n" +
                "    </div>");
	}
}

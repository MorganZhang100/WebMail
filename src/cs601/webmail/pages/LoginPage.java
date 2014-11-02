package cs601.webmail.pages;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;

public class LoginPage extends Page {
	public LoginPage(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}

	public void verify() { }

    @Override
    public void headerMore() {
        out.println("<link href=\"http://localhost:8081/css/login.css\" rel=\"stylesheet\">");
    }

	@Override
	public void body() {
        out.println("<div class=\"container\">\n" +
                "\n" +
                "      <h2 class=\"form-signin-heading text-center\">Sign in with your Account</h2>\n" +
                "\n" +
                "      <div class=\"card card-signin\">\n" +
                "        <img class=\"img-circle profile-img\" src=\"http://localhost:8081/img/avatar.png\" alt=\"\">\n" +
                "        <form class=\"form-signin\" role=\"form\">\n" +
                "          <input type=\"email\" class=\"form-control\" placeholder=\"Email\" id=\"loginName\" required autofocus>\n" +
                "          <input type=\"password\" class=\"form-control\" placeholder=\"Password\" id=\"Password\" required>\n" +
                "          <a class=\"btn btn-lg btn-primary btn-block\" id=\"login_button\">Sign in</a>\n" +
                "\n" +
                "          <div>\n" +
                "            <a class=\"pull-right\">Need help?</a>\n" +
                "            <label class=\"checkbox\">\n" +
                "              <input type=\"checkbox\" value=\"remember-me\"> Stay signed in\n" +
                "            </label>\n" +
                "          </div>\n" +
                "\n" +
                "        </form>\n" +
                "      </div>\n" +
                "\n" +
                "      <p class=\"text-center\">\n" +
                "        <a href=\"register\">Create an account</a>\n" +
                "      </p>\n" +
                "\n" +
                "    </div>" +
                "    <script src=\"http://localhost:8081/js/login.js\"></script>");
	}
}

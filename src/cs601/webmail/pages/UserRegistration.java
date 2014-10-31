package cs601.webmail.pages;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;

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
                "        <form class=\"form-signin\" role=\"form\" id=\"login_form\">\n" +
                "          <div class=\"form-group\">\n" +
                "            <label for=\"loginName\">Login Name</label>\n" +
                "            <input type=\"text\" class=\"form-control\" id=\"loginName\" placeholder=\"Your Login Name\" name=\"loginName\">\n" +
                "          </div>" +
                "          <div class=\"form-group\">\n" +
                "            <label for=\"Password\">Password</label>\n" +
                "            <input type=\"password\" class=\"form-control\" id=\"Password\" placeholder=\"Your Password\" name=\"pwd\">\n" +
                "          </div>" +
                "          <a class=\"btn btn-lg btn-primary btn-block\" id=\"register_button\">Register</a> \n" +
                "\n" +
                "        </form>\n" +
                "      </div>\n" +
                "\n" +
                "      <p class=\"text-center\">\n" +
                "        <a href=\"/\">back to login page</a>\n" +
                "      </p>\n" +
                "\n" +
                "    </div>" +

                "<script>\n" +
                "$('#register_button').click(function() {\n" +
                "   $.post(\"registerPost\", " +
                "   { name: $('#loginName').val()  , " +
                "     pwd: $('#Password').val()  " +
                "   }, " +
                "   function(result) {" +
                "       alert(result); " +
                "   });\n" +
                "});" +
                "</script>");
	}
}

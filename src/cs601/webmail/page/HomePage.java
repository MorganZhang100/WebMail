package cs601.webmail.page;

import cs601.webmail.module.UserModule;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;

public class HomePage extends Page {
	public HomePage(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}


    @Override
    public void headerMore() {
        out.println("<link href=\"http://localhost:8081/css/home.css\" rel=\"stylesheet\">");
    }

	@Override
	public void body() throws SQLException, ClassNotFoundException {
        UserModule user = new UserModule();
        user.getCurrentUser(request);

        out.println(user.getLoginName());
        out.println("<button id='logout'>Log Out</button><br><script src=\"http://localhost:8081/js/logout.js\"></script>");
        out.println("<div id=\"up_big_containter\"> " +
                    "   <div class=\"container\">\n" +
                    "       <div id=\"up_containter\">\n" +
                    "       </div>" +
                    "   </div>" +
                    "</div>" +
                    "<div id=\"mid_big_containter\"> " +
                    "   <div class=\"container\">\n" +
                    "       <div id=\"mid_containter\">\n" +
                    "       </div>" +
                    "   </div>" +
                    "</div>" +
                    "<div id=\"down_big_containter\"> " +
                    "   <div class=\"container\">\n" +
                    "       <div id=\"down_containter\">\n" +
                    "           <div class=\"row\">" +
                    "               <div class=\"col-lg-3\" id=\"down_left_big\">" +
                    "                   <div class=\"left_buttons\">" +
                    "                       <a href=\"#inbox\" id=\"inbox_button\" >Inbox</a>" +
                    "                       <script src=\"http://localhost:8081/js/home.js\"></script>" +
                    "                   </div>" +
                    "                   <div class=\"left_buttons\">" +
                    "                       <a href=\"\">Sent</a>" +
                    "                   </div>" +
                    "                   <div class=\"left_buttons\">" +
                    "                       <a href=\"\">Spam</a>" +
                    "                   </div>" +
                    "               </div>" +
                    "               <div class=\"col-lg-9\" id=\"down_right_big\">" +
                    "               </div>" +
                    "           </div>" +
                    "       </div>" +
                    "   </div>" +
                    "</div>");
	}
}

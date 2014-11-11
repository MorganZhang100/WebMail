package cs601.webmail.post_handler;

import cs601.webmail.manager.SMTPManager;
import cs601.webmail.module.MailModule;
import cs601.webmail.module.UserModule;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.sql.SQLException;


public class HomeComposePost extends PostHandler {
	public HomeComposePost(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}

	@Override
	public void body() throws IOException, SQLException, ClassNotFoundException, JSONException {
        response.setContentType("application/json;charset=utf-8");
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        PrintWriter out = response.getWriter();

        String subject =request.getParameter("subject");
        subject = URLDecoder.decode(subject, "utf-8");

        String toAddress =request.getParameter("toAddress");
        toAddress = URLDecoder.decode(toAddress, "utf-8");

        String body =request.getParameter("body");
        body = URLDecoder.decode(body, "utf-8");

        UserModule user = new UserModule();
        user.getCurrentUser(request);

        MailModule mail = new MailModule(subject,user.getEmailAddress(),toAddress,body);

        SMTPManager smtp = new SMTPManager(user);
        boolean result = smtp.sendMail(mail,user);

        JSONObject msg = new JSONObject();

        msg.put("success",result);

        out.print(msg);
    }
}
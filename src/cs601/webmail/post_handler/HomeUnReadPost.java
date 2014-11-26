package cs601.webmail.post_handler;

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


public class HomeUnReadPost extends PostHandler {
	public HomeUnReadPost(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}

	@Override
	public void body() throws IOException, SQLException, ClassNotFoundException, JSONException {
        response.setContentType("application/json;charset=utf-8");
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        PrintWriter out = response.getWriter();

        String mail_id =request.getParameter("mail_id");

        mail_id = URLDecoder.decode(mail_id, "utf-8");
        int IntMailId = Integer.parseInt(mail_id);

        UserModule user = new UserModule();
        user.getCurrentUser(request);

        MailModule mail = new MailModule(user,IntMailId);
        mail.unRead();

        JSONObject msg = new JSONObject();

        msg.put("state","done");

        out.print(msg);
    }
}
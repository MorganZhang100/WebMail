package cs601.webmail.post_handler;

import cs601.webmail.module.MailModule;
import cs601.webmail.module.UserModule;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.util.ArrayList;


public class HomeEmailDetail extends PostHandler {
	public HomeEmailDetail(HttpServletRequest request, HttpServletResponse response) {
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

        JSONObject msg = new JSONObject();

        msg.put("subject",mail.getSubject());
        msg.put("from_name",mail.getFromName());
        msg.put("from_address",mail.getFromAddress());
        msg.put("to_address",mail.getToAddress());
        msg.put("body",mail.getBody());

        out.print(msg);
    }
}
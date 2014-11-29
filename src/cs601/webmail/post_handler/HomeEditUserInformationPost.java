package cs601.webmail.post_handler;

import cs601.webmail.module.UserModule;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.sql.SQLException;


public class HomeEditUserInformationPost extends PostHandler {
	public HomeEditUserInformationPost(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}

	@Override
	public void body() throws IOException, SQLException, ClassNotFoundException, JSONException {
        response.setContentType("application/json;charset=utf-8");
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        PrintWriter out = response.getWriter();

        UserModule user = new UserModule();
        user.getCurrentUser(request);

        JSONObject msg = new JSONObject();
        msg.put("nick_name",user.getNickName());
        msg.put("login_name",user.getLoginName());
        msg.put("pop_server",user.getPOP3Server());
        msg.put("pop_port",user.getPOP3Port());
        msg.put("smtp_server",user.getSMTPServer());
        msg.put("smtp_port",user.getSMTPPort());
        msg.put("email_address",user.getEmailAddress());
        msg.put("email_pwd",user.getEmailPwd());

        out.print(msg);
    }
}

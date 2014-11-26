package cs601.webmail.post_handler;

import cs601.webmail.manager.DBManager;
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


public class HomeEmptyAllTrashPost extends PostHandler {
	public HomeEmptyAllTrashPost(HttpServletRequest request, HttpServletResponse response) {
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

        DBManager sql = new DBManager("update MAIL set mail_state = 2 where user_id = " + user.getUser_id() + " and mail_state = 1;");
        sql.execute();

        JSONObject msg = new JSONObject();

        msg.put("state","done");

        out.print(msg);
    }
}
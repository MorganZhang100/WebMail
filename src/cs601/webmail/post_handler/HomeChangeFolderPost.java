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
import java.sql.ResultSet;
import java.sql.SQLException;


public class HomeChangeFolderPost extends PostHandler {
	public HomeChangeFolderPost(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}

	@Override
	public void body() throws IOException, SQLException, ClassNotFoundException, JSONException {
        response.setContentType("application/json;charset=utf-8");
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        PrintWriter out = response.getWriter();

        String mailId =request.getParameter("mailId");

        mailId = URLDecoder.decode(mailId, "utf-8");
        int IntmailId = Integer.parseInt(mailId);

        String folderId =request.getParameter("folderId");

        folderId = URLDecoder.decode(folderId, "utf-8");
        int IntfolderId = Integer.parseInt(folderId);

        UserModule user = new UserModule();
        user.getCurrentUser(request);

        MailModule mail = new MailModule();
        mail.changeFolder(user,IntmailId,IntfolderId);

        JSONObject msg = new JSONObject();
        msg.put("state","done");

        out.print(msg);
    }
}
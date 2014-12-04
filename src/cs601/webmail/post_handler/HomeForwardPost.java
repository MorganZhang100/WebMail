package cs601.webmail.post_handler;

import cs601.webmail.manager.DBManager;
import cs601.webmail.manager.DecodeManager;
import cs601.webmail.module.MailModule;
import cs601.webmail.module.UserModule;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.sql.ResultSet;
import java.sql.SQLException;


public class HomeForwardPost extends PostHandler {
	public HomeForwardPost(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}

	@Override
	public void body() throws IOException, SQLException, ClassNotFoundException, JSONException {
        response.setContentType("application/json;charset=utf-8");
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        PrintWriter out = response.getWriter();

        String id =request.getParameter("id");

        id = URLDecoder.decode(id, "utf-8");
        int IntId = Integer.parseInt(id);

        String type =request.getParameter("type");

        type = URLDecoder.decode(type, "utf-8");

        UserModule user = new UserModule();
        user.getCurrentUser(request);

        MailModule mail ;
        if(type.equals("fwdReceive")) mail = new MailModule(user,IntId);
        else mail = new MailModule(IntId,user);

        JSONObject msg = new JSONObject();

        msg.put("subject","Fwd: " + DecodeManager.getSafeHTMLOutputFromString(mail.getSubject()));
        msg.put("body",DecodeManager.getSafeHTMLOutputFromString(mail.getBody()));

        System.out.println(mail.getSubject() + mail.getBody());

        out.print(msg);
    }
}
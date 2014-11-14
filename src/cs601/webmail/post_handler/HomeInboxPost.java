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


public class HomeInboxPost extends PostHandler {
	public HomeInboxPost(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}

	@Override
	public void body() throws IOException, SQLException, ClassNotFoundException, JSONException {
        response.setContentType("application/json;charset=utf-8");
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        PrintWriter out = response.getWriter();

        String pageNumber =request.getParameter("pageNumber");

        pageNumber = URLDecoder.decode(pageNumber, "utf-8");
        int IntpageNumber = Integer.parseInt(pageNumber);

        UserModule user = new UserModule();
        user.getCurrentUser(request);

        MailModule mail = new MailModule();
        ArrayList<MailModule> mailList = mail.getBriefUserMails(user, IntpageNumber);

        JSONObject msg = new JSONObject();
        JSONArray mailsBrief = new JSONArray();

        int i;
        for(i=0;i<mailList.size();i++) {
            mail = mailList.get(i);
            JSONObject oneMailBrief = new JSONObject();
            oneMailBrief.put("from_name",mail.getFromName());
            oneMailBrief.put("subject",mail.getSubject());
            oneMailBrief.put("body",mail.getBody());
            oneMailBrief.put("mail_id",mail.getMailId());

            mailsBrief.put(oneMailBrief);
        }

        msg.put("mailAmount",mailList.size());
        msg.put("mailsBrief",mailsBrief);

        out.print(msg);
    }
}
package cs601.webmail.post_handler;

import cs601.webmail.manager.DBManager;
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


public class HomeAdvanceSearchPost extends PostHandler {
	public HomeAdvanceSearchPost(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}

	@Override
	public void body() throws IOException, SQLException, ClassNotFoundException, JSONException {
        response.setContentType("application/json;charset=UTF-8");
        request.setCharacterEncoding("utf-8");

        response.setCharacterEncoding("utf8");
        PrintWriter out = response.getWriter();

        UserModule user = new UserModule();
        user.getCurrentUser(request);

        String subject =request.getParameter("subject");
        String body =request.getParameter("body");
        String fromName =request.getParameter("fromName");
        String fromAddress =request.getParameter("fromAddress");
        String toName =request.getParameter("toName");
        String toAddress =request.getParameter("toAddress");

        subject = URLDecoder.decode(subject, "utf-8");
        body = URLDecoder.decode(body, "utf-8");
        fromName = URLDecoder.decode(fromName,"utf-8");
        fromAddress = URLDecoder.decode(fromAddress, "utf-8");
        toName = URLDecoder.decode(toName,"utf-8");
        toAddress = URLDecoder.decode(toAddress, "utf-8");

        DBManager sql = new DBManager();

        MailModule mail;

        ArrayList<MailModule> mailList = null;

        if(!subject.equals("")) {
            mailList = sql.advanceSearch("subject",subject,user);
        }
        else if(!body.equals("")) {
            mailList = sql.advanceSearch("body",body,user);
        }
        else if(!fromName.equals("")) {
            mailList = sql.advanceSearch("from_name",fromName,user);
        }
        else if(!fromAddress.equals("")) {
            mailList = sql.advanceSearch("from_address",fromAddress,user);
        }
        else if(!toName.equals("")) {
            mailList = sql.advanceSearch("to_name",toName,user);
        }
        else if(!toAddress.equals("")) {
            mailList = sql.advanceSearch("to_address",toAddress,user);
        }
        else {
            return;
        }

        JSONObject msg = new JSONObject();
        JSONArray mailsBrief = new JSONArray();

        int i;
        for(i=0;i<mailList.size();i++) {
            mail = mailList.get(i);
            JSONObject oneMailBrief = new JSONObject();
            oneMailBrief.put("from_name",mail.getFromName());
            oneMailBrief.put("subject",mail.getSubject());
            String temBody = mail.getBody();
            if(temBody.length() > 8) temBody = temBody.substring(0,7);
            oneMailBrief.put("body",temBody);
            oneMailBrief.put("mail_id",mail.getMailId());
            oneMailBrief.put("read_flag",mail.getReadFlag());

            mailsBrief.put(oneMailBrief);
        }

        msg.put("mailAmount",mailList.size());
        msg.put("mailsBrief",mailsBrief);

        out.print(msg);
    }
}
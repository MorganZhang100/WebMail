package cs601.webmail.post_handler;

import cs601.webmail.manager.DBManager;
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
        msg.put("to_address",mail.getToFirstAddress());
        msg.put("body",mail.getBody());
        msg.put("mail_state",mail.getMailState());

        JSONArray attachmentsArray = new JSONArray();

        DBManager sql = new DBManager("select name,content from ATTA where user_id = " + user.getUser_id() + " and message_id = '" + mail.getMessageId() + "';");
        ResultSet rs = sql.query();

        int i = 0;
        while(rs.next()) {
            JSONObject attachment = new JSONObject();
            String attaName = rs.getString("name");
            byte[] content = rs.getBytes("content");

            attachment.put("name",attaName);

            File file = new File("./Public/tem/" + attaName);
            try {
                FileOutputStream fop = new FileOutputStream(file);

                // if file doesnt exists, then create it
                if (!file.exists()) {
                    file.createNewFile();
                }

                fop.write(content);
                fop.flush();
                fop.close();
            } catch(IOException e) {
                e.printStackTrace();
            }

            attachmentsArray.put(attachment);
            i++;
        }

        msg.put("attachments",attachmentsArray);
        msg.put("attachmentAmount",i);

        out.print(msg);
    }
}
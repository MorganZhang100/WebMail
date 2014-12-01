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


public class HomeAddNewFolderPost extends PostHandler {
	public HomeAddNewFolderPost(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}

	@Override
	public void body() throws IOException, SQLException, ClassNotFoundException, JSONException {
        response.setContentType("application/json;charset=utf-8");
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        PrintWriter out = response.getWriter();

        String newFolderName =request.getParameter("name");

        newFolderName = URLDecoder.decode(newFolderName, "utf-8");

        UserModule user = new UserModule();
        user.getCurrentUser(request);

        DBManager sql = new DBManager();
        sql.newFolder(user,newFolderName);

        sql.newQuery("select id from FOLDER where user_id = " + user.getUser_id() + " and name = '" + newFolderName + "';");
        ResultSet rs = sql.query();

        if(rs.next()) {
            int folderId = rs.getInt("id");
            sql.close();

            JSONObject msg = new JSONObject();

            msg.put("state","done");
            msg.put("folder_id",folderId);
            msg.put("name",newFolderName);

            out.print(msg);
        }
    }
}
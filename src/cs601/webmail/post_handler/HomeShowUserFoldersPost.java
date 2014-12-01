package cs601.webmail.post_handler;

import cs601.webmail.manager.DBManager;
import cs601.webmail.module.UserModule;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.sql.ResultSet;
import java.sql.SQLException;


public class HomeShowUserFoldersPost extends PostHandler {
	public HomeShowUserFoldersPost(HttpServletRequest request, HttpServletResponse response) {
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

        DBManager sql = new DBManager("select id,name from FOLDER where user_id = " + user.getUser_id() + " order by id ;");

        ResultSet rs = sql.query();

        JSONObject msg = new JSONObject();
        JSONArray folders = new JSONArray();

        int i = 0;

        while(rs.next()) {
            JSONObject oneFolder = new JSONObject();

            int folderId = rs.getInt("id");
            String folderName = rs.getString("name");

            oneFolder.put("id",folderId);
            oneFolder.put("name",folderName);

            folders.put(oneFolder);

            i++;
        }

        msg.put("folderAmount",i);
        msg.put("folders",folders);
        msg.put("state","done");

        sql.close();
        out.print(msg);
    }
}
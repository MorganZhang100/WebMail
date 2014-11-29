package cs601.webmail.post_handler;

import cs601.webmail.manager.DecodeManager;
import cs601.webmail.module.UserModule;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.sql.SQLException;


public class HomeChangeUserPwdPost extends PostHandler {
	public HomeChangeUserPwdPost(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}

	@Override
	public void body() throws IOException, SQLException, ClassNotFoundException, JSONException {
        response.setContentType("application/json;charset=utf-8");
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        PrintWriter out = response.getWriter();

        String oldPwd =request.getParameter("oldPwd");
        String newPwd =request.getParameter("newPwd");

        oldPwd = URLDecoder.decode(oldPwd, "utf-8");
        newPwd = URLDecoder.decode(newPwd, "utf-8");

        oldPwd = DecodeManager.getMD5(oldPwd.getBytes());
        newPwd = DecodeManager.getMD5(newPwd.getBytes());

        UserModule user = new UserModule();
        user.getCurrentUser(request);

        JSONObject msg = new JSONObject();

        if(user.changePwd(oldPwd,newPwd))
        {
            msg.put("state","success");
        }
        else {
            msg.put("state","Incorrect Old Password");
        }

        out.print(msg);
    }
}

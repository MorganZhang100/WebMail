package cs601.webmail.post_handler;

import cs601.webmail.managers.UserManager;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.sql.SQLException;


public class LoginPost extends PostHandler {
	public LoginPost(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}

	@Override
	public void body() throws IOException, SQLException, ClassNotFoundException, JSONException {
        response.setContentType("application/json;charset=utf-8");
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        PrintWriter out = response.getWriter();

        String name =request.getParameter("name");
        String pwd =request.getParameter("pwd");

        name = URLDecoder.decode(name, "utf-8");
        pwd = URLDecoder.decode(pwd,"utf-8");

        UserManager user = new UserManager();
        user.setLoginName(name);
        user.setPwd(pwd);

        if(user.isValidUser(request,response)) {
            JSONObject msg = new JSONObject();
            msg.put("state","success");
            out.print(msg);
        }
        else {
            JSONObject msg = new JSONObject();
            msg.put("state","fail");
            msg.put("message","Login name or password not correct");
            out.print(msg);
        }
    }
}

package cs601.webmail.post_handler;

import cs601.webmail.module.UserModule;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.sql.*;

import org.json.JSONException;
import org.json.JSONObject;


public class RegisterPost extends PostHandler {
	public RegisterPost(HttpServletRequest request, HttpServletResponse response) {
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

        UserModule user = new UserModule();

        if(user.newUser(name, pwd, response)) {
            JSONObject msg = new JSONObject();
            msg.put("state","success");
            out.print(msg);
        }
        else {
            JSONObject msg = new JSONObject();
            msg.put("state","fail");
            msg.put("message","This login name already exists");
            out.print(msg);
        }
    }
}

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
        String nickName =request.getParameter("nickName");
        String realEmailAddress =request.getParameter("realEmailAddress");
        String realEmailPwd =request.getParameter("realEmailPwd");
        String popServerAddress =request.getParameter("popServerAddress");
        String popServerPort =request.getParameter("popServerPort");
        String smtpServerAddress =request.getParameter("smtpServerAddress");
        String smtpServerPort =request.getParameter("smtpServerPort");

        name = URLDecoder.decode(name, "utf-8");
        pwd = URLDecoder.decode(pwd,"utf-8");
        nickName = URLDecoder.decode(nickName, "utf-8");
        realEmailAddress = URLDecoder.decode(realEmailAddress,"utf-8");
        realEmailPwd = URLDecoder.decode(realEmailPwd, "utf-8");
        popServerAddress = URLDecoder.decode(popServerAddress,"utf-8");
        popServerPort = URLDecoder.decode(popServerPort, "utf-8");
        smtpServerAddress = URLDecoder.decode(smtpServerAddress,"utf-8");
        smtpServerPort = URLDecoder.decode(smtpServerPort, "utf-8");

        int IntPopPort = Integer.parseInt(popServerPort);
        int IntSmtpPort = Integer.parseInt(smtpServerPort);

        UserModule user = new UserModule(name, pwd, nickName, realEmailAddress, realEmailPwd, popServerAddress, IntPopPort, smtpServerAddress, IntSmtpPort);


        if(user.newUser(response)) {
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

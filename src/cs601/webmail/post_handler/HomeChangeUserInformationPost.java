package cs601.webmail.post_handler;

import cs601.webmail.module.UserModule;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.sql.SQLException;


public class HomeChangeUserInformationPost extends PostHandler {
	public HomeChangeUserInformationPost(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}

	@Override
	public void body() throws IOException, SQLException, ClassNotFoundException, JSONException {
        response.setContentType("application/json;charset=utf-8");
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        PrintWriter out = response.getWriter();

        String loginName =request.getParameter("loginName");
        String nickName =request.getParameter("nickName");
        String emailAddress =request.getParameter("emailAddress");
        String emailPwd =request.getParameter("emailPwd");
        String popServer =request.getParameter("popServer");
        String popPort =request.getParameter("popPort");
        String smtpServer =request.getParameter("smtpServer");
        String smtpPort =request.getParameter("smtpPort");

        loginName = URLDecoder.decode(loginName, "utf-8");
        nickName = URLDecoder.decode(nickName, "utf-8");
        emailAddress = URLDecoder.decode(emailAddress,"utf-8");
        emailPwd = URLDecoder.decode(emailPwd, "utf-8");
        popServer = URLDecoder.decode(popServer,"utf-8");
        popPort = URLDecoder.decode(popPort, "utf-8");
        smtpServer = URLDecoder.decode(smtpServer,"utf-8");
        smtpPort = URLDecoder.decode(smtpPort, "utf-8");

        int IntPopPort = Integer.parseInt(popPort);
        int IntSmtpPort = Integer.parseInt(smtpPort);


        UserModule user = new UserModule();
        user.getCurrentUser(request);
        user.changeInformation(loginName, nickName, emailAddress, emailPwd, popServer, IntPopPort, smtpServer, IntSmtpPort);

        JSONObject msg = new JSONObject();
        msg.put("state","success");
        out.print(msg);
    }
}

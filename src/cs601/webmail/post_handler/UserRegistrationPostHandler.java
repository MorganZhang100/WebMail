package cs601.webmail.post_handler;

import cs601.webmail.SQLQueryHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.*;

public class UserRegistrationPostHandler extends PostHandler {
	public UserRegistrationPostHandler(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}

	@Override
	public void body() throws IOException, SQLException, ClassNotFoundException {
        response.setContentType("text/html;charset=utf-8");
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        PrintWriter out = response.getWriter();

        String name =request.getParameter("name");
        String pwd =request.getParameter("pwd");

        name = URLDecoder.decode(name, "utf-8");                 //处理中文乱码2
        pwd = URLDecoder.decode(pwd,"utf-8");

        SQLQueryHandler sql = new SQLQueryHandler("select * from USER where login_name = '" + name + "';");
        ResultSet rs = sql.query();
        if(rs.next()) {
            out.println("{state:\"fail\",message=\"This login name already exists\"} ");
        }
        else {
            sql.newQuery("insert into USER(login_name,pwd) values('" + name + "','" + pwd + "');");
            sql.execute();
            out.println("{state:\"success\"}");
        }
        sql.close();
    }
}

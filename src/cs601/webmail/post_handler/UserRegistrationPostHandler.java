package cs601.webmail.post_handler;

import cs601.webmail.pages.Page;

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
	public void body() throws IOException {
        response.setContentType("text/html;charset=utf-8");
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        PrintWriter out = response.getWriter();


//        String name =request.getParameter("name");
//        String password =request.getParameter("password");
//
//        name = URLDecoder.decode(name, "utf-8");                 //处理中文乱码2
//        password = URLDecoder.decode(password,"utf-8");

        out.println("{data from:\"UserRegistrationPostHandler\"} ");

    }
}

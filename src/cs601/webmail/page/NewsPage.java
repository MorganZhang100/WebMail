package cs601.webmail.page;

import cs601.webmail.manager.SQLQueryManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;

public class NewsPage extends Page {
	public NewsPage(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}

	@Override
	public void body() throws SQLException, ClassNotFoundException {
        SQLQueryManager sql = new SQLQueryManager("insert into mytable(value) values('Morgan');");
        sql.execute();

        sql.newQuery("select * from mytable");
        ResultSet rs = sql.query();

        while( rs.next() ) {
            out.println("id = " + rs.getString("id"));
            out.println("value = " + rs.getInt("value"));
            out.println("<br>");
        }

        sql.close();

        out.println("This is the news page");
	}
}

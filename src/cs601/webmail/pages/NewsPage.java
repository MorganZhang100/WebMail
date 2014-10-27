package cs601.webmail.pages;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;

public class NewsPage extends Page {
	public NewsPage(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}

	@Override
	public void body() {
        String dbFile = "/Users/Morgan/test.db";
        Connection db = null;
        long start = System.currentTimeMillis();
        try {
            try {
                Class.forName("org.sqlite.JDBC"); // force load of driver
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                db = DriverManager.getConnection("jdbc:sqlite:" + dbFile);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            // DO SOMETHINE WITH db to read/write

            long stop = System.currentTimeMillis();
            out.printf("SQL exe time %1.1f minutes\n", (stop-start)/1000.0/60.0);
            out.println("<br>");

            Statement statement = null;

            try {
                statement = db.createStatement();

                statement.executeUpdate("insert into mytable(value) values('Morgan'); ");

                statement.close();

                ResultSet rs = statement.executeQuery("select * from mytable");
                while( rs.next() ) {
                    out.println("id = " + rs.getString("id"));
                    out.println("value = " + rs.getInt("value"));
                    out.println("<br>");
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        finally {
            if ( db!=null ) {
                try {
                    db.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        out.println("This is the news page");
	}
}

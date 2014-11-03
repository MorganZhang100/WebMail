package cs601.webmail.manager;

import java.sql.*;


public class SQLQueryManager {

	String query;
    String dbFile = "/Users/Morgan/Documents/MorganZhang1991-webmail/src/cs601/webmail/MorganWebMail.sqlite";
    Connection db = null;
    Statement statement = null;

	public SQLQueryManager(String s) throws ClassNotFoundException, SQLException {
		this.query = s;
        Class.forName("org.sqlite.JDBC");
        db = DriverManager.getConnection("jdbc:sqlite:" + dbFile);
        statement = db.createStatement();
	}

    public void newQuery(String s) {
        this.query = s;
    }

    public int execute() throws SQLException {
        //System.out.println(query);
        return statement.executeUpdate(query);
    }

    public ResultSet query() throws SQLException {
        //System.out.println(query);
        return statement.executeQuery(query);
    }

    public void close() throws SQLException {
        statement.close();
        if (db != null) db.close();
    }
}

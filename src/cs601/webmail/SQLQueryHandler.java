package cs601.webmail;

import cs601.webmail.managers.ErrorManager;
import cs601.webmail.misc.VerifyException;

import java.sql.*;


public class SQLQueryHandler {

	String query;
    String dbFile = "/Users/Morgan/test.db";
    Connection db = null;
    Statement statement = null;

	public SQLQueryHandler(String s) throws ClassNotFoundException, SQLException {
		this.query = s;
        Class.forName("org.sqlite.JDBC");
        db = DriverManager.getConnection("jdbc:sqlite:" + dbFile);
        statement = db.createStatement();
	}

    public void newQuery(String s) {
        this.query = s;
    }

    public int execute() throws SQLException {
        return statement.executeUpdate(query);
    }

    public ResultSet query() throws SQLException {
        return statement.executeQuery(query);
    }

    public void close() throws SQLException {
        statement.close();
        if (db != null) db.close();
    }
}

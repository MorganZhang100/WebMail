package cs601.webmail.manager;

import cs601.webmail.module.MailModule;
import java.sql.*;

public class SQLQueryManager {

	String query;
    String dbFile = "/Users/Morgan/Documents/MorganZhang1991-webmail/src/cs601/webmail/MorganWebMail.sqlite";
    Connection db = null;
    Statement statement = null;

    public SQLQueryManager() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        db = DriverManager.getConnection("jdbc:sqlite:" + dbFile);
        statement = db.createStatement();
    }

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

    public void newEmail(MailModule mail) throws SQLException {
        PreparedStatement insert = db.prepareStatement("insert into MAIL(user_id,subject,from_name,from_address,to_name,to_address,body,content_type,charset,content_transfer_encoding,mime_version,raw,date_string,is_complete,message_id,add_time) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
        insert.setInt(1,0);
        insert.setString(2, mail.getSubject());
        insert.setString(3, mail.getFromName());
        insert.setString(4, mail.getFromAddress());
        insert.setString(5, mail.getToName());
        insert.setString(6, mail.getToAddress());
        insert.setString(7, mail.getBody());
        insert.setString(8, mail.getContentType());
        insert.setString(9, mail.getCharset());
        insert.setString(10, mail.getContentTransferEncoding());
        insert.setString(11, mail.getMimeVersion());
        insert.setString(12, mail.getRaw());
        insert.setString(13, mail.getDate());
        insert.setInt(14, mail.getIsComplateInt());
        insert.setString(15, mail.getMessageId());
        insert.setInt(16, (int) System.currentTimeMillis());

        int n = insert.executeUpdate();
        if(n!=1) {
            System.err.println("Bad update");
        }
    }
}

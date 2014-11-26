package cs601.webmail.manager;

import cs601.webmail.module.MailModule;
import cs601.webmail.module.UserModule;

import java.sql.*;

public class DBManager {

	String query;
    String dbFile = "/Users/Morgan/Documents/MorganZhang1991-webmail/src/cs601/webmail/MorganWebMail.sqlite";
    Connection db = null;
    Statement statement = null;

    public DBManager() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        db = DriverManager.getConnection("jdbc:sqlite:" + dbFile);
        statement = db.createStatement();
    }

    public DBManager(String s) throws ClassNotFoundException, SQLException {
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
        PreparedStatement insert = db.prepareStatement("insert into MAIL(user_id,subject,from_name,from_address,to_name,to_address,body,to_addresses,cc_addresses,bcc_addresses,mail_state,raw,sent_date_string,is_complete,message_id,add_time,read_flag) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
        insert.setInt(1,0);
        insert.setString(2, mail.getSubject());
        insert.setString(3, mail.getFromName());
        insert.setString(4, mail.getFromAddress());
        insert.setString(5, mail.getToName());
        insert.setString(6, mail.getToFirstAddress());
        insert.setString(7, mail.getBody());

        insert.setString(8, mail.getToAddresses());
        insert.setString(9, mail.getCcAddresses());
        insert.setString(10, mail.getBccAddresses());

        insert.setInt(11, mail.getMailState());

        insert.setString(12, mail.getRaw());
        insert.setString(13, mail.getSentDate());
        insert.setInt(14, mail.getIsComplateInt());
        insert.setString(15, mail.getMessageId());
        insert.setInt(16, (int) System.currentTimeMillis()/1000);
        insert.setInt(17, 0); //read_flag = 0: unRead, =1: read;

        int n = insert.executeUpdate();
        if(n!=1) {
            System.err.println("Bad update");
        }
    }

    public void newUser(UserModule user, int addTime) throws SQLException {
        PreparedStatement insert = db.prepareStatement("insert into USER(login_name,pwd,add_time,nick_name,real_email_address,real_email_pwd,pop_server_address,pop_server_port,smtp_server_address,smtp_server_port) values(?,?,?,?,?,?,?,?,?,?)");
        insert.setString(1,user.getLoginName());
        insert.setString(2, user.getPwd());
        insert.setInt(3, addTime);
        insert.setString(4, user.getNickName());
        insert.setString(5, user.getEmailAddress());
        insert.setString(6, user.getEmailPwd());
        insert.setString(7, user.getPOP3Server());
        insert.setInt(8, user.getPOP3Port());
        insert.setString(9, user.getSMTPServer());
        insert.setInt(10, user.getSMTPPort());

        int n = insert.executeUpdate();
        if(n!=1) {
            System.err.println("Bad update");
        }
    }
}

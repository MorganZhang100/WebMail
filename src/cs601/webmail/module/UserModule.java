package cs601.webmail.module;

import cs601.webmail.manager.SQLQueryManager;
import cs601.webmail.manager.CookieManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserModule {

    private String loginName = null;
    private int user_id = -1;
    private String pwd = "zfzztc114";

    private String SMTPServer = "smtp.gmail.com";
    private int SMTPPort = 465;

    private String POP3Server = "pop.gmail.com";
    private int POP3Port = 995;

    private String userNameForRealEmailAccount = "morgantest601";

    private String emailAddress = "morgantest601@gmail.com";

    private String nickName = "Morgan";

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    //    private String SMTPServer = "smtp.163.com";
//    private int SMTPPort = 465;
//
//    private String POP3Server = "pop.gmail.com";
//    private int POP3Port = 995;
//
//    private String userNameForRealEmailAccount = "zfzzyx";
//
//    private String emailAddress = "zfzzyx@163.com";

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String name) {
        loginName = name;
    }

    public void setPwd(String password) {
        pwd = password;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getSMTPServer() {
        return SMTPServer;
    }

    public String getPwd() {
        return pwd;
    }

    public String getUserNameForRealEmailAccount() {
        return userNameForRealEmailAccount;
    }

    public void setUserNameForRealEmailAccount(String userNameForRealEmailAccount) {
        this.userNameForRealEmailAccount = userNameForRealEmailAccount;
    }

    public void setSMTPServer(String SMTPServer) {
        this.SMTPServer = SMTPServer;
    }

    public int getSMTPPort() {
        return SMTPPort;
    }

    public void setSMTPPort(int SMTPPort) {
        this.SMTPPort = SMTPPort;
    }

    public String getPOP3Server() {
        return POP3Server;
    }

    public void setPOP3Server(String POP3Server) {
        this.POP3Server = POP3Server;
    }

    public int getPOP3Port() {
        return POP3Port;
    }

    public void setPOP3Port(int POP3Port) {
        this.POP3Port = POP3Port;
    }

    public boolean newUser(String name, String pwd, HttpServletResponse reponse) throws SQLException, ClassNotFoundException {
        SQLQueryManager sql = new SQLQueryManager("select * from USER where login_name = '" + name + "';");
        ResultSet rs = sql.query();

        if(rs.next()) {
            sql.close();
            return false;
        }
        else {
            long currentTime = System.currentTimeMillis();

            sql.newQuery("insert into USER(login_name,pwd,add_time) values('" + name + "','" + pwd + "'," + currentTime + " );");
            sql.execute();
            sql.close();

            loginName = name;
            this.userLogin(reponse);

            return true;
        }
    }

    public boolean getCurrentUser(HttpServletRequest request) throws SQLException, ClassNotFoundException {
        String name = CookieManager.getCookieValue(request, "login_name");
        this.loginName = name;

        SQLQueryManager sql = new SQLQueryManager("select user_id from USER where login_name = '" + name + "'; ");
        ResultSet rs = sql.query();

        if(rs.next()) {
            this.user_id = rs.getInt("user_id");

            sql.close();
            return true;
        }
        else {
            sql.close();

            return false;
        }
    }

    private void userLogin(HttpServletResponse response) {
        CookieManager.setCookieValue(response,"login_name",loginName,60*60*24*15);
    }

    public void userLogout(HttpServletResponse response) {
        CookieManager.killCookie(response,"login_name");
    }

    public boolean isValidLoginedUser(HttpServletRequest request,HttpServletResponse response) throws SQLException, ClassNotFoundException {
        String name = CookieManager.getCookieValue(request,"login_name");
        if(name == null) return false;
        else {
            SQLQueryManager sql = new SQLQueryManager("select * from USER where login_name = '" + name + "';");
            ResultSet rs = sql.query();

            if(rs.next()) {
                this.loginName = name;
                userLogin(response);
                sql.close();
                return true;
            }
            else {
                this.userLogout(response);
                sql.close();
                return false;
            }
        }
    }

    public boolean isValidUser(HttpServletRequest request,HttpServletResponse response) throws SQLException, ClassNotFoundException {
        SQLQueryManager sql = new SQLQueryManager("select * from USER where login_name = '" + loginName + "' and pwd ='" + pwd + "';");
        ResultSet rs = sql.query();

        if(rs.next()) {
            userLogin(response);
            sql.close();
            return true;
        }
        else {
            sql.close();
            return false;
        }
    }
}

package cs601.webmail.module;

import cs601.webmail.manager.DBManager;
import cs601.webmail.manager.CookieManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserModule {

    private String loginName = null;
    private int user_id = -1;
    private String pwd = "zfzztc114";

    private String SMTPServer = "smtp.gmail.com";
    private int SMTPPort = 465;

    private String POP3Server = "pop.gmail.com";
    private int POP3Port = 995;

    private String userNameForRealEmailAccount = "m organtest601";

    private String emailAddress = "m organtest601@gmail.com";
    private String emailPwd = "z fzztc114";

    private String nickName = "M organ";

    //    private String SMTPServer = "smtp.163.com";
//    private int SMTPPort = 465;
//
//    private String POP3Server = "pop.gmail.com";
//    private int POP3Port = 995;
//
//    private String userNameForRealEmailAccount = "zfzzyx";
//
//    private String emailAddress = "zfzzyx@163.com";

    public UserModule(String name, String pwd, String nickName, String realEmailAddress, String realEmailPwd, String popServerAddress, int popServerPort, String smtpServerAddress, int smtpServerPort) {
        this.loginName = name;
        this.pwd = pwd;
        this.nickName = nickName;
        this.emailAddress = realEmailAddress;
        this.emailPwd = realEmailPwd;
        this.POP3Server = popServerAddress;
        this.POP3Port = popServerPort;
        this.SMTPServer = smtpServerAddress;
        this.SMTPPort = smtpServerPort;
    }

    public UserModule() {

    }

    public String getEmailPwd() {
        return emailPwd;
    }

    public void setEmailPwd(String emailPwd) {
        this.emailPwd = emailPwd;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

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

    public boolean getCurrentUser(HttpServletRequest request) throws SQLException, ClassNotFoundException {
        String name = CookieManager.getCookieValue(request, "login_name");
        this.loginName = name;

        DBManager sql = new DBManager("select user_id,login_name,pwd,nick_name,real_email_address,real_email_pwd,pop_server_address,pop_server_port,smtp_server_address,smtp_server_port from USER where login_name = '" + name + "'; ");
        ResultSet rs = sql.query();

        if(rs.next()) {
            this.user_id = rs.getInt("user_id");
            this.loginName = rs.getString("login_name");
            this.pwd = rs.getString("pwd");
            this.nickName = rs.getString("nick_name");
            this.emailAddress = rs.getString("real_email_address");
            this.emailPwd = rs.getString("real_email_pwd");
            this.POP3Server = rs.getString("pop_server_address");
            this.POP3Port = rs.getInt("pop_server_port");
            this.SMTPServer = rs.getString("smtp_server_address");
            this.SMTPPort = rs.getInt("smtp_server_port");

            Pattern p = Pattern.compile("[\\S\\s]+(?=@)");
            Matcher m = p.matcher(this.emailAddress);

            this.userNameForRealEmailAccount = null;
            if(m.find()) this.userNameForRealEmailAccount = m.group();

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
            DBManager sql = new DBManager("select * from USER where login_name = '" + name + "';");
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
        DBManager sql = new DBManager("select * from USER where login_name = '" + loginName + "' and pwd ='" + pwd + "';");
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

    public boolean newUser(HttpServletResponse response) throws SQLException, ClassNotFoundException {
        DBManager sql = new DBManager("select * from USER where login_name = '" + this.loginName + "';");
        ResultSet rs = sql.query();

        if(rs.next()) {
            sql.close();
            return false;
        }
        else {
            int currentTime = (int)System.currentTimeMillis()/1000;

            sql.newUser(this,currentTime);
            sql.close();

            this.userLogin(response);

            return true;
        }
    }

    public boolean changeInformation(String loginName, String nickName, String realEmailAddress, String realEmailPwd, String popServerAddress, int intPopPort, String smtpServerAddress, int intSmtpPort) throws SQLException, ClassNotFoundException {
        DBManager sql = new DBManager("select * from USER where login_name = '" + loginName + "' and user_id != " + this.user_id + ";");
        ResultSet rs = sql.query();

        if(rs.next()) {
            sql.close();
            return false;
        }
        else {
            sql.newQuery("update USER set login_name = '" + loginName + "', nick_name = '" + nickName + "', real_email_address = '" + realEmailAddress + "', real_email_pwd = '" + realEmailPwd + "', pop_server_address = '" + popServerAddress + "', pop_server_port = " + intPopPort + ", smtp_server_address = '" + smtpServerAddress + "',smtp_server_port = " + intSmtpPort + " where user_id = " + this.user_id + "; ");
            sql.execute();

            sql.close();

            return true;
        }
    }

    public boolean changePwd(String oldPwd, String newPwd) throws SQLException, ClassNotFoundException {
        if(!this.pwd.equals(oldPwd)) {
            System.out.println("this.pwd = " + this.pwd);
            System.out.println("oldPwd = " + oldPwd);
            return false;
        }

        DBManager sql = new DBManager("update USER set pwd = '" + newPwd + "' where user_id = " + this.user_id + "; ");
        sql.execute();
        sql.close();

        return true;
    }

}

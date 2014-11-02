package cs601.webmail.managers;

import cs601.webmail.SQLQueryHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserManager {

    private String loginName = null;
    private String pwd = null;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String name) {
        loginName = name;
    }

    public void setPwd(String password) {
        pwd = password;
    }

    public boolean newUser(String name, String pwd, HttpServletResponse reponse) throws SQLException, ClassNotFoundException {
        SQLQueryHandler sql = new SQLQueryHandler("select * from USER where login_name = '" + name + "';");
        ResultSet rs = sql.query();

        if(rs.next()) {
            sql.close();
            return false;
        }
        else {
            sql.newQuery("insert into USER(login_name,pwd) values('" + name + "','" + pwd + "');");
            sql.execute();
            sql.close();

            loginName = name;
            this.userLogin(reponse);

            return true;
        }
    }

    public void currentUser(HttpServletRequest request) {
        String name = CookieManager.getCookieValue(request,"login_name");
        this.loginName = name;
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
            SQLQueryHandler sql = new SQLQueryHandler("select * from USER where login_name = '" + name + "';");
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
        SQLQueryHandler sql = new SQLQueryHandler("select * from USER where login_name = '" + loginName + "' and pwd ='" + pwd + "';");
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

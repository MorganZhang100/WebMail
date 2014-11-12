package cs601.webmail.page;

import cs601.webmail.manager.SimpleStringTemplate;
import cs601.webmail.module.UserModule;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class HomePage extends Page {
	public HomePage(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}

    @Override
    public void headerMore() {
        out.println("<link href=\"http://localhost:8081/css/home.css\" rel=\"stylesheet\">");
    }

	@Override
	public void body() throws SQLException, ClassNotFoundException, IOException {
        UserModule user = new UserModule();
        user.getCurrentUser(request);

        SimpleStringTemplate SST = new SimpleStringTemplate("HomePage.sst");
        SST.add("userLoginName",user.getLoginName());
        out.println(SST.render());
	}
}

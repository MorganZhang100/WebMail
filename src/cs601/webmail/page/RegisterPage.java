package cs601.webmail.page;

import cs601.webmail.manager.SimpleStringTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RegisterPage extends Page {
	public RegisterPage(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}

    @Override
    public void headerMore() {
        out.println("<link href=\"http://localhost:8081/css/login.css\" rel=\"stylesheet\">" );
    }

	@Override
	public void body() throws IOException {

        SimpleStringTemplate SST = new SimpleStringTemplate("RegisterPage.sst");
        out.println(SST.render());
	}
}

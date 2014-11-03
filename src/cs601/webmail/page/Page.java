package cs601.webmail.page;

import cs601.webmail.manager.ErrorManager;
import cs601.webmail.module.UserModule;
import cs601.webmail.misc.VerifyException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

public abstract class Page {
	HttpServletRequest request;
	HttpServletResponse response;
	PrintWriter out;
	int pageNum;

	public Page(HttpServletRequest request,
				HttpServletResponse response)
	{
		this.request = request;
		this.response = response;
		try {
			out = response.getWriter();
		}
		catch (IOException ioe) {
			ErrorManager.instance().error(ioe);
		}
	}

	public void verify() throws VerifyException, SQLException, ClassNotFoundException, IOException {
		// handle default args like page number, etc...
		// verify that arguments make sense
		// implemented by subclass typically
		// VerifyException is a custom Exception subclass
        String uri = request.getRequestURI();
        if(uri.equals("/home")) {
            UserModule user = new UserModule();
            if(!user.isValidLoginedUser(request,response)) {
                response.sendRedirect("/");
            }
        }
	}

	public void handleDefaultArgs() {
		// handle default args like page number, etc...
		String pageStr = request.getParameter("page");
		if ( pageStr!=null ) {
			pageNum = Integer.valueOf(pageStr);
		}
	}

	public void generate() {
		handleDefaultArgs();
		try {
			verify(); // check args before generation
			header();
			body();
			footer();
		}
		catch (VerifyException ve) {
			try {
				response.sendRedirect("/files/error.html");
			}
			catch (IOException ioe) {
				ErrorManager.instance().error(ioe);
			}
		}
		catch (Exception e) {
			ErrorManager.instance().error(e);
		}
		finally {
			out.close();
		}
	}

    public void headerMore() {} // overwrite by other page

	public void header() {
        out.println("<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "  <head>\n" +
                "    <meta charset=\"utf-8\">\n" +
                "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
                "    <title>Morgan Mail</title>\n" +
                "\n" +
                "    <!-- Bootstrap -->\n" +
                "    <link href=\"http://localhost:8081/todc-bootstrap/css/bootstrap.min.css\" rel=\"stylesheet\">\n" +
                "    <!-- TODC Bootstrap theme -->\n" +
                "    <link href=\"http://localhost:8081/todc-bootstrap/css/todc-bootstrap.min.css\" rel=\"stylesheet\">" +
                "    <script src=\"http://localhost:8081/js/jquery-1.11.1.min.js\"></script>\n" +
                "    <!-- Include all compiled plugins (below), or include individual files as needed -->\n" +
                "    <script src=\"http://localhost:8081/todc-bootstrap/js/bootstrap.min.js\"></script>");

        headerMore();

        out.println("</head>" + "<body>");
	}

	public abstract void body() throws SQLException, ClassNotFoundException;

    public void footerMore() {} // overwrite by other page

	public void footer() {
		out.println("</body>");
        footerMore();
		out.println("</html>");
	}
}

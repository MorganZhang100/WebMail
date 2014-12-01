package cs601.webmail.page;

import cs601.webmail.manager.ErrorManager;
import cs601.webmail.post_handler.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

public class DispatchServlet extends HttpServlet {
	public static Map<String,Class> mapping = new HashMap<String, Class>();
	static {
		mapping.put("/", LoginPage.class);
        mapping.put("/register", RegisterPage.class);
        mapping.put("/home", HomePage.class);
        mapping.put("/RegisterPost", RegisterPost.class);
        mapping.put("/LogoutPost", LogoutPost.class);
        mapping.put("/LoginPost", LoginPost.class);
        mapping.put("/HomeInboxPost", HomeInboxPost.class);
        mapping.put("/HomeEmailDetail", HomeEmailDetail.class);
        mapping.put("/HomeComposePost", HomeComposePost.class);
        mapping.put("/HomeCheckEmailPost", HomeCheckEmailPost.class);
        mapping.put("/HomeUnReadPost", HomeUnReadPost.class);
        mapping.put("/HomeDeleteToTrashPost", HomeDeleteToTrashPost.class);
        mapping.put("/HomeTrashFolderPost", HomeTrashFolderPost.class);
        mapping.put("/HomeEmptyAllTrashPost", HomeEmptyAllTrashPost.class);
        mapping.put("/HomeEditUserInformationPost", HomeEditUserInformationPost.class);
        mapping.put("/HomeChangeUserInformationPost", HomeChangeUserInformationPost.class);
        mapping.put("/HomeChangeUserPwdPost", HomeChangeUserPwdPost.class);
        mapping.put("/HomeAddNewFolderPost", HomeAddNewFolderPost.class);
        mapping.put("/HomeShowUserFoldersPost", HomeShowUserFoldersPost.class);
        mapping.put("/HomeChangeFolderPost", HomeChangeFolderPost.class);
	}

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {
//        super.doPost(req, resp);
        String uri = request.getRequestURI();
        System.out.println("Post: " + uri);
        PostHandler p = createPostHandler(uri, request, response);
        if ( p==null ) {
            response.sendRedirect("/files/error.html");
            return;
        }
        response.setContentType("text/html");
        p.generate();
    }

    public void doGet(HttpServletRequest request,
					  HttpServletResponse response)
		throws ServletException, IOException
	{
		String uri = request.getRequestURI();
        System.out.println(uri);
		Page p = createPage(uri, request, response);
		if ( p==null ) {
			response.sendRedirect("/files/error.html");
			return;
		}
		response.setContentType("text/html");
		p.generate();
	}

    public Page createPage(String uri,
                           HttpServletRequest request,
                           HttpServletResponse response)
    {
        Class pageClass = mapping.get(uri);
        try {
            Constructor<Page> ctor = pageClass.getConstructor(HttpServletRequest.class,
                    HttpServletResponse.class);
            return ctor.newInstance(request, response);
        }
        catch (Exception e) {
            ErrorManager.instance().error(e);
        }
        return null;
    }

    public PostHandler createPostHandler(String uri,
                           HttpServletRequest request,
                           HttpServletResponse response)
    {
        Class posthandlerClass = mapping.get(uri);
        try {
            Constructor<PostHandler> ctor = posthandlerClass.getConstructor(HttpServletRequest.class,
                    HttpServletResponse.class);
            return ctor.newInstance(request, response);
        }
        catch (Exception e) {
            ErrorManager.instance().error(e);
        }
        return null;
    }
}

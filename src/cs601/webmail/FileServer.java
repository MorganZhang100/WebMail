package cs601.webmail;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;

public class FileServer {
    public static void main(String[] args) throws Exception {
        Server server = new Server(8081);
        ResourceHandler resource_handler = new ResourceHandler();
        resource_handler.setDirectoriesListed(true);
        resource_handler.setResourceBase("./src/cs601/webmail/Public/");                    // must set root dir

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[] { resource_handler, 	  // file handler
                new DefaultHandler() // handles 404 etc...
        });
        server.setHandler(handlers);

        server.start();
        server.join();
    }

}
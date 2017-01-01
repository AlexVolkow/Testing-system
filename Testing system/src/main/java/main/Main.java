package main;

import accounts.AccountService;
import accounts.AccountServiceImpl;
import answers.AnswersService;
import answers.AnswersServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import servlets.*;
import tests.TestService;
import tests.TestServiceImpl;
import utils.AppProperties;

import java.util.Map;


/**
 * Created by disqustingman on 23.05.16.
 */
public class Main {
    private final static Logger logger = LogManager.getLogger(Main.class.getName());
    public static void main(String[] args) throws Exception{
        AccountService accountService = new AccountServiceImpl();
        TestService testService = new TestServiceImpl();
        AnswersService answersService = new AnswersServiceImpl();

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(new SignUpServlet(accountService)), SignUpServlet.PAGE_URL);
        context.addServlet(new ServletHolder(new SignInServlet(accountService)), SignInServlet.PAGE_URL);
        context.addServlet(new ServletHolder(new ThemesMenuServlet(accountService,testService)),ThemesMenuServlet.PAGE_URL);
        context.addServlet(new ServletHolder(new TestServlet(accountService,testService,answersService)),TestServlet.PAGE_URL);
        context.addServlet(new ServletHolder(new AddAnswersServlet(accountService,answersService)),AddAnswersServlet.PAGE_URL);
        context.addServlet(new ServletHolder(new CheckAnswersServlet(accountService,answersService,testService)),CheckAnswersServlet.PAGE_URL);
        context.addServlet(new ServletHolder(new LogOutServlet(accountService)),LogOutServlet.PAGE_URL);
        context.addServlet(new ServletHolder(new AdminServlet(accountService,testService)),AdminServlet.PAGE_URL);
        context.addServlet(new ServletHolder(new CreateTestServlet(accountService,testService)),CreateTestServlet.PAGE_URL);
        context.addServlet(new ServletHolder(new AdminActionServlet(accountService,testService)),AdminActionServlet.PAGE_URL);
        context.addServlet(new ServletHolder(new ProfileServlet(accountService)),ProfileServlet.PAGE_URL);

        ResourceHandler resource_handler = new ResourceHandler();
        resource_handler.setResourceBase("public_html");


        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resource_handler, context});

        Map<String,String> serverProp = AppProperties.instance().getServerProp();
        logger.info("Port "+serverProp.get("port"));
        logger.info("Admin password "+serverProp.get("adminpassword"));
        Server server = new Server(Integer.valueOf(serverProp.get("port")));
        server.setHandler(handlers);

        server.start();
        logger.info("Server started");
        server.join();
    }
}

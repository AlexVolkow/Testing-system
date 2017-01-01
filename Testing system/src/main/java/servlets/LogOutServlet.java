package servlets;

import accounts.AccountService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by disqustingman on 11.06.16.
 */
public class LogOutServlet extends HttpServlet {
    private static final Logger logger = LogManager.getLogger(LogOutServlet.class.getName());
    private AccountService accountService;
    public final static String PAGE_URL = "/logout";

    public LogOutServlet(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String sessionId = req.getSession().getId();
        if (accountService.isEnter(sessionId)){
            logger.info("User with email {} came out",accountService.getUserBySessionId(sessionId).getEmail());
            accountService.deleteSession(sessionId);
        }
        resp.sendRedirect("/");
    }
}

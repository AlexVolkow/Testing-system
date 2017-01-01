package servlets;

import accounts.AccountService;
import accounts.Admin;
import accounts.UserProfile;
import dbService.DBException;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.AppProperties;
import utils.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by disqustingman on 28.05.16.
 */
public class SignInServlet extends HttpServlet{
    private final static Logger logger = LogManager.getLogger(SignInServlet.class.getName());
    private AccountService accountService;
    public static final String PAGE_URL = "/signin";
    private String adminPass;

    public SignInServlet(AccountService accountService){
        adminPass = AppProperties.instance().getServerProp().get("adminpassword");
        this.accountService = accountService;
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String pass = req.getParameter("password");

        if (email == null || pass == null) {
            resp.setContentType("text/html;charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        if (email.equals("admin@admin") && pass.equals(adminPass)){
            resp.setStatus(HttpServletResponse.SC_OK);
            accountService.addSession(req.getSession().getId(),new Admin());
            logger.info("Admin entered");
            resp.sendRedirect(AdminServlet.PAGE_URL+"?viewmode=themes");
            return;
        }
        UserProfile profile = null;
        try {
            profile = accountService.getUserByEmail(email);
        } catch (DBException e) {
            e.printStackTrace();
        }
        pass = DigestUtils.md5Hex(pass);
        if (profile == null || !profile.getPass().equals(pass)) {
            resp.setContentType("text/html;charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            logger.info("User with email {} can not enter",email);
            Map<String,Object> pageVariables = new HashMap<>();
            pageVariables.put("content","<p>Unauthorized</p>" +
                    "            <p>A user with such data does not exist. Please register</p>" +
                    "            <p><a href = '/signup'>SignUp</a></p>");
            resp.getWriter().println(PageGenerator.instance().getPage("startPage.html",pageVariables));
            return;
        }
        accountService.addSession(req.getSession().getId(), profile);
        logger.info("User with email {} entered",email);
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.sendRedirect(ThemesMenuServlet.PAGE_URL);
    }
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String sessionId = req.getSession().getId();
        UserProfile profile = accountService.getUserBySessionId(sessionId);
        if (profile == null) {
            resp.setContentType("text/html;charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } else {
            accountService.deleteSession(sessionId);
            resp.setContentType("text/html;charset=utf-8");
            resp.getWriter().println("Goodbye!");
            resp.setStatus(HttpServletResponse.SC_OK);
        }
    }
}

package servlets;

import accounts.AccountService;
import accounts.UserProfile;
import dbService.DBException;
import dbService.DBServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by disqustingman on 13.06.16.
 */
public class ProfileServlet extends HttpServlet {
    private static final Logger logger = LogManager.getLogger(ProfileServlet.class.getName());
    public static final String PAGE_URL = "/profile";
    private AccountService accountService;

    public ProfileServlet(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String sessionId = req.getSession().getId();
        if (accountService.isEnter(sessionId)==false){
            resp.setContentType("text/html;charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        long userId = accountService.getUserBySessionId(sessionId).getId();
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String clas = req.getParameter("clas");
        String pass = accountService.getUserBySessionId(sessionId).getPass();
        long clasId = 0;
        try {
            clasId = DBServiceImpl.instance().getClasId(clas);
            UserProfile userProfile = new UserProfile(userId,name,email,pass,clasId);
            accountService.updateUser(userProfile);
            accountService.deleteSession(sessionId);
            accountService.addSession(sessionId,userProfile);
            logger.info("User with email {} updated profile",userProfile.getEmail());
        } catch (DBException e) {
            e.printStackTrace();
        }
        resp.sendRedirect("/test/themes");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String sessionId = req.getSession().getId();
        if (accountService.isEnter(sessionId)==false){
            resp.setContentType("text/html;charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        UserProfile profile = accountService.getUserBySessionId(sessionId);
        String clas = null;
        try {
            clas = accountService.getClasName(profile.getClasId());
        } catch (DBException e) {
            e.printStackTrace();
        }
        String insertHTML = "<div class=\"container text-center\">\n" +
                "<form class=\"form-horizontal\" method=\"post\" action=\"/profile\">\n" +
                "         <h1>Profile</h1>\n" +
                "         <div class=\"form-group\">\n" +
                "            <label for=\"inputName3\" class=\"col-sm-4 control-label\">Name</label>\n" +
                "            <div class=\"col-sm-13\">\n" +
                "              <input type=\"text\" name=\"name\" class=\"form-control\" id=\"inputName3\" placeholder=\"Name\" value='"+profile.getLogin()+"'>\n" +
                "            </div>\n" +
                "          </div>\n" +
                "          <div class=\"form-group\">\n" +
                "            <label for=\"inputEmail3\" class=\"col-sm-4 control-label\">Email</label>\n" +
                "            <div class=\"col-sm-13\">\n" +
                "              <input type=\"email\" name=\"email\" class=\"form-control\" id=\"inputEmail3\" placeholder=\"Email\" value='"+profile.getEmail()+"'>\n" +
                "            </div>\n" +
                "          </div>\n" +
                "          <div class=\"form-group\">\n" +
                "            <label for=\"inputClass3\" class=\"col-sm-4 control-label\">Class</label>\n" +
                "            <div class=\"col-sm-13\">\n" +
                "              <input type=\"text\" name=\"clas\" class=\"form-control\" id=\"inputClass3\" placeholder=\"Class\" value='"+clas+"'>\n" +
                "            </div>\n" +
                "          </div>\n" +
                "          <div class=\"form-group\">\n" +
                "            <div class=\"\">\n" +
                "              <button type=\"submit\" class=\"btn btn-default\">Submit</button>\n" +
                "            </div>\n" +
                "          </div>\n" +
                "        </form>";
        Map<String,Object> pageVariables = new HashMap<>();
        pageVariables.put("content",insertHTML);
        pageVariables.put("css","input[type='text'] {width:35%;}");
        resp.setContentType("text/html;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().println(PageGenerator.instance().getPage("page.html",pageVariables));
    }

}

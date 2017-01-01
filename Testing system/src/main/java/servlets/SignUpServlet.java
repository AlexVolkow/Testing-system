package servlets;

import accounts.AccountService;
import accounts.UserProfile;
import dbService.DBException;
import dbService.DBServiceImpl;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.CheckInjection;
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
public class SignUpServlet extends HttpServlet {
    private static final Logger logger = LogManager.getLogger(SignUpServlet.class.getName());
    private AccountService accountService;
    public static final String PAGE_URL = "/signup";

    public SignUpServlet(AccountService accountService){
        this.accountService = accountService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String,Object> pageVariables = new HashMap<>();
        String insertHTML = "<div class=\"container text-center\">\n" +
                "        <form class=\"form-horizontal\" method=\"post\" action=\"/signup\">\n" +
                "         <h1>Sign Up</h1>\n" +
                "         <div class=\"form-group\">\n" +
                "            <label for=\"inputName3\" class=\"col-sm-4 control-label\">Name</label>\n" +
                "            <div class=\"col-sm-13\">\n" +
                "              <input type=\"text\" name=\"name\" class=\"form-control\" id=\"inputName3\" placeholder=\"Name\">\n" +
                "            </div>\n" +
                "          </div>\n" +
                "          <div class=\"form-group\">\n" +
                "            <label for=\"inputEmail3\" class=\"col-sm-4 control-label\">Email</label>\n" +
                "            <div class=\"col-sm-13\">\n" +
                "              <input type=\"email\" name=\"email\" class=\"form-control\" id=\"inputEmail3\" placeholder=\"Email\">\n" +
                "            </div>\n" +
                "          </div>\n" +
                "          <div class=\"form-group\">\n" +
                "            <label for=\"inputPassword3\" class=\"col-sm-4 control-label\">Password</label>\n" +
                "            <div class=\"col-sm-13\">\n" +
                "              <input type=\"password\" name=\"password\" class=\"form-control\" id=\"inputPassword3\" placeholder=\"Password\">\n" +
                "            </div>\n" +
                "          </div>\n" +
                "          <div class=\"form-group\">\n" +
                "            <label for=\"inputClass3\" class=\"col-sm-4 control-label\">Class</label>\n" +
                "            <div class=\"col-sm-13\">\n" +
                "              <input type=\"text\" name=\"clas\" class=\"form-control\" id=\"inputClass3\" placeholder=\"Class\">\n" +
                "            </div>\n" +
                "          </div>\n" +
                "          <div class=\"form-group\">\n" +
                "            <div class=\"\">\n" +
                "              <button type=\"submit\" class=\"btn btn-default\">Submit</button>\n" +
                "            </div>\n" +
                "          </div>\n" +
                "        </form>\n" +
                "    </div>";
        pageVariables.put("content",insertHTML);
        resp.getWriter().println(PageGenerator.instance().getPage("startPage.html",pageVariables));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String name = req.getParameter("name");
        String clas = req.getParameter("clas");
        if (email ==null || password==null || name==null || clas==null){
            resp.setContentType("text/html;charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        name = CheckInjection.normalize(name);
        email = CheckInjection.normalize(email);
        clas = CheckInjection.normalize(clas);
        password = DigestUtils.md5Hex(password);
        Long clasId = new Long(0);
        try {
            clasId = accountService.getClasId(clas);
        }catch (DBException e){

        }catch (NullPointerException e){
            logger.error("Class with name = "+clas +" not found",e);
            resp.setContentType("text/html;charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        UserProfile profile = new UserProfile(name,email,password,clasId);
        boolean isOk = true;
        UserProfile testProfile = null;
        try {
            testProfile = accountService.getUserByEmail(email);
        }catch (DBException ignore){}

        if (testProfile!=null){
            isOk = false;
        }
        if (isOk == false || email.equals("admin@admin")){
            resp.setContentType("text/html;charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            Map<String,Object> pageVariables = new HashMap<>();
            pageVariables.put("content","<p>A person with this email is already registered</p>"+
                                        "<p><a href = '/signup'>SignUp</a>");
            resp.getWriter().println(PageGenerator.instance().getPage("startPage.html",pageVariables));
            return;
        }
        try {
            long userId = accountService.addNewUser(profile);
            profile.setId(userId);
        } catch (DBException e) {
            e.printStackTrace();
        }
        logger.info("The user registered with the email {}",email);
        resp.setContentType("text/html;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);
        StringBuilder insertHTML = new StringBuilder();
        insertHTML.append("<center><p>Registration completed successfully</p>");
        insertHTML.append("<p><a href = '/'>Sign In</a></p></center>");
        Map<String,Object> pageVariables = new HashMap<>();
        pageVariables.put("content",insertHTML.toString());
        resp.getWriter().println(PageGenerator.instance().getPage("startPage.html",pageVariables));
    }
}

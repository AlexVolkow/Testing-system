package servlets;

import accounts.AccountService;
import accounts.UserProfile;
import dbService.DBException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.PageGenerator;
import tests.TestService;
import tests.Theme;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by disqustingman on 11.06.16.
 */
public class AdminServlet extends HttpServlet {
    public final static String PAGE_URL = "/admin";
    private AccountService accountService;
    private TestService testService;

    public AdminServlet(AccountService accountService,TestService testService) {
        this.accountService = accountService;
        this.testService = testService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String sessionId = req.getSession().getId();
        if (!accountService.isEnter(sessionId) || !accountService.getUserBySessionId(sessionId).getEmail().equals("admin")){
            resp.setContentType("text/html;charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        String viewmode = req.getParameter("viewmode");
        if (viewmode==null || (!viewmode.equals("themes") && !viewmode.equals("users"))){
            resp.setContentType("text/html;charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        if (viewmode.equals("themes")){
            StringBuilder insertHTML = new StringBuilder();
            insertHTML.append("<div class='panel panel-default' ><table class='table'>");
            insertHTML.append("<tr><td>Name<td>Class<td>Action</tr>");
            try {
                for (int i = 1;i<=11;i++){
                    List<Theme> themes = testService.getThemesByClasId(i);
                    if (themes==null){
                        continue;
                    }
                    for (Theme theme : themes){
                        insertHTML.append("<tr>");
                        insertHTML.append("<td>"+theme.getName());
                        insertHTML.append("<td>"+theme.getClas());
                        insertHTML.
                                append("<td><a href='/admin/action?type=edit&id="+theme.getId()+"'>Edit</a>, ").
                                append("<a href='/admin/action?type=delete&id="+theme.getId()+"'>Delete</a>, ").
                                append("<a href='/admin/action?type=reset&id="+theme.getId()+"'>Reset results</a>");
                    }
                }
            } catch (DBException e) {
                e.printStackTrace();
            }
            insertHTML.append("</table></div>");
            insertHTML.append("<a href='/admin/create'>Create test</a>");
            resp.setContentType("text/html;charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_OK);
            Map<String,Object> pageVariables = new HashMap<>();
            pageVariables.put("content",insertHTML);
            resp.getWriter().print(PageGenerator.instance().getPage("adminPanel.html",pageVariables));
            return;
        }
        if (viewmode.equals("users")){
            StringBuilder insertHTML = new StringBuilder();
            insertHTML.append("<div class='panel panel-default' ><table class='table'>");
            insertHTML.append("<tr><td>№<td>Name<td>Clas<td>Email<td>Action</tr>");
            int count = 0;
            try{
                List<UserProfile> userProfiles = accountService.getAllUsers();
                try{
                    for (UserProfile user : userProfiles){
                        count++;
                        insertHTML.append("<tr><td>"+count).
                                   append("<td>"+user.getLogin()).
                                   append("<td>"+accountService.getClasName(user.getClasId())).
                                   append("<td>"+user.getEmail()).
                                   append("<td><a href='/admin/action?type=deleteuser&id="+user.getId()+"'>Delete</a>");
                    }
                }catch (NullPointerException e){
                    e.printStackTrace();
                }
                insertHTML.append("</table></div>");
                resp.setContentType("text/html;charset=utf-8");
                resp.setStatus(HttpServletResponse.SC_OK);
                Map<String,Object> pageVariables = new HashMap<>();
                pageVariables.put("content",insertHTML);
                resp.getWriter().print(PageGenerator.instance().getPage("adminPanel.html",pageVariables));
                return;
            }catch (DBException e){
                e.printStackTrace();
            }
        }
    }
}

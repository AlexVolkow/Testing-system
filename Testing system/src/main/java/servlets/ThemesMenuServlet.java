package servlets;

import accounts.AccountService;
import accounts.UserProfile;
import dbService.DBException;
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
 * Created by disqustingman on 28.05.16.
 */
public class ThemesMenuServlet extends HttpServlet {
    public static final String PAGE_URL = "/test/themes";
    private AccountService accountService;
    private TestService testService;

    public ThemesMenuServlet(AccountService accountService, TestService testService){
        this.accountService = accountService;
        this.testService = testService;
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
        resp.setContentType("text/html;charset=utf-8");
        long userId = profile.getId();
        try {
            List<Theme> themes = testService.getThemesByClasId(profile.getClasId());
            resp.setStatus(HttpServletResponse.SC_OK);
            StringBuilder insertHTML = new StringBuilder();
            insertHTML.append("<h3>"+accountService.getUserBySessionId(sessionId).getLogin()+"("+accountService.getClasName(profile.getClasId())+" class)</h3>");
            insertHTML.append("<ul type='circle'>");
            if (themes!=null){
                for (Theme theme : themes){
                    boolean isPassedTest = testService.isPassedTest(userId,theme.getId());
                    insertHTML.append("<li>" + theme.getName()+" ");
                    if (!isPassedTest) {
                        insertHTML.append("<a href='" + TestServlet.PAGE_URL + "?id=" + theme.getId() + "'><font color='green'>Start</font></a></td>");
                    }else{
                        insertHTML.append("<a href='" + CheckAnswersServlet.PAGE_URL + "?testId=" + theme.getId() + "'><font color='red'>View results</font></a></p>");
                    }
                    insertHTML.append("<hr>");
                    insertHTML.append("</li>");
                }
            }else{
                insertHTML.append("For your class has no tests");
            }
            insertHTML.append("</ul>");
            Map<String,Object> pageVariables = new HashMap<>();
            pageVariables.put("content",insertHTML);
            pageVariables.put("css","input[type='text'] {width:100%;}");
            resp.getWriter().print(PageGenerator.instance().getPage("page.html",pageVariables));
            return;
        } catch (DBException e) {
            e.printStackTrace();
        }
        resp.setContentType("text/html;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
}

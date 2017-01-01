package servlets;

import accounts.AccountService;
import dbService.DBException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.CheckInjection;
import utils.PageGenerator;
import tests.TestService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * Created by disqustingman on 12.06.16.
 */
public class CreateTestServlet extends HttpServlet {
    private static final Logger logger = LogManager.getLogger(CreateTestServlet.class.getName());
    public final static String PAGE_URL = "/admin/create";
    private AccountService accountService;
    private TestService testService;

    public CreateTestServlet(AccountService accountService, TestService testService) {
        this.accountService = accountService;
        this.testService = testService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String sessionId = req.getSession().getId();
        if (!accountService.isEnter(sessionId)|| !accountService.getUserBySessionId(sessionId).getEmail().equals("admin")){
            resp.setContentType("text/html;charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        resp.setContentType("text/html;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);
        Map<String,Object> pageVariables = new HashMap<>();
        pageVariables.put("caption","Generate test");
        pageVariables.put("countQuestions","1");
        pageVariables.put("theme","");
        pageVariables.put("clas","");
        String insertHTML = "<tbody><tr id = \"t1\">" +
                "<td>1</td>" +
                "<td><textarea name = \"q1\" cols=\"30\" rows=\"7\"></textarea></td>" +
                "<td><textarea name = \"a1\" cols=\"30\" rows=\"7\"></textarea></td>" +
                "<td><input class=\"btn btn-default\" type=\"button\" onclick=\"del(1);\" value =\"Delete\">" +
                "</tr></tbody>";
        pageVariables.put("content",insertHTML);
        resp.getWriter().println(PageGenerator.instance().getPage("editPage.html",pageVariables));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String sessionId = req.getSession().getId();
        if (!accountService.isEnter(sessionId) || req.getParameter("theme")==null || req.getParameter("clas")==null
                || !accountService.getUserBySessionId(sessionId).getEmail().equals("admin")){
            resp.setContentType("text/html;charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        String theme = req.getParameter("theme");
        String clas = req.getParameter("clas");
        theme = CheckInjection.normalize(theme);
        clas = CheckInjection.normalize(clas);
        List<String> questions = new ArrayList<>();
        List<String> rightAnswers = new ArrayList<>();
        Enumeration parameterNames = req.getParameterNames();
        while (parameterNames.hasMoreElements()){
            String paramName = (String) parameterNames.nextElement();
            if (paramName.equals("theme") || paramName.equals("clas")){
                continue;
            }
            String text = req.getParameter(paramName);
            text = text.replaceAll("\r\n|\r|\n","<br>");
            if (paramName.charAt(0)=='q'){
                questions.add(text);
            }
            if (paramName.charAt(0)=='a'){
                rightAnswers.add(text);
            }
        }
        try {
            String update = req.getParameter("update");
            if (update != null){
                long themeId = Long.valueOf(update);
                testService.updateTest(themeId,theme,clas,questions,rightAnswers);
                logger.info("Test with theme {} updated",theme);
            }else {
                testService.addTest(theme, clas, questions, rightAnswers);
                logger.info("Test with theme {} added",theme);
            }
            resp.sendRedirect("/admin?viewmode=themes");
        } catch (DBException e) {
            e.printStackTrace();
        }
    }
}

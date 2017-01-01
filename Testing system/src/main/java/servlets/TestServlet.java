package servlets;

import accounts.AccountService;
import answers.AnswersService;
import dbService.DBException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.PageGenerator;
import tests.Question;
import tests.TestService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by disqustingman on 06.06.16.
 */
public class TestServlet extends HttpServlet {
    private static final Logger logger = LogManager.getLogger(TestServlet.class.getName());
    public static final String PAGE_URL = "/test/passing";
    private AccountService accountService;
    private TestService testService;
    private AnswersService answersService;

    public TestServlet(AccountService accountService, TestService testService, AnswersService answersService) {
        this.accountService = accountService;
        this.testService = testService;
        this.answersService = answersService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String sessionId = req.getSession().getId();
        if (req.getParameter("id")==null || !accountService.isEnter(sessionId)){
            resp.setContentType("text/html;charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        long userId = accountService.getUserBySessionId(sessionId).getId();
        long themeId = Long.valueOf(req.getParameter("id"));
        try {
            if (testService.isPassedTest(userId,themeId)){
                resp.setContentType("text/html;charset=utf-8");
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }else{
                testService.addPassedTest(userId,themeId);
            }
            String theme = testService.getTheme(themeId).getName();
            List<Question> questions = testService.getQuestionsByThemeId(themeId);
            resp.setContentType("text/html;charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_OK);
            StringBuilder insertHTML = new StringBuilder();
            insertHTML.append("<h3>"+theme+"</h3>");
            insertHTML.append("<form method='post' action='"+AddAnswersServlet.PAGE_URL+"'>");
            insertHTML.append("<table width = 90% cellspacing='5'>");
            int count = 0;
            for (Question question : questions){
                answersService.addAnswer(userId,question.getId(),"");
                count++;
                insertHTML.append("<tr><td width='20'><img src='http://olymp.ifmo.ru/icms/shared/images/fe/marker_na.gif'></td><td>Question №"+count+"</td></tr>");
                insertHTML.append("<tr><td colspan='3' height='1' width='1'><hr></td></tr>");
                insertHTML.append("<tr><td width='20'></td>");
                insertHTML.append("<td>"+question.getText()+"</td>");
                insertHTML.append("<td valign='top' align='right'><p>Your answer<p><input class='form-control' name='"+question.getId()+"' type='text' ></td></tr>");
            }
            insertHTML.append("<tr><td colspan='3' height='1' width='1'><hr></td></tr>");
            insertHTML.append("</table>");
            insertHTML.append("<p><input type='submit' class='btn btn-default'></p>");
            insertHTML.append("</form>");
            Map<String,Object> pageVariables = new HashMap<>();
            pageVariables.put("content",insertHTML.toString());
            pageVariables.put("css","input[type='text'] {width:100%;}");
            logger.info("User with id = {} is started test with theme = {}",userId,theme);
            resp.getWriter().println(PageGenerator.instance().getPage("page.html",pageVariables));
            return;
        } catch (DBException e) {
            e.printStackTrace();
        }
        resp.setContentType("text/html;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
}

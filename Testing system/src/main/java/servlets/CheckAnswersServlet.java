package servlets;

import accounts.AccountService;
import answers.Answer;
import answers.AnswersService;
import dbService.DBException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.PageGenerator;
import tests.RightAnswer;
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
 * Created by disqustingman on 07.06.16.
 */
public class CheckAnswersServlet extends HttpServlet {
    public static final String PAGE_URL = "/test/results";
    private final static Logger logger = LogManager.getLogger(CheckAnswersServlet.class.getName());
    private AccountService accountService;
    private TestService testService;
    private AnswersService answersService;

    public CheckAnswersServlet(AccountService accountService,AnswersService answersService,TestService testService){
        this.accountService = accountService;
        this.testService = testService;
        this.answersService = answersService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String sessionId = req.getSession().getId();
        if (!accountService.isEnter(sessionId) || req.getParameter("testId")==null){
            resp.setContentType("text/html;charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        long themeId = Long.valueOf(req.getParameter("testId"));
        long userId = accountService.getUserBySessionId(sessionId).getId();
        int countRightAnswer = 0;
        int countQuestions = 0;
        try {
            List<Answer> userAnswers = answersService.getUserAnswersByThemeId(userId,themeId);
            StringBuilder table = new StringBuilder();
            table.
                    append("<div class='panel panel-default' ><table class='table'>").
                    append("<tr><td>Question<td>Result");
            for (Answer answer : userAnswers){
                countQuestions++;
                long questionId = answer.getQuestionId();
                table.append("<tr><td>"+Integer.toString(countQuestions));
                RightAnswer rightAnswer = testService.getRightAnswerByQuestionId(questionId);
                if (rightAnswer.isRight(answer.getText())){
                    countRightAnswer++;
                    table.append("<td bgcolor ='green'>OK");
                }else{
                    table.append("<td bgcolor ='red'>NO");
                }
            }
            table.append("</table></div>");
            resp.setContentType("text/html;charset=utf-8");
            logger.info("User with id = {} is checked result test with id = {}",userId,themeId);
            resp.setStatus(HttpServletResponse.SC_OK);
            StringBuilder insertHTML = new StringBuilder();
            insertHTML.append("You result: "+Integer.toString(countRightAnswer)+"/"+Integer.toString(countQuestions));
            insertHTML.append(table);
            insertHTML.append("<a href='/test/themes'>Back to list of theme</a>");
            Map<String,Object> pageVariables = new HashMap<>();
            pageVariables.put("content",insertHTML.toString());
            pageVariables.put("css","div[class='panel panel-default'] {width:50%;}");
            resp.getWriter().println(PageGenerator.instance().getPage("page.html",pageVariables));
        }catch (DBException e){
            e.printStackTrace();
        }

    }
}

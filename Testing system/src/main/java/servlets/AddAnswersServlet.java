package servlets;

import accounts.AccountService;
import answers.AnswersService;
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
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by disqustingman on 07.06.16.
 */
public class AddAnswersServlet extends HttpServlet {
    private static final Logger logger = LogManager.getLogger(AddAnswersServlet.class.getName());
    public static final String PAGE_URL = "/test/addanswers";
    private AccountService accountService;
    private AnswersService answersService;

    public AddAnswersServlet(AccountService accountService, AnswersService answersService) {
        this.accountService = accountService;
        this.answersService = answersService;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String sessionId = req.getSession().getId();
        if (!accountService.isEnter(sessionId)){
            resp.setContentType("text/html;charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        long userId = accountService.getUserBySessionId(sessionId).getId();
        long themeId = -1;
        Enumeration parameterNames = req.getParameterNames();
        while (parameterNames.hasMoreElements()){
            String paramName = (String) parameterNames.nextElement();
            String text = req.getParameter(paramName);
            if (text==null){
                text = "";
            }
            long questionId = Long.valueOf(paramName);
            try {
                if (themeId==-1) {
                    themeId = DBServiceImpl.instance().getQuestion(questionId).getThemeId();
                }
                answersService.updateAnswer(userId, questionId, text);
            }catch(DBException e){
                e.printStackTrace();
            }
        }
        resp.setContentType("text/html;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);
        logger.info("User with id = {} finished test with id = {}",userId,themeId);
        StringBuilder insertHTML = new StringBuilder();
        insertHTML.append("<center>");
        insertHTML.append("<p>Congratulations , the test is completed</p>");
        insertHTML.append("<p><a href = '"+CheckAnswersServlet.PAGE_URL+"?testId="+Long.toString(themeId)+"'>Check result</a></p>");
        insertHTML.append("<a href='/test/themes'>Back to list of theme</a>");
        insertHTML.append("</center>");
        Map<String,Object> pageVariables = new HashMap<>();
        pageVariables.put("content",insertHTML.toString());
        pageVariables.put("css","input[type='text'] {width:100%;}");
        resp.getWriter().println(PageGenerator.instance().getPage("page.html",pageVariables));
    }
}

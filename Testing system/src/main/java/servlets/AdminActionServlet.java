package servlets;

import accounts.AccountService;
import dbService.DBException;
import freemarker.ext.beans.HashAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.PageGenerator;
import tests.Question;
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
 * Created by disqustingman on 12.06.16.
 */
public class AdminActionServlet extends HttpServlet {
    private static final Logger logger = LogManager.getLogger(AddAnswersServlet.class.getName());
    public final static String PAGE_URL = "/admin/action";
    private AccountService accountService;
    private TestService testService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String sessionId = req.getSession().getId();
        if (!accountService.isEnter(sessionId) || !accountService.getUserBySessionId(sessionId).getEmail().equals("admin")){
            resp.setContentType("text/html;charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        String type = req.getParameter("type");
        String id = req.getParameter("id");
        if (type==null || id == null || type.equals("") || id.equals("")){
            resp.setContentType("text/html;charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        if (type.equals("deleteuser")){
            try {
                accountService.deleteUser(Long.valueOf(id));
                logger.info("User with id = {} deleted",id);
            } catch (DBException e) {
                e.printStackTrace();
            }
            resp.sendRedirect("/admin?viewmode=users");
        }
        if (type.equals("delete")){
            try {
                testService.deleteTest(Long.valueOf(id));
                logger.info("Test with id = {} deleted",id);
            } catch (DBException e) {
                e.printStackTrace();
            }
            resp.sendRedirect("/admin?viewmode=themes");
        }
        if (type.equals("edit")){
            try{
                Map<String,Object> pageVariables = new HashMap<>();
                pageVariables.put("caption","EditTest");
                long themeId = Long.valueOf(id);
                Theme theme = testService.getTheme(themeId);
                String themeName = theme.getName();
                String clas = theme.getClas();
                pageVariables.put("theme",themeName);
                pageVariables.put("clas",clas);
                List<Question> questions = testService.getQuestionsByThemeId(themeId);
                pageVariables.put("countQuestions",Integer.toString(questions.size()));
                StringBuilder insertHTML = new StringBuilder();
                for (int i = 1;i<=questions.size();i++){
                    String rightAnswer = testService.getRightAnswerByQuestionId(questions.get(i-1).getId()).getText();
                    String question = questions.get(i-1).getText().replaceAll("<br>","\n");
                    insertHTML.append("<tr id = 't"+i+"'>").
                               append("<td>"+i+"</td>").
                               append("<td><textarea name = 'q"+i+"'cols='30' rows='7'>"+question+"</textarea></td>").
                               append("<td><textarea name = 'a"+i+"' cols='30' rows='7'>"+rightAnswer+"</textarea></td>");
                }
                insertHTML.append("<input type='hidden' name='update' value='"+themeId+"'>");
                pageVariables.put("content",insertHTML.toString());
                resp.setContentType("text/html;charset=utf-8");
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().println(PageGenerator.instance().getPage("editPage.html",pageVariables));
            }catch (DBException e){
                e.printStackTrace();
            }
        }
        if (type.equals("reset")){
            try {
                long themeId = Long.valueOf(id);
                testService.resetResults(themeId);
                logger.info("Test results with id = {} are reset",themeId);
                String insertHTML = "<p>Test with theme "+ testService.getTheme(themeId).getName() +" reset</p>" +
                        "<p><a href='/admin?viewmode=themes'>Return to the list of tests</a>";
                Map<String,Object> pageVariables = new HashMap<>();
                pageVariables.put("content",insertHTML);
                resp.setContentType("text/html;charset=utf-8");
                resp.setStatus(HttpServletResponse.SC_OK);
                String crutch = PageGenerator.instance().getPage("adminPanel.html",pageVariables);
                crutch = crutch.replace("img/","../img/");
                resp.getWriter().println(crutch);
            }catch (DBException e){
                e.printStackTrace();
            }
            //resp.sendRedirect("/admin?viewmode=themes");
        }
    }

    public AdminActionServlet(AccountService accountService, TestService testService) {
        this.accountService = accountService;
        this.testService = testService;
    }
}

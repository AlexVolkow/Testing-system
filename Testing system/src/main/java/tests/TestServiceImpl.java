package tests;

import dbService.DBException;
import dbService.DBService;
import dbService.DBServiceImpl;
import dbService.dataSets.QuestionsDataSet;
import dbService.dataSets.ThemesDataSet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by disqustingman on 23.05.16.
 */
public class TestServiceImpl implements TestService {
    @Override
    public void addPassedTest(long userId, long themeId) throws DBException {
        dbService.addPassedTest(userId,themeId);
    }

    private DBService dbService;

    public TestServiceImpl() throws DBException{
        dbService = DBServiceImpl.instance();
    }
    @Override
    public List<Question> getQuestionsByThemeId(long themeId) throws DBException {
        List<Question> res = new ArrayList<>();
        List<QuestionsDataSet> temp = dbService.getQuestionsByThemeId(themeId);
        if (temp==null){
            return null;
        }
        for (QuestionsDataSet it : temp){
            res.add(it.getQuestion());
        }
        return res;
    }

    @Override
    public Question getQuestion(long id) throws DBException {
        return dbService.getQuestion(id).getQuestion();
    }

    @Override
    public void deleteTest(long themeId) throws DBException {
        List<QuestionsDataSet> questions = dbService.getQuestionsByThemeId(themeId);
        if (questions==null){
            return;
        }
        for (QuestionsDataSet question : questions){
            dbService.deleteAnswersByQuestionId(question.getId());
            dbService.deleteRightAnswer(question.getId());
            dbService.deleteQuestion(question.getName(),question.getThemeId());
        }
        dbService.deleteRecordOfTest(themeId);
        dbService.deleteTheme(themeId);
    }

    @Override
    public void addTest(String theme, String clas, List<String> questions, List<String> rightAnswers) throws DBException {
        dbService.addTheme(theme,clas);
        for (int i = 0;i<questions.size();i++){
            long questionId = dbService.addQuestion(questions.get(i),theme);
            dbService.addRightAnswer(questionId,rightAnswers.get(i));
        }
    }

    @Override
    public RightAnswer getRightAnswer(long id) throws DBException {
        return dbService.getRightAnswer(id).getRightAnswer();
    }

    @Override
    public RightAnswer getRightAnswerByQuestionId(long questionId) throws DBException {
        return dbService.getRightAnswerByQuestionId(questionId).getRightAnswer();
    }

    @Override
    public Long getThemeId(String name) throws DBException {
        return dbService.getThemeId(name);
    }

    @Override
    public void updateTest(long themeId, String theme, String clas, List<String> questions, List<String> rightAnswers) throws DBException {
        dbService.updateTheme(themeId,theme,clas);
        List<Question> oldQuestions = this.getQuestionsByThemeId(themeId);
        for (int i = 0;i < oldQuestions.size();i++){
            String oldText = oldQuestions.get(i).getText();
            String newText = questions.get(i);
            long questionId = dbService.updateQuestion(themeId,oldText,newText);
            dbService.deleteRightAnswer(questionId);
            dbService.addRightAnswer(questionId,rightAnswers.get(i));
        }
        if (oldQuestions.size()<questions.size()) {
            for (int i = oldQuestions.size(); i < questions.size(); i++) {
                long questionId = dbService.addQuestion(questions.get(i), theme);
                dbService.addRightAnswer(questionId, rightAnswers.get(i));
                List<Long> usersPassedTest = dbService.getAllUserWherePassedTest(themeId);
                for (Long userId : usersPassedTest){
                    dbService.addAnswer(userId,questionId,"");
                }
            }
        }
    }

    @Override
    public void resetResults(long themeId) throws DBException {
        dbService.deleteRecordOfTest(themeId);
        List<Question> questions = this.getQuestionsByThemeId(themeId);
        if (questions==null){
            return;
        }
        for (Question question : questions){
            dbService.deleteAnswersByQuestionId(question.getId());
        }
    }

    @Override
    public Theme getTheme(long id) throws DBException {
        return dbService.getTheme(id).getTheme();
    }

    @Override
    public boolean isPassedTest(long userId, long themeId) throws DBException {
        return dbService.isPassedTest(userId,themeId);
    }

    @Override
    public List<Theme> getThemesByClasId(long clasId) throws DBException{
        List<ThemesDataSet> themes = dbService.getThemesByClasId(clasId);
        if (themes==null){
            return null;
        }
        List<Theme> res = new ArrayList<>();
        for (ThemesDataSet it : themes){
            res.add(it.getTheme());
        }
        return res;
    }
}

package tests;

import dbService.DBException;

import java.util.List;

/**
 * Created by disqustingman on 23.05.16.
 */
public interface TestService {
    List<Question> getQuestionsByThemeId(long themeId) throws DBException;
    Question getQuestion(long id) throws DBException;

    RightAnswer getRightAnswer(long id) throws DBException;
    RightAnswer getRightAnswerByQuestionId(long questionId) throws DBException;

    List<Theme> getThemesByClasId(long clasId) throws DBException;
    Theme getTheme(long id) throws DBException;
    Long getThemeId(String name) throws DBException;

    void resetResults(long themeId) throws DBException;

    boolean isPassedTest(long userId,long themeId) throws DBException;
    void addPassedTest(long userId,long themeId) throws DBException;
    void updateTest(long themeId,String theme,String clas,List<String> questions,List<String> rightAnswers) throws DBException;
    void addTest(String theme,String clas,List<String> questions,List<String> rightAnswers) throws DBException;
    void deleteTest(long themeId) throws DBException;
}

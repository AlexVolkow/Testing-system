package dbService;

import dbService.dataSets.*;

import java.sql.Connection;
import java.util.List;

/**
 * Created by disqustingman on 24.05.16.
 */
public interface DBService {
    UsersDataSet getUser(long id) throws DBException;
    UsersDataSet getUserByEmail(String email) throws DBException;
    void updateUser(long userId,String name,String email,long clasId) throws DBException;
    List<UsersDataSet> getAllUsers() throws DBException;
    void deleteUser(long id) throws DBException;
    Long addUser(String name,String email,String password,long clasId) throws DBException;

    void cleanUp() throws DBException;
    Connection getMysqlConnection();

    Long addTheme(String name,String clas) throws DBException;
    void deleteTheme(long themeId) throws DBException;
    void updateTheme(long id,String theme,String clas) throws DBException;
    ThemesDataSet getTheme(long id) throws DBException;
    Long getThemeId(String name) throws DBException;
    List<ThemesDataSet> getThemesByClasId(long clasId) throws DBException;

    String getClasName(long id) throws DBException;
    Long getClasId(String name) throws DBException;

    List<QuestionsDataSet> getQuestionsByThemeId(long themeId) throws DBException;
    Long addQuestion(String text,String theme) throws DBException;
    Long updateQuestion(long themeId,String oldText,String newText) throws DBException;
    void deleteQuestion(String text,long themeId) throws DBException;
    QuestionsDataSet getQuestion(long id) throws DBException;

    Long addRightAnswer(long questionId,String text) throws DBException;
    void deleteRightAnswer(long questionId) throws DBException;
    RightAnswersDataSet getRightAnswer(long id) throws DBException;
    RightAnswersDataSet getRightAnswerByQuestionId(long questionId) throws DBException;

    Long addAnswer(long userId,long questionId,String text) throws DBException;
    void deleteAnswer(long userId, long questionId,String text) throws DBException;
    Long updateAnswer(long userId,long questionId, String text) throws DBException;
    void deleteAnswersByQuestionId(long questionId) throws DBException;
    AnswersDataSet getAnswer(long id) throws DBException;
    List<AnswersDataSet> getUserAnswersByThemeId(long userId,long themeId) throws DBException;

    void addPassedTest(long userId,long themeId) throws DBException;
    boolean isPassedTest(long userId,long themeId) throws DBException;
    void deleteRecordOfTest(long themeId) throws DBException;
    List<Long> getAllUserWherePassedTest(long themeId) throws DBException;
}

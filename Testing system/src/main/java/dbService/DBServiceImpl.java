package dbService;

import dbService.dao.*;
import dbService.dataSets.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.h2.jdbcx.JdbcDataSource;
import tests.Question;
import utils.AppProperties;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by disqustingman on 24.05.16.
 */
public class DBServiceImpl implements DBService {
    private final static Logger logger = LogManager.getLogger(DBServiceImpl.class.getName());
    private Connection connection;
    private static DBService dbService;

    public static DBService instance() throws DBException{
        if (dbService == null){
            dbService = new DBServiceImpl();
        }
        return dbService;
    }
    private DBServiceImpl() throws DBException{
        this.connection = this.getMysqlConnection();
    }

    @Override
    public List<QuestionsDataSet> getQuestionsByThemeId(long themeId) throws DBException {
        try{
            return new QuestionsDAO(connection).getQuestionByThemeId(themeId);
        }catch (SQLException e){
            logger.error("Filed with themeId = "+themeId,e);
            throw new DBException(e);
        }
    }

    @Override
    public Long updateAnswer(long userId, long questionId, String text) throws DBException {
        try {
            connection.setAutoCommit(false);
            AnswersDAO dao = new AnswersDAO(connection);
            dao.updateAnswer(userId,questionId,text);
            connection.commit();
            return dao.getUserAnswerByQuestionId(userId,questionId).getId();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ignore) {
            }
            logger.error("Filed with userId = "+userId+" questionId = "+questionId,e);
            throw new DBException(e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ignore) {
            }
        }
    }

    @Override
    public String getClasName(long id) throws DBException {
        try{
            return new ClasesDAO(connection).get(id).getName();
        }catch (SQLException e){
            logger.error("Filed with id = "+id,e);
            throw new DBException(e);
        }
    }

    @Override
    public Long addTheme(String name,String clas) throws DBException {
        try {
            connection.setAutoCommit(false);
            ThemesDAO dao = new ThemesDAO(connection);
            dao.insertTheme(name,clas);
            connection.commit();
            return dao.getThemeId(name);
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ignore) {
            }
            logger.error("Filed with name = "+name+" clas = "+clas,e);
            throw new DBException(e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ignore) {
            }
        }
    }

    @Override
    public UsersDataSet getUserByEmail(String email) throws DBException {
        try{
            Long userId = new UsersDAO(connection).getUserId(email);
            if (userId==null){
                return null;
            }
            return getUser(userId);
        }catch (SQLException e){
            logger.error("Filed with email = "+email,e);
            throw new DBException(e);
        }
    }

    @Override
    public ThemesDataSet getTheme(long id) throws DBException {
        try{
            return new ThemesDAO(connection).get(id);
        }catch (SQLException e){
            logger.error("Filed with id = "+id,e);
            throw new DBException(e);
        }
    }

    @Override
    public void deleteUser(long id) throws DBException {
        try{
            new UsersDAO(connection).deleteUser(id);
        }catch (SQLException e){
            logger.error("Filed with id = "+id,e);
            throw new DBException(e);
        }
    }

    @Override
    public List<UsersDataSet> getAllUsers() throws DBException {
        try{
           return new UsersDAO(connection).getAllUsers();
        }catch(SQLException e){
            logger.error(e);
            throw new DBException(e);
        }
    }

    @Override
    public List<ThemesDataSet> getThemesByClasId(long clasId) throws DBException {
       try{
            return new ThemesDAO(connection).getThemesByClasId(clasId);
       }catch (SQLException e){
           logger.error("Filed with clasId = "+clasId,e);
           throw new DBException(e);
       }
    }

    @Override
    public UsersDataSet getUser(long id) throws DBException {
        try{
            return (new UsersDAO(connection).get(id));
        }catch (SQLException e){
            logger.error("Filed with id = "+id,e);
            throw new DBException(e);
        }
    }

    @Override
    public Long addAnswer(long userId, long questionId, String text) throws DBException {
        try {
            connection.setAutoCommit(false);
            AnswersDAO dao = new AnswersDAO(connection);
            dao.insertAnswer(userId,questionId,text);
            connection.commit();
            return dao.getUserAnswerByQuestionId(userId,questionId).getId();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ignore) {
            }
            logger.error("Filed with usedId = "+userId+" questionId = "+questionId,e);
            throw new DBException(e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ignore) {
            }
        }
    }

    @Override
    public AnswersDataSet getAnswer(long id) throws DBException {
        try{
            return new AnswersDAO(connection).get(id);
        }catch(SQLException e){
            logger.error("Filed with id = "+id,e);
            throw new DBException(e);
        }
    }

    @Override
    public List<AnswersDataSet> getUserAnswersByThemeId(long userId,long themeId) throws DBException {
        try{
            return new AnswersDAO(connection).getUserAnswersByThemeId(userId,themeId);
        }catch(SQLException e){
            logger.error("Filed with userId = "+userId+" themeId = "+themeId,e);
            throw new DBException(e);
        }
    }

    @Override
    public QuestionsDataSet getQuestion(long id) throws DBException {
        try{
            return new QuestionsDAO(connection).get(id);
        }catch (SQLException e){
            logger.error("Filed with id = "+id,e);
            throw new DBException(e);
        }
    }

    @Override
    public Long addRightAnswer(long questionId, String text) throws DBException {
        try {
            connection.setAutoCommit(false);
            RightAnswersDAO dao = new RightAnswersDAO(connection);
            dao.insertRightAnswer(questionId,text);
            connection.commit();
            return dao.getRightAnswerByQuestionId(questionId).getId();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ignore) {
            }
            logger.error("Filed with questionId = "+questionId,e);
            throw new DBException(e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ignore) {
            }
        }

    }

    @Override
    public RightAnswersDataSet getRightAnswer(long id) throws DBException {
        try{
            return new RightAnswersDAO(connection).get(id);
        }catch(SQLException e){
            logger.error("Filed with id = "+id,e);
            throw new DBException(e);
        }
    }

    @Override
    public RightAnswersDataSet getRightAnswerByQuestionId(long questionId) throws DBException {
        try{
            return new RightAnswersDAO(connection).getRightAnswerByQuestionId(questionId);
        }catch(SQLException e){
            logger.error("Filed with questionId = "+questionId,e);
            throw new DBException(e);
        }
    }

    @Override
    public void deleteRecordOfTest(long themeId) throws DBException {
        try{
            new UsersHasThemesDAO(connection).deleteRecordOfTest(themeId);
        }catch(SQLException e){
            logger.error("Filed with themeId = "+themeId,e);
            throw new DBException(e);
        }
    }

    @Override
    public Long addQuestion(String text, String theme) throws DBException {
        try {
            connection.setAutoCommit(false);
            QuestionsDAO dao = new QuestionsDAO(connection);
            dao.insertQuestion(text,theme);
            connection.commit();
            return dao.getQuestionId(text);
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ignore) {
            }
            logger.error("Filed with text = "+text+" theme = "+theme,e);
            throw new DBException(e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ignore) {
            }
        }
    }

    @Override
    public Long getClasId(String name) throws DBException {
        try{
            return (new ClasesDAO(connection).getClasId(name));
        }catch (SQLException e){
            logger.error("Filed with name = "+name,e);
            throw new DBException(e);
        }
    }

    @Override
    public void updateTheme(long id, String theme,String clas) throws DBException {
        try {
            long clasId = this.getClasId(clas);
            new ThemesDAO(connection).updateTheme(id,theme,clasId);
        }catch (SQLException e){
            logger.error("Filed with id = "+id,e);
            throw new DBException(e);
        }
    }

    @Override
    public void addPassedTest(long userId, long themeId) throws DBException {
        try {
            connection.setAutoCommit(false);
            UsersHasThemesDAO dao = new UsersHasThemesDAO(connection);
            dao.insertRecord(userId, themeId);
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ignore) {
            }
            logger.error("Filed with userId = "+userId+" themeId = "+themeId,e);
            throw new DBException(e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ignore) {
            }
        }
    }

    @Override
    public Long updateQuestion(long themeId, String oldText, String newText) throws DBException {
        try{
            return new QuestionsDAO(connection).updateQuestion(themeId,oldText,newText);
        }catch (SQLException e){
            logger.error("Filed with themeId = "+themeId+" oldText = "+oldText,e);
            throw new DBException(e);
        }
    }

    @Override
    public boolean isPassedTest(long userId, long themeId) throws DBException {
        try {
            return new UsersHasThemesDAO(connection).isPassedTest(userId,themeId);
        }catch (SQLException e){
            logger.error("Filed with userId = "+userId+" themeId = "+themeId,e);
            throw new DBException(e);
        }
    }

    @Override
    public void deleteTheme(long themeId) throws DBException {
        try {
            new ThemesDAO(connection).deleteTheme(themeId);
        }catch (SQLException e){
            logger.error("Filed with themId = "+themeId,e);
            throw new DBException(e);
        }
    }

    @Override
    public void deleteQuestion(String text, long themeId) throws DBException {
        try {
            new QuestionsDAO(connection).deleteQuestion(text,themeId);
        }catch (SQLException e){
            logger.error("Filed with themeId = "+themeId,e);
            throw new DBException(e);
        }
    }

    @Override
    public void deleteRightAnswer(long questionId) throws DBException {
        try {
            new RightAnswersDAO(connection).deleteRightAnswer(questionId);
        }catch (SQLException e){
            logger.error("Filed with questionId = "+questionId,e);
            throw new DBException(e);
        }
    }

    @Override
    public void deleteAnswersByQuestionId(long questionId) throws DBException {
        try {
            new AnswersDAO(connection).deleteAnswersByQuestionId(questionId);
        }catch (SQLException e){
            logger.error("Filed with questionId = "+questionId,e);
            throw new DBException(e);
        }
    }

    @Override
    public Long getThemeId(String name) throws DBException {
        try {
            return new ThemesDAO(connection).getThemeId(name);
        }catch (SQLException e){
            logger.error("Filed with name = "+name,e);
            throw new DBException(e);
        }
    }

    @Override
    public List<Long> getAllUserWherePassedTest(long themeId) throws DBException {
        try {
            return new UsersHasThemesDAO(connection).getAllUserWherePassedTest(themeId);
        }catch (SQLException e){
            logger.error("Filed with themeId = "+themeId,e);
            throw new DBException(e);
        }
    }

    @Override
    public void updateUser(long userId, String name, String email, long clasId) throws DBException {
        try{
            new UsersDAO(connection).updateUser(userId,name,email,clasId);
        }catch (SQLException e){
            logger.error("Filed with userId = "+userId+" name = "+name+" email = "+email+" clasId = "+clasId,e);
            throw new DBException(e);
        }
    }

    @Override
    public void deleteAnswer(long userId, long questionId, String text) throws DBException {
        try {
            new AnswersDAO(connection).deleteAnswer(userId, questionId, text);
        }catch (SQLException e){
            logger.error("Filed with questionId = "+questionId+" userId = "+userId,e);
            throw new DBException(e);
        }
    }

    public Long addUser(String name, String email, String password, long clasId) throws DBException {
        try {
            connection.setAutoCommit(false);
            UsersDAO dao = new UsersDAO(connection);
            dao.insertUser(name,email,password,clasId);
            connection.commit();
            return dao.getUserId(email);
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ignore) {
            }
            logger.error("Filed with name = "+name+" email = "+email+" clasId = "+clasId,e);
            throw new DBException(e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ignore) {
            }
        }
    }

    public void cleanUp() throws DBException {
        try {
            new AnswersDAO(connection).dropTable();
            new ClasesDAO(connection).dropTable();
            new QuestionsDAO(connection).dropTable();
            new RightAnswersDAO(connection).dropTable();
            new ThemesDAO(connection).dropTable();
            new UsersDAO(connection).dropTable();
            new UsersHasThemesDAO(connection).dropTable();
        }catch (SQLException e){
            throw new DBException(e);
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    public Connection getMysqlConnection(){
        Map<String,String> dbProp = AppProperties.instance().getDbProp();
        try {
            DriverManager.registerDriver((Driver) Class.forName("com.mysql.jdbc.Driver").newInstance());

            StringBuilder url = new StringBuilder();

            url.
                    append("jdbc:mysql://").                   //db type
                    append(dbProp.get("host")+":").            //host name
                    append(dbProp.get("port")+"/").            //port
                    append(dbProp.get("database")+"?").        //db name
                    append("user="+dbProp.get("user")+"&").    //login
                    append("password="+dbProp.get("password"));//password

            logger.info("URL {}",url);
            Connection connection = DriverManager.getConnection(url.toString());
            return connection;
        } catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}

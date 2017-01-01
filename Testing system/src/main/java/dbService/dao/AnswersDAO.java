package dbService.dao;

import dbService.dataSets.AnswersDataSet;
import dbService.executor.Executor;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by disqustingman on 08.06.16.
 */
public class AnswersDAO {
    private Connection connection;
    private Executor executor;

    public AnswersDAO(Connection connection) throws SQLException{
        this.connection = connection;
        this.executor = new Executor(connection);
        this.createTable();
    }
    public void createTable() throws SQLException{
        executor.execUpdate("create table if not exists answers(id int(10) auto_increment,user_id int(10),question_id int(10),text varchar(255),primary key(id));");
    }
    public void dropTable() throws SQLException{
        executor.execUpdate("drop table answers");
    }
    public void insertAnswer(long userId,long questionId,String text) throws SQLException{
        executor.execUpdate("insert into answers(user_id,question_id,text) values("+Long.toString(userId)+","+Long.toString(questionId)+",'"+text+"');");
    }
    public void updateAnswer(long userId,long questionId,String text) throws SQLException{
        executor.execUpdate("update answers set text ='"+text+"' where answers.user_id="+userId+" and answers.question_id="+questionId);
    }
    public void deleteAnswer(long userId,long questionId,String text) throws SQLException{
        executor.execUpdate("delete from answers where answers.user_id="+Long.toString(userId)+" and answers.question_id="+Long.toString(questionId)+" and text = '"+text+"'");
    }
    public void deleteAnswersByQuestionId(long questionId) throws SQLException{
        executor.execUpdate("delete from answers where answers.question_id="+Long.toString(questionId));
    }
    public AnswersDataSet get(long id) throws SQLException{
        return executor.execQuery("select * from answers where answers.id="+Long.toString(id),result->{
            if (!result.next()){
                return null;
            }
            return new AnswersDataSet(result.getLong(1),result.getLong(2),result.getLong(3),result.getString(3));
        });
    }
    public AnswersDataSet getUserAnswerByQuestionId(long userId,long questionId) throws SQLException{
        return executor.execQuery("select * from answers where answers.user_id="+Long.toString(userId)+" and answers.question_id="+Long.toString(questionId),result->{
            if (!result.next()){
                return null;
            }
            return new AnswersDataSet(result.getLong(1),result.getLong(2),result.getLong(3),result.getString(3));
        });
    }
    public List<AnswersDataSet> getUserAnswersByThemeId(long userId,long themeId) throws SQLException{
        Statement stmt = connection.createStatement();
        stmt.execute("select answers.id,answers.user_id,answers.question_id,answers.text from answers inner join questions on answers.question_id=questions.id where questions.theme_id="+Long.toString(themeId)+" and answers.user_id="+Long.toString(userId));
        ResultSet result = stmt.getResultSet();
        List<AnswersDataSet> res = new ArrayList<>();
        if (!result.next()){
            return res;
        }
        while (!result.isLast()){
            res.add(new AnswersDataSet(result.getLong(1),result.getLong(2),result.getLong(3),result.getString(4)));
            result.next();
        }
        res.add(new AnswersDataSet(result.getLong(1),result.getLong(2),result.getLong(3),result.getString(4)));
        stmt.close();
        result.close();
        return res;
    }
}

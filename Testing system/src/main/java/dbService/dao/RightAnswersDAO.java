package dbService.dao;

import dbService.dataSets.RightAnswersDataSet;
import dbService.executor.Executor;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by disqustingman on 07.06.16.
 */
public class RightAnswersDAO {
    private Connection connection;
    private Executor executor;

    public RightAnswersDAO(Connection connection) throws SQLException{
        this.executor = new Executor(connection);
        this.connection = connection;
        this.createTable();
    }
    public void createTable() throws SQLException{
        executor.execUpdate("create table if not exists right_answers(id int(10) auto_increment,question_id int(10),text varchar(255),primary key(id));");
    }
    public void dropTable() throws SQLException{
        executor.execUpdate("drop table right_answers");
    }
    public RightAnswersDataSet get(long id) throws SQLException{
        return executor.execQuery("select * from right_answers where right_answers.id="+Long.toString(id),result->{
            if (!result.next()){
                return null;
            }
            return new RightAnswersDataSet(result.getLong(1),result.getLong(2),result.getString(3));
        });
    }
    public RightAnswersDataSet getRightAnswerByQuestionId(long questionId) throws SQLException{
        return executor.execQuery("select * from right_answers where right_answers.question_id="+Long.toString(questionId),result->{
            if (!result.next()){
                return null;
            }
            return new RightAnswersDataSet(result.getLong(1),result.getLong(2),result.getString(3));
        });
    }
    public void insertRightAnswer(long questionId,String text) throws SQLException{
        executor.execUpdate("insert into right_answers(question_id,text) values ("+Long.toString(questionId)+",'"+text+"');");
    }
    public void deleteRightAnswer(long questionId) throws SQLException{
        executor.execUpdate("delete from right_answers where right_answers.question_id="+Long.toString(questionId));
    }
}

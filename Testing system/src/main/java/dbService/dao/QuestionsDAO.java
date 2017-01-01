package dbService.dao;

import dbService.dataSets.QuestionsDataSet;
import dbService.executor.Executor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by disqustingman on 06.06.16.
 */
public class QuestionsDAO {
    private Connection connection;
    private Executor executor;

    public QuestionsDAO(Connection connection) throws SQLException {
        this.connection = connection;
        this.executor = new Executor(connection);
        this.createTable();
    }
    public void createTable() throws SQLException{
        executor.execUpdate("create table if not exists questions(id int(10) auto_increment,name varchar(500),theme_id int(10),primary key(id));");
    }
    public void dropTable() throws SQLException{
        executor.execUpdate("drop table questions");
    }
    public void insertQuestion(String text,String theme) throws SQLException{
        long themeId = executor.execQuery("select * from themes where themes.name='"+theme+"'",result->{
            result.next();
            return result.getLong(1);
        });
        executor.execUpdate("insert into questions(name,theme_id) values ('"+text+"',"+themeId+")");
    }
    public void deleteQuestion(String text,long themeId) throws SQLException{
        executor.execUpdate("delete from questions where questions.name = '"+text+"' and questions.theme_id="+themeId);
    }
    public List<QuestionsDataSet> getQuestionByThemeId(long themeId) throws SQLException{
        Statement stmt = connection.createStatement();
        stmt.execute("select * from questions where questions.theme_id="+Long.toString(themeId));
        ResultSet result = stmt.getResultSet();
        List<QuestionsDataSet> res = new ArrayList<>();
        if (!result.next()){
            return null;
        }
        while (!result.isLast()){
            res.add(new QuestionsDataSet(result.getLong(1),result.getString(2),result.getLong(3)));
            result.next();
        }
        res.add(new QuestionsDataSet(result.getLong(1),result.getString(2),result.getLong(3)));
        stmt.close();
        result.close();
        return res;
    }
    public Long getQuestionId(String name) throws SQLException{
        return executor.execQuery("select * from questions where questions.name='"+name+"'",result->{
            if (!result.next()){
                return null;
            }
            return result.getLong(1);
        });
    }
    public QuestionsDataSet get(long id) throws SQLException{
        return executor.execQuery("select * from questions where questions.id='"+Long.toString(id)+"'",result->{
            if (!result.next()){
                return null;
            }
            return new QuestionsDataSet(result.getLong(1),result.getString(2),result.getLong(3));
        });
    }
    public Long updateQuestion(long themeId,String oldText,String newText) throws SQLException{
        executor.execUpdate("update questions set questions.name='"+newText+"' where questions.theme_id="+themeId+" and questions.name = '"+oldText+"'");
        return this.getQuestionId(newText);
    }
}

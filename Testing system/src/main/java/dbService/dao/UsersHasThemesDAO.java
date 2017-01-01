package dbService.dao;

import dbService.executor.Executor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by disqustingman on 09.06.16.
 */
public class UsersHasThemesDAO {
    private Connection connection;
    private Executor executor;

    public UsersHasThemesDAO(Connection connection) throws SQLException{
        this.connection = connection;
        this.executor = new Executor(connection);
        this.createTable();
    }
    public void createTable() throws SQLException{
        executor.execUpdate("create table if not exists users_has_themes(user_id int(10) not null,theme_id int(10) not null,primary key(user_id,theme_id));");
    }
    public void dropTable() throws SQLException{
        executor.execUpdate("drop table users_has_themes;");
    }
    public void insertRecord(long userId,long themeId) throws SQLException{
        executor.execUpdate("insert into users_has_themes(user_id,theme_id) values("+userId+","+themeId+");");
    }
    public void deleteRecordOfTest(long themeId) throws SQLException{
        executor.execUpdate("delete from users_has_themes where theme_id="+themeId);
    }
    public boolean isPassedTest(long userId,long themeId) throws SQLException{
        return executor.execQuery("select * from users_has_themes where user_id="+userId+" and theme_id="+themeId,result->{
            if (!result.next()){
                return false;
            }else{
                return true;
            }
        });
    }
    public List<Long> getAllUserWherePassedTest(long themeId) throws SQLException{
        Statement stmt = connection.createStatement();
        stmt.execute("select * from users_has_themes where theme_id="+themeId);
        ResultSet result = stmt.getResultSet();
        List<Long> res = new ArrayList<>();
        if (!result.next()){
            return null;
        }
        while (!result.isLast()){
            res.add(result.getLong(1));
            result.next();
        }
        res.add(result.getLong(1));
        stmt.close();
        result.close();
        return res;
    }
}

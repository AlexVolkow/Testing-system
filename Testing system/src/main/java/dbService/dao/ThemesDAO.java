package dbService.dao;

import dbService.DBException;
import dbService.DBServiceImpl;
import dbService.dataSets.ClasesDataSet;
import dbService.dataSets.ThemesDataSet;
import dbService.executor.Executor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by disqustingman on 27.05.16.
 */
public class ThemesDAO {
    private Executor executor;
    private Connection connection;

    public ThemesDAO(Connection connection) throws SQLException{
        this.executor = new Executor(connection);
        this.connection = connection;
        this.createTable();
    }

    public void createTable() throws SQLException {
        executor.execUpdate("create table if not exists themes(id int(10) auto_increment,name varchar(100),clas_id int(10),primary key(id));");
    }

    public void dropTable() throws SQLException{
        executor.execUpdate("drop table themes;");
    }

    public List<ThemesDataSet> getThemesByClasId(long clasId) throws SQLException{
        Statement stmt = connection.createStatement();
        stmt.execute("select * from themes where themes.clas_id="+Long.toString(clasId));
        ResultSet result = stmt.getResultSet();
        List<ThemesDataSet> res = new ArrayList<>();
        if (!result.next()){
            return null;
        }
        while (!result.isLast()){
            res.add(new ThemesDataSet(result.getLong(1),result.getString(2),result.getLong(3)));
            result.next();
        }
        res.add(new ThemesDataSet(result.getLong(1),result.getString(2),result.getLong(3)));
        stmt.close();
        result.close();
        return res;
    }

    public void insertTheme(String name,String clas) throws SQLException{
        long clasId = executor.execQuery("select * from clases where clases.name="+clas,result ->{
            result.next();
            return result.getLong(1);
        }) ;
        executor.execUpdate("insert into themes(name,clas_id) values('"+name+"',"+Long.toString(clasId)+")");
    }
    public void updateTheme(long id,String theme,long clasId) throws SQLException{
        executor.execUpdate("update themes set themes.name='"+theme+"' , themes.clas_id="+clasId+" where themes.id="+id);
    }
    public void deleteTheme(long themeId) throws SQLException{
        executor.execUpdate("delete from themes where themes.id="+themeId);
    }
    public Long getThemeId(String name) throws SQLException{
        return executor.execQuery("select * from themes where themes.name='"+name+"'",result ->{
            if (!result.next()){
                return null;
            }
            return result.getLong(1);
        });
    }
    public ThemesDataSet get(long id) throws SQLException{
        return executor.execQuery("select * from themes where themes.id='"+id+"'",result ->{
            result.next();
            return new ThemesDataSet(result.getLong(1),result.getString(2),result.getLong(3));
        });
    }
}

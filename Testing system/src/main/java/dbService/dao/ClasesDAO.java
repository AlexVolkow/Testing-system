package dbService.dao;

import dbService.dataSets.ClasesDataSet;
import dbService.executor.Executor;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by disqustingman on 27.05.16.
 */
public class ClasesDAO {
    private Executor executor;

    public ClasesDAO(Connection connection) throws SQLException{
        this.executor = new Executor(connection);
        this.createTable();
    }

    public void createTable() throws SQLException{
        executor.execUpdate("create table if not exists clases(id int(10) auto_increment,name varchar(3),primary key(id));");
    }

    public void dropTable() throws SQLException{
        executor.execUpdate("drop table clases");
    }

    public void insertClas(String name) throws SQLException{
        executor.execUpdate("insert into clases(name) values('"+name+"')");
    }
    public Long getClasId(String name) throws SQLException{
        return executor.execQuery("select * from clases where clases.name='"+name+"'",result ->{
            if (!result.next()){
                return null;
            }
            return result.getLong(1);
        });
    }
    public ClasesDataSet get(long id) throws SQLException{
        return executor.execQuery("select * from clases where clases.id='"+id+"'",result ->{
            if (!result.next()){
                return null;
            }
            return new ClasesDataSet(result.getLong(1),result.getString(2));
        });
    }
}

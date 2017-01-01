package dbService.dao;

import dbService.dataSets.UsersDataSet;
import dbService.executor.Executor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by disqustingman on 27.05.16.
 */
public class UsersDAO {
    private Executor executor;
    private Connection connection;

    public UsersDAO(Connection connection) throws SQLException{
        this.executor = new Executor(connection);
        this.connection = connection;
        this.createTable();
    }

    public void createTable() throws SQLException{
        executor.execUpdate("create table if not exists users(id int(10) auto_increment,name varchar(100) not null,email varchar(100) not null,password varchar(100) not null,clas_id int,primary key(id));");
    }

    public void dropTable() throws SQLException{
        executor.execUpdate("drop table users");
    }
    public void insertUser(String name,String email,String password,long clasId) throws SQLException{
        executor.execUpdate("insert into users(name,email,password,clas_id) values ('"+name+"','"+email+"','"+password+"','"+clasId+"')");
    }
    public Long getUserId(String email) throws SQLException{
        Statement stmt = connection.createStatement();
        stmt.execute("select * from users where users.email='" + email + "'");
        ResultSet result = stmt.getResultSet();
        if (!result.next()){
            return null;
        }
        long res = result.getLong(1);
        stmt.close();
        result.close();
        return res;
    }
    public UsersDataSet get(long id) throws SQLException{
        Statement stmt = connection.createStatement();
        stmt.execute("select * from users where users.id = "+id);
        ResultSet result = stmt.getResultSet();
        if (!result.next()){
            return null;
        }
        UsersDataSet res = new UsersDataSet(result.getLong(1),result.getString(2),result.getString(3),result.getString(4),result.getLong(5));
        stmt.close();
        result.close();
        return res;
    }
    public void deleteUser(long id) throws SQLException{
        executor.execUpdate("delete from users where users.id = "+id);
    }
    public List<UsersDataSet> getAllUsers() throws SQLException{
        Statement stmt = connection.createStatement();
        stmt.execute("select * from users;");
        ResultSet result = stmt.getResultSet();
        List<UsersDataSet> res = new ArrayList<>();
        if (!result.next()){
            return null;
        }
        while (!result.isLast()){
            res.add(new UsersDataSet(result.getLong(1),result.getString(2),result.getString(3),result.getString(4),result.getLong(5)));
            result.next();
        }
        res.add(new UsersDataSet(result.getLong(1),result.getString(2),result.getString(3),result.getString(4),result.getLong(5)));
        stmt.close();
        result.close();
        return res;
    }
    public void updateUser(long userId,String name,String email,long clasId) throws SQLException{
        executor.execUpdate("update users set users.name='"+name+"' , users.email = '"+email+"' , users.clas_id="+clasId+"  where users.id="+userId);
    }
}

package dbService.executor;

/**
 * Created by disqustingman on 24.05.16.**/
 import java.sql.ResultSet;
 import java.sql.SQLException;

public interface ResultHandler<T> {
    T handle(ResultSet resultSet) throws SQLException;
}
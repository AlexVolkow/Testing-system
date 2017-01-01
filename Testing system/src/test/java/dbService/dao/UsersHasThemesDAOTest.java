package dbService.dao;

import dbService.DBServiceImpl;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by disqustingman on 10.06.16.
 */
public class UsersHasThemesDAOTest {

    @Test
    public void testDropTable() throws Exception {
        new UsersHasThemesDAO(DBServiceImpl.instance().getMysqlConnection()).dropTable();
    }
}
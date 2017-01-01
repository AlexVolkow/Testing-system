package dbService.dao;

import dbService.DBService;
import dbService.DBServiceImpl;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by disqustingman on 10.06.16.
 */
public class ThemesDAOTest {

    @Test
    public void testDropTable() throws Exception {
        new ThemesDAO(DBServiceImpl.instance().getMysqlConnection()).dropTable();
    }
}
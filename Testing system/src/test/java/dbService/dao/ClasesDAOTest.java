package dbService.dao;

import dbService.DBService;
import dbService.DBServiceImpl;
import dbService.dataSets.ClasesDataSet;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

import static org.junit.Assert.*;

/**
 * Created by disqustingman on 27.05.16.
 */
public class ClasesDAOTest {
    private DBService dbService;
    private ClasesDAO clasesDAO;

    @Before
    public void setUp() throws Exception {
        dbService = DBServiceImpl.instance();
        clasesDAO = new ClasesDAO(dbService.getMysqlConnection());
        clasesDAO.createTable();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testInsertClas() throws Exception {
        for (int i = 1; i<=11;i++){
            clasesDAO.insertClas(Integer.toString(i));
        }
    }
}
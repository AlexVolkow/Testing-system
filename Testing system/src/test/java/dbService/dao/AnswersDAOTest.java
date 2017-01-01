package dbService.dao;

import dbService.DBServiceImpl;
import dbService.dataSets.AnswersDataSet;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by disqustingman on 08.06.16.
 */
public class AnswersDAOTest {
    private AnswersDAO answersDAO;

    @Before
    public void setUp() throws Exception {
        answersDAO = new AnswersDAO(DBServiceImpl.instance().getMysqlConnection());
        answersDAO.dropTable();
        answersDAO.createTable();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testGetUserAnswersByThemeId() throws Exception {
       // List<AnswersDataSet> temp = answersDAO.getUserAnswersByThemeId(8,1);
    }
}
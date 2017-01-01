package dbService.dao;

import dbService.DBService;
import dbService.DBServiceImpl;
import dbService.dataSets.UsersDataSet;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by disqustingman on 27.05.16.
 */
public class UsersDAOTest {
    private DBService dbService;
    private UsersDAO usersDAO;

    @Before
    public void setUp() throws Exception {
        dbService = DBServiceImpl.instance();
        usersDAO = new UsersDAO(dbService.getMysqlConnection());
        usersDAO.createTable();
    }

    @After
    public void tearDown() throws Exception {
        long testId = usersDAO.getUserId("test@mail.ru");
        if (testId>0){
            usersDAO.deleteUser(testId);
        }
    }

    @Test
    public void testGetUserId() throws Exception {
        usersDAO.insertUser("test","test@mail.ru","1234",11);
        UsersDataSet usersDataSet = usersDAO.get(usersDAO.getUserId("test@mail.ru"));
        Assert.assertEquals(usersDataSet.getName(),"test");
    }
}
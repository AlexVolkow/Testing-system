package dbService;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;

import static org.junit.Assert.*;

/**
 * Created by disqustingman on 27.05.16.
 */
public class DBServiceTest {

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetMysqlConnection() throws Exception {
        DBService dbService = DBServiceImpl.instance();
    }
}
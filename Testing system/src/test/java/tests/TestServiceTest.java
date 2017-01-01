package tests;

import dbService.DBService;
import dbService.DBServiceImpl;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by disqustingman on 10.06.16.
 */
public class TestServiceTest {

    @Test
    public void testAddTest() throws Exception {
        TestService testService = new TestServiceImpl();
        testService.deleteTest(1);
    }
}
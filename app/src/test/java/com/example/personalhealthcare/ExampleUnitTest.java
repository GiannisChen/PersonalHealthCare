package com.example.personalhealthcare;

import com.example.personalhealthcare.Dao.UserDao;
import com.example.personalhealthcare.DaoImpl.UserDaoImpl;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        UserDao userDao = new UserDaoImpl();
        System.out.println(userDao.findAdminByID(2));
    }
}
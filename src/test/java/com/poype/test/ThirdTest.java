package com.poype.test;

import com.poype.second.model.Address;
import com.poype.second.model.Customer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

public class ThirdTest {

    EntityManagerFactory entityManagerFactory;

    EntityManager entityManager;

    @Before
    public void init() {
        entityManagerFactory = Persistence.createEntityManagerFactory( "studyJpa" );
        entityManager = entityManagerFactory.createEntityManager();
    }

    @After
    public void destory() {
        entityManager.close();
        entityManagerFactory.close();
    }

    @Test
    public void testSaveCustomer() {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin(); // 要明确调用begin方法才能开启事务

        Customer customer = new Customer();
        customer.setName("Eric");
        customer.setEmail("LiLei@qq.com");
        customer.setPassword("qqqq123");
        customer.setPhone(4246123);
        customer.setSex('F');
        customer.setMarried(false);
        customer.setDescription("Beautiful");
        customer.setBirthday(new Date());

        customer.setAddress(new Address("Shenzen", "Shanghai", "100100"));

        entityManager.persist(customer);

        transaction.commit();
    }

    @Test
    public void testQueryAllCustomer() {
        Query query = entityManager.createQuery("from Customer");
        List<Customer> customers = query.getResultList();

        for (Customer customer : customers) {
            System.out.println(customer);
            System.out.println("-----------------------------");
        }
    }

}


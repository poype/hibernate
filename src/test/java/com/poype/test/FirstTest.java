package com.poype.test;

import com.poype.first.model.Customer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.List;

public class FirstTest {

    SessionFactory sessionFactory;

    Session session;

    Transaction transaction;

    @Before
    public void init() {
        // 从hibernate.cfg.xml文件中加载配置
        StandardServiceRegistry serviceRegistry =
                new StandardServiceRegistryBuilder().configure().build();

        Metadata metadata = new MetadataSources(serviceRegistry).buildMetadata();

        sessionFactory = metadata.getSessionFactoryBuilder().build();
        session = sessionFactory.openSession();
    }

    @After
    public void destory() {
        session.close();
        sessionFactory.close();
    }

    @Test
    public void testSaveCustomer() {
        // 开启事务， 必须开启事务，否则不会执行insert语句
        transaction = session.beginTransaction();

        Customer customer = new Customer();
        customer.setName("Jack");
        customer.setEmail("test@qq.com");
        customer.setPassword("qwe123");
        customer.setPhone(4246771);
        customer.setAddress("Beijing");
        customer.setSex('M');
        customer.setMarried(true);
        customer.setDescription("Handsome");
        customer.setBirthday(new Date());

        /*
         * session.save方法保存一个对象时会分别执行两个SQL语句
         * 1. select max(ID) from CUSTOMERS
         * 2. insert into CUSTOMERS (NAME, EMAIL, PASSWORD, PHONE, ADDRESS, SEX, IS_MARRIED, DESCRIPTION, BIRTHDAY, REGISTERED_TIME, ID) values ...
         * 即先从DB中query出下一个ID value，将ID value赋值给model对象后，再将所有field的值insert到DB中
         * 其中第1条SQL会在session.save()方法调用时马上执行，但第2条SQL会一致等到transaction.commit()执行时才会执行，
         * 如果不开启transaction的话只会执行第1条SQL，不会执行第2条SQL，导致customer不会被正常保存的DB中。
         * session.save方法的返回值是下一个记录ID
         */
        Long nextId = (Long)session.save(customer);
        System.out.println("nextId: " + nextId);

        // 真正的insert SQL在事务commit时才会执行
        transaction.commit();
    }

    @Test
    public void testQueryAllCustomer() {
        // 查询所有的记录，因为只是查询，所以不需要开启transaction
        Query<Customer> query = session.createQuery("from Customer", Customer.class);
        List<Customer> customers = query.list();

        for (Customer customer : customers) {
            System.out.println(customer);
            System.out.println("-----------------------------");
        }
    }
}

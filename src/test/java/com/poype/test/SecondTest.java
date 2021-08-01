package com.poype.test;

import com.poype.first.model.Customer;
import com.poype.first.model.Order;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * 使用JPA API进行操作，JPA API与Hibernate API的对应关系如下
 * SessionFactory  <-->    EntityManagerFactory
 * Session         <-->    EntityManager
 * Transaction     <-->    EntityTransaction
 * Query           <-->    Query
 *
 * 使用JPA API与使用Hibernate API在流程上是一样的，结果也一样。只是API换个名字
 */
public class SecondTest {

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
        customer.setName("Lucy");
        customer.setEmail("test@qq.com");
        customer.setPassword("qwe123");
        customer.setPhone(4246771);
        customer.setAddress("Beijing");
        customer.setSex('M');
        customer.setMarried(true);
        customer.setDescription("Handsome");
        customer.setBirthday(new Date());

        /*
         * 执行的SQL与直接使用hibernate一样，也是先后执行如下两个SQL
         * select max(ID) from CUSTOMERS
         * insert into CUSTOMERS (NAME, EMAIL, PASSWORD, PHONE, ADDRESS, SEX, ...) values (?, ?, ?, ?, ?, ...)
         */
        entityManager.persist(customer);

        transaction.commit();
    }

    @Test
    public void testQueryAll() {
        Query query = entityManager.createQuery("from Customer");
        List<Customer> customers = query.getResultList();

        for (Customer customer : customers) {
            System.out.println(customer);
            System.out.println("-----------------------------");
        }
    }

    @Test
    public void testQueryAllWithTransaction() {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        Query query = entityManager.createQuery("from Customer");
        List<Customer> customers = query.getResultList();

        for (Customer customer : customers) {
            System.out.println(customer);
            System.out.println("-----------------------------");

            customer.setEmail("newEmail@163.com");
        }

        // 与直接使用hibernate API一样，在事务中更新了domain model的value，事务提交时会自动同步回DB(执行update SQL)
        transaction.commit();
    }

    @Test
    public void testQueryOne() {
        // 根据primary key查找单个对象
        Customer customer1 = entityManager.find(Customer.class, 14L);
        Customer customer2 = entityManager.find(Customer.class, 14L); // 命中hibernate缓存，并没有Select SQL执行
        Customer customer3 = entityManager.find(Customer.class, 13L);

        System.out.println(customer1 == customer2); // true customer1与customer2指向同一个对象
        System.out.println(customer1 == customer3); // false
    }

    @Test
    public void testDelete() {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        // JPA的delete逻辑只能是先query，在remove，不能直接塞一个ID删除record
        // 否则会报错：Removing a detached instance
        Customer customer = entityManager.find(Customer.class, 10L);
        entityManager.remove(customer);

        transaction.commit();
    }

    @Test
    public void testSaveOrder() {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin(); // 要明确调用begin方法才能开启事务

        Order order = new Order();
        order.setOrderNumber("123-456");
        order.setPrice(33.44);

        entityManager.persist(order);

        transaction.commit();
    }

    @Test
    public void testQueryAllOrder() {
        Query query = entityManager.createQuery("from Order");
        List<Order> orders = query.getResultList();

        for (Order order : orders) {
            System.out.println(order);
            System.out.println("-----------------------------");
        }
    }
}

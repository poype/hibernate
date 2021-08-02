package com.poype.test;

import com.poype.second.model.Address;
import com.poype.second.model.Customer;
import com.poype.second.model.Gender;
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
        customer.setSex(Gender.FEMALE);
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

    @Test
    public void testCache() {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin(); // 要明确调用begin方法才能开启事务

        // 1. 第一次读取model对象，缓存中没有，会向DB发送SQL
        Customer customer = entityManager.find(Customer.class, 14L);
        System.out.println(customer);

        // 2. 第二次从缓存中直接读取对象，不会向DB发送SQL
        Customer customer1 = entityManager.find(Customer.class, 14L);
        System.out.println("-----------------------");

        // 4. 由于对象没有任何变化，所以flush操作不会向DB发送任何SQL。且flush对缓存没有任何影响，下面的find操作仍然没有向DB发送SQL
        entityManager.flush();
        Customer customer2 = entityManager.find(Customer.class, 14L);
        System.out.println("-----------------------");

        // 5. 提交事务，对缓存也没有任何影响，缓存中的数据还在，所以下面find操作仍然不会向DB发送SQL
        transaction.commit();
        Customer customer3 = entityManager.find(Customer.class, 14L);
        System.out.println("end ---------------------");

        // 6. refresh操作会强制从DB重新读取一次最新的数据
        entityManager.refresh(customer);

        // 7. entityManager.close() 执行这个操作后缓存会被真正清空
    }
}

/*
 * transient(短暂的)：一个对象被new出来后还没有被持久化，就是个普通对象
 * persistent: 在Hibernate缓存和DB中都存在的对象处于persistent状态
 *             例如将一个处于transient状态的对象save到DB中后该对象就会转变到persistent状态。
 *             或者find等操作从DB中去取一个对象也是persistent状态。
 *             在清理Hibernate缓存时，会根据persistent对象的属性变化来同步更新DB。
 * removed: 不再处于Hibernate缓存，并且session已经计划将其从DB中删除。(调用remove操作后会直接清空缓存，但要等到事务commit时才会真正操作DB)
 * detached: 对象数据存在于DB中，但不存在于Hibernate缓存中。即调用了entityManager.close()方法，对象不再受entityManager控制
 */
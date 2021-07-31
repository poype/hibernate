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

    @Test
    public void testQueryAllCustomerWithTransaction() {
        // 开启事务
        transaction = session.beginTransaction();

        Query<Customer> query = session.createQuery("from Customer", Customer.class);
        List<Customer> customers = query.list();

        for (Customer customer : customers) {
            System.out.println(customer);
            System.out.println("-----------------------------");
            // 此处修改了对象的值，仅仅是在内存中修改，并没有显示的声明要把修改的值同步回DB
            customer.setName("Jack Ma");
        }

        /*
         * 查询操作一般是只读操作，通常没有必要开启transaction，但如果开启transaction会发生奇妙的事情
         * 在transaction中从DB查询domain model，之后在内存中修改domain model的值，那么在transaction commit时，
         * 被修改后的model的值会自动同步回DB，即会执行下面的update SQL，本例中有多少个domain model就会执行几次update SQL
         * update CUSTOMERS set NAME=?, EMAIL=?, PASSWORD=?, PHONE=?, EMAIL=?, PASSWORD=?, PHONE=?, ... where ID=?
         *
         * 注意update SQL要等到transaction commit时才会自动执行
         * 另外可以看到执行的SQL是把customer对象的所有field都update了一次，但本例中只是更新的name字段，这很浪费DB性能。
         * 应该寻找更好的方法更新DB
         */
        transaction.commit();
    }

    @Test
    public void testQueryOneCustomer() {
        // 如果仅仅是查询数据，不要开启事务
        transaction = session.beginTransaction();

        /** 根据primary ID查询单个对象，会执行下面的SQL
         *    select
         *         customer0_.ID as ID1_0_0_,
         *         customer0_.NAME as NAME2_0_0_,
         *         customer0_.EMAIL as EMAIL3_0_0_,
         *         customer0_.PASSWORD as PASSWORD4_0_0_,
         *         customer0_.PHONE as PHONE5_0_0_,
         *         customer0_.ADDRESS as ADDRESS6_0_0_,
         *         customer0_.SEX as SEX7_0_0_,
         *         customer0_.IS_MARRIED as IS_MARRI8_0_0_,
         *         customer0_.DESCRIPTION as DESCRIPT9_0_0_,
         *         customer0_.BIRTHDAY as BIRTHDA10_0_0_,
         *         customer0_.REGISTERED_TIME as REGISTE11_0_0_
         *     from
         *         CUSTOMERS customer0_
         *     where
         *         customer0_.ID=?
         */
        Customer customer = session.get(Customer.class, 1L);
        System.out.println(customer);

        customer.setName("Jacky Ma2");
        /** 同样的，只要在事务中修改了model对象的值，在transaction commit时都会同步回DB，及执行下面的SQL
         *     update
         *         CUSTOMERS
         *     set
         *         NAME=?,
         *         EMAIL=?,
         *         PASSWORD=?,
         *         PHONE=?,
         *         ADDRESS=?,
         *         SEX=?,
         *         IS_MARRIED=?,
         *         DESCRIPTION=?,
         *         BIRTHDAY=?,
         *         REGISTERED_TIME=?
         *     where
         *         ID=?
         * 把所有的field都update一次，效率太差！！！ 好傻逼！！！！
         */
        transaction.commit();
    }
}

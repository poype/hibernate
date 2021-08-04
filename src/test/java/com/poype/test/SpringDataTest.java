package com.poype.test;

import com.poype.springdata.model.Person;
import com.poype.springdata.repository.PersonRepository;
import com.poype.springdata.service.PersonService;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class SpringDataTest {

    private ApplicationContext ctx;

    private PersonRepository personRepository;

    private PersonService personService;

    {
        ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        personRepository = ctx.getBean(PersonRepository.class);
        personService = ctx.getBean(PersonService.class);
    }

    @Test
    public void testQuery() {
        Person person = personRepository.getByLastName("BB");
        System.out.println(person);
    }

    @Test
    public void testSave() {
        String[] names = {"AA", "BB", "CC", "DD", "EE", "FF", "GG", "HH", "II", "JJ", "KK", "LL", "MM", "NN"};
        for (String name : names) {
            Person person = new Person();
            person.setFirstName("F_" + name);
            person.setLastName(name);
            person.setBirth(new Date());
            person.setEmail(name + "@qq.com");
            personRepository.save(person);
        }
    }

    @Test
    public void test1() {
        List<Person> personList = personRepository.findByLastNameStartingWithAndIdLessThan("a", 10);
        System.out.println(personList);
    }

    @Test
    public void test2() {
        List<Person> personList = personRepository.readByLastNameEndingWithAndIdLessThan("F", 2);
        System.out.println(personList);
    }

    @Test
    public void test3() {
        List<Person> personList = personRepository.getByEmailInAndBirthLessThan(Arrays.asList("EE@qq.com", "GG@qq.com"), new Date());
        System.out.println(personList);
    }

    @Test
    public void test4() {
        Person person = personRepository.getMaxIdPerson();
        System.out.println(person);
    }

    @Test
    public void test5() {
        long count = personRepository.getTotalCount();
        System.out.println(count);
    }

    @Test
    public void test6() {
        personService.updatePersonEmail("test@qqqqq.com", 3);
    }

    /**
     * 分别执行了如下两条SQL
     * select
     *    person0_.id as id1_0_,
     *    person0_.birth as birth2_0_,
     *    person0_.email as email3_0_,
     *    person0_.firstName as firstNam4_0_,
     *    person0_.lastName as lastName5_0_
     * from
     *    PERSONS person0_ limit ?, ?
     *
     * select
     *    count(person0_.id) as col_0_0_
     * from
     *    PERSONS person0_
     */
    @Test
    public void testPagingAndSortingRespository(){
        // pageNo 从 0 开始.
        int pageNo = 3 - 1;
        int pageSize = 3;

        PageRequest pageRequest = PageRequest.of(pageNo, pageSize);
        Page<Person> page = personRepository.findAll(pageRequest);

        System.out.println("总记录数: " + page.getTotalElements());
        System.out.println("当前第几页: " + (page.getNumber() + 1));
        System.out.println("总页数: " + page.getTotalPages());
        System.out.println("当前页面的 List: " + page.getContent());
        System.out.println("当前页面的记录数: " + page.getNumberOfElements());
    }
}

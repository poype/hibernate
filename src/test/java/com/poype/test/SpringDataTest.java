package com.poype.test;

import com.poype.springdata.model.Person;
import com.poype.springdata.repository.PersonRepository;
import com.poype.springdata.service.PersonService;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

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
}

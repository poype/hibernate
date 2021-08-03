package com.poype.test;

import com.poype.springdata.model.Person;
import com.poype.springdata.repository.PersonRepository;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class SpringDataTest {

    private ApplicationContext ctx;

    private PersonRepository personRepository;

    {
        ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        personRepository = ctx.getBean(PersonRepository.class);
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
}

package com.poype.test;

import com.poype.springdata.model.Person;
import com.poype.springdata.repository.PersonRepository;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Date;

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
}
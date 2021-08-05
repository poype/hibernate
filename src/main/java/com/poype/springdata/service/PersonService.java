package com.poype.springdata.service;

import com.poype.springdata.model.Person;
import com.poype.springdata.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    @Transactional
    public void updatePersonEmail(String email, Integer id){
        int count = personRepository.updatePersonEmail(id, email);
        System.out.println("count: " + count);
    }

    @Transactional
    public void queryInTransaction() {
        Person person = personRepository.getByLastName("FF");
        person.setEmail("abcdefghijklmn");
        System.out.println(person);

        // 由于person对象在事务中被修改了，在事务结束时，会将person对象同步回DB，即自动执行update SQL
    }
}


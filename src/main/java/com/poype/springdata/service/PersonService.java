package com.poype.springdata.service;

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
}


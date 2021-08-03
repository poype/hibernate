package com.poype.springdata.repository;

import com.poype.springdata.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Integer> {

    Person getByLastName(String lastName);
}

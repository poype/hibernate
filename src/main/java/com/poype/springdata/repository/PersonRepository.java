package com.poype.springdata.repository;

import com.poype.springdata.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 1. org.springframework.data.repository.Repository 是一个空接口. 它是一个标记接口
 * 2. 若我们定义的接口继承了 Repository, 则 IOC 容器会为该接口自动创建一个 Bean实例(利用proxy技术)
 *    并将该bean实例纳入到 IOC 容器中. 进而可以在该接口中定义满足一定规范的方法
 */
public interface PersonRepository extends JpaRepository<Person, Integer> {

    Person getByLastName(String lastName);
}

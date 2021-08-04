package com.poype.springdata.repository;

import com.poype.springdata.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

/**
 * 1. org.springframework.data.repository.Repository 是一个空接口. 它是一个标记接口
 * 2. 若我们定义的接口继承了 Repository, 则 IOC 容器会为该接口自动创建一个 Bean实例(利用proxy技术)
 *    并将该bean实例纳入到 IOC 容器中. 进而可以在该接口中定义满足一定规范的方法
 */
public interface PersonRepository extends JpaRepository<Person, Integer>, JpaSpecificationExecutor<Person> {

    Person getByLastName(String lastName);

    /** 查询方法以 find | read | get 开头 */
    // WHERE lastName LIKE ?% AND id < ?
    List<Person> findByLastNameStartingWithAndIdLessThan(String lastName, Integer id);

    // WHERE lastName LIKE %? AND id < ?
    List<Person> readByLastNameEndingWithAndIdLessThan(String lastName, Integer id);

    // WHERE email IN (?, ?, ?) OR birth < ?
    List<Person> getByEmailInAndBirthLessThan(List<String> emails, Date birth);

    //查询 id 值最大的那个 Person
    //使用 @Query 注解可以自定义 JPQL 语句以实现更灵活的查询
    @Query("SELECT p FROM Person p WHERE p.id = (SELECT max(p2.id) FROM Person p2)")
    Person getMaxIdPerson();

    //为 @Query 注解传递参数的方式1: 使用占位符.
    @Query("SELECT p FROM Person p WHERE p.lastName = ?1 AND p.email = ?2")
    List<Person> testQueryAnnotationParams1(String lastName, String email);

    //为 @Query 注解传递参数的方式1: 命名参数的方式.
    @Query("SELECT p FROM Person p WHERE p.lastName = :lastName AND p.email = :email")
    List<Person> testQueryAnnotationParams2(@Param("email") String email, @Param("lastName") String lastName);

    //SpringData 允许在占位符上添加 %%.
    @Query("SELECT p FROM Person p WHERE p.lastName LIKE %?1% OR p.email LIKE %?2%")
    List<Person> testQueryAnnotationLikeParam(String lastName, String email);

    //SpringData 允许在占位符上添加 %%.
    @Query("SELECT p FROM Person p WHERE p.lastName LIKE %:lastName% OR p.email LIKE %:email%")
    List<Person> testQueryAnnotationLikeParam2(@Param("email") String email, @Param("lastName") String lastName);

    //设置 nativeQuery=true 即可以使用原生的 SQL 查询
    @Query(value="SELECT count(id) FROM persons", nativeQuery=true)
    long getTotalCount();

    // 可以通过自定义的 JPQL 完成 UPDATE 和 DELETE 操作. 但是 JPQL 不支持 INSERT 操作
    // 在 @Query 注解中编写 JPQL 语句, 但必须使用 @Modifying 进行修饰. 以通知 SpringData, 这是一个 UPDATE 或 DELETE 操作
    // UPDATE 或 DELETE 操作必须在事务中
    // 默认情况下, SpringData 的每个方法上有事务, 但都是一个只读事务. 他们不能完成修改操作!
    // 这里方法的返回值是update记录的数量，如果没有任何一条记录被update就返回0
    @Modifying
    @Query("UPDATE Person p SET p.email = :email WHERE p.id = :id")
    int updatePersonEmail(@Param("id") Integer id, @Param("email") String email);
}

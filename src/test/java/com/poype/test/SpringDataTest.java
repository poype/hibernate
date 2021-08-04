package com.poype.test;

import com.poype.springdata.model.Person;
import com.poype.springdata.repository.PersonRepository;
import com.poype.springdata.service.PersonService;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
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


    /**
     * 目标: 实现带查询条件的分页. id > 5 的条件
     *
     * 调用 JpaSpecificationExecutor 的 Page<T> findAll(Specification<T> spec, Pageable pageable);
     * Specification: 封装了 JPA Criteria 查询的查询条件
     * Pageable: 封装了请求分页的信息: 例如 pageNo, pageSize, Sort
     */
    @Test
    public void testJpaSpecificationExecutor(){
        int pageNo = 1 - 1;
        int pageSize = 2;

        PageRequest pageRequest = PageRequest.of(pageNo, pageSize);

        // 通常使用 Specification 的匿名内部类
        Specification<Person> specification = new Specification<Person>() {
            /**
             * @param root: 代表查询的实体类.
             * @param query: 可以从中可到 Root 对象, 即告知 JPA Criteria 查询要查询哪一个实体类. 还可以
             * 来添加查询条件, 还可以结合 EntityManager 对象得到最终查询的 TypedQuery 对象.
             * @param cb: CriteriaBuilder 对象. 用于创建 Criteria 相关对象的工厂. 当然可以从中获取到 Predicate 对象
             * @return Predicate 类型, 代表一个查询条件.
             */
            @Override
            public Predicate toPredicate(Root<Person> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Path<Integer> path = root.get("id");
                Predicate predicate = cb.equal(cb.mod(path, 2), 0);
                return predicate;
            }
        };

        Page<Person> page = personRepository.findAll(specification, pageRequest);

        System.out.println("总记录数: " + page.getTotalElements());
        System.out.println("当前第几页: " + (page.getNumber() + 1));
        System.out.println("总页数: " + page.getTotalPages());
        System.out.println("当前页面的 List: " + page.getContent());
        System.out.println("当前页面的记录数: " + page.getNumberOfElements());
    }
}

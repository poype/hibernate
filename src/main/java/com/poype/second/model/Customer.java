package com.poype.second.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "CUSTOMERS")
public class Customer implements Serializable {

    private Long id;

    private String name;

    private String email;

    private String password;

    private int phone;

    private Address address;

    private Gender sex;

    private boolean married;

    private String description;

    private Date birthday;

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", phone=" + phone +
                ", address=" + address +
                ", sex=" + sex +
                ", married=" + married +
                ", description='" + description + '\'' +
                ", birthday=" + birthday +
                '}';
    }

    /*
     * 本例重点就是address的处理，address property是一个value object
     * 此处我们对这个value object做序列化转换后再存储到DB中
     */

    @Transient  // @Transient在这里是必须的，表示address属性不会存储到DB中
    public Address getAddress() {
        return address;
    }

    @Transient
    public void setAddress(Address address) {
        this.address = address;
    }

    // 由于这个property和DB中的column名字不一样，所以一样要添加@column annotation
    @Column(name="ADDRESS")
    public String getAddressValue() {
        return address.getProvince() + "-" + address.getCity() + "-" + address.getPostCode();
    }

    @Column(name="ADDRESS")
    public void setAddressValue(String addressValue) {
        System.out.println(addressValue);
        this.address = new Address("1", "2", "3");
    }

    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    // 默认情况下，枚举类型的property会把其常量对应的序号存储到DB中(0, 1, 2这种)，这种数字很不容易记忆
    // 如果想要存储枚举对应的字符串，用下面的annotation明确声明
    @Enumerated(EnumType.STRING)
    public Gender getSex() {
        return sex;
    }

    @Enumerated(EnumType.STRING)
    public void setSex(Gender sex) {
        this.sex = sex;
    }

    public boolean isMarried() {
        return married;
    }

    public void setMarried(boolean married) {
        this.married = married;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }
}

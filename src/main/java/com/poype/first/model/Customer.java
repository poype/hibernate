package com.poype.first.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "CUSTOMERS")
public class Customer implements Serializable {
    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    @Column(name="ID")
    private Long id;

    @Basic(optional=false)
    @Column(name="NAME", length=15)
    private String name;

    @Basic(optional=false)
    @Column(name="EMAIL", length=128)
    private String email;

    @Basic(optional=false)
    @Column(name="PASSWORD", length=8)
    private String password;

    @Column(name="PHONE")
    private int phone;

    @Column(name="ADDRESS",length=255)
    private String address;

    @Column(name="SEX")
    private char sex;

    @Column(name="IS_MARRIED")
    private boolean married;

    @Column(name="DESCRIPTION")
    private String description;

    @Column(name="BIRTHDAY")
    private Date birthday;

    @Column(name="REGISTERED_TIME")
    private Date registeredTime;

    public Customer() {
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", phone=" + phone +
                ", address='" + address + '\'' +
                ", sex=" + sex +
                ", married=" + married +
                ", description='" + description + '\'' +
                ", birthday=" + birthday +
                ", registeredTime=" + registeredTime +
                '}';
    }

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public char getSex() {
        return sex;
    }

    public void setSex(char sex) {
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

    public Date getRegisteredTime() {
        return registeredTime;
    }

    public void setRegisteredTime(Date registeredTime) {
        this.registeredTime = registeredTime;
    }
}

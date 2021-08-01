package com.poype.first.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * “@Table” annotation不是必须要加
 * 如果不加@Table annotation的话那么默认表名与类名相同
 * 如果表名和类名不同那么就必须要加table annotation明确告知表名
 * 此外如果表的column名和类的property名相同的话，@Column annotation也可以省略(此处就没有用到 @Column)
 */
@Entity
@Table(name = "ORDERS")
public class Order implements Serializable {

    private Long id;

    private String orderNumber;

    private Double price;

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", orderNumber='" + orderNumber + '\'' +
                ", price=" + price +
                '}';
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


    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}

package com.tasks.completed.deepclone;

import com.tasks.completed.deepclone.model.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/*
 * Задача
 *     Реализовать глубокое копирование объекта
 *     класса Order через интерфейс Cloneable.
 * */

public class Main {
    public static void main(String[] args) {
        Order order = getOrder();
        Order clone = order.clone();

        System.out.println(order);
        System.out.println(clone);

        System.out.println();

        applySomeChanges(clone);

        System.out.println("Order: " + order.getOrderDate());
        System.out.println("Clone: " + clone.getOrderDate());
        System.out.println();
        System.out.println("Order: " + order.getUser());
        System.out.println("Clone: " + clone.getUser());
        System.out.println();
        System.out.println("Order: " + order.getProducts());
        System.out.println("Clone: " + clone.getProducts());
    }

    private static List<Product> getListOfProducts() {
        return new ArrayList<>(List.of(
                new Product("Product1", "d1", 1, new SensitiveObject(1)),
                new Product("Product2", "d2", 2, new SensitiveObject(2)),
                new Product("Product3", "d3", 3, new SensitiveObject(3))
        ));
    }

    private static Order getOrder() {
        return new Order(LocalDate.now(), getListOfProducts(),
                new User("UserName", new Address(), new SensitiveObject(123))
        );
    }

    private static void applySomeChanges(Order order) {
        order.setOrderDate(order.getOrderDate().plusYears(2));

        order.getProducts().set(0, null);
        order.getProducts().get(1).setPrice(-999);
        order.getProducts().get(2).getSensitiveObject().setSecretCode(-999);

        order.getUser().setName("New user name");
        order.getUser().setAddress(null);
        order.getUser().getSensitiveObject().setSecretCode(-999);
    }
}
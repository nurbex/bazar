package com.eshop.bazar.domain;

import javax.persistence.*;
import java.util.List;

@Entity
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;

    public enum TYPE {
        OFFLINE,
        ONLINE
    };

    @OneToOne(cascade=CascadeType.ALL)
    Address address;

    @OneToMany
    @JoinTable(name = "store_product")
    List<Product> products;

    private TYPE storeType;

    public Store(){}

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

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public TYPE getStoreType() {
        return storeType;
    }

    public void setStoreType(TYPE storeType) {
        this.storeType = storeType;
    }
}

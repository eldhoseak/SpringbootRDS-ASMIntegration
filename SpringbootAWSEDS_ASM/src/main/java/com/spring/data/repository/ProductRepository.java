package com.spring.data.repository;

import org.springframework.data.repository.CrudRepository;

import com.spring.data.model.Product;

import java.util.List;

public interface ProductRepository<P> extends CrudRepository<Product, Long> {
    List<Product> findByName(String name);
}
package com.spring.data.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.data.model.Product;
import com.spring.data.repository.ProductRepository;


@Service
public class ProductService {

	@Autowired
	ProductRepository<Product> productRepository;

	@Transactional
	public List<Product> getAllProducts() {
		return (List<Product>) productRepository.findAll();
	}

	@Transactional
	public List<Product> findByName(String name) {

		System.out.println("inside findByName method");
		return productRepository.findByName(name);
	}

	@Transactional
	public boolean addProduct(Product product) {
		return productRepository.save(product) != null;
	}

	@Transactional
	public boolean updateProduct(Product product) {
		return productRepository.save(product) != null;
	}
}

package com.spring.data.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.spring.data.model.Product;
import com.spring.data.service.ProductService;


@RestController
public class ProductController {

	@Autowired
	ProductService productService;

	@RequestMapping(value = "/productByName/{name}", method = RequestMethod.GET)
	public List<Product> getproducteByName(@PathVariable String name) {
		return productService.findByName(name);
	}

	@RequestMapping(value = "/product", method = RequestMethod.GET)
	public List<Product> getAll() {
		return productService.getAllProducts();
	}

	@RequestMapping(value = "/product", method = RequestMethod.PUT)
	public HttpStatus insertproducte(@RequestBody Product product) {
		return productService.addProduct(product) ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST;
	}

	@RequestMapping(value = "/product", method = RequestMethod.POST)
	public HttpStatus updateproducte(@RequestBody Product product) {
		return productService.updateProduct(product) ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST;
	}
}


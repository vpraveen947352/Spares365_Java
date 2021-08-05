package com.stigentech.spares365.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stigentech.spares365.model.Product;
import com.stigentech.spares365.repository.ProductRepository;

@Service
public class ProductService {

	@Autowired
	private ProductRepository productRepository;
	
	public Product saveProduct(Product prod) {
		return productRepository.save(prod);		
	}

	public Product findById(int id) {
		return productRepository.findById(id);
	}
	
	public List<Product> findAllProducts() {
		return productRepository.findAll();
	}

	public Product findByName(String name) {
		return productRepository.findByName(name);
	}
}

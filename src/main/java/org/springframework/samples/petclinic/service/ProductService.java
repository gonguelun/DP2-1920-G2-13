package org.springframework.samples.petclinic.service;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Beautician;
import org.springframework.samples.petclinic.model.BeautyCenter;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Product;
import org.springframework.samples.petclinic.repository.PetRepository;
import org.springframework.samples.petclinic.repository.ProductRepository;
import org.springframework.samples.petclinic.repository.VisitRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {
	private ProductRepository productRepository;
	

	@Autowired
	public ProductService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}
	
	@Transactional(readOnly = true)
	public Collection<Product> findProducts() throws DataAccessException {
		return productRepository.findProducts();
	}

	public Collection<Product> findProductsByPet(int petTypeId) {
		return productRepository.findProductsByPet(petTypeId);
	}
	
	@Transactional
	public void save(final Product product) {
		this.productRepository.save(product);
		
	}
	@Transactional
	public Collection<PetType> findSpecializationsByBeauticianId(int beauticianId) {
		return productRepository.findSpecializationsByBeauticianId(beauticianId);
	}

	public Beautician findBeauticianById(int beauticianId) {
		return productRepository.findBeauticianById(beauticianId);
	}

	public Product findProductById(int productId) {
		return productRepository.findProductById(productId);
	}
	@Transactional
	public void deleteProduct(Product product) {
		this.productRepository.delete(product);
		
	}
	
	
	
}

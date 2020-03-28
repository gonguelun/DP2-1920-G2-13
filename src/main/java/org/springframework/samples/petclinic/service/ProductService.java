
package org.springframework.samples.petclinic.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Beautician;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Product;
import org.springframework.samples.petclinic.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

	private ProductRepository productRepository;


	@Autowired
	public ProductService(final ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	@Transactional(readOnly = true)
	public Collection<Product> findProducts() throws DataAccessException {
		return this.productRepository.findProducts();
	}

	@Transactional
	public Collection<Product> findProductsByPet(final int petTypeId) {
		return this.productRepository.findProductsByPet(petTypeId);
	}

	@Transactional
	public void save(final Product product) {
		this.productRepository.save(product);

	}
	@Transactional
	public Collection<PetType> findSpecializationsByBeauticianId(final int beauticianId) {
		return this.productRepository.findSpecializationsByBeauticianId(beauticianId);
	}

	@Transactional
	public Beautician findBeauticianById(final int beauticianId) {
		return this.productRepository.findBeauticianById(beauticianId);
	}

	@Transactional
	public Product findProductById(final int productId) {
		return this.productRepository.findProductById(productId);
	}

	@Transactional
	public void deleteProduct(final Product product) {
		this.productRepository.delete(product);

	}

	@Transactional
	public Beautician findBeauticianByProductId(final int productId) {
		return this.productRepository.findBeauticianByProductId(productId);
	}

}

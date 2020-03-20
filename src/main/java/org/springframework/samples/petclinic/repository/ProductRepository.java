
package org.springframework.samples.petclinic.repository;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Beautician;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Product;

public interface ProductRepository {

	Collection<Product> findProducts() throws DataAccessException;

	Collection<Product> findProductsByPet(int petTypeId) throws DataAccessException;

	Collection<PetType> findSpecializationsByBeauticianId(int beauticianId) throws DataAccessException;

	Beautician findBeauticianById(int beauticianId) throws DataAccessException;

	Product findProductById(int productId) throws DataAccessException;

	void save(Product product) throws DataAccessException;

	void delete(Product product) throws DataAccessException;

	Beautician findBeauticianByProductId(int productId) throws DataAccessException;

}

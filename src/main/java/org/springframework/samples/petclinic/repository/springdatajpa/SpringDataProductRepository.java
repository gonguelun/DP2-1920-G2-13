
package org.springframework.samples.petclinic.repository.springdatajpa;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.Beautician;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Product;
import org.springframework.samples.petclinic.repository.ProductRepository;

public interface SpringDataProductRepository extends ProductRepository, Repository<Product, Integer> {

	@Override
	@Query("SELECT product FROM Product product ORDER BY product.name")
	Collection<Product> findProducts() throws DataAccessException;

	@Override
	@Query("SELECT product FROM Product product WHERE product.type.id=?1")
	Collection<Product> findProductsByPet(@Param("petType") int petType) throws DataAccessException;

	@Override
	@Query("SELECT specializations FROM Beautician beautician WHERE beautician.id=?1")
	Collection<PetType> findSpecializationsByBeauticianId(@Param("beauticianId") int beauticianId) throws DataAccessException;

	@Override
	@Query("SELECT b FROM Beautician b WHERE b.id=?1")
	Beautician findBeauticianById(@Param("beauticianId") int beauticianId) throws DataAccessException;

	@Override
	@Query("SELECT p FROM Product p WHERE p.id=?1")
	Product findProductById(@Param("productId") int productId) throws DataAccessException;

	@Override
	@Query("SELECT p.beautician FROM Product p WHERE p.id = ?1 ")
	Beautician findBeauticianByProductId(@Param("productId") int productId) throws DataAccessException;
}

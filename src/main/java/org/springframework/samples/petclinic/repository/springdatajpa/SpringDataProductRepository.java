package org.springframework.samples.petclinic.repository.springdatajpa;

import java.util.Collection;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Product;
import org.springframework.samples.petclinic.repository.PetRepository;
import org.springframework.samples.petclinic.repository.ProductRepository;

public interface SpringDataProductRepository extends ProductRepository, Repository<Product, Integer>{
	@Override
	@Query("SELECT product FROM Product product ORDER BY product.name")
	Collection<Product> findProducts() throws DataAccessException;
	
	@Override
	@Query("SELECT product FROM Product product WHERE product.type.id=?1")
	Collection<Product> findProductsByPet(@Param("petType") int petType) throws DataAccessException;
	
	@Override
	@Query("SELECT specializations FROM Beautician beautician WHERE beautician.id=?1")
	Collection<PetType> findSpecializationsByBeauticianId(@Param("beauticianId") int beauticianId) throws DataAccessException;
}

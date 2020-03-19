
package org.springframework.samples.petclinic.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.BeautyDate;
import org.springframework.samples.petclinic.model.Pet;

public interface BeautyDateRepository extends CrudRepository<BeautyDate, Integer> {

	@Query("SELECT b FROM BeautyDate b WHERE b.pet.id = ?1")
	BeautyDate findBeautyDateByPetId(int id);

	@Query("SELECT p FROM Pet p WHERE p.owner.user.username = ?1 AND p.type.id = ?2")
	Collection<Pet> findPetsByOwnerAndType(String ownerUsername, int petTypeId);

	@Query("SELECT bd FROM BeautyDate bd WHERE bd.pet.owner.user.username = ?1")
	Collection<BeautyDate> findBeautyDatesByOwnerUsername(String ownerUsername);

}

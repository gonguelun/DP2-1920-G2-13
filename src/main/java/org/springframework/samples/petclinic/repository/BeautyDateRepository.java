
package org.springframework.samples.petclinic.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.BeautyDate;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Product;

public interface BeautyDateRepository extends CrudRepository<BeautyDate, Integer> {

	@Query("SELECT b FROM BeautyDate b WHERE b.pet.id = ?1")
	BeautyDate findBeautyDateByPetId(int id);

	@Query("SELECT p FROM Pet p WHERE p.owner.user.username = ?1 AND p.type.id = ?2")
	Collection<Pet> findPetsByOwnerAndType(String ownerUsername, int petTypeId);

	@Query("SELECT bd FROM BeautyDate bd WHERE bd.pet.owner.user.username = ?1")
	Collection<BeautyDate> findBeautyDatesByOwnerUsername(String ownerUsername);

	@Transactional
	@Modifying
	@Query("DELETE FROM BeautyDate b WHERE b.id=?1")
	void remove(int beautyCenterId);

	@Transactional
	@Query("SELECT bd FROM BeautyDate bd WHERE bd.beautyCenter.beautician.id = ?1")
	List<BeautyDate> findBeautyDatesByBeauticianId(int beauticianId);

	@Transactional
	@Query("SELECT p FROM Product p WHERE p.beautician.id = ?1 AND p.type.id = ?2")
	List<Product> bringProductsFromBeauticianWithPetType(Integer beauticianId, Integer petTypeId);
	
	@Transactional
	@Query("SELECT bd FROM BeautyDate bd WHERE bd.beautyCenter.beautician.id=?1 and bd.startDate>sysdate and bd.startDate<=?2")
	Collection<BeautyDate> findBeautyDatesByBeauticianIdAndDate(int beauticianId,LocalDateTime dateMaxHour);

}

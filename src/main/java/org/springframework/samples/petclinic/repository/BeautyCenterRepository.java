
package org.springframework.samples.petclinic.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.BeautyCenter;
import org.springframework.samples.petclinic.model.PetType;

public interface BeautyCenterRepository extends CrudRepository<BeautyCenter, Integer> {

	@Query("SELECT b.specializations FROM Beautician b WHERE b.id = ?1")
	List<PetType> findPetTypesByBeauticianId(int beauticianId) throws DataAccessException;

	@Query("SELECT b FROM BeautyCenter b WHERE b.beautician.id = ?1 ")
	Collection<BeautyCenter> findAllBeautyCenterByBeauticianId(int beauticianId);
	
	@Query("SELECT b FROM BeautyCenter b WHERE b.id=?1")
	BeautyCenter findBeautyCenterByBeautyCenterId(int beautyCenterId);
	
	@Query("SELECT b FROM BeautyCenter b WHERE b.petType.id=?1")
	public Collection<BeautyCenter> findAllBeautyCentersByPetType(@Param("petTypeId") int petTypeId);

}

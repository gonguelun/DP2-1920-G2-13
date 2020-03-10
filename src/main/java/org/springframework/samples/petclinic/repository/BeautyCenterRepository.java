
package org.springframework.samples.petclinic.repository;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.BeautyCenter;
import org.springframework.samples.petclinic.model.PetType;

public interface BeautyCenterRepository extends CrudRepository<BeautyCenter, Integer> {

	@Query("SELECT b.specializations FROM Beautician b WHERE b.id = ?1")
	List<PetType> findPetTypesByBeauticianId(int beauticianId) throws DataAccessException;

}

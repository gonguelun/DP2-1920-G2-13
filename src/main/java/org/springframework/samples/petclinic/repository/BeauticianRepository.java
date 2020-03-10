
package org.springframework.samples.petclinic.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Beautician;

public interface BeauticianRepository extends CrudRepository<Beautician, Integer> {

	@Query("SELECT b FROM Beautician b WHERE b.user.username = ?1")
	Beautician findByUsername(String beauticianUsername);

}

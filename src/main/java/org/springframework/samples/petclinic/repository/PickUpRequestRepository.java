
package org.springframework.samples.petclinic.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.PickUpRequest;

public interface PickUpRequestRepository extends CrudRepository<PickUpRequest, Integer> {

	@Query("SELECT pur FROM PickUpRequest pur WHERE pur.owner.user.username = ?1")
	Collection<PickUpRequest> findPickUpRequestsByOwnerUsername(String ownerUsername);

	@Query("SELECT o FROM Owner o WHERE o.user.username = ?1")
	Owner findOwnerByUsername(String ownerUsername);

}

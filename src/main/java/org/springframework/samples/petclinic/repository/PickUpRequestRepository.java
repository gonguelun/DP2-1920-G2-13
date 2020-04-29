
package org.springframework.samples.petclinic.repository;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.PickUpRequest;

public interface PickUpRequestRepository extends CrudRepository<PickUpRequest, Integer> {

	@Query("SELECT pur FROM PickUpRequest pur WHERE pur.owner.user.username = ?1")
	Collection<PickUpRequest> findPickUpRequestsByOwnerUsername(String ownerUsername);

	@Query("SELECT o FROM Owner o WHERE o.user.username = ?1")
	Owner findOwnerByUsername(String ownerUsername);

	@Transactional
	@Modifying(clearAutomatically = true)
	@Query("UPDATE PickUpRequest p SET p.isAccepted = ?1, p.isClosed = ?2, p.contact = ?3 WHERE p.id = ?4")
	void update(Boolean isAccepted, Boolean isClosed, String contact, int pickUpId);

	@Transactional
	@Modifying
	@Query("DELETE FROM PickUpRequest p WHERE p.id=?1")
	void remove(int pickUpRequestId);

	@Query("SELECT p FROM PickUpRequest p WHERE p.id=?1")
	PickUpRequest findPickUpRequestByPickUpRequestId(int pickUpRequestiD);
}


package org.springframework.samples.petclinic.service;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.PickUpRequest;
import org.springframework.samples.petclinic.repository.PickUpRequestRepository;
import org.springframework.samples.petclinic.service.exceptions.NoPetTypeException;
import org.springframework.stereotype.Service;

@Service
public class PickUpRequestService {

	private PickUpRequestRepository pickUpRequestRepository;


	@Autowired
	public PickUpRequestService(final PickUpRequestRepository pickUpRequestRepository) {
		this.pickUpRequestRepository = pickUpRequestRepository;
	}

	public void savePickUpRequest(@Valid final PickUpRequest pickUpRequest) throws NoPetTypeException {
		if (pickUpRequest.getPetType() != null) {
			this.pickUpRequestRepository.save(pickUpRequest);
		} else {
			throw new NoPetTypeException();
		}
	}

	public Collection<PickUpRequest> findPickUpRequestsByOwnerUsername(final String ownerUsername) {
		return this.pickUpRequestRepository.findPickUpRequestsByOwnerUsername(ownerUsername);
	}

	public Owner findOwnerByUsername(final String ownerUsername) {
		return this.pickUpRequestRepository.findOwnerByUsername(ownerUsername);
	}

}

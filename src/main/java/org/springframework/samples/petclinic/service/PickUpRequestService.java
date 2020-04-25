
package org.springframework.samples.petclinic.service;

import java.util.Collection;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.PickUpRequest;
import org.springframework.samples.petclinic.repository.PickUpRequestRepository;
import org.springframework.samples.petclinic.service.exceptions.NoPetTypeException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PickUpRequestService {

	private PickUpRequestRepository pickUpRequestRepository;


	@Autowired
	public PickUpRequestService(final PickUpRequestRepository pickUpRequestRepository) {
		this.pickUpRequestRepository = pickUpRequestRepository;
	}

	@Transactional
	public void savePickUpRequest(@Valid final PickUpRequest pickUpRequest) throws NoPetTypeException {
		if (pickUpRequest.getPetType() != null) {
			this.pickUpRequestRepository.save(pickUpRequest);
		} else {
			throw new NoPetTypeException();
		}
	}

	@Transactional
	public Collection<PickUpRequest> findPickUpRequestsByOwnerUsername(final String ownerUsername) {
		return this.pickUpRequestRepository.findPickUpRequestsByOwnerUsername(ownerUsername);
	}

	@Transactional
	public Owner findOwnerByUsername(final String ownerUsername) {
		return this.pickUpRequestRepository.findOwnerByUsername(ownerUsername);
	}

	@Transactional
	public List<PickUpRequest> findAllPickUpRequests() {
		return (List<PickUpRequest>) this.pickUpRequestRepository.findAll();
	}

	@Transactional
	public void remove(final int pickUpRequestId) {

		this.pickUpRequestRepository.remove(pickUpRequestId);

	}

	@Transactional
	public PickUpRequest findPickUpRequestByPickUpRequestId(final int pickUpRequestId) {
		return this.pickUpRequestRepository.findPickUpRequestByPickUpRequestId(pickUpRequestId);
	}
}

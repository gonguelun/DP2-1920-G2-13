
package org.springframework.samples.petclinic.service;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.PickUpRequest;
import org.springframework.samples.petclinic.repository.PickUpRequestRepository;
import org.springframework.stereotype.Service;

@Service
public class PickUpRequestService {

	private PickUpRequestRepository pickUpRequestRepository;


	@Autowired
	public PickUpRequestService(final PickUpRequestRepository pickUpRequestRepository) {
		this.pickUpRequestRepository = pickUpRequestRepository;
	}

	public void savePickUpRequest(@Valid final PickUpRequest pickUpRequest) {
		this.pickUpRequestRepository.save(pickUpRequest);
	}

}

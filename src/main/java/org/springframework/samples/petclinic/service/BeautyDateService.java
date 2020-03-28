
package org.springframework.samples.petclinic.service;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.BeautyDate;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.repository.BeautyDateRepository;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedPetNameException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BeautyDateService {

	private BeautyDateRepository	beautyDateRepository;

	private PetService				petService;


	@Autowired
	public BeautyDateService(final BeautyDateRepository beautyDateRepository, final PetService petService) {
		this.beautyDateRepository = beautyDateRepository;
		this.petService = petService;
	}

	@Transactional(readOnly = true)
	public BeautyDate findBeautyDateById(final int id) throws DataAccessException {
		return this.beautyDateRepository.findById(id).get();
	}

	@Transactional
	public void saveBeautyDate(final BeautyDate beautyDate) throws DataAccessException, DuplicatedPetNameException {
		this.petService.savePet(beautyDate.getPet());
		this.beautyDateRepository.save(beautyDate);
	}

	public BeautyDate findBeautyDateByPetId(final int id) {
		return this.beautyDateRepository.findBeautyDateByPetId(id);
	}

	public Collection<Pet> findPetsByOwnerAndType(final String ownerUsername, final int petTypeId) {
		return this.beautyDateRepository.findPetsByOwnerAndType(ownerUsername, petTypeId);
	}

	public Collection<BeautyDate> findBeautyDatesByOwnerUsername(final String ownerUsername) {
		return this.beautyDateRepository.findBeautyDatesByOwnerUsername(ownerUsername);
	}

	public BeautyDate findById(final int beautyDateId) {
		return this.beautyDateRepository.findById(beautyDateId).get();
	}

	public void remove(final int beautyDateId) {
		this.beautyDateRepository.remove(beautyDateId);

	}

	public List<BeautyDate> findBeautyDatesByBeauticianId(final int beauticianId) {
		return this.beautyDateRepository.findBeautyDatesByBeauticianId(beauticianId);
	}
}

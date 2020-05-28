
package org.springframework.samples.petclinic.service;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Beautician;
import org.springframework.samples.petclinic.model.BeautyCenter;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.repository.BeauticianRepository;
import org.springframework.samples.petclinic.repository.BeautyCenterRepository;
import org.springframework.samples.petclinic.repository.PetRepository;
import org.springframework.samples.petclinic.service.exceptions.NoPetTypeException;
import org.springframework.samples.petclinic.service.exceptions.NullOrShortNameException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BeautyCenterService {

	private BeautyCenterRepository	beautyRepository;

	private BeauticianRepository	beauticianRepository;

	private PetRepository			petRepository;


	@Autowired
	public BeautyCenterService(final BeautyCenterRepository beautyCenterRepository, final BeauticianRepository beauticianRepository, final PetRepository petRepository) {
		this.beauticianRepository = beauticianRepository;
		this.beautyRepository = beautyCenterRepository;
		this.petRepository = petRepository;

	}

	@Transactional
	public int beautyCount() {
		return (int) this.beautyRepository.count();
	}

	@Transactional
	public Iterable<BeautyCenter> findAll() {
		return this.beautyRepository.findAll();
	}

	@Transactional(rollbackFor = {
		NoPetTypeException.class, NullOrShortNameException.class
	})
	public boolean save(final BeautyCenter beautyCenter) throws NullOrShortNameException, NoPetTypeException {
		if (beautyCenter.getName() == null || beautyCenter.getName().length() < 3) {
			throw new NullOrShortNameException();
		} else if (beautyCenter.getPetType() == null) {
			throw new NoPetTypeException();

		} else {
			this.beautyRepository.save(beautyCenter);
			return true;
		}
	}

	@Transactional
	public Collection<PetType> findPetTypesByBeauticianId(final int beauticianId) throws DataAccessException {
		return this.beautyRepository.findPetTypesByBeauticianId(beauticianId);
	}

	@Transactional
	public Beautician findBeauticianById(final int beauticianId) throws DataAccessException {
		return this.beauticianRepository.findById(beauticianId).get();
	}

	@Transactional
	public BeautyCenter findById(final int beautyCenterId) throws DataAccessException {
		return this.beautyRepository.findById(beautyCenterId).get();
	}

	@Transactional
	public Collection<BeautyCenter> findAllBeautyCenterByBeauticianId(final int beauticianId) {
		return this.beautyRepository.findAllBeautyCenterByBeauticianId(beauticianId);
	}

	@Transactional
	public boolean update(@Valid final BeautyCenter beauticianCenter, final int beauticianId) throws NullOrShortNameException, NoPetTypeException {
		String name = beauticianCenter.getName();
		String description = beauticianCenter.getDescription();
		PetType petType = beauticianCenter.getPetType();
		if (beauticianCenter.getName().length() < 3 || beauticianCenter.getName().isEmpty()) {
			throw new NullOrShortNameException();
		} else if (petType == null || petType.getName() == "" || !beauticianCenter.getPetType().getName().equals("bird") && !beauticianCenter.getPetType().getName().equals("cat") && !beauticianCenter.getPetType().getName().equals("dog")
			&& !beauticianCenter.getPetType().getName().equals("hamster") && !beauticianCenter.getPetType().getName().equals("lizard") && !beauticianCenter.getPetType().getName().equals("snake")) {
			throw new NoPetTypeException();
		} else {
			this.beautyRepository.update(name, description, petType, beauticianId);
			return true;
		}
	}

	@Transactional
	public BeautyCenter findBeautyCenterByBeautyCenterId(final int beautyCenterId) {
		return this.beautyRepository.findBeautyCenterByBeautyCenterId(beautyCenterId);
	}

	@Transactional
	public void remove(final int beautyCenterId) {

		this.beautyRepository.remove(beautyCenterId);

	}

	@Transactional(readOnly = true)
	public Collection<PetType> findPetTypes() throws DataAccessException {
		return this.petRepository.findPetTypes();
	}

}

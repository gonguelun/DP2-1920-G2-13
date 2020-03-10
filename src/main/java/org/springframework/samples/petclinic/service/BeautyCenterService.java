
package org.springframework.samples.petclinic.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Beautician;
import org.springframework.samples.petclinic.model.BeautyCenter;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.repository.BeauticianRepository;
import org.springframework.samples.petclinic.repository.BeautyCenterRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BeautyCenterService {

	@Autowired
	private BeautyCenterRepository	beautyRepository;

	@Autowired
	private BeauticianRepository	beauticianRepository;


	@Transactional
	public int beautyCount() {
		return (int) this.beautyRepository.count();
	}

	@Transactional
	public Iterable<BeautyCenter> findAll() {
		return this.beautyRepository.findAll();
	}

	@Transactional
	public void save(final BeautyCenter beautyCenter) {
		this.beautyRepository.save(beautyCenter);
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

}

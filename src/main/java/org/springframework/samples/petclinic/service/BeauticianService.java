
package org.springframework.samples.petclinic.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Beautician;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.repository.BeauticianRepository;
import org.springframework.samples.petclinic.repository.PetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BeauticianService {

	@Autowired
	private BeauticianRepository	beauticianRepository;

	@Autowired
	private PetRepository			petRepository;

	@Autowired
	private UserService				userService;

	@Autowired
	private AuthoritiesService		authoritiesService;


	@Transactional(readOnly = true)
	public Beautician findBeauticianById(final int id) throws DataAccessException {
		return this.beauticianRepository.findById(id).get();
	}

	@Transactional
	public void saveBeautician(final Beautician beautician) throws DataAccessException {
		this.beauticianRepository.save(beautician);
		this.userService.saveUser(beautician.getUser());
		this.authoritiesService.saveAuthorities(beautician.getUser().getUsername(), "beautician");
	}

	@Transactional
	public Integer countBeauticians() {
		Integer count = (int) this.beauticianRepository.count();
		return count;
	}

	@Transactional
	public List<PetType> allTypes() {

		return this.petRepository.findPetTypes();
	}

}

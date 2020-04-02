
package org.springframework.samples.petclinic.service;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Beautician;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.repository.BeauticianRepository;
import org.springframework.samples.petclinic.repository.PetRepository;
import org.springframework.samples.petclinic.service.exceptions.InvalidBeauticianException;
import org.springframework.samples.petclinic.service.exceptions.InvalidSpecializationException;
import org.springframework.samples.petclinic.service.exceptions.NullOrShortNameException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;

@Service
public class BeauticianService {

	private BeauticianRepository	beauticianRepository;

	private PetRepository			petRepository;

	private UserService				userService;

	private AuthoritiesService		authoritiesService;

	private PetService				petService;

	@Autowired
	public BeauticianService(BeauticianRepository beauticianRepository,PetRepository petRepository,
			UserService userService,AuthoritiesService authoritiesService,PetService petService) {
		this.beauticianRepository=beauticianRepository;
		this.petRepository=petRepository;
		this.userService=userService;
		this.authoritiesService=authoritiesService;
		this.petService=petService;
	}
	@ModelAttribute("types")
	public Collection<PetType> populatePetTypes() {
		return this.petService.findPetTypes();
	}

	@Transactional(readOnly = true)
	public Beautician findBeauticianById(final int id) throws DataAccessException {
		return this.beauticianRepository.findById(id).get();
	}

	@Transactional
	public void saveBeautician(final Beautician beautician) throws DataAccessException {
		this.userService.saveUser(beautician.getUser());
		this.beauticianRepository.save(beautician);
		this.authoritiesService.saveAuthorities(beautician.getUser().getUsername(), "beautician");
	}

	@Transactional
	public Integer countBeauticians() {
		return (int) this.beauticianRepository.count();
		
	}

	@Transactional
	public List<PetType> allTypes() {

		return this.petRepository.findPetTypes();
	}

	public Beautician findBeauticianByUsername(final String beauticianUsername) throws DataAccessException {
		return this.beauticianRepository.findByUsername(beauticianUsername);
	}
	
	@Transactional
	public void isBeauticianGuardado(User usuario,Beautician beautician,Beautician beauticianGuardado) throws InvalidBeauticianException,InvalidSpecializationException, NullOrShortNameException {
		boolean aux= beautician.getUser().getUsername().equals(beauticianGuardado.getUser().getUsername()) || usuario == null;
		if (!aux) {
			throw new InvalidBeauticianException();
		}else if (!beautician.getSpecializations().stream().anyMatch(i -> i.getName().equals("bird")) && 
				!beautician.getSpecializations().stream().anyMatch(i -> i.getName().equals("cat")) && 
				!beautician.getSpecializations().stream().anyMatch(i -> i.getName().equals("dog")) && 
				!beautician.getSpecializations().stream().anyMatch(i -> i.getName().equals("hamster")) && 
				!beautician.getSpecializations().stream().anyMatch(i -> i.getName().equals("lizard")) && 
				!beautician.getSpecializations().stream().anyMatch(i -> i.getName().equals("snake"))) {
			throw new InvalidSpecializationException();
			
		}else if(beautician.getFirstName()==null || beautician.getFirstName()=="") {
			throw new NullOrShortNameException();
		}else {
			beautician.getUser().setId(beauticianGuardado.getUser().getId());
			beautician.getUser().setEnabled(true);
		}
		
	}

}

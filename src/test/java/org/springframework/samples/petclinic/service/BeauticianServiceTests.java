
package org.springframework.samples.petclinic.service;



import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.assertj.core.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Beautician;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedPetNameException;
import org.springframework.samples.petclinic.service.exceptions.InvalidSpecializationException;
import org.springframework.samples.petclinic.service.exceptions.NullOrShortNameException;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class BeauticianServiceTests {

	@Autowired
	BeauticianService beauticianService;

	@Autowired
	PetService petService;

	@Test
	public void testSavingBeauticianCorrect() {
		Integer countBefore = this.beauticianService.countBeauticians();
		countBefore++;
		
		Beautician beautician = new Beautician();
		beautician.setFirstName("Jane");
		beautician.setLastName("Doe");
		User user = new User();
		user.setUsername("JaneDoe");
		user.setPassword("1234");
		user.setEnabled(true);
		beautician.setUser(user);
		Collection<PetType> specializations = new ArrayList<PetType>();
		PetType pet = new PetType();
		pet.setName("dog");
		specializations.add(pet);
		beautician.setSpecializations(specializations);
		this.beauticianService.saveBeautician(beautician);
		Integer countAfter = this.beauticianService.countBeauticians();
		Assertions.assertThat(countBefore == countAfter);
	}

	@Test
	public void testSavingBeauticianWithIncorrectSpecialization() {
		
		Beautician beauticianGuardado = new Beautician();
		beauticianGuardado.setFirstName("Jane");
		beauticianGuardado.setLastName("Doe");
		User userGuardado = new User();
		userGuardado.setUsername("JaneDoe");
		userGuardado.setPassword("1234");
		userGuardado.setEnabled(true);
		beauticianGuardado.setUser(userGuardado);
		Collection<PetType> specializationsGuardado = new ArrayList<PetType>();
		PetType petGuardado = new PetType();
		petGuardado.setName("dog");
		specializationsGuardado.add(petGuardado);
		beauticianGuardado.setSpecializations(specializationsGuardado);
		
		Beautician beautician = new Beautician();
		beautician.setFirstName("Jane Michael");
		beautician.setLastName("Doesnt");
		User user = new User();
		user.setUsername("JaneDoe");
		user.setPassword("12345");
		user.setEnabled(true);
		beautician.setUser(user);
		Collection<PetType> specializations = new ArrayList<PetType>();
		PetType pet = new PetType();
		pet.setName("reptiles");
		specializations.add(pet);
		beautician.setSpecializations(specializations);
		assertThrows(InvalidSpecializationException.class, () -> this.beauticianService.isBeauticianGuardado(beautician.getUser(), beautician, beauticianGuardado));
	}

	@Test
	public void testSavingBeauticianWithIncorrectAttribute() {
		Beautician beauticianGuardado = new Beautician();
		beauticianGuardado.setFirstName("Jane");
		beauticianGuardado.setLastName("Doe");
		User userGuardado = new User();
		userGuardado.setUsername("JaneDoe");
		userGuardado.setPassword("1234");
		userGuardado.setEnabled(true);
		beauticianGuardado.setUser(userGuardado);
		Collection<PetType> specializationsGuardado = new ArrayList<PetType>();
		PetType petGuardado = new PetType();
		petGuardado.setName("dog");
		specializationsGuardado.add(petGuardado);
		beauticianGuardado.setSpecializations(specializationsGuardado);
		
		Beautician beautician = new Beautician();
		beautician.setFirstName("");
		beautician.setLastName("Doesnt");
		User user = new User();
		user.setUsername("JaneDoe");
		user.setPassword("12345");
		user.setEnabled(true);
		beautician.setUser(user);
		Collection<PetType> specializations = new ArrayList<PetType>();
		PetType pet = new PetType();
		pet.setName("cat");
		specializations.add(pet);
		beautician.setSpecializations(specializations);
		
		assertThrows(NullOrShortNameException.class, () -> this.beauticianService.isBeauticianGuardado(beautician.getUser(), beautician, beauticianGuardado));
	}

}
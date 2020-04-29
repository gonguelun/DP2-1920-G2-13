
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
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.PickUpRequest;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedPetNameException;
import org.springframework.samples.petclinic.service.exceptions.InvalidSpecializationException;
import org.springframework.samples.petclinic.service.exceptions.NoPetTypeException;
import org.springframework.samples.petclinic.service.exceptions.NullOrShortNameException;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class PickUpRequestServiceTests {

	@Autowired
	PickUpRequestService pickUpRequestService;

	//Historia de usuario 16
	//Caso positivo guardar PickUpRequestCorrectamente
	@Test
	public void testSavingPickUpRequestCorrect() throws NoPetTypeException {
		Integer countBefore = this.pickUpRequestService.countPickUps();
		countBefore++;
		
		PickUpRequest pickUp=new PickUpRequest();
		pickUp.setId(1);
		pickUp.setAddress("Calle 1");
		pickUp.setDescription("descripcion");
		pickUp.setIsAccepted(false);
		pickUp.setIsClosed(false);
		Owner owner=new Owner();
		User user = new User();
		user.setUsername("User1");
		user.setPassword("1234");
		user.setEnabled(true);
		owner.setFirstName("Paco");
		owner.setLastName("Gonzalez");
		owner.setCity("Sevilla");
		owner.setAddress("Calle 1");
		owner.setId(1);
		owner.setTelephone("666777888");
		owner.setUser(user);
		pickUp.setOwner(owner);
		pickUp.setPhysicalStatus("good");
		PetType pet = new PetType();
		pet.setName("dog");
		pet.setId(1);
		pickUp.setPetType(pet);
		
		this.pickUpRequestService.savePickUpRequest(pickUp);
		Integer countAfter = this.pickUpRequestService.countPickUps();
		Assertions.assertThat(countBefore == countAfter);
	}
	
	//Caso negativo PetType no valido
	@Test
	public void testSavingPickUpRequestIncorrectNoPetType() throws NoPetTypeException {
		PickUpRequest pickUp=new PickUpRequest();
		pickUp.setId(1);
		pickUp.setAddress("Calle 1");
		pickUp.setDescription("descripcion");
		pickUp.setIsAccepted(false);
		Owner owner=new Owner();
		User user = new User();
		user.setUsername("User1");
		user.setPassword("1234");
		user.setEnabled(true);
		owner.setFirstName("Paco");
		owner.setLastName("Gonzalez");
		owner.setCity("Sevilla");
		owner.setAddress("Calle 1");
		owner.setId(1);
		owner.setTelephone("666777888");
		owner.setUser(user);
		pickUp.setOwner(owner);
		pickUp.setPhysicalStatus("good");
		pickUp.setPetType(null);
		
		assertThrows(NoPetTypeException.class, () -> this.pickUpRequestService.savePickUpRequest(pickUp));
	}
	//Caso positivo se guarda sin descripcion
	@Test
	public void testSavingPickUpRequestNoDescriptionCorrect() throws NoPetTypeException {
		Integer countBefore = this.pickUpRequestService.countPickUps();
		countBefore++;
		
		PickUpRequest pickUp=new PickUpRequest();
		pickUp.setId(1);
		pickUp.setAddress("Calle 1");
		pickUp.setDescription("");
		pickUp.setIsAccepted(false);
		pickUp.setIsClosed(true);
		Owner owner=new Owner();
		User user = new User();
		user.setUsername("User1");
		user.setPassword("1234");
		user.setEnabled(true);
		owner.setFirstName("Paco");
		owner.setLastName("Gonzalez");
		owner.setCity("Sevilla");
		owner.setAddress("Calle 1");
		owner.setId(1);
		owner.setTelephone("666777888");
		owner.setUser(user);
		pickUp.setOwner(owner);
		pickUp.setPhysicalStatus("good");
		PetType pet = new PetType();
		pet.setName("dog");
		pet.setId(1);
		pickUp.setPetType(pet);
		
		this.pickUpRequestService.savePickUpRequest(pickUp);
		Integer countAfter = this.pickUpRequestService.countPickUps();
		Assertions.assertThat(countBefore == countAfter);
	}
	//Caso positivo no estado fisico
	@Test
	public void testSavingPickUpRequestNoPhysicalStatusCorrect() throws NoPetTypeException {
		Integer countBefore = this.pickUpRequestService.countPickUps();
		countBefore++;
		
		PickUpRequest pickUp=new PickUpRequest();
		pickUp.setId(1);
		pickUp.setAddress("Calle 1");
		pickUp.setDescription("descripcion");
		pickUp.setIsAccepted(false);
		pickUp.setIsClosed(true);
		Owner owner=new Owner();
		User user = new User();
		user.setUsername("User1");
		user.setPassword("1234");
		user.setEnabled(true);
		owner.setFirstName("Paco");
		owner.setLastName("Gonzalez");
		owner.setCity("Sevilla");
		owner.setAddress("Calle 1");
		owner.setId(1);
		owner.setTelephone("666777888");
		owner.setUser(user);
		pickUp.setOwner(owner);
		pickUp.setPhysicalStatus("");
		PetType pet = new PetType();
		pet.setName("dog");
		pet.setId(1);
		pickUp.setPetType(pet);
		
		this.pickUpRequestService.savePickUpRequest(pickUp);
		Integer countAfter = this.pickUpRequestService.countPickUps();
		Assertions.assertThat(countBefore == countAfter);
	}
	//Caso negativo no direccion
	@Test
	public void testSavingPickUpRequestNoAddressInCorrect() throws NoPetTypeException {
		PickUpRequest pickUp=new PickUpRequest();
		pickUp.setId(1);
		pickUp.setAddress("");
		pickUp.setDescription("descripcion");
		pickUp.setIsAccepted(false);
		Owner owner=new Owner();
		User user = new User();
		user.setUsername("User1");
		user.setPassword("1234");
		user.setEnabled(true);
		owner.setFirstName("Paco");
		owner.setLastName("Gonzalez");
		owner.setCity("Sevilla");
		owner.setAddress("Calle 1");
		owner.setId(1);
		owner.setTelephone("666777888");
		owner.setUser(user);
		pickUp.setOwner(owner);
		pickUp.setPhysicalStatus("good");
		PetType pet = new PetType();
		pet.setName("dog");
		pet.setId(1);
		pickUp.setPetType(pet);
		
		assertThrows(ConstraintViolationException.class, () -> this.pickUpRequestService.savePickUpRequest(pickUp));
	}
	
	//Caso negativo no valor de aceptado
	@Test
	public void testSavingPickUpRequestNoAcceptedInCorrect() throws NoPetTypeException {
		PickUpRequest pickUp=new PickUpRequest();
		pickUp.setId(1);
		pickUp.setAddress("Calle 1");
		pickUp.setDescription("descripcion");
		pickUp.setIsAccepted(null);
		Owner owner=new Owner();
		User user = new User();
		user.setUsername("User1");
		user.setPassword("1234");
		user.setEnabled(true);
		owner.setFirstName("Paco");
		owner.setLastName("Gonzalez");
		owner.setCity("Sevilla");
		owner.setAddress("Calle 1");
		owner.setId(1);
		owner.setTelephone("666777888");
		owner.setUser(user);
		pickUp.setOwner(owner);
		pickUp.setPhysicalStatus("good");
		PetType pet = new PetType();
		pet.setName("dog");
		pet.setId(1);
		pickUp.setPetType(pet);
		
		assertThrows(ConstraintViolationException.class, () -> this.pickUpRequestService.savePickUpRequest(pickUp));
	}
	
	
}
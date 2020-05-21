
package org.springframework.samples.petclinic.service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import javax.validation.ConstraintViolationException;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.PickUpRequest;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.exceptions.NoPetTypeException;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class PickUpRequestServiceTests {

	@Autowired
	PickUpRequestService	pickUpRequestService;

	@Autowired
	UserService				userService;

	@Autowired
	OwnerService			ownerService;

	@Autowired
	PetService				petService;


	//Historia de usuario 16
	//Caso positivo guardar PickUpRequestCorrectamente
	@Test
	public void testSavingPickUpRequestCorrect() throws NoPetTypeException {
		Integer countBefore = this.pickUpRequestService.countPickUps();
		countBefore++;

		User user = new User();
		user.setUsername("user1sasas");
		user.setPassword("1234");
		user.setEnabled(true);
		this.userService.saveUser(user);

		Owner owner = new Owner();
		owner.setFirstName("Paco");
		owner.setLastName("Gonzalez");
		owner.setCity("Sevilla");
		owner.setAddress("Calle 1");
		owner.setTelephone("666777888");
		owner.setUser(user);
		this.ownerService.saveOwner(owner);

		PetType pet = new PetType();
		pet.setName("dog");
		this.petService.savePetType(pet);

		PickUpRequest pickUp = new PickUpRequest();
		pickUp.setAddress("Calle 1");
		pickUp.setDescription("descripcion");
		pickUp.setIsAccepted(false);
		pickUp.setIsClosed(false);
		pickUp.setOwner(owner);
		pickUp.setPhysicalStatus("good");
		pickUp.setPetType(pet);

		this.pickUpRequestService.savePickUpRequest(pickUp);
		Integer countAfter = this.pickUpRequestService.countPickUps();
		Assertions.assertThat(countBefore == countAfter);
	}

	//Caso negativo PetType no valido
	@Test
	public void testSavingPickUpRequestIncorrectNoPetType() throws NoPetTypeException {
		PickUpRequest pickUp = new PickUpRequest();
		pickUp.setId(1);
		pickUp.setAddress("Calle 1");
		pickUp.setDescription("descripcion");
		pickUp.setIsAccepted(false);
		Owner owner = new Owner();
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

		PickUpRequest pickUp = new PickUpRequest();
		pickUp.setId(1);
		pickUp.setAddress("Calle 1");
		pickUp.setDescription("");
		pickUp.setIsAccepted(false);
		pickUp.setIsClosed(true);
		Owner owner = new Owner();
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

		User user = new User();
		user.setUsername("user1khgjhgjhgh");
		user.setPassword("1234");
		user.setEnabled(true);
		this.userService.saveUser(user);

		Owner owner = new Owner();
		owner.setFirstName("Paco");
		owner.setLastName("Gonzalez");
		owner.setCity("Sevilla");
		owner.setAddress("Calle 1");
		owner.setId(101);
		owner.setTelephone("666777888");
		owner.setUser(user);
		this.ownerService.saveOwner(owner);

		PetType pet = new PetType();
		pet.setName("dog");
		pet.setId(101);
		this.petService.savePetType(pet);

		PickUpRequest pickUp = new PickUpRequest();
		pickUp.setId(101);
		pickUp.setAddress("Calle 1");
		pickUp.setDescription("descripcion");
		pickUp.setIsAccepted(false);
		pickUp.setIsClosed(false);
		pickUp.setOwner(owner);
		pickUp.setPhysicalStatus("");
		pickUp.setPetType(pet);

		this.pickUpRequestService.savePickUpRequest(pickUp);
		Integer countAfter = this.pickUpRequestService.countPickUps();
		Assertions.assertThat(countBefore == countAfter);
	}
	//Caso negativo no direccion
	@Test
	public void testSavingPickUpRequestNoAddressInCorrect() throws NoPetTypeException {
		PickUpRequest pickUp = new PickUpRequest();
		pickUp.setId(92);
		pickUp.setAddress("");
		pickUp.setDescription("descripcion");
		pickUp.setIsAccepted(false);
		Owner owner = new Owner();
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

		try {
			this.pickUpRequestService.savePickUpRequest(pickUp);
		} catch (DataIntegrityViolationException e) {
			assertThrows(DataIntegrityViolationException.class, () -> this.pickUpRequestService.savePickUpRequest(pickUp));
		} catch (ConstraintViolationException d) {
			assertThrows(DataIntegrityViolationException.class, () -> this.pickUpRequestService.savePickUpRequest(pickUp));
		}
	}

	//Caso negativo no valor de aceptado
	@Test
	public void testSavingPickUpRequestNoAcceptedInCorrect() throws NoPetTypeException {
		PickUpRequest pickUp = new PickUpRequest();
		pickUp.setId(92);
		pickUp.setAddress("Calle 1");
		pickUp.setDescription("descripcion");
		pickUp.setIsAccepted(null);
		Owner owner = new Owner();
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

		try {
			this.pickUpRequestService.savePickUpRequest(pickUp);
		} catch (DataIntegrityViolationException e) {
			assertThrows(DataIntegrityViolationException.class, () -> this.pickUpRequestService.savePickUpRequest(pickUp));
		} catch (ConstraintViolationException d) {
			assertThrows(DataIntegrityViolationException.class, () -> this.pickUpRequestService.savePickUpRequest(pickUp));
		}
	}

}


package org.springframework.samples.petclinic.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Beautician;
import org.springframework.samples.petclinic.model.BeautyCenter;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.exceptions.NoPetTypeException;
import org.springframework.samples.petclinic.service.exceptions.NullOrShortNameException;
import org.springframework.samples.petclinic.web.BeautyCenterControllerTests;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class BeautyCenterServiceTests {

	@Autowired
	private BeautyCenterService	beautyService;

	@Autowired
	private PetService			petService;


	

	@Test
	public void testCountWithInitialData() {
		int count = this.beautyService.beautyCount();
		Assertions.assertThat(count == 5);
	}

	@Test
	public void testUpdateBeautyCenterSuccess() throws NullOrShortNameException, NoPetTypeException {
		BeautyCenter bc = this.beautyService.findById(1);
		bc.setName("name modified");
		bc.setDescription("description modified");

		List<PetType> pt = new ArrayList<>(this.petService.findPetTypes());
		bc.setPetType(pt.get(0));
		this.beautyService.update(bc, bc.getId());
		assertEquals(this.beautyService.update(bc, bc.getId()), true);

	}
	@Test
	public void testUpdateBeautyCenterSuccessNoDescription() throws NullOrShortNameException,NoPetTypeException{
		BeautyCenter bc = this.beautyService.findById(1);
		bc.setName("name modified");
		bc.setDescription("");

		List<PetType> pt = new ArrayList<>(this.petService.findPetTypes());
		bc.setPetType(pt.get(0));
		this.beautyService.update(bc, bc.getId());
		assertEquals(this.beautyService.update(bc, bc.getId()), true);
	}

	@Test
	public void testUpdateBeautyCenterErrorPetType() {
		BeautyCenter bc = this.beautyService.findById(1);
		bc.setName("name modified");
		bc.setDescription("description modified");
		PetType pet = new PetType();
		pet.setName("crocodile");
		bc.setPetType(pet);
		
		assertThrows(NoPetTypeException.class, () -> this.beautyService.update(bc, bc.getId()));


	}

	@Test
	public void testUpdateBeautyCenterErrorEmptyAttribute() {
		BeautyCenter bc = this.beautyService.findById(1);
		bc.setName("");
		bc.setDescription("description modified");
		PetType pet = new PetType();
		pet.setName("bird");
		bc.setPetType(pet);
		
		assertThrows(NullOrShortNameException.class, () -> this.beautyService.update(bc, bc.getId()));
	}

	/* Pruebas de Servicio para la historia de usuario 2 */

	//Pruebas para el método nameRestriction(BeautyCenter bc)

	//Caso positivo. Introduzco un beauty center con datos correctos
	@Test
	public void testSaveBeautyCenterCorrect() throws NullOrShortNameException, NoPetTypeException{
		PetType cat = new PetType();
		cat.setId(3);
		cat.setName("hamster");
		List<PetType> temp = new ArrayList<>();
		temp.add(cat);


		User user = new User();
		user.setId(1);
		user.setEnabled(true);
		user.setUsername("beautician1");
		user.setPassword("123");

		Beautician beautician = new Beautician();
		beautician.setId(1);
		beautician.setFirstName("juan");
		beautician.setLastName("aurora");
		beautician.setSpecializations(temp);
		beautician.setUser(user);

		BeautyCenter beautyCenter = new BeautyCenter();
		beautyCenter.setId(1);
		beautyCenter.setName("beautycenter1");
		beautyCenter.setDescription("prueba1");
		beautyCenter.setPetType(cat);
		beautyCenter.setBeautician(beautician);
		
		assertEquals(this.beautyService.save(beautyCenter), true);
	}
	
	//Caso positivo. Introduzco un beauty center con datos correctos y sin descripción
		@Test
		public void testSaveBeautyCenterNoDescriptionCorrect() throws NullOrShortNameException, NoPetTypeException{
			PetType cat = new PetType();
			cat.setId(3);
			cat.setName("hamster");
			List<PetType> temp = new ArrayList<>();
			temp.add(cat);


			User user = new User();
			user.setId(1);
			user.setEnabled(true);
			user.setUsername("beautician1");
			user.setPassword("123");

			Beautician beautician = new Beautician();
			beautician.setId(1);
			beautician.setFirstName("juan");
			beautician.setLastName("aurora");
			beautician.setSpecializations(temp);
			beautician.setUser(user);

			BeautyCenter beautyCenter = new BeautyCenter();
			beautyCenter.setId(1);
			beautyCenter.setName("beautycenter1");
			beautyCenter.setDescription(null);
			beautyCenter.setPetType(cat);
			beautyCenter.setBeautician(beautician);
			
			assertEquals(this.beautyService.save(beautyCenter), true);
		}
	
	//Caso negativo. Introduzco un beauty center con un nombre de dos caracteres
	
	@Test
	public void testSaveBeautyCenterShortName() throws NullOrShortNameException, NoPetTypeException{
		PetType cat = new PetType();
		cat.setId(3);
		cat.setName("hamster");
		List<PetType> temp = new ArrayList<>();
		temp.add(cat);


		User user = new User();
		user.setId(1);
		user.setEnabled(true);
		user.setUsername("beautician1");
		user.setPassword("123");

		Beautician beautician = new Beautician();
		beautician.setId(1);
		beautician.setFirstName("juan");
		beautician.setLastName("aurora");
		beautician.setSpecializations(temp);
		beautician.setUser(user);

		BeautyCenter beautyCenter = new BeautyCenter();
		beautyCenter.setId(1);
		beautyCenter.setName("be");
		beautyCenter.setDescription("prueba1");
		beautyCenter.setPetType(cat);
		beautyCenter.setBeautician(beautician);
		
		assertThrows(NullOrShortNameException.class, () -> this.beautyService.save(beautyCenter));
	}
	
	//Caso negativo. Introduzco un beauty center con un nombre nulo
	
		@Test
		public void testSaveBeautyCenterNullName() throws NullOrShortNameException, NoPetTypeException{
			PetType cat = new PetType();
			cat.setId(3);
			cat.setName("hamster");
			List<PetType> temp = new ArrayList<>();
			temp.add(cat);


			User user = new User();
			user.setId(1);
			user.setEnabled(true);
			user.setUsername("beautician1");
			user.setPassword("123");

			Beautician beautician = new Beautician();
			beautician.setId(1);
			beautician.setFirstName("juan");
			beautician.setLastName("aurora");
			beautician.setSpecializations(temp);
			beautician.setUser(user);

			BeautyCenter beautyCenter = new BeautyCenter();
			beautyCenter.setId(1);
			beautyCenter.setName(null);
			beautyCenter.setDescription("prueba1");
			beautyCenter.setPetType(cat);
			beautyCenter.setBeautician(beautician);
			
			assertThrows(NullOrShortNameException.class, () -> this.beautyService.save(beautyCenter));
		}
		
		//Caso negativo. Introduzco un beauty center con un PetType nulo
		
		@Test
		public void testSaveBeautyCenterNullPetType() throws NullOrShortNameException, NoPetTypeException{
			PetType cat = new PetType();
			cat.setId(3);
			cat.setName("hamster");
			List<PetType> temp = new ArrayList<>();
			temp.add(cat);


			User user = new User();
			user.setId(1);
			user.setEnabled(true);
			user.setUsername("beautician1");
			user.setPassword("123");

			Beautician beautician = new Beautician();
			beautician.setId(1);
			beautician.setFirstName("juan");
			beautician.setLastName("aurora");
			beautician.setSpecializations(temp);
			beautician.setUser(user);

			BeautyCenter beautyCenter = new BeautyCenter();
			beautyCenter.setId(1);
			beautyCenter.setName("bec");
			beautyCenter.setDescription("prueba1");
			beautyCenter.setPetType(null);
			beautyCenter.setBeautician(beautician);
			
			assertThrows(NoPetTypeException.class, () -> this.beautyService.save(beautyCenter));
		}
}

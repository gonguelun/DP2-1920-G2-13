
package org.springframework.samples.petclinic.service;

import java.util.Collection;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.BeautyCenter;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
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
	public void testUpdateBeautyCenterSuccess() {
		BeautyCenter bc = this.beautyService.findById(1);
		String oldname = bc.getName();
		String oldDescription = bc.getDescription();
		PetType oldP = bc.getPetType();
		bc.setName("name modified");
		bc.setDescription("description modified");
		PetType pet = new PetType();
		pet.setName("bird");
		bc.setPetType(pet);
		this.beautyService.update(bc, bc.getId());
		String newname = bc.getName();
		String newDescription = bc.getDescription();
		PetType newP = bc.getPetType();

		Assertions.assertThat(newname).isNotEqualTo(oldname);
		Assertions.assertThat(newDescription).isNotEqualTo(oldDescription);
		Assertions.assertThat(newP).isNotEqualTo(oldP);

	}

	@Test
	public void testUpdateBeautyCenterErrorPetType() {
		BeautyCenter bc = this.beautyService.findById(1);
		Collection<PetType> types = this.petService.findPetTypes();
		String oldname = bc.getName();
		String oldDescription = bc.getDescription();
		PetType oldP = bc.getPetType();
		bc.setName("name modified");
		bc.setDescription("description modified");
		PetType pet = new PetType();
		pet.setName("crocodile");
		if (types.stream().anyMatch(i -> i.getName().equals(pet.getName()))) {
			bc.setPetType(pet);
			this.beautyService.update(bc, bc.getId());
			String newname = bc.getName();
			String newDescription = bc.getDescription();
			PetType newP = bc.getPetType();

			Assertions.assertThat(newname).isNotEqualTo(oldname);
			Assertions.assertThat(newDescription).isNotEqualTo(oldDescription);
			Assertions.assertThat(newP).isNotEqualTo(oldP);
		} else {
			Assert.fail("You have selected an incorrect specialization!");
		}

	}

	@Test
	public void testUpdateBeautyCenterErrorEmptyAttribute() {
		BeautyCenter bc = this.beautyService.findById(1);
		String oldname = bc.getName();
		String oldDescription = bc.getDescription();
		PetType oldP = bc.getPetType();
		bc.setName("");
		bc.setDescription("description modified");
		PetType pet = new PetType();
		pet.setName("bird");
		bc.setPetType(pet);
		if (!bc.getName().isEmpty()) {
			this.beautyService.update(bc, bc.getId());
			String newname = bc.getName();
			String newDescription = bc.getDescription();
			PetType newP = bc.getPetType();

			Assertions.assertThat(newname).isNotEqualTo(oldname);
			Assertions.assertThat(newDescription).isNotEqualTo(oldDescription);
			Assertions.assertThat(newP).isNotEqualTo(oldP);
		} else {
			Assert.fail("Name can't be empty!");

		}

	}

	/* Pruebas de Servicio para la historia de usuario 2 */

	//Pruebas para el método nameRestriction(BeautyCenter bc)

	//Caso positivo. No lanza error debido a que el nombre del beauty center es distinto de nulo y superior o igual a 3 caracteres
	/*
	 * @Test
	 * public void testNameRestrictionCorrect() {
	 * BeautyCenter bc = new BeautyCenter();
	 * bc.setId(1);
	 * bc.setDescription("ajajaj");
	 * bc.setName("halaa");
	 * assertDoesNotThrow(() -> this.beautyService.nameRestriction(bc));
	 * }
	 * 
	 * // Caso negativo. Lanza error debido a que el nombre es inferior a 3 caracteres
	 * 
	 * @Test
	 * public void testNameRestrictionShort() {
	 * BeautyCenter bc = new BeautyCenter();
	 * bc.setId(1);
	 * bc.setDescription("ajajaj");
	 * bc.setName("ha");
	 * assertThrows(InvalidActivityException.class, () -> this.beautyService.nameRestriction(bc));
	 * }
	 * 
	 * // Caso negativo. Lanza error debido a que el nombre es nulo
	 * 
	 * @Test
	 * public void testNameRestrictionNull() {
	 * BeautyCenter bc = new BeautyCenter();
	 * bc.setId(1);
	 * bc.setDescription("ajajaj");
	 * bc.setName(null);
	 * assertThrows(InvalidActivityException.class, () -> this.beautyService.nameRestriction(bc));
	 * }
	 * 
	 * // Pruebas para el método hasMandatoryPetType(BeautyCenter bc)
	 * 
	 * //Caso positivo. Tiene un PetType
	 * 
	 * @Test
	 * public void testHasMandatoryPetTypeCorrect() {
	 * PetType pt = new PetType();
	 * pt.setId(1);
	 * 
	 * BeautyCenter bc = new BeautyCenter();
	 * bc.setPetType(pt);
	 * 
	 * assertDoesNotThrow(() -> this.beautyService.hasMandatoryPetType(bc));
	 * }
	 * 
	 * //Caso negativo. No tiene PetType
	 * 
	 * @Test
	 * public void testHasMandatoryPetTypeError() {
	 * 
	 * BeautyCenter bc = new BeautyCenter();
	 * assertThrows(InvalidActivityException.class, () -> this.beautyService.hasMandatoryPetType(bc));
	 * }
	 * 
	 */
}

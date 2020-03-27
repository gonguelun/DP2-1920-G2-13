
package org.springframework.samples.petclinic.service;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Collection;

import javax.validation.ConstraintViolationException;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Beautician;
import org.springframework.samples.petclinic.model.BeautyCenter;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.User;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class BeautyCenterServiceTests {

	@Autowired
	private BeautyCenterService	beautyService;
	
	@Autowired
	private PetService petService;


	@Test
	public void testCountWithInitialData() {
		int count = this.beautyService.beautyCount();
		Assertions.assertThat(count==5);
	}

	@Test
	  public void testUpdateBeautyCenterSuccess() {
		BeautyCenter bc=this.beautyService.findById(1);
		String oldname=bc.getName();
		String oldDescription=bc.getDescription();
		PetType oldP=bc.getPetType();
		bc.setName("name modified");
		bc.setDescription("description modified");
		PetType pet=new PetType();
		pet.setName("bird");
		bc.setPetType(pet);
		this.beautyService.update(bc, bc.getId());
		String newname=bc.getName();
		String newDescription=bc.getDescription();
		PetType newP=bc.getPetType();
		
		
		Assertions.assertThat(newname).isNotEqualTo(oldname);
		Assertions.assertThat(newDescription).isNotEqualTo(oldDescription);
		Assertions.assertThat(newP).isNotEqualTo(oldP);
		
	  
	  }
	
	@Test
	  public void testUpdateBeautyCenterErrorPetType() {
		BeautyCenter bc=this.beautyService.findById(1);
		Collection<PetType> types=this.petService.findPetTypes();
		String oldname=bc.getName();
		String oldDescription=bc.getDescription();
		PetType oldP=bc.getPetType();
		bc.setName("name modified");
		bc.setDescription("description modified");
		PetType pet=new PetType();
		pet.setName("crocodile");
		if (types.stream().anyMatch(i -> i.getName().equals(pet.getName()))) {
		bc.setPetType(pet);
		this.beautyService.update(bc, bc.getId());
		String newname=bc.getName();
		String newDescription=bc.getDescription();
		PetType newP=bc.getPetType();
		
		
		Assertions.assertThat(newname).isNotEqualTo(oldname);
		Assertions.assertThat(newDescription).isNotEqualTo(oldDescription);
		Assertions.assertThat(newP).isNotEqualTo(oldP);
		}else {
			fail("You have selected an incorrect specialization!");
		}
		
	  
	  }
	
	@Test
	  public void testUpdateBeautyCenterErrorEmptyAttribute() {
		BeautyCenter bc=this.beautyService.findById(1);
		String oldname=bc.getName();
		String oldDescription=bc.getDescription();
		PetType oldP=bc.getPetType();
		bc.setName("");
		bc.setDescription("description modified");
		PetType pet=new PetType();
		pet.setName("bird");
		bc.setPetType(pet);
	if(!bc.getName().isEmpty()) {
		this.beautyService.update(bc, bc.getId());
		String newname=bc.getName();
		String newDescription=bc.getDescription();
		PetType newP=bc.getPetType();
		
		
		Assertions.assertThat(newname).isNotEqualTo(oldname);
		Assertions.assertThat(newDescription).isNotEqualTo(oldDescription);
		Assertions.assertThat(newP).isNotEqualTo(oldP);
	}else {
		fail("Name can't be empty!");
		
	  
	  }
	
}
}


package org.springframework.samples.petclinic.service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.time.Month;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.BeautyCenter;
import org.springframework.samples.petclinic.model.BeautyDate;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedPetNameException;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class BeautyDateServiceTests {

	@Autowired
	BeautyDateService beautyDateService;
	
	@Autowired
	BeauticianService beauticianService;

	@Autowired
	PetService petService;
	
	@Autowired
	BeautyCenterService beautyService;

	@Test
	public void testSavingBeautyDateCorrect() throws DataAccessException, DuplicatedPetNameException {
		Integer countBefore = this.beautyDateService.countBeautyDates();
		countBefore++;

		BeautyDate beautyDate=new BeautyDate();
		beautyDate.setId(1);
		beautyDate.setDescription("description");
		beautyDate.setStartDate(LocalDateTime.of(2020,Month.APRIL,29,18,00));
		BeautyCenter bc=this.beautyService.findBeautyCenterByBeautyCenterId(1);
		beautyDate.setBeautyCenter(bc);
		Pet pet=this.petService.findPetById(1);
		beautyDate.setPet(pet);
		this.beautyDateService.saveBeautyDate(beautyDate);
		Integer countAfter = this.beautyDateService.countBeautyDates();
		Assertions.assertEquals(countBefore , countAfter);
	}
	
	@Test
	public void testSavingBeautyDateIncorrectEmptyDate() throws DataAccessException, DuplicatedPetNameException {

		BeautyDate beautyDate=new BeautyDate();
		beautyDate.setId(1);
		beautyDate.setDescription("description");
		assertThrows(ConstraintViolationException.class, () -> {
			beautyDate.setStartDate(null);
		});
		BeautyCenter bc=this.beautyService.findBeautyCenterByBeautyCenterId(1);
		beautyDate.setBeautyCenter(bc);
		Pet pet=this.petService.findPetById(1);
		beautyDate.setPet(pet);
		this.beautyDateService.saveBeautyDate(beautyDate);
	}
	
	@Test
	public void testSavingBeautyIncorrectDateEmptyPet() throws DataAccessException, DuplicatedPetNameException {

		BeautyDate beautyDate=new BeautyDate();
		beautyDate.setId(1);
		beautyDate.setDescription("description");
		beautyDate.setStartDate(LocalDateTime.of(2020,Month.APRIL,29,18,00));
		BeautyCenter bc=this.beautyService.findBeautyCenterByBeautyCenterId(1);
		beautyDate.setBeautyCenter(bc);
		assertThrows(ConstraintViolationException.class, () -> {
			beautyDate.setPet(null);
		});
		this.beautyDateService.saveBeautyDate(beautyDate);
	}



}
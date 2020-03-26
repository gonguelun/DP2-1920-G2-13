
package org.springframework.samples.petclinic.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class BeautyCenterServiceTests {

	@Autowired
	private BeautyCenterService	beautyService;

	@Autowired
	private BeauticianService	beauticianService;


	@Test
	public void testCountWithInitialData() {
		int count = this.beautyService.beautyCount();
		Assertions.assertEquals(count, 5);
	}

	/*
	 * Test para la historia de usuario 2
	 * 
	 * //Casos positivos
	 * 
	 * @Test
	 * public void testSaveBeautyCenter() {
	 * int count = this.beautyService.beautyCount();
	 * 
	 * PetType pt = this.beauticianService.allTypes().get(0);
	 * 
	 * Beautician beautician = new Beautician();
	 * beautician.setFirstName("Juan");
	 * beautician.setId(1);
	 * beautician.setLastName("Prueba");
	 * BeautyCenter beautyCenter = new BeautyCenter();
	 * beautyCenter.setBeautician(beautician);
	 * beautyCenter.setDescription("Test description");
	 * beautyCenter.setId(count + 1);
	 * beautyCenter.setPetType(pt);
	 * this.beautyService.save(beautyCenter);
	 * BeautyCenter beautyCenterBD = this.beautyService.findById(count + 1);
	 * Assertions.assertEquals(beautyCenter, beautyCenterBD);
	 * }
	 * 
	 * //Casos negativos
	 * 
	 * @Test
	 * public void testSaveBeautyCenterWithoutPetType() {
	 * 
	 * int count = this.beautyService.beautyCount();
	 * Beautician beautician = new Beautician();
	 * beautician.setFirstName("Juan");
	 * beautician.setId(1);
	 * beautician.setLastName("Prueba");
	 * BeautyCenter beautyCenter = new BeautyCenter();
	 * beautyCenter.setBeautician(beautician);
	 * beautyCenter.setDescription("Test description");
	 * beautyCenter.setId(count + 1);
	 * try {
	 * this.beautyService.save(beautyCenter);
	 * } catch (DuplicatedPetNameException e) {
	 * // The pet already exists!
	 * e.printStackTrace();
	 * }
	 * 
	 * Assertions.assertEquals(beautyCenter, beautyCenterBD);
	 * 
	 * }
	 * 
	 */

}

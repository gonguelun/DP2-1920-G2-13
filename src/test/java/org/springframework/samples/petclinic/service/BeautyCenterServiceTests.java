
package org.springframework.samples.petclinic.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Beautician;
import org.springframework.samples.petclinic.model.BeautyCenter;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class BeautyCenterServiceTests {

	@Autowired
	private BeautyCenterService beautyService;


	@Test
	public void testCountWithInitialData() {
		int count = this.beautyService.beautyCount();
		Assertions.assertEquals(count, 0);
	}

	@Test
	public void testSaveBeautyCenter() {
		Beautician beautician = new Beautician();
		beautician.setFirstName("g");
		beautician.setId(1);
		beautician.setLastName("g");
		BeautyCenter beautyCenter = new BeautyCenter();
		beautyCenter.setBeautician(beautician);
		beautyCenter.setDescription("a");
		beautyCenter.setId(1);
		this.beautyService.save(beautyCenter);
		BeautyCenter beautyCenterBD = this.beautyService.findById(1);
		Assertions.assertEquals(beautyCenter, beautyCenterBD);
	}

}

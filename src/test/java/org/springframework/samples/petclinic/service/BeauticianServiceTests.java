
package org.springframework.samples.petclinic.service;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Beautician;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.User;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class BeauticianServiceTests {

	@Autowired
	BeauticianService beauticianService;


	@Test
	public void testSavingBeautician() {
		Integer countBefore = this.beauticianService.countBeauticians();
		countBefore++;

		Beautician beautician = new Beautician();
		beautician.setFirstName("Jane");
		beautician.setLastName("Doe");
		// TODO: Specializations
		User user = new User();
		user.setUsername("JaneDoe");
		user.setPassword("1234");
		user.setEnabled(true);
		beautician.setUser(user);
		this.beauticianService.saveBeautician(beautician);

		Integer countAfter = this.beauticianService.countBeauticians();
		Assertions.assertEquals(countBefore, countAfter);
	}

	@Test
	public void testTypes() {
		List<PetType> res = this.beauticianService.allTypes();
		Assertions.assertEquals(6, res.size());
	}

}

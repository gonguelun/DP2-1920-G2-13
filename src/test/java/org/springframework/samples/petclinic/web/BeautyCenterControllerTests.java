
package org.springframework.samples.petclinic.web;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.BeautyCenter;
import org.springframework.samples.petclinic.service.BeautyCenterService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = BeautyCenterController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
public class BeautyCenterControllerTests {

	private static final int	TEST_BEAUTYCENTER_ID	= 1;

	private static final int	TEST_BEAUTICIAN_ID		= 1;

	@MockBean
	private BeautyCenterService	beautyService;

	@Autowired
	private MockMvc				mockMvc;


	@BeforeEach
	void setup() {
		BDDMockito.given(this.beautyService.findById(BeautyCenterControllerTests.TEST_BEAUTYCENTER_ID)).willReturn(new BeautyCenter());
	}

}

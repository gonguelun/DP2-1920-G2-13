
package org.springframework.samples.petclinic.web;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Beautician;
import org.springframework.samples.petclinic.model.BeautyCenter;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.BeauticianService;
import org.springframework.samples.petclinic.service.BeautyCenterService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = BeautyCenterController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
public class BeautyCenterControllerTests {

	private static final int		TEST_BEAUTYCENTER_ID	= 1;

	private static final int		TEST_BEAUTICIAN_ID		= 1;

	@Autowired
	private BeautyCenterController	beautyCenterController;

	@MockBean
	private BeautyCenterService		beautyService;

	@MockBean
	private BeauticianService		beauticianService;

	@MockBean
	private UserService				userService;

	@MockBean
	private AuthoritiesService		authoritiesService;

	@MockBean
	private PetService				petService;

	@Autowired
	private MockMvc					mockMvc;

	private User					user1;

	private Beautician				beautician1;

	private BeautyCenter			beautyCenter1;


	@BeforeEach
	void setup() {
		PetType cat = new PetType();
		cat.setId(1);
		cat.setName("gato");
		BDDMockito.given(this.petService.findPetTypes()).willReturn(Lists.newArrayList(cat));

		this.user1 = new User();
		this.user1.setId(1);
		this.user1.setEnabled(true);
		this.user1.setUsername("beautician1");
		this.user1.setPassword("beautician1");

		this.beautician1 = new Beautician();
		this.beautician1.setId(BeautyCenterControllerTests.TEST_BEAUTICIAN_ID);
		this.beautician1.setFirstName("z");
		this.beautician1.setLastName("z");
		this.beautician1.setUser(this.user1);

		this.beautyCenter1 = new BeautyCenter();
		this.beautyCenter1.setId(BeautyCenterControllerTests.TEST_BEAUTYCENTER_ID);
		this.beautyCenter1.setName("beautycenter1");
		this.beautyCenter1.setDescription("prueba1");
		this.beautyCenter1.setBeautician(this.beautician1);

		BDDMockito.given(this.beauticianService.findBeauticianById(BeautyCenterControllerTests.TEST_BEAUTICIAN_ID)).willReturn(this.beautician1);
		BDDMockito.given(this.beautyService.findById(BeautyCenterControllerTests.TEST_BEAUTYCENTER_ID)).willReturn(this.beautyCenter1);
	}

	@WithMockUser(username = "beautician1", roles = {
		"beautician"
	}, password = "beautician1")
	@Test
	void testProcessDeleteBeautyCenterFormSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/beauticians/{beauticianId}/beauty-centers/{beautyCenterId}/delete", BeautyCenterControllerTests.TEST_BEAUTICIAN_ID, BeautyCenterControllerTests.TEST_BEAUTYCENTER_ID)
			.with(SecurityMockMvcRequestPostProcessors.csrf())).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/beauticians/{beauticianId}"));
	}

}


package org.springframework.samples.petclinic.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.PickUpRequestService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = PickUpRequestController.class, includeFilters = @ComponentScan.Filter(value = PetTypeFormatter.class, type = FilterType.ASSIGNABLE_TYPE),
	excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
public class PickUpRequestControllerTests {

	private static final int		TEST_PICK_UP_REQUEST_ID	= 1;

	@Autowired
	private PickUpRequestController	pickUpRequestController;

	@MockBean
	private PickUpRequestService	pickUpRequestService;

	@MockBean
	private AuthoritiesService		authoritiesService;

	@MockBean
	private OwnerService			ownerService;

	@MockBean
	private PetService				petService;

	@Autowired
	private MockMvc					mockMvc;


	@BeforeEach
	void setup() {

	}

	// TESTS HISTORIA DE USUARIO 20 (Métodos en PickUpRequestController)

	/* DELETE PICK UP REQUEST /vets/pick-up-requests/{pickUpId}/delete */

	// Caso positivo: redirige a /vets/pick-up-requests

	@WithMockUser(username = "owner1", roles = {
		"owner"
	}, password = "123")
	@Test
	void testProcessDeleteBeautyCenterFormSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/vets/pick-up-requests/{pickUpId}/delete", PickUpRequestControllerTests.TEST_PICK_UP_REQUEST_ID).with(SecurityMockMvcRequestPostProcessors.csrf()))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/vets/pick-up-requests"));
	}

}

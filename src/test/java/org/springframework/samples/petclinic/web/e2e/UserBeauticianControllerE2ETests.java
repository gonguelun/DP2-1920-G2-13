
package org.springframework.samples.petclinic.web.e2e;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.web.UserController;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

/**
 * Test class for {@link UserController}
 *
 * @author Colin But
 */

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
class UserBeauticianControllerE2ETests {

	private static final int		TEST_BEAUTICIAN_ID	= 1;
	private MockMvc					mockMvc;

	@Autowired
	private WebApplicationContext	webApplicationContext;


	@BeforeEach
	public void setUp() {

		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
	}

	@WithMockUser(value = "spring")
	@Test
	void testInitCreationForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/users/new-beautician")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("beautician"))
			.andExpect(MockMvcResultMatchers.view().name("users/createBeauticianForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessCreationFormSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/users/new-beautician").param("firstName", "Michael").param("lastName", "Skere").with(SecurityMockMvcRequestPostProcessors.csrf()).param("specializations", "cat").param("user.username", "MicSker")
			.param("user.password", "1234")).andExpect(MockMvcResultMatchers.status().is3xxRedirection());

	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessCreationFormHasErrorsFirstName() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/users/new-beautician").with(SecurityMockMvcRequestPostProcessors.csrf()).param("lastName", "Skere").param("specializations", "cat").param("firstName", ""))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeHasErrors("beautician")).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("beautician", "firstName"))
			.andExpect(MockMvcResultMatchers.view().name("users/createBeauticianForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessCreationFormHasErrorsSpecializations() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/users/new-beautician").with(SecurityMockMvcRequestPostProcessors.csrf()).param("lastName", "Skere").param("firstName", "Michael").param("specializations", "reptil"))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeHasErrors("beautician")).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("beautician", "specializations"))
			.andExpect(MockMvcResultMatchers.view().name("users/createBeauticianForm"));
	}

}

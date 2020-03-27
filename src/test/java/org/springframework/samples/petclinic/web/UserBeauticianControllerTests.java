package org.springframework.samples.petclinic.web;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Beautician;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.BeauticianService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

/**
 * Test class for {@link UserController}
 *
 * @author Colin But
 */

@WebMvcTest(controllers = UserController.class, includeFilters = @ComponentScan.Filter(value = PetTypeFormatter.class, type = FilterType.ASSIGNABLE_TYPE), excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
class UserBeauticianControllerTests {

	private static final int TEST_BEAUTICIAN_ID = 1;

	@Autowired
	private UserController userController;

	@MockBean
	private UserService userService;

	@MockBean
	private OwnerService ownerService;

	@MockBean
	private VetService vetService;

	@MockBean
	private BeauticianService beauticianService;

	@MockBean
	private PetService petService;

	@MockBean
	private AuthoritiesService authoritiesService;

	@Autowired
	private MockMvc mockMvc;


	@BeforeEach
	void setup() {
		Collection<PetType> specialization = new ArrayList<>();
		PetType pet1 = new PetType();
		pet1.setName("cat");
		specialization.add(pet1);
		PetType pet2 = new PetType();
		pet2.setName("dog");
		specialization.add(pet2);
		PetType pet3 = new PetType();
		pet3.setName("bird");
		specialization.add(pet3);

		given(this.petService.findPetTypes()).willReturn(specialization);

	}

	@WithMockUser(value = "spring")
	@Test
	void testInitCreationForm() throws Exception {
		mockMvc.perform(get("/users/new-beautician")).andExpect(status().isOk())
				.andExpect(model().attributeExists("beautician")).andExpect(view().name("users/createBeauticianForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessCreationFormSuccess() throws Exception {
		mockMvc.perform(post("/users/new-beautician").param("firstName", "Michael").param("lastName", "Skere")
				.with(csrf()).param("specializations", "cat").param("user.username", "MicSker")
				.param("user.password", "1234")).andExpect(status().is3xxRedirection());

	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessCreationFormHasErrorsFirstName() throws Exception {
		mockMvc.perform(post("/users/new-beautician").with(csrf()).param("lastName", "Skere")
				.param("specializations","cat")
				.param("firstName",""))
				.andExpect(status().isOk()).andExpect(model().attributeHasErrors("beautician"))
				.andExpect(model().attributeHasFieldErrors("beautician", "firstName"))
				.andExpect(view().name("users/createBeauticianForm"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testProcessCreationFormHasErrorsSpecializations() throws Exception {
		mockMvc.perform(post("/users/new-beautician").with(csrf()).param("lastName", "Skere")
				.param("firstName","Michael")
				.param("specializations","reptil"))
				.andExpect(status().isOk()).andExpect(model().attributeHasErrors("beautician"))
				.andExpect(model().attributeHasFieldErrors("beautician", "specializations"))
				.andExpect(view().name("users/createBeauticianForm"));
	}

}

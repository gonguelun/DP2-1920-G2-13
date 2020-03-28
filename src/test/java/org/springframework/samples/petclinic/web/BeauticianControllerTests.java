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

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

/**
 * Test class for {@link UserController}
 *
 * @author Colin But
 */

@WebMvcTest(controllers=BeauticianController.class,
		includeFilters = @ComponentScan.Filter(value = PetTypeFormatter.class, type = FilterType.ASSIGNABLE_TYPE),
		excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, 
		classes = WebSecurityConfigurer.class),
		excludeAutoConfiguration= SecurityConfiguration.class)
class BeauticianControllerTests {

	private static final int TEST_BEAUTICIAN_ID = 1;

	@Autowired
	private BeauticianController beauticianController;
        
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

	private Beautician michael;

	@BeforeEach
	void setup() {

		michael = new Beautician();
		michael.setId(TEST_BEAUTICIAN_ID);
		michael.setFirstName("Michael");
		michael.setLastName("Skere");
		Collection<PetType> specialization=new ArrayList<>();
		PetType pet=new PetType();
		pet.setName("cat");
		specialization.add(pet);
		PetType pet2=new PetType();
		pet2.setName("dog");
		specialization.add(pet2);
		michael.setSpecializations(specialization);
		User user = new User();
		user.setUsername("MicSker");
		user.setPassword("1234");
		user.setEnabled(true);
		michael.setBeautyCenters(null);
		michael.setUser(user);
		given(this.beauticianService.findBeauticianById(TEST_BEAUTICIAN_ID)).willReturn(michael);
		given(this.petService.findPetTypes()).willReturn(specialization);

	}
        @WithMockUser(username = "MicSker",roles= {
        		"beautician"
        },password="1234")
	@Test
	void testInitUpdateBeauticianForm() throws Exception {
        	Collection<PetType> specialization=new ArrayList<>();
    		PetType pet=new PetType();
    		pet.setName("cat");
    		PetType pet2=new PetType();
    		pet2.setName("dog");
    		specialization.add(pet);
    		specialization.add(pet2);
		mockMvc.perform(get("/beauticians/{beauticianId}/edit", TEST_BEAUTICIAN_ID)).andExpect(status().isOk())
				.andExpect(model().attributeExists("beautician"))
				.andExpect(model().attribute("beautician", hasProperty("lastName", is("Skere"))))
				.andExpect(model().attribute("beautician", hasProperty("firstName", is("Michael"))))
				.andExpect(model().attribute("beautician", hasProperty("specializations",is(specialization))))
				.andExpect(view().name("beauticians/updateBeauticianForm"));
	}

        @WithMockUser(username = "MicSker",roles= {
        		"beautician"
        },password="1234")
	@Test
	void testProcessUpdateBeauticianFormSuccess() throws Exception {
		mockMvc.perform(post("/beauticians/{beauticianId}/edit", TEST_BEAUTICIAN_ID)
							.with(csrf())
							.param("firstName", "Michaelangelo")
							.param("lastName", "Skere")
							.param("specializations","dog")
							.param("user.username", "MicSker")
							.param("user.password", "1234"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/beauticians/{beauticianId}"));
	}

        @WithMockUser(username = "MicSker",roles= {
        		"beautician"
        },password="1234")
	@Test
	void testProcessUpdateBeauticianFormHasErrorsFirstName() throws Exception {
		mockMvc.perform(post("/beauticians/{beauticianId}/edit", TEST_BEAUTICIAN_ID)
							.with(csrf())
							.param("lastName", "Skere")
							.param("specializations","cat"))
				.andExpect(status().isOk())
				.andExpect(model().attributeHasErrors("beautician"))
				.andExpect(model().attributeHasFieldErrors("beautician", "firstName"))
				.andExpect(view().name("beauticians/updateBeauticianForm"));
	}
        @WithMockUser(username = "MicSker",roles= {
        		"beautician"
        },password="1234")
        @Test
    	void testProcessUpdateBeauticianFormHasErrorsSpecialization() throws Exception {
    		mockMvc.perform(post("/beauticians/{beauticianId}/edit", TEST_BEAUTICIAN_ID)
    							.with(csrf())
    							.param("firstName", "Michael")
    							.param("lastName", "Skere")
    							.param("specializations","reptil"))
    				.andExpect(status().isOk())
    				.andExpect(model().attributeHasErrors("beautician"))
    				.andExpect(model().attributeHasFieldErrors("beautician", "specializations"))
    				.andExpect(view().name("beauticians/updateBeauticianForm"));
    	}

        @WithMockUser(username = "MicSker",roles= {
        		"beautician"
        },password="1234")
	@Test
	void testShowBeautician() throws Exception {
        	Collection<PetType> specialization=new ArrayList<>();
    		PetType pet=new PetType();
    		pet.setName("cat");
    		PetType pet2=new PetType();
    		pet2.setName("dog");
    		specialization.add(pet);
    		specialization.add(pet2);
		mockMvc.perform(get("/beauticians/{beauticianId}", TEST_BEAUTICIAN_ID)).andExpect(status().isOk())
				.andExpect(model().attribute("beautician", hasProperty("firstName", is("Michael"))))
				.andExpect(model().attribute("beautician", hasProperty("lastName", is("Skere"))))
				.andExpect(model().attribute("beautician", hasProperty("specializations", is(specialization))))
				.andExpect(view().name("beauticians/beauticianDetails"));
	}

}

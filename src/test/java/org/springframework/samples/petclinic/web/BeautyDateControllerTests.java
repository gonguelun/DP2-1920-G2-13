
package org.springframework.samples.petclinic.web;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.BeauticianService;
import org.springframework.samples.petclinic.service.BeautyCenterService;
import org.springframework.samples.petclinic.service.BeautyDateService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = BeautyCenterController.class, includeFilters = @ComponentScan.Filter(value = PetTypeFormatter.class, type = FilterType.ASSIGNABLE_TYPE),
	excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
public class BeautyDateControllerTests {

	private static final int		TEST_BEAUTYCENTER_ID	= 1;

	private static final int		TEST_PETTYPE_ID		= 1;
	
	private static final String		TEST_OWNER_USERNAME		= "MicSker";

	@Autowired
	private BeautyCenterController	beautyCenterController;

	@MockBean
	private BeautyCenterService		beautyService;

	@MockBean
	private BeauticianService		beauticianService;
	
	@MockBean
	private BeautyDateService beautyDateService;

	@MockBean
	private PetService				petService;

	@MockBean
	private UserService				userService;

	@MockBean
	private AuthoritiesService		authoritiesService;

	@Autowired
	private MockMvc					mockMvc;

	private User					user2;

	private Owner					owner;

	private BeautyCenter			beautyCenter;



	@BeforeEach
	void setup() {
		PetType cat = new PetType();
		cat.setId(TEST_PETTYPE_ID);
		cat.setName("cat");
		List<PetType> temp = new ArrayList<>();
		temp.add(cat);

		User user = new User();
		user.setId(1);
		user.setEnabled(true);
		user.setUsername("beautician1");
		user.setPassword("123");

		Beautician beautician = new Beautician();
		beautician.setId(1);
		beautician.setFirstName("juan");
		beautician.setLastName("aurora");
		beautician.setSpecializations(temp);
		beautician.setUser(user);

		BeautyCenter beautyCenter = new BeautyCenter();
		beautyCenter.setId(TEST_BEAUTYCENTER_ID);
		beautyCenter.setName("beautycenter1");
		beautyCenter.setDescription("prueba1");
		beautyCenter.setPetType(cat);
		beautyCenter.setBeautician(beautician);
		
		Owner owner=new Owner();
		owner.setId(1);
		owner.setFirstName("Michael");
		owner.setLastName("Skere");
		owner.setCity("Badajoz");
		owner.setAddress("Calle 1");
		owner.setTelephone("666777888");
		User user2= new User();
		user2.setId(2);
		user2.setEnabled(true);
		user2.setUsername(TEST_OWNER_USERNAME);
		user2.setPassword("123");
		owner.setUser(user2);
		
		
		BDDMockito.given(this.beautyService.findById(TEST_BEAUTYCENTER_ID)).willReturn(beautyCenter);
		BDDMockito.given(this.beautyDateService.findPetsByOwnerAndType(TEST_OWNER_USERNAME, TEST_PETTYPE_ID));
	}

	@WithMockUser(username = TEST_OWNER_USERNAME, roles = {
		"owner"
	}, password = "123")
	@Test
	void testInitCreationForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners/{ownerUsername}/beauty-centers/{beautyCenterId}/{petTypeId}/beauty-dates/new",
				TEST_OWNER_USERNAME,TEST_BEAUTYCENTER_ID,TEST_PETTYPE_ID)).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("beauty-dates/createOrUpdateBeautyDateForm"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("beautyDate"));
	}
	
//	@WithMockUser(username = TEST_OWNER_USERNAME, roles = {
//			"owner"
//		}, password = "123")
//	@Test
//	void testProcessCreateBeautyDateFormSuccess() throws Exception {
//		mockMvc.perform(post("/owners/{ownerUsername}/beauty-centers/{beautyCenterId}/{petTypeId}/beauty-dates/new",TEST_OWNER_USERNAME,TEST_BEAUTYCENTER_ID,TEST_PETTYPE_ID )
//							.with(csrf())
//							.param("description","descripcion")
//							.param("startDate", "2020/04/29 16:00")
//							.param("pet","avelino"))
//				.andExpect(status().is3xxRedirection())
//				.andExpect(view().name("redirect:/"));
//	}



}

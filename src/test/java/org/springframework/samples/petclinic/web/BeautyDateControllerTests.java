
package org.springframework.samples.petclinic.web;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
import org.springframework.samples.petclinic.model.BeautyDate;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
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

@WebMvcTest(controllers = BeautyDateController.class, includeFilters = @ComponentScan.Filter(value = PetFormatter.class, type = FilterType.ASSIGNABLE_TYPE),
	excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
public class BeautyDateControllerTests {

	private static final int		TEST_BEAUTYCENTER_ID	= 1;

	private static final int		TEST_PETTYPE_ID			= 1;

	private static final String		TEST_OWNER_USERNAME		= "MicSker";

	@Autowired
	private BeautyDateController	beautyDateController;

	@MockBean
	private BeautyCenterService		beautyService;

	@MockBean
	private BeauticianService		beauticianService;

	@MockBean
	private BeautyDateService		beautyDateService;

	@MockBean
	private PetService				petService;

	@MockBean
	private UserService				userService;

	@MockBean
	private AuthoritiesService		authoritiesService;

	@Autowired
	private MockMvc					mockMvc;


	@BeforeEach
	void setup() {
		PetType cat = new PetType();
		cat.setId(BeautyDateControllerTests.TEST_PETTYPE_ID);
		cat.setName("cat");
		List<PetType> temp = new ArrayList<>();
		temp.add(cat);
		Collection<Pet> temp2 = new ArrayList<>();
		Pet pet = new Pet();
		pet.setId(1);
		pet.setName("currupipi");
		pet.setBirthDate(LocalDate.of(2020, Month.JANUARY, 5));
		pet.setType(cat);
		temp2.add(pet);

		Pet peto = new Pet();
		peto.setId(2);
		peto.setBirthDate(LocalDate.of(2020, Month.JANUARY, 5));
		peto.setType(cat);
		peto.setName("peto");
		temp2.add(pet);

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
		beautyCenter.setId(BeautyDateControllerTests.TEST_BEAUTYCENTER_ID);
		beautyCenter.setName("beautycenter1");
		beautyCenter.setDescription("prueba1");
		beautyCenter.setPetType(cat);
		beautyCenter.setBeautician(beautician);

		Owner owner = new Owner();
		owner.setId(1);
		owner.setFirstName("Michael");
		owner.setLastName("Skere");
		owner.setCity("Badajoz");
		owner.setAddress("Calle 1");
		owner.setTelephone("666777888");
		User user2 = new User();
		user2.setId(2);
		user2.setEnabled(true);
		user2.setUsername(BeautyDateControllerTests.TEST_OWNER_USERNAME);
		user2.setPassword("123");
		owner.setUser(user2);

		BeautyDate bc = new BeautyDate();
		bc.setId(1);
		bc.setDescription("jjjajaja");
		bc.setBeautyCenter(beautyCenter);
		bc.setPet(peto);

		bc.setStartDate(LocalDateTime.of(LocalDate.of(2020, 3, 31), LocalTime.of(16, 0)));

		BDDMockito.given(this.beautyService.findById(BeautyDateControllerTests.TEST_BEAUTYCENTER_ID)).willReturn(beautyCenter);
		BDDMockito.given(this.beautyDateService.findPetsByOwnerAndType(BeautyDateControllerTests.TEST_OWNER_USERNAME, BeautyDateControllerTests.TEST_PETTYPE_ID)).willReturn(temp2);
		BDDMockito.given(this.petService.findPetById(pet.getId())).willReturn(pet);
		BDDMockito.given(this.beautyDateService.findBeautyDateByPetId(peto.getId())).willReturn(bc);
	}

	//Tests historia de usuario 11
	//Casos positivos
	@WithMockUser(username = BeautyDateControllerTests.TEST_OWNER_USERNAME, roles = {
		"owner"
	}, password = "123")
	@Test
	void testInitCreationForm() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.get("/owners/{ownerUsername}/beauty-centers/{beautyCenterId}/{petTypeId}/beauty-dates/new", BeautyDateControllerTests.TEST_OWNER_USERNAME, BeautyDateControllerTests.TEST_BEAUTYCENTER_ID,
				BeautyDateControllerTests.TEST_PETTYPE_ID))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("beauty-dates/createOrUpdateBeautyDateForm")).andExpect(MockMvcResultMatchers.model().attributeExists("beautyDate"));
	}

	@WithMockUser(username = BeautyDateControllerTests.TEST_OWNER_USERNAME, roles = {
		"owner"
	}, password = "123")
	@Test
	void testProcessCreateBeautyDateFormSuccess() throws Exception {

		this.mockMvc
			.perform(MockMvcRequestBuilders
				.post("/owners/{ownerUsername}/beauty-centers/{beautyCenterId}/{petTypeId}/beauty-dates/new", BeautyDateControllerTests.TEST_OWNER_USERNAME, BeautyDateControllerTests.TEST_BEAUTYCENTER_ID, BeautyDateControllerTests.TEST_PETTYPE_ID)
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("description", "descripcion").param("startDate", "2020/04/29 16:00").param("pet", "currupipi - 1"))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/"));
	}
	//Casos negativos
	@WithMockUser(username = BeautyDateControllerTests.TEST_OWNER_USERNAME, roles = {
		"owner"
	}, password = "123")
	@Test
	void testProcessCreateBeautyDateFormErrorEmptyDate() throws Exception {

		this.mockMvc
			.perform(MockMvcRequestBuilders
				.post("/owners/{ownerUsername}/beauty-centers/{beautyCenterId}/{petTypeId}/beauty-dates/new", BeautyDateControllerTests.TEST_OWNER_USERNAME, BeautyDateControllerTests.TEST_BEAUTYCENTER_ID, BeautyDateControllerTests.TEST_PETTYPE_ID)
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("description", "descripcion").param("startDate", "").param("pet", "currupipi - 1"))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeHasErrors("beautyDate")).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("beautyDate", "startDate"))
			.andExpect(MockMvcResultMatchers.view().name("beauty-dates/createOrUpdateBeautyDateForm"));
	}

	@WithMockUser(username = BeautyDateControllerTests.TEST_OWNER_USERNAME, roles = {
		"owner"
	}, password = "123")
	@Test
	void testProcessCreateBeautyDateFormErrorEmptyPet() throws Exception {

		this.mockMvc
			.perform(MockMvcRequestBuilders
				.post("/owners/{ownerUsername}/beauty-centers/{beautyCenterId}/{petTypeId}/beauty-dates/new", BeautyDateControllerTests.TEST_OWNER_USERNAME, BeautyDateControllerTests.TEST_BEAUTYCENTER_ID, BeautyDateControllerTests.TEST_PETTYPE_ID)
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("description", "descripcion").param("startDate", "2020/04/29 16:00").param("pet", ""))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	/* Pruebas para la historia de usuario 8 */

	/* PostMapping "/owners/{ownerUsername}/beauty-centers/{beautyCenterId}/{petTypeId}/beauty-dates/new" */

	//Caso positivo. Introduzco una fecha en el horario de 16:00 a 20:00 un martes

	@WithMockUser(username = BeautyDateControllerTests.TEST_OWNER_USERNAME, roles = {
		"owner"
	}, password = "123")
	@Test
	void testProcessCreateBeautyDateFormSuccessCorrectTime() throws Exception {

		this.mockMvc
			.perform(MockMvcRequestBuilders
				.post("/owners/{ownerUsername}/beauty-centers/{beautyCenterId}/{petTypeId}/beauty-dates/new", BeautyDateControllerTests.TEST_OWNER_USERNAME, BeautyDateControllerTests.TEST_BEAUTYCENTER_ID, BeautyDateControllerTests.TEST_PETTYPE_ID)
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("description", "descripcion").param("startDate", "2020/04/14 17:00").param("pet", "currupipi - 1"))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/"));
	}

	//Caso Negativo. Introduzo un Domingo

	@WithMockUser(username = BeautyDateControllerTests.TEST_OWNER_USERNAME, roles = {
		"owner"
	}, password = "123")
	@Test
	void testProcessCreateBeautyDateFormErrorWeekendDate() throws Exception {

		this.mockMvc
			.perform(MockMvcRequestBuilders
				.post("/owners/{ownerUsername}/beauty-centers/{beautyCenterId}/{petTypeId}/beauty-dates/new", BeautyDateControllerTests.TEST_OWNER_USERNAME, BeautyDateControllerTests.TEST_BEAUTYCENTER_ID, BeautyDateControllerTests.TEST_PETTYPE_ID)
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("description", "descripcion").param("startDate", "2020/03/28 16:00").param("pet", "currupipi - 1"))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeHasErrors("beautyDate")).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("beautyDate", "startDate"))
			.andExpect(MockMvcResultMatchers.view().name("beauty-dates/createOrUpdateBeautyDateForm"));
	}

	//Caso Negativo. Introduzco una pet que ya tiene una cita
	@WithMockUser(username = BeautyDateControllerTests.TEST_OWNER_USERNAME, roles = {
		"owner"
	}, password = "123")
	@Test
	void testProcessCreateBeautyDateFormErrorPetAlreadyHasDate() throws Exception {

		this.mockMvc
			.perform(MockMvcRequestBuilders
				.post("/owners/{ownerUsername}/beauty-centers/{beautyCenterId}/{petTypeId}/beauty-dates/new", BeautyDateControllerTests.TEST_OWNER_USERNAME, BeautyDateControllerTests.TEST_BEAUTYCENTER_ID, BeautyDateControllerTests.TEST_PETTYPE_ID)
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("description", "descripcion").param("startDate", "2020/03/31 16:00").param("pet", "peto - 2"))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeHasErrors("beautyDate")).andExpect(MockMvcResultMatchers.view().name("beauty-dates/createOrUpdateBeautyDateForm"));
	}
}
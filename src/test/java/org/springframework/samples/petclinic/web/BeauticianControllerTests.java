
package org.springframework.samples.petclinic.web;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.activity.InvalidActivityException;

import org.assertj.core.util.Lists;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Beautician;
import org.springframework.samples.petclinic.model.BeautyDate;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.BeauticianService;
import org.springframework.samples.petclinic.service.BeautyDateService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.samples.petclinic.service.exceptions.PastDateException;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 * Test class for {@link UserController}
 *
 * @author Colin But
 */

@WebMvcTest(controllers = BeauticianController.class, includeFilters = @ComponentScan.Filter(value = PetTypeFormatter.class, type = FilterType.ASSIGNABLE_TYPE),
	excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
class BeauticianControllerTests {

	private static final int		TEST_BEAUTICIAN_ID	= 1;

	private static final int		TEST_BEAUTICIAN_ID2	= 2;

	@Autowired
	private BeauticianController	beauticianController;

	@MockBean
	private UserService				userService;

	@MockBean
	private OwnerService			ownerService;

	@MockBean
	private VetService				vetService;

	@MockBean
	private BeauticianService		beauticianService;

	@MockBean
	private BeautyDateService		beautyDateService;

	@MockBean
	private PetService				petService;

	@MockBean
	private AuthoritiesService		authoritiesService;

	@Autowired
	private MockMvc					mockMvc;

	private Beautician				michael;

	private Beautician				lola;


	@BeforeEach
	void setup() {

		this.michael = new Beautician();
		this.michael.setId(BeauticianControllerTests.TEST_BEAUTICIAN_ID);
		this.michael.setFirstName("Michael");
		this.michael.setLastName("Skere");
		Collection<PetType> specialization = new ArrayList<>();
		PetType pet = new PetType();
		pet.setName("cat");
		specialization.add(pet);
		PetType pet2 = new PetType();
		pet2.setName("dog");
		specialization.add(pet2);
		this.michael.setSpecializations(specialization);
		User user = new User();
		user.setId(3);
		user.setUsername("MicSker");
		user.setPassword("1234");
		user.setEnabled(true);
		this.michael.setBeautyCenters(null);
		this.michael.setUser(user);

		this.lola = new Beautician();
		this.lola.setId(BeauticianControllerTests.TEST_BEAUTICIAN_ID2);
		this.lola.setFirstName("Lola");
		this.lola.setLastName("Indi");
		Collection<PetType> specializationLola = new ArrayList<>();
		PetType pet1 = new PetType();
		pet1.setName("bird");
		specializationLola.add(pet1);
		PetType pet3 = new PetType();
		pet3.setName("lizard");
		specializationLola.add(pet3);
		this.lola.setSpecializations(specializationLola);
		User userLola = new User();
		userLola.setId(4);
		userLola.setUsername("LolIn");
		userLola.setPassword("12345");
		userLola.setEnabled(true);
		this.lola.setBeautyCenters(null);
		this.lola.setUser(userLola);

		LocalDateTime dateHourMax = LocalDateTime.now().plusDays(10).withHour(12).withMinute(0).withSecond(0).withNano(0);
		BeautyDate date1 = new BeautyDate();
		BeautyDate date2 = new BeautyDate();
		List<BeautyDate> beautyDatesByBeauticianAndDate = Lists.list(date1, date2);

		BDDMockito.given(this.beauticianService.findBeauticianById(BeauticianControllerTests.TEST_BEAUTICIAN_ID)).willReturn(this.michael);
		BDDMockito.given(this.beauticianService.findBeauticianById(BeauticianControllerTests.TEST_BEAUTICIAN_ID2)).willReturn(this.lola);
		BDDMockito.given(this.petService.findPetTypes()).willReturn(specialization);
		BDDMockito.given(this.beauticianService.findBeauticianByUsername(this.michael.getUser().getUsername())).willReturn(this.michael);
		BDDMockito.given(this.beautyDateService.findBeautyDatesByBeauticianIdAndDate(BeauticianControllerTests.TEST_BEAUTICIAN_ID, dateHourMax)).willReturn(beautyDatesByBeauticianAndDate);

	}

	@WithMockUser(username = "MicSker", roles = {
		"beautician"
	}, password = "1234")
	@Test
	void testInitUpdateBeauticianForm() throws Exception {
		Collection<PetType> specialization = new ArrayList<>();
		PetType pet = new PetType();
		pet.setName("cat");
		PetType pet2 = new PetType();
		pet2.setName("dog");
		specialization.add(pet);
		specialization.add(pet2);
		this.mockMvc.perform(MockMvcRequestBuilders.get("/beauticians/{beauticianId}/edit", BeauticianControllerTests.TEST_BEAUTICIAN_ID)).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeExists("beautician")).andExpect(MockMvcResultMatchers.model().attribute("beautician", Matchers.hasProperty("lastName", Matchers.is("Skere"))))
			.andExpect(MockMvcResultMatchers.model().attribute("beautician", Matchers.hasProperty("firstName", Matchers.is("Michael"))))
			.andExpect(MockMvcResultMatchers.model().attribute("beautician", Matchers.hasProperty("specializations", Matchers.is(specialization)))).andExpect(MockMvcResultMatchers.view().name("beauticians/updateBeauticianForm"));
	}

	@WithMockUser(username = "MicSker", roles = {
		"beautician"
	}, password = "1234")
	@Test
	void testProcessUpdateBeauticianFormSuccess() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/beauticians/{beauticianId}/edit", BeauticianControllerTests.TEST_BEAUTICIAN_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("firstName", "Michaelangelo").param("lastName", "Skere")
				.param("specializations", "dog").param("user.username", "MicSker").param("user.password", "1234"))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/beauticians/{beauticianId}"));
	}

	@WithMockUser(username = "MicSker", roles = {
		"beautician"
	}, password = "1234")
	@Test
	void testProcessUpdateBeauticianFormHasErrorsFirstName() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/beauticians/{beauticianId}/edit", BeauticianControllerTests.TEST_BEAUTICIAN_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("lastName", "Skere").param("specializations", "cat"))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeHasErrors("beautician")).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("beautician", "firstName"))
			.andExpect(MockMvcResultMatchers.view().name("beauticians/updateBeauticianForm"));
	}

	@WithMockUser(username = "MicSker", roles = {
		"beautician"
	}, password = "1234")
	@Test
	void testProcessUpdateBeauticianFormHasErrorsSpecialization() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/beauticians/{beauticianId}/edit", BeauticianControllerTests.TEST_BEAUTICIAN_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("firstName", "Michael").param("lastName", "Skere")
				.param("specializations", "reptil"))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeHasErrors("beautician")).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("beautician", "specializations"))
			.andExpect(MockMvcResultMatchers.view().name("beauticians/updateBeauticianForm"));
	}

	@WithMockUser(username = "MicSker", roles = {
		"beautician"
	}, password = "1234")
	@Test
	void testShowBeautician() throws Exception {
		Collection<PetType> specialization = new ArrayList<>();
		PetType pet = new PetType();
		pet.setName("cat");
		PetType pet2 = new PetType();
		pet2.setName("dog");
		specialization.add(pet);
		specialization.add(pet2);
		this.mockMvc.perform(MockMvcRequestBuilders.get("/beauticians/{beauticianId}", BeauticianControllerTests.TEST_BEAUTICIAN_ID)).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attribute("beautician", Matchers.hasProperty("firstName", Matchers.is("Michael"))))
			.andExpect(MockMvcResultMatchers.model().attribute("beautician", Matchers.hasProperty("lastName", Matchers.is("Skere"))))
			.andExpect(MockMvcResultMatchers.model().attribute("beautician", Matchers.hasProperty("specializations", Matchers.is(specialization)))).andExpect(MockMvcResultMatchers.view().name("beauticians/beauticianDetails"));
	}

	@WithMockUser(username = "LolIn", roles = {
		"beautician"
	}, password = "1234")
	@Test
	void testShowBeauticianIncorrectLogIn() throws Exception {
		Mockito.when(this.authoritiesService.isAuthor(ArgumentMatchers.any())).thenThrow(new InvalidActivityException());
		this.mockMvc.perform(MockMvcRequestBuilders.get("/beauticians/{beauticianId}", BeauticianControllerTests.TEST_BEAUTICIAN_ID).with(SecurityMockMvcRequestPostProcessors.csrf())).andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	// TESTS HISTORIA DE USUARIO 9 (Métodos en BeauticianController)

	/* SEARCH BEAUTY DATES /beauticians/searchBeautyDates/{beauticianUsername} */

	// Caso positivo: redirige a /beauticians/searchDates/1

	@WithMockUser(username = "MicSker", roles = {
		"beautician"
	}, password = "1234")
	@Test
	void testSearchBeautyDates() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/beauticians/searchBeautyDates/{beauticianUsername}", "MicSker")).andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/beauticians/searchDates/1"));
	}

	/* SEARCH BEAUTY DATES ID /beauticians/searchDates/{beauticianId} */

	// Caso positivo: Devuelve la vista "beauticians/searchBeautyDates" y existe "beautician" en el modelo (la clave "beautician" se genera automaticamente, mirar addAttribute de ModelMap.class)

	@WithMockUser(username = "MicSker", roles = {
		"beautician"
	}, password = "1234")
	@Test
	void testSearchBeautyDatesId() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/beauticians/searchDates/{beauticianId}", BeauticianControllerTests.TEST_BEAUTICIAN_ID)).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("beauticians/searchBeautyDates")).andExpect(MockMvcResultMatchers.model().attributeExists("beautician"));
	}

	// Caso negativo: El usuario no tiene permiso para acceder y se le redirige a la página de excepción (COMPROBAMOS QUE NOS REDIRIGE BIEN SI SALTA EXCEPCION DE isAuthor)
	@WithMockUser(username = "MicSker", roles = {
		"beautician"
	}, password = "1234")
	@Test
	void testSearchBeautyDatesIdNoPermission() throws Exception {
		Mockito.when(this.authoritiesService.isAuthor(ArgumentMatchers.any())).thenThrow(new InvalidActivityException());
		this.mockMvc.perform(MockMvcRequestBuilders.get("/beauticians/searchDates/{beauticianId}", BeauticianControllerTests.TEST_BEAUTICIAN_ID).with(SecurityMockMvcRequestPostProcessors.csrf())).andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	/* SHOW BEAUTY DATES /beauticians/{beauticianId}/beautyDates/{date}/{hour} */

	// Caso positivo: Devuelve la vista "beauticians/beautyDatesList" y existe "beautyDates" en el modelo
	@WithMockUser(username = "MicSker", roles = {
		"beautician"
	}, password = "1234")
	@Test
	void testShowBeautyDates() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/beauticians/{beauticianId}/beautyDates/{date}/{hour}", BeauticianControllerTests.TEST_BEAUTICIAN_ID, LocalDate.now().plusDays(10).toString(), 12)).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("beauticians/beautyDatesList")).andExpect(MockMvcResultMatchers.model().attributeExists("beautyDates"));
	}

	// Caso negativo: El usuario no tiene permiso para acceder y se le redirige a la página de excepción
	@WithMockUser(username = "MicSker", roles = {
		"beautician"
	}, password = "1234")
	@Test
	void testShowBeautyDatesNoPermission() throws Exception {
		Mockito.when(this.authoritiesService.isAuthor(ArgumentMatchers.any())).thenThrow(new InvalidActivityException());
		this.mockMvc.perform(MockMvcRequestBuilders.get("/beauticians/{beauticianId}/beautyDates/{date}/{hour}", BeauticianControllerTests.TEST_BEAUTICIAN_ID, LocalDate.now().plusDays(10).toString(), 12).with(SecurityMockMvcRequestPostProcessors.csrf()))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
	}

	// Caso negativo: la fecha introducida es una fecha pasada
	@WithMockUser(username = "MicSker", roles = {
		"beautician"
	}, password = "1234")
	@Test
	void testShowBeautyDatesWrongDate() throws Exception {
		Mockito.when(this.beautyDateService.isDateValid(ArgumentMatchers.any())).thenThrow(new PastDateException());
		this.mockMvc.perform(MockMvcRequestBuilders.get("/beauticians/{beauticianId}/beautyDates/{date}/{hour}", BeauticianControllerTests.TEST_BEAUTICIAN_ID, LocalDate.now().minusDays(10).toString(), 12).with(SecurityMockMvcRequestPostProcessors.csrf()))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
	}
}

package org.springframework.samples.petclinic.web.e2e;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.activity.InvalidActivityException;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
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
import org.springframework.samples.petclinic.service.exceptions.AlreadyDateException;
import org.springframework.samples.petclinic.service.exceptions.EmptyPetException;
import org.springframework.samples.petclinic.service.exceptions.IsNotInTimeException;
import org.springframework.samples.petclinic.service.exceptions.IsWeekendException;
import org.springframework.samples.petclinic.web.BeautyDateController;
import org.springframework.samples.petclinic.web.BeautyDateControllerTests;
import org.springframework.samples.petclinic.web.PetFormatter;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
public class BeautyDateControllerE2ETests {

	private static final int		TEST_BEAUTYCENTER_ID	= 1;

	private static final int		TEST_PETTYPE_ID			= 1;
	
	private static final int TEST_BEAUTYDATE_ID=1;
	
	private static final int TEST_BEAUTICIAN_ID=1;

	private static final String		TEST_OWNER_USERNAME		= "owner1";

	private MockMvc					mockMvc;
	

	@Autowired
	private WebApplicationContext	webApplicationContext;


	@BeforeEach
	public void setUp() {

		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
	}


	//Tests historia de usuario 11
	//Casos positivos
	@WithMockUser(username = "owner1", roles = {
			"owner"
		}, password = "0wn3r")
	@Test
	void testInitCreationForm() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.get("/owners/{ownerUsername}/beauty-centers/{beautyCenterId}/{petTypeId}/beauty-dates/new",
					BeautyDateControllerE2ETests.TEST_OWNER_USERNAME, 
					BeautyDateControllerE2ETests.TEST_BEAUTYCENTER_ID,
				BeautyDateControllerE2ETests.TEST_PETTYPE_ID))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("beauty-dates/createOrUpdateBeautyDateForm")).andExpect(MockMvcResultMatchers.model().attributeExists("beautyDate"));
	}

	@WithMockUser(username = BeautyDateControllerE2ETests.TEST_OWNER_USERNAME, roles = {
		"owner"
	}, password = "0wn3r")
	@Test
	void testProcessCreateBeautyDateFormSuccess() throws Exception {

		this.mockMvc
			.perform(MockMvcRequestBuilders
				.post("/owners/{ownerUsername}/beauty-centers/{beautyCenterId}/{petTypeId}/beauty-dates/new", BeautyDateControllerE2ETests.TEST_OWNER_USERNAME, BeautyDateControllerE2ETests.TEST_BEAUTYCENTER_ID, BeautyDateControllerE2ETests.TEST_PETTYPE_ID)
				.with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("description", "descripcion")
				.param("startDate", "2020/04/29 16:00")
				.param("pet", "currupipi - 1"))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/"));
	}
	//Casos negativos
	@WithMockUser(username = BeautyDateControllerE2ETests.TEST_OWNER_USERNAME, roles = {
		"owner"
	}, password = "0wn3r")
	@Test
	void testProcessCreateBeautyDateFormErrorEmptyDate() throws Exception {

		this.mockMvc
			.perform(MockMvcRequestBuilders
				.post("/owners/{ownerUsername}/beauty-centers/{beautyCenterId}/{petTypeId}/beauty-dates/new", BeautyDateControllerE2ETests.TEST_OWNER_USERNAME, BeautyDateControllerE2ETests.TEST_BEAUTYCENTER_ID, BeautyDateControllerE2ETests.TEST_PETTYPE_ID)
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("description", "descripcion").param("startDate", "").param("pet", "currupipi - 1"))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeHasErrors("beautyDate")).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("beautyDate", "startDate"))
			.andExpect(MockMvcResultMatchers.view().name("beauty-dates/createOrUpdateBeautyDateForm"));
	}

	@WithMockUser(username = BeautyDateControllerE2ETests.TEST_OWNER_USERNAME, roles = {
		"owner"
	}, password = "0wn3r")
	@Test
	void testProcessCreateBeautyDateFormErrorEmptyPet() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders
				.post("/owners/{ownerUsername}/beauty-centers/{beautyCenterId}/{petTypeId}/beauty-dates/new", BeautyDateControllerE2ETests.TEST_OWNER_USERNAME, BeautyDateControllerE2ETests.TEST_BEAUTYCENTER_ID, BeautyDateControllerE2ETests.TEST_PETTYPE_ID)
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("description", "descripcion").param("startDate", "2020/04/29 16:00").param("pet", ""))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeHasErrors("beautyDate")).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("beautyDate", "pet"))
			.andExpect(MockMvcResultMatchers.view().name("beauty-dates/createOrUpdateBeautyDateForm"));
	}

	/* Pruebas para la historia de usuario 8 */

	/* PostMapping "/owners/{ownerUsername}/beauty-centers/{beautyCenterId}/{petTypeId}/beauty-dates/new" */

	//Caso positivo. Introduzco una fecha en el horario de 16:00 a 20:00 un martes

	@WithMockUser(username = BeautyDateControllerE2ETests.TEST_OWNER_USERNAME, roles = {
		"owner"
	}, password = "0wn3r")
	@Test
	void testProcessCreateBeautyDateFormSuccessCorrectTime() throws Exception {

		this.mockMvc
			.perform(MockMvcRequestBuilders
				.post("/owners/{ownerUsername}/beauty-centers/{beautyCenterId}/{petTypeId}/beauty-dates/new", BeautyDateControllerE2ETests.TEST_OWNER_USERNAME, BeautyDateControllerE2ETests.TEST_BEAUTYCENTER_ID, BeautyDateControllerE2ETests.TEST_PETTYPE_ID)
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("description", "descripcion").param("startDate", "2020/04/14 17:00").param("pet", "currupipi - 1"))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/"));
	}

	//Caso Negativo. Introduzo un Domingo

	@WithMockUser(username = BeautyDateControllerE2ETests.TEST_OWNER_USERNAME, roles = {
		"0wn3r"
	}, password = "123")
	@Test
	void testProcessCreateBeautyDateFormErrorWeekendDate() throws Exception {
	
		this.mockMvc
			.perform(MockMvcRequestBuilders
				.post("/owners/{ownerUsername}/beauty-centers/{beautyCenterId}/{petTypeId}/beauty-dates/new", BeautyDateControllerE2ETests.TEST_OWNER_USERNAME, BeautyDateControllerE2ETests.TEST_BEAUTYCENTER_ID, BeautyDateControllerE2ETests.TEST_PETTYPE_ID)
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("description", "descripcion").param("startDate", "2020/03/28 16:00").param("pet", "currupipi - 1"))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeHasErrors("beautyDate")).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("beautyDate", "startDate"))
			.andExpect(MockMvcResultMatchers.view().name("beauty-dates/createOrUpdateBeautyDateForm"));
	}

	//Caso Negativo. Introduzo un día a las 12:00

	@WithMockUser(username = BeautyDateControllerE2ETests.TEST_OWNER_USERNAME, roles = {
		"0wn3r"
	}, password = "123")
	@Test
	void testProcessCreateBeautyDateFormErrorTimeDate() throws Exception {
		
		this.mockMvc
			.perform(MockMvcRequestBuilders
				.post("/owners/{ownerUsername}/beauty-centers/{beautyCenterId}/{petTypeId}/beauty-dates/new", BeautyDateControllerE2ETests.TEST_OWNER_USERNAME, BeautyDateControllerE2ETests.TEST_BEAUTYCENTER_ID, BeautyDateControllerE2ETests.TEST_PETTYPE_ID)
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("description", "descripcion").param("startDate", "2020/03/28 12:00").param("pet", "currupipi - 1"))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeHasErrors("beautyDate")).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("beautyDate", "startDate"))
			.andExpect(MockMvcResultMatchers.view().name("beauty-dates/createOrUpdateBeautyDateForm"));
	}

	//Caso Negativo. Introduzco una pet que ya tiene una cita
	@WithMockUser(username = BeautyDateControllerE2ETests.TEST_OWNER_USERNAME, roles = {
		"0wn3r"
	}, password = "123")
	@Test
	void testProcessCreateBeautyDateFormErrorPetAlreadyHasDate() throws Exception {
	
		this.mockMvc
			.perform(MockMvcRequestBuilders
				.post("/owners/{ownerUsername}/beauty-centers/{beautyCenterId}/{petTypeId}/beauty-dates/new",
						BeautyDateControllerE2ETests.TEST_OWNER_USERNAME, 
						BeautyDateControllerE2ETests.TEST_BEAUTYCENTER_ID,
						BeautyDateControllerE2ETests.TEST_PETTYPE_ID)
				.with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("description", "descripcion")
				.param("startDate", "2020-03-31 16:00")
				.param("pet", "peto - 2"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeHasErrors("beautyDate"))
			.andExpect(MockMvcResultMatchers.view()
			.name("beauty-dates/createOrUpdateBeautyDateForm"));
	}
	
	//Historia de usuario 14
	//Test  inicial de update
	@WithMockUser(username = BeautyDateControllerE2ETests.TEST_OWNER_USERNAME, roles = {
			"0wn3r"
		}, password = "123")
		@Test
		void testInitUpdateBeautyDateForm() throws Exception {
		Pet pet = new Pet();
		pet.setId(2);
		pet.setBirthDate(LocalDate.of(2020, Month.APRIL, 1));
		PetType cat = new PetType();
		cat.setId(BeautyDateControllerE2ETests.TEST_PETTYPE_ID);
		cat.setName("cat");
		pet.setType(cat);
		pet.setName("Leo");
		LocalDateTime date=LocalDateTime.of(LocalDate.of(2020, 4, 1), LocalTime.of(16, 0));
		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners/{ownerUsername}/beauty-dates/{beautyDateId}/edit", BeautyDateControllerE2ETests.TEST_OWNER_USERNAME, BeautyDateControllerE2ETests.TEST_BEAUTYDATE_ID))
		.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("beautyDate"))
		.andExpect(MockMvcResultMatchers.model().attribute("beautyDate", Matchers.hasProperty("description", Matchers.is("prueba"))))
		.andExpect(MockMvcResultMatchers.model().attribute("beautyDate", Matchers.hasProperty("startDate", Matchers.is(date))))
		.andExpect(MockMvcResultMatchers.model().attribute("beautyDate", Matchers.hasProperty("pet", Matchers.is(pet))))
		.andExpect(MockMvcResultMatchers.view().name("beauty-dates/createOrUpdateBeautyDateForm"));
		}
	
	//Caso negativo not author
	@WithMockUser(username = BeautyDateControllerE2ETests.TEST_OWNER_USERNAME, roles = {
			"0wn3r"
		}, password = "123")
		@Test
		void testBeautyDateNoAuthorForm() throws Exception {
	
		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners/{ownerUsername}/beauty-dates/{beautyDateId}/edit","Invalido", BeautyDateControllerE2ETests.TEST_BEAUTYDATE_ID))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
		.andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
		}
	
	//Caso positivo de update
	@WithMockUser(username = BeautyDateControllerE2ETests.TEST_OWNER_USERNAME, roles = {
			"0wn3r"
		}, password = "123")
		@Test
		void testProcessUpdateBeautyDateFormSuccess() throws Exception {
			this.mockMvc
				.perform(MockMvcRequestBuilders.post("/owners/{ownerUsername}/beauty-dates/{beautyDateId}/edit", BeautyDateControllerE2ETests.TEST_OWNER_USERNAME, BeautyDateControllerE2ETests.TEST_BEAUTYDATE_ID)
					.with(SecurityMockMvcRequestPostProcessors.csrf())
					.param("description", "modified")
					.param("startDate", "2020/03/31 16:00")
					.param("pet", "currupipi - 1"))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/owners/{ownerUsername}/beauty-dates"));
		}
	
	//Caso positivo de update sin descripcion
	@WithMockUser(username = BeautyDateControllerE2ETests.TEST_OWNER_USERNAME, roles = {
			"0wn3r"
		}, password = "123")
		@Test
		void testProcessUpdateBeautyDateNoDescriptionFormSuccess() throws Exception {
			this.mockMvc
				.perform(MockMvcRequestBuilders.post("/owners/{ownerUsername}/beauty-dates/{beautyDateId}/edit", BeautyDateControllerE2ETests.TEST_OWNER_USERNAME, BeautyDateControllerE2ETests.TEST_BEAUTYDATE_ID)
					.with(SecurityMockMvcRequestPostProcessors.csrf())
					.param("description", "")
					.param("startDate", "2020/03/31 16:00")
					.param("pet", "currupipi - 1"))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/owners/{ownerUsername}/beauty-dates"));
		}
	//Caso negativo se deja la fecha vacia
	@WithMockUser(username = BeautyDateControllerE2ETests.TEST_OWNER_USERNAME, roles = {
			"owner"
		}, password = "0wn3r")
		@Test
		void testProcessUpdateBeautyDateEmptyDateFormSuccess() throws Exception {
			this.mockMvc
				.perform(MockMvcRequestBuilders.post("/owners/{ownerUsername}/beauty-dates/{beautyDateId}/edit", BeautyDateControllerE2ETests.TEST_OWNER_USERNAME, BeautyDateControllerE2ETests.TEST_BEAUTYDATE_ID)
					.with(SecurityMockMvcRequestPostProcessors.csrf())
					.param("description", "")
					.param("pet", "currupipi - 1"))
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("beautyDate"))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("beautyDate", "startDate"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("beauty-dates/createOrUpdateBeautyDateForm"));
		}
	//Caso negativo la fecha es fin de semana
	@WithMockUser(username = BeautyDateControllerE2ETests.TEST_OWNER_USERNAME, roles = {
			"owner"
		}, password = "0wn3r")
		@Test
		void testProcessUpdateBeautyDateWeekendDateFormSuccess() throws Exception {
		
		this.mockMvc
		.perform(MockMvcRequestBuilders.post("/owners/{ownerUsername}/beauty-dates/{beautyDateId}/edit", BeautyDateControllerE2ETests.TEST_OWNER_USERNAME, BeautyDateControllerE2ETests.TEST_BEAUTYDATE_ID)
					.with(SecurityMockMvcRequestPostProcessors.csrf())
					.param("description", "modified")
					.param("startDate", "2020/04/18 16:00")
					.param("pet", "currupipi - 1"))
			.andExpect(MockMvcResultMatchers.model().attributeHasErrors("beautyDate"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("beautyDate", "startDate"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("beauty-dates/createOrUpdateBeautyDateForm"));
		}
	//Caso negativo fecha concurrente
		@WithMockUser(username = BeautyDateControllerE2ETests.TEST_OWNER_USERNAME, roles = {
				"owner"
			}, password = "0wn3r")
			@Test
			void testProcessUpdateBeautyDateConcurrentDateFormSuccess() throws Exception {
		
			this.mockMvc
			.perform(MockMvcRequestBuilders.post("/owners/{ownerUsername}/beauty-dates/{beautyDateId}/edit", BeautyDateControllerE2ETests.TEST_OWNER_USERNAME, BeautyDateControllerE2ETests.TEST_BEAUTYDATE_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("description", "modified")
						.param("startDate", "2020/04/04 16:00")
						.param("pet", "currupipi - 1"))
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("beautyDate"))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("beautyDate", "startDate"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("beauty-dates/createOrUpdateBeautyDateForm"));
			}
	//Casi negativo hora incorrecta
		@WithMockUser(username = BeautyDateControllerE2ETests.TEST_OWNER_USERNAME, roles = {
				"owner"
			}, password = "0wn3r")
			@Test
			void testProcessUpdateBeautyDateIncorrectTimeFormSuccess() throws Exception {
		
			this.mockMvc
			.perform(MockMvcRequestBuilders.post("/owners/{ownerUsername}/beauty-dates/{beautyDateId}/edit", BeautyDateControllerE2ETests.TEST_OWNER_USERNAME, BeautyDateControllerE2ETests.TEST_BEAUTYDATE_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("description", "modified")
						.param("startDate", "2020/04/29 22:00")
						.param("pet", "currupipi - 1"))
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("beautyDate"))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("beautyDate", "startDate"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("beauty-dates/createOrUpdateBeautyDateForm"));
			}
	//Caso negativo mascota vacia
		@WithMockUser(username = BeautyDateControllerE2ETests.TEST_OWNER_USERNAME, roles = {
				"owner"
			}, password = "0wn3r")
			@Test
			void testProcessUpdateBeautyDateEmptyPetFormSuccess() throws Exception {
		
			this.mockMvc
			.perform(MockMvcRequestBuilders.post("/owners/{ownerUsername}/beauty-dates/{beautyDateId}/edit", BeautyDateControllerE2ETests.TEST_OWNER_USERNAME, BeautyDateControllerE2ETests.TEST_BEAUTYDATE_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("description", "modified")
						.param("startDate", "2020-04-29 16:00")
						.param("pet", "null"))
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("beautyDate"))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("beautyDate", "pet"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("beauty-dates/createOrUpdateBeautyDateForm"));
			}
}



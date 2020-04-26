
package org.springframework.samples.petclinic.web;

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
import org.springframework.samples.petclinic.service.exceptions.NoPetTypeException;
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
	
	private static final int TEST_BEAUTYDATE_ID=1;
	
	private static final int TEST_BEAUTICIAN_ID=1;

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
		bc.setId(BeautyDateControllerTests.TEST_BEAUTYDATE_ID);
		bc.setDescription("jjjajaja");
		bc.setBeautyCenter(beautyCenter);
		bc.setPet(peto);

		bc.setStartDate(LocalDateTime.of(LocalDate.of(2020, 3, 31), LocalTime.of(16, 0)));
		
		BeautyDate bc2 = new BeautyDate();
		bc2.setId(2);
		bc2.setDescription("description 2");
		bc2.setBeautyCenter(beautyCenter);
		bc2.setPet(peto);

		bc2.setStartDate(LocalDateTime.of(LocalDate.of(2020, 4, 4), LocalTime.of(16, 0)));
		
		List<BeautyDate> lista=new ArrayList<BeautyDate>();
		lista.add(bc2);

		BDDMockito.given(this.beautyService.findById(BeautyDateControllerTests.TEST_BEAUTYCENTER_ID)).willReturn(beautyCenter);
		BDDMockito.given(this.beautyDateService.findPetsByOwnerAndType(BeautyDateControllerTests.TEST_OWNER_USERNAME, BeautyDateControllerTests.TEST_PETTYPE_ID)).willReturn(temp2);
		BDDMockito.given(this.petService.findPetById(pet.getId())).willReturn(pet);
		BDDMockito.given(this.beautyDateService.findBeautyDateByPetId(peto.getId())).willReturn(bc);
		BDDMockito.given(this.beautyDateService.findById(TEST_BEAUTYDATE_ID)).willReturn(bc);
		BDDMockito.given(this.beautyDateService.findBeautyDateById(TEST_BEAUTYDATE_ID)).willReturn(bc);
		BDDMockito.given(this.beautyDateService.findBeautyDatesByBeauticianId(TEST_BEAUTICIAN_ID)).willReturn(lista);
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
				.with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("description", "descripcion")
				.param("startDate", "2020/04/29 16:00")
				.param("pet", "currupipi - 1"))
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
		Mockito.when(this.beautyDateService.saveBeautyDate(ArgumentMatchers.any())).thenThrow(new EmptyPetException());
		this.mockMvc
			.perform(MockMvcRequestBuilders
				.post("/owners/{ownerUsername}/beauty-centers/{beautyCenterId}/{petTypeId}/beauty-dates/new", BeautyDateControllerTests.TEST_OWNER_USERNAME, BeautyDateControllerTests.TEST_BEAUTYCENTER_ID, BeautyDateControllerTests.TEST_PETTYPE_ID)
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("description", "descripcion").param("startDate", "2020/04/29 16:00").param("pet", ""))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeHasErrors("beautyDate")).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("beautyDate", "pet"))
			.andExpect(MockMvcResultMatchers.view().name("beauty-dates/createOrUpdateBeautyDateForm"));
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
		Mockito.when(this.beautyDateService.saveBeautyDate(ArgumentMatchers.any())).thenThrow(new IsWeekendException());
		this.mockMvc
			.perform(MockMvcRequestBuilders
				.post("/owners/{ownerUsername}/beauty-centers/{beautyCenterId}/{petTypeId}/beauty-dates/new", BeautyDateControllerTests.TEST_OWNER_USERNAME, BeautyDateControllerTests.TEST_BEAUTYCENTER_ID, BeautyDateControllerTests.TEST_PETTYPE_ID)
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("description", "descripcion").param("startDate", "2020/03/28 16:00").param("pet", "currupipi - 1"))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeHasErrors("beautyDate")).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("beautyDate", "startDate"))
			.andExpect(MockMvcResultMatchers.view().name("beauty-dates/createOrUpdateBeautyDateForm"));
	}

	//Caso Negativo. Introduzo un día a las 12:00

	@WithMockUser(username = BeautyDateControllerTests.TEST_OWNER_USERNAME, roles = {
		"owner"
	}, password = "123")
	@Test
	void testProcessCreateBeautyDateFormErrorTimeDate() throws Exception {
		Mockito.when(this.beautyDateService.saveBeautyDate(ArgumentMatchers.any())).thenThrow(new IsNotInTimeException());
		this.mockMvc
			.perform(MockMvcRequestBuilders
				.post("/owners/{ownerUsername}/beauty-centers/{beautyCenterId}/{petTypeId}/beauty-dates/new", BeautyDateControllerTests.TEST_OWNER_USERNAME, BeautyDateControllerTests.TEST_BEAUTYCENTER_ID, BeautyDateControllerTests.TEST_PETTYPE_ID)
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("description", "descripcion").param("startDate", "2020/03/28 12:00").param("pet", "currupipi - 1"))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeHasErrors("beautyDate")).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("beautyDate", "startDate"))
			.andExpect(MockMvcResultMatchers.view().name("beauty-dates/createOrUpdateBeautyDateForm"));
	}

	//Caso Negativo. Introduzco una pet que ya tiene una cita
	@WithMockUser(username = BeautyDateControllerTests.TEST_OWNER_USERNAME, roles = {
		"owner"
	}, password = "123")
	@Test
	void testProcessCreateBeautyDateFormErrorPetAlreadyHasDate() throws Exception {
		Mockito.when(this.beautyDateService.saveBeautyDate(ArgumentMatchers.any())).thenThrow(new AlreadyDateException());
		this.mockMvc
			.perform(MockMvcRequestBuilders
				.post("/owners/{ownerUsername}/beauty-centers/{beautyCenterId}/{petTypeId}/beauty-dates/new", BeautyDateControllerTests.TEST_OWNER_USERNAME, BeautyDateControllerTests.TEST_BEAUTYCENTER_ID, BeautyDateControllerTests.TEST_PETTYPE_ID)
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("description", "descripcion").param("startDate", "2020/03/31 16:00").param("pet", "peto - 2"))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeHasErrors("beautyDate")).andExpect(MockMvcResultMatchers.view().name("beauty-dates/createOrUpdateBeautyDateForm"));
	}
	
	//Historia de usuario 14
	//Test  inicial de update
	@WithMockUser(username = BeautyDateControllerTests.TEST_OWNER_USERNAME, roles = {
			"owner"
		}, password = "123")
		@Test
		void testInitUpdateBeautyDateForm() throws Exception {
		Pet pet = new Pet();
		pet.setId(2);
		pet.setBirthDate(LocalDate.of(2020, Month.JANUARY, 5));
		PetType cat = new PetType();
		cat.setId(BeautyDateControllerTests.TEST_PETTYPE_ID);
		cat.setName("cat");
		pet.setType(cat);
		pet.setName("peto");
		LocalDateTime date=LocalDateTime.of(LocalDate.of(2020, 3, 31), LocalTime.of(16, 0));
		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners/{ownerUsername}/beauty-dates/{beautyDateId}/edit", BeautyDateControllerTests.TEST_OWNER_USERNAME, BeautyDateControllerTests.TEST_BEAUTYDATE_ID))
		.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("beautyDate"))
		.andExpect(MockMvcResultMatchers.model().attribute("beautyDate", Matchers.hasProperty("description", Matchers.is("jjjajaja"))))
		.andExpect(MockMvcResultMatchers.model().attribute("beautyDate", Matchers.hasProperty("startDate", Matchers.is(date))))
		.andExpect(MockMvcResultMatchers.model().attribute("beautyDate", Matchers.hasProperty("pet", Matchers.is(pet)))).andExpect(MockMvcResultMatchers.view().name("beauty-dates/createOrUpdateBeautyDateForm"));
		}
	
	//Caso negativo not author
	@WithMockUser(username = BeautyDateControllerTests.TEST_OWNER_USERNAME, roles = {
			"owner"
		}, password = "123")
		@Test
		void testBeautyDateNoAuthorForm() throws Exception {
		Mockito.when(this.authoritiesService.isAuthor(ArgumentMatchers.any())).thenThrow(new InvalidActivityException());
		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners/{ownerUsername}/beauty-dates/{beautyDateId}/edit","Invalido", BeautyDateControllerTests.TEST_BEAUTYDATE_ID))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
		.andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
		}
	
	//Caso positivo de update
	@WithMockUser(username = BeautyDateControllerTests.TEST_OWNER_USERNAME, roles = {
			"owner"
		}, password = "123")
		@Test
		void testProcessUpdateBeautyDateFormSuccess() throws Exception {
			this.mockMvc
				.perform(MockMvcRequestBuilders.post("/owners/{ownerUsername}/beauty-dates/{beautyDateId}/edit", BeautyDateControllerTests.TEST_OWNER_USERNAME, BeautyDateControllerTests.TEST_BEAUTYDATE_ID)
					.with(SecurityMockMvcRequestPostProcessors.csrf())
					.param("description", "modified")
					.param("startDate", "2020/03/31 16:00")
					.param("pet", "currupipi - 1"))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/owners/{ownerUsername}/beauty-dates"));
		}
	
	//Caso positivo de update sin descripcion
	@WithMockUser(username = BeautyDateControllerTests.TEST_OWNER_USERNAME, roles = {
			"owner"
		}, password = "123")
		@Test
		void testProcessUpdateBeautyDateNoDescriptionFormSuccess() throws Exception {
			this.mockMvc
				.perform(MockMvcRequestBuilders.post("/owners/{ownerUsername}/beauty-dates/{beautyDateId}/edit", BeautyDateControllerTests.TEST_OWNER_USERNAME, BeautyDateControllerTests.TEST_BEAUTYDATE_ID)
					.with(SecurityMockMvcRequestPostProcessors.csrf())
					.param("description", "")
					.param("startDate", "2020/03/31 16:00")
					.param("pet", "currupipi - 1"))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/owners/{ownerUsername}/beauty-dates"));
		}
	//Caso negativo se deja la fecha vacia
	@WithMockUser(username = BeautyDateControllerTests.TEST_OWNER_USERNAME, roles = {
			"owner"
		}, password = "123")
		@Test
		void testProcessUpdateBeautyDateEmptyDateFormSuccess() throws Exception {
			this.mockMvc
				.perform(MockMvcRequestBuilders.post("/owners/{ownerUsername}/beauty-dates/{beautyDateId}/edit", BeautyDateControllerTests.TEST_OWNER_USERNAME, BeautyDateControllerTests.TEST_BEAUTYDATE_ID)
					.with(SecurityMockMvcRequestPostProcessors.csrf())
					.param("description", "")
					.param("pet", "currupipi - 1"))
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("beautyDate"))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("beautyDate", "startDate"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("beauty-dates/createOrUpdateBeautyDateForm"));
		}
	//Caso negativo la fecha es fin de semana
	@WithMockUser(username = BeautyDateControllerTests.TEST_OWNER_USERNAME, roles = {
			"owner"
		}, password = "123")
		@Test
		void testProcessUpdateBeautyDateWeekendDateFormSuccess() throws Exception {
		Mockito.when(this.beautyDateService.saveBeautyDate(ArgumentMatchers.any())).thenThrow(new IsWeekendException());
		this.mockMvc
		.perform(MockMvcRequestBuilders.post("/owners/{ownerUsername}/beauty-dates/{beautyDateId}/edit", BeautyDateControllerTests.TEST_OWNER_USERNAME, BeautyDateControllerTests.TEST_BEAUTYDATE_ID)
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
		@WithMockUser(username = BeautyDateControllerTests.TEST_OWNER_USERNAME, roles = {
				"owner"
			}, password = "123")
			@Test
			void testProcessUpdateBeautyDateConcurrentDateFormSuccess() throws Exception {
			Mockito.when(this.beautyDateService.saveBeautyDate(ArgumentMatchers.any())).thenThrow(new AlreadyDateException());
			this.mockMvc
			.perform(MockMvcRequestBuilders.post("/owners/{ownerUsername}/beauty-dates/{beautyDateId}/edit", BeautyDateControllerTests.TEST_OWNER_USERNAME, BeautyDateControllerTests.TEST_BEAUTYDATE_ID)
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
		@WithMockUser(username = BeautyDateControllerTests.TEST_OWNER_USERNAME, roles = {
				"owner"
			}, password = "123")
			@Test
			void testProcessUpdateBeautyDateIncorrectTimeFormSuccess() throws Exception {
			Mockito.when(this.beautyDateService.saveBeautyDate(ArgumentMatchers.any())).thenThrow(new IsNotInTimeException());
			this.mockMvc
			.perform(MockMvcRequestBuilders.post("/owners/{ownerUsername}/beauty-dates/{beautyDateId}/edit", BeautyDateControllerTests.TEST_OWNER_USERNAME, BeautyDateControllerTests.TEST_BEAUTYDATE_ID)
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
		@WithMockUser(username = BeautyDateControllerTests.TEST_OWNER_USERNAME, roles = {
				"owner"
			}, password = "123")
			@Test
			void testProcessUpdateBeautyDateEmptyPetFormSuccess() throws Exception {
			Mockito.when(this.beautyDateService.saveBeautyDate(ArgumentMatchers.any())).thenThrow(new EmptyPetException());
			this.mockMvc
			.perform(MockMvcRequestBuilders.post("/owners/{ownerUsername}/beauty-dates/{beautyDateId}/edit", BeautyDateControllerTests.TEST_OWNER_USERNAME, BeautyDateControllerTests.TEST_BEAUTYDATE_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("description", "modified")
						.param("startDate", "2020/04/29 16:00")
						.param("pet", ""))
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("beautyDate"))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("beautyDate", "pet"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("beauty-dates/createOrUpdateBeautyDateForm"));
			}
}


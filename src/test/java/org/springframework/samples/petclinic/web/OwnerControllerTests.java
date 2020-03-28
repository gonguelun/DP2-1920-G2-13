
package org.springframework.samples.petclinic.web;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

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
import org.springframework.samples.petclinic.model.BeautyDate;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.BeauticianService;
import org.springframework.samples.petclinic.service.BeautyCenterService;
import org.springframework.samples.petclinic.service.BeautyDateService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 * Test class for {@link OwnerController}
 *
 * @author Colin But
 */

@WebMvcTest(controllers = OwnerController.class, includeFilters = @ComponentScan.Filter(value = PetTypeFormatter.class, type = FilterType.ASSIGNABLE_TYPE),
	excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
class OwnerControllerTests {

	private static final int	TEST_OWNER_ID			= 1;

	private static final int	TEST_BEAUTICIAN_ID		= 1;

	private static final int	TEST_BEAUTYCENTER_ID	= 1;

	private static final int	TEST_BEAUTYDATE_ID		= 1;

	private static final int	TEST_PET_ID				= 1;

	private static final int	TEST_OWNER2_ID			= 2;

	@Autowired
	private OwnerController		ownerController;

	@MockBean
	private UserService			userService;

	@MockBean
	private AuthoritiesService	authoritiesService;

	@MockBean
	private BeautyDateService	beautyDateService;

	@MockBean
	private PetService			petService;

	@MockBean
	private BeautyCenterService	beautyService;

	@MockBean
	private BeauticianService	beauticianService;

	@MockBean
	private OwnerService		ownerService;

	@Autowired
	private MockMvc				mockMvc;

	private Owner				george;

	private BeautyCenter		beautyCenter1;

	private BeautyDate			beautyDate1;

	private User				user1;

	private Beautician			beautician1;

	private User				user2;

	private Pet					pet1;

	private User				user3;

	private User				user4;

	private Owner				pepa;


	@BeforeEach
	void setup() {

		// Creación de user para owner
		this.user1 = new User();
		this.user1.setId(1);
		this.user1.setEnabled(true);
		this.user1.setUsername("owner1");
		this.user1.setPassword("owner1");

		// Creación de owner
		this.george = new Owner();
		this.george.setId(OwnerControllerTests.TEST_OWNER_ID);
		this.george.setFirstName("George");
		this.george.setLastName("Franklin");
		this.george.setAddress("110 W. Liberty St.");
		this.george.setCity("Madison");
		this.george.setTelephone("6085551023");
		this.george.setUser(this.user1);

		//Creación de user para beautician

		this.user2 = new User();
		this.user2.setId(2);
		this.user2.setEnabled(true);
		this.user2.setUsername("beautician1");
		this.user2.setPassword("beautician1");

		//Creación de beautician

		this.beautician1 = new Beautician();
		this.beautician1.setId(OwnerControllerTests.TEST_BEAUTICIAN_ID);
		this.beautician1.setFirstName("f");
		this.beautician1.setLastName("f");
		this.beautician1.setUser(this.user2);

		//Creación de specializations para beautician

		Collection<PetType> specializations = new ArrayList<>();
		PetType cat = new PetType();
		cat.setId(1);
		cat.setName("gato");
		specializations.add(cat);

		this.beautician1.setSpecializations(specializations);

		// Creación de pet para owner
		this.pet1 = new Pet();
		this.pet1.setId(OwnerControllerTests.TEST_PET_ID);
		this.pet1.setBirthDate(LocalDate.of(2020, 3, 20));
		this.pet1.setName("gato");
		this.pet1.setType(cat);

		this.george.addPet(this.pet1);

		//Creación de BeautyCenter

		this.beautyCenter1 = new BeautyCenter();
		this.beautyCenter1.setId(OwnerControllerTests.TEST_BEAUTYCENTER_ID);
		this.beautyCenter1.setName("beautycenter1");
		this.beautyCenter1.setDescription("prueba1");
		this.beautyCenter1.setPetType(cat);
		this.beautyCenter1.setBeautician(this.beautician1);

		//Creación de BeautyDate

		this.beautyDate1 = new BeautyDate();
		this.beautyDate1.setId(OwnerControllerTests.TEST_BEAUTYDATE_ID);
		this.beautyDate1.setBeautyCenter(this.beautyCenter1);
		this.beautyDate1.setStartDate(LocalDateTime.now());
		this.beautyDate1.setPet(this.pet1);

		// Creación de user para owner que va a intentar borrar una cita que no es suya
		this.user2 = new User();
		this.user2.setId(3);
		this.user2.setEnabled(true);
		this.user2.setUsername("owner2");
		this.user2.setPassword("owner2");

		// Creación de owner
		this.pepa = new Owner();
		this.pepa.setId(OwnerControllerTests.TEST_OWNER2_ID);
		this.pepa.setFirstName("Pepa");
		this.pepa.setLastName("Franklin");
		this.pepa.setAddress("110 W. Liberty St.");
		this.pepa.setCity("Madison");
		this.pepa.setTelephone("6085551023");
		this.pepa.setUser(this.user3);

		this.user4 = new User();
		this.user4.setId(4);
		this.user4.setEnabled(true);
		this.user4.setUsername("user4");
		this.user4.setPassword("user4");

		BDDMockito.given(this.ownerService.findOwnerById(OwnerControllerTests.TEST_OWNER_ID)).willReturn(this.george);
		BDDMockito.given(this.ownerService.findOwnerById(OwnerControllerTests.TEST_OWNER2_ID)).willReturn(this.pepa);
		BDDMockito.given(this.beautyDateService.findById(OwnerControllerTests.TEST_BEAUTYDATE_ID)).willReturn(this.beautyDate1);
		BDDMockito.given(this.ownerService.findAllBeautyCentersByPetType(1)).willReturn(Lists.newArrayList(this.beautyCenter1));
		BDDMockito.given(this.ownerService.findAllBeautyCentersByPetType(7)).willReturn(null);
		BDDMockito.given(this.petService.findPetTypes()).willReturn(Lists.newArrayList(cat));
		BDDMockito.given(this.beautyDateService.findBeautyDatesByOwnerUsername(this.george.getUser().getUsername())).willReturn(Lists.newArrayList(this.beautyDate1));

	}

	//	@WithMockUser(value = "spring")
	//	@Test
	//	void testInitCreationForm() throws Exception {
	//		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners/new")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("owner"))
	//			.andExpect(MockMvcResultMatchers.view().name("owners/createOrUpdateOwnerForm"));
	//	}
	//
	//	@WithMockUser(value = "spring")
	//	@Test
	//	void testProcessCreationFormSuccess() throws Exception {
	//		this.mockMvc.perform(
	//			MockMvcRequestBuilders.post("/owners/new").param("firstName", "Joe").param("lastName", "Bloggs").with(SecurityMockMvcRequestPostProcessors.csrf()).param("address", "123 Caramel Street").param("city", "London").param("telephone", "01316761638"))
	//			.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	//	}
	//
	//	@WithMockUser(value = "spring")
	//	@Test
	//	void testProcessCreationFormHasErrors() throws Exception {
	//		this.mockMvc.perform(MockMvcRequestBuilders.post("/owners/new").with(SecurityMockMvcRequestPostProcessors.csrf()).param("firstName", "Joe").param("lastName", "Bloggs").param("city", "London")).andExpect(MockMvcResultMatchers.status().isOk())
	//			.andExpect(MockMvcResultMatchers.model().attributeHasErrors("owner")).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("owner", "address")).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("owner", "telephone"))
	//			.andExpect(MockMvcResultMatchers.view().name("owners/createOrUpdateOwnerForm"));
	//	}
	//
	//	@WithMockUser(value = "spring")
	//	@Test
	//	void testInitFindForm() throws Exception {
	//		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners/find")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("owner")).andExpect(MockMvcResultMatchers.view().name("owners/findOwners"));
	//	}
	//
	//	@WithMockUser(value = "spring")
	//	@Test
	//	void testProcessFindFormSuccess() throws Exception {
	//		BDDMockito.given(this.clinicService.findOwnerByLastName("")).willReturn(Lists.newArrayList(this.george, new Owner()));
	//
	//		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("owners/ownersList"));
	//	}
	//
	//	@WithMockUser(value = "spring")
	//	@Test
	//	void testProcessFindFormByLastName() throws Exception {
	//		BDDMockito.given(this.clinicService.findOwnerByLastName(this.george.getLastName())).willReturn(Lists.newArrayList(this.george));
	//
	//		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners").param("lastName", "Franklin")).andExpect(MockMvcResultMatchers.status().is3xxRedirection())
	//			.andExpect(MockMvcResultMatchers.view().name("redirect:/owners/" + OwnerControllerTests.TEST_OWNER_ID));
	//	}
	//
	//	@WithMockUser(value = "spring")
	//	@Test
	//	void testProcessFindFormNoOwnersFound() throws Exception {
	//		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners").param("lastName", "Unknown Surname")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("owner", "lastName"))
	//			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrorCode("owner", "lastName", "notFound")).andExpect(MockMvcResultMatchers.view().name("owners/findOwners"));
	//	}
	//
	//	@WithMockUser(value = "spring")
	//	@Test
	//	void testInitUpdateOwnerForm() throws Exception {
	//		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners/{ownerId}/edit", OwnerControllerTests.TEST_OWNER_ID)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("owner"))
	//			.andExpect(MockMvcResultMatchers.model().attribute("owner", Matchers.hasProperty("lastName", Matchers.is("Franklin")))).andExpect(MockMvcResultMatchers.model().attribute("owner", Matchers.hasProperty("firstName", Matchers.is("George"))))
	//			.andExpect(MockMvcResultMatchers.model().attribute("owner", Matchers.hasProperty("address", Matchers.is("110 W. Liberty St.")))).andExpect(MockMvcResultMatchers.model().attribute("owner", Matchers.hasProperty("city", Matchers.is("Madison"))))
	//			.andExpect(MockMvcResultMatchers.model().attribute("owner", Matchers.hasProperty("telephone", Matchers.is("6085551023")))).andExpect(MockMvcResultMatchers.view().name("owners/createOrUpdateOwnerForm"));
	//	}
	//
	//	@WithMockUser(value = "spring")
	//	@Test
	//	void testProcessUpdateOwnerFormSuccess() throws Exception {
	//		this.mockMvc.perform(MockMvcRequestBuilders.post("/owners/{ownerId}/edit", OwnerControllerTests.TEST_OWNER_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("firstName", "Joe").param("lastName", "Bloggs")
	//			.param("address", "123 Caramel Street").param("city", "London").param("telephone", "01616291589")).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/owners/{ownerId}"));
	//	}
	//
	//	@WithMockUser(value = "spring")
	//	@Test
	//	void testProcessUpdateOwnerFormHasErrors() throws Exception {
	//		this.mockMvc.perform(MockMvcRequestBuilders.post("/owners/{ownerId}/edit", OwnerControllerTests.TEST_OWNER_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("firstName", "Joe").param("lastName", "Bloggs").param("city", "London"))
	//			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeHasErrors("owner")).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("owner", "address"))
	//			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("owner", "telephone")).andExpect(MockMvcResultMatchers.view().name("owners/createOrUpdateOwnerForm"));
	//	}
	//
	//	@WithMockUser(value = "spring")
	//	@Test
	//	void testShowOwner() throws Exception {
	//		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners/{ownerId}", OwnerControllerTests.TEST_OWNER_ID)).andExpect(MockMvcResultMatchers.status().isOk())
	//			.andExpect(MockMvcResultMatchers.model().attribute("owner", Matchers.hasProperty("lastName", Matchers.is("Franklin")))).andExpect(MockMvcResultMatchers.model().attribute("owner", Matchers.hasProperty("firstName", Matchers.is("George"))))
	//			.andExpect(MockMvcResultMatchers.model().attribute("owner", Matchers.hasProperty("address", Matchers.is("110 W. Liberty St.")))).andExpect(MockMvcResultMatchers.model().attribute("owner", Matchers.hasProperty("city", Matchers.is("Madison"))))
	//			.andExpect(MockMvcResultMatchers.model().attribute("owner", Matchers.hasProperty("telephone", Matchers.is("6085551023")))).andExpect(MockMvcResultMatchers.view().name("owners/ownerDetails"));
	//	}

	//	Caso positivo: Al seleccionar el listado de mis citas en la aplicación, aparece un listad con las citas que ha pedido el usuario y aún no se han efectuado y al seleccionar la opción eliminar se elimina de la base de datos.
	@WithMockUser(username = "owner1", roles = {
		"owner"
	}, password = "owner1")
	@Test
	void testProcessDeleteBeautyDateFormSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners/{ownerUsername}/beauty-dates/{beautyDateId}/delete", this.george.getUser().getUsername(), OwnerControllerTests.TEST_BEAUTYDATE_ID).with(SecurityMockMvcRequestPostProcessors.csrf()))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/owners/{ownerUsername}/beauty-dates"));
	}

	//	Caso negativo: Un propietario accede a eliminar un servicio que no es suyo mediante la url y la aplicación le lanza un mensaje de error.

	@WithMockUser(username = "owner2", roles = {
		"owner"
	}, password = "owner2")
	@Test
	void testProcessDeleteBeautyDateFormWithErrors() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners/{ownerUsername}/beauty-dates/{beautyDateId}/delete", this.george.getUser().getUsername(), OwnerControllerTests.TEST_BEAUTYDATE_ID).with(SecurityMockMvcRequestPostProcessors.csrf()))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
	}

	// TESTS HISTORIA DE USUARIO 10

	// SEARCH BEAUTY CENTER (Caso positivo)
	@WithMockUser(username = "owner1", roles = {
		"owner"
	}, password = "owner1")
	@Test
	void testShowBeautyCenter() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners/beauty-centers/{petTypeId}", 1)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("owners/beautyCenterList"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("beautyCenters"));
	}

	// SEARCH BEAUTY CENTER (Caso positivo)
	@WithMockUser(username = "owner1", roles = {
		"owner"
	}, password = "owner1")
	@Test
	void testSearchBeautyCenter() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners/search-beauty-center")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("owners/searchBeautyCenter"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("type"));
	}

	//Caso positivo: El propietario lista correctamente las citas pasadas y futuras.
	@WithMockUser(username = "owner1", roles = {
		"owner"
	}, password = "owner1")
	@Test
	void testShowBeautyDate() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners/{ownerUsername}/beauty-dates", this.george.getUser().getUsername())).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("beauty-dates/beautyDatesList")).andExpect(MockMvcResultMatchers.model().attributeExists("beautyDates"));
	}

}

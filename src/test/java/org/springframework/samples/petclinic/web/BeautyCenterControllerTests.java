
package org.springframework.samples.petclinic.web;

import java.util.ArrayList;
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
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.BeauticianService;
import org.springframework.samples.petclinic.service.BeautyCenterService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.samples.petclinic.service.exceptions.NoPetTypeException;
import org.springframework.samples.petclinic.service.exceptions.NullOrShortNameException;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = BeautyCenterController.class, includeFilters = @ComponentScan.Filter(value = PetTypeFormatter.class, type = FilterType.ASSIGNABLE_TYPE),
	excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
public class BeautyCenterControllerTests {

	private static final int		TEST_BEAUTYCENTER_ID	= 1;

	private static final int		TEST_BEAUTICIAN_ID		= 1;

	private static final int		TEST_BEAUTICIAN2_ID		= 2;

	private static final int		TEST_BEAUTYCENTER2_ID	= 2;

	private static final int		TEST_BEAUTYCENTER3_ID	= 3;

	@Autowired
	private BeautyCenterController	beautyCenterController;

	@MockBean
	private BeautyCenterService		beautyService;

	@MockBean
	private BeauticianService		beauticianService;

	@MockBean
	private PetService				petService;

	@MockBean
	private UserService				userService;

	@MockBean
	private AuthoritiesService		authoritiesService;

	@Autowired
	private MockMvc					mockMvc;

	private User					user2;

	private Beautician				beautician2;

	private BeautyCenter			beautyCenter2;


	/* Test controlador hisoria de usuario 2 */

	@BeforeEach
	void setup() throws NullOrShortNameException, NoPetTypeException {
		PetType cat = new PetType();
		cat.setId(3);
		cat.setName("hamster");
		List<PetType> temp = new ArrayList<>();
		temp.add(cat);
		List<PetType> temp2 = new ArrayList<>();
		PetType dog = new PetType();
		dog.setId(4);
		dog.setName("dog");
		temp2.add(cat);
		temp2.add(dog);

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
		beautyCenter.setId(BeautyCenterControllerTests.TEST_BEAUTYCENTER2_ID);
		beautyCenter.setName("beautycenter1");
		beautyCenter.setDescription("prueba1");
		beautyCenter.setPetType(cat);
		beautyCenter.setBeautician(beautician);

		this.user2 = new User();
		this.user2.setId(2);
		this.user2.setEnabled(true);
		this.user2.setUsername("beautician2");
		this.user2.setPassword("beautician2");

		this.beautician2 = new Beautician();
		this.beautician2.setId(BeautyCenterControllerTests.TEST_BEAUTICIAN2_ID);
		this.beautician2.setFirstName("x");
		this.beautician2.setLastName("x");
		this.beautician2.setUser(this.user2);

		this.beautyCenter2 = new BeautyCenter();
		this.beautyCenter2.setId(BeautyCenterControllerTests.TEST_BEAUTYCENTER3_ID);
		this.beautyCenter2.setName("beautycenter2");
		this.beautyCenter2.setDescription("prueba2");
		this.beautyCenter2.setBeautician(this.beautician2);

		BDDMockito.given(this.petService.findPetTypes()).willReturn(temp2);
		BDDMockito.given(this.beauticianService.findBeauticianById(BeautyCenterControllerTests.TEST_BEAUTICIAN_ID)).willReturn(beautician);
		BDDMockito.given(this.beauticianService.findBeauticianById(BeautyCenterControllerTests.TEST_BEAUTICIAN2_ID)).willReturn(this.beautician2);
		BDDMockito.given(this.beautyService.findBeautyCenterByBeautyCenterId(BeautyCenterControllerTests.TEST_BEAUTYCENTER_ID)).willReturn(new BeautyCenter());
		BDDMockito.given(this.beautyService.findById(BeautyCenterControllerTests.TEST_BEAUTYCENTER2_ID)).willReturn(beautyCenter);
		BDDMockito.given(this.beautyService.findById(BeautyCenterControllerTests.TEST_BEAUTYCENTER3_ID)).willReturn(this.beautyCenter2);

	}

	/* GetMapping /beauticians/{beauticianId}/beauty-centers/new */

	//Caso positivo. Intentar acceder al create con el id del autenticado

	@WithMockUser(username = "beautician1", roles = {
		"beautician"
	}, password = "123")
	@Test
	void testInitCreationForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/beauticians/{beauticianId}/beauty-centers/new", BeautyCenterControllerTests.TEST_BEAUTICIAN_ID)).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("beauty-centers/createOrUpdateBeautyCenterForm")).andExpect(MockMvcResultMatchers.model().attributeExists("beautyCenter"));
	}

	//Caso negativo. Intentar acceder al create con un id distinto al del autenticado
	@WithMockUser(username = "beautician1", roles = {
		"beautician"
	}, password = "123")
	@Test
	void testInitCreationFormError() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/beauticians/{beauticianId}/beauty-centers/new", 0)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	/* PostMapping /beauticians/{beauticianId}/beauty-centers/new */

	//Casos positivo

	//Todo Correcto

	@WithMockUser(username = "beautician1", roles = {
		"beautician"
	}, password = "123")
	@Test
	void testProcessCreationFormSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/beauticians/{beauticianId}/beauty-centers/new", BeautyCenterControllerTests.TEST_BEAUTICIAN_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "beautyc 1")
			.param("description", "this is a description").param("petType", "hamster")).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/beauticians/{beauticianId}"));
	}

	//Todo correcto sin descripción

	@WithMockUser(username = "beautician1", roles = {
		"beautician"
	}, password = "123")
	@Test
	void testProcessCreationNoDescriptionFormSuccess() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/beauticians/{beauticianId}/beauty-centers/new", BeautyCenterControllerTests.TEST_BEAUTICIAN_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "beautyc 1").param("petType", "hamster"))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/beauticians/{beauticianId}"));
	}

	//Casos negativos

	//No tiene PetType

	@WithMockUser(username = "beautician1", roles = {
		"beautician"
	}, password = "123")
	@Test
	void testProcessCreationNoPetTypeHasErrors() throws Exception {
		Mockito.when(this.beautyService.save(ArgumentMatchers.any())).thenThrow(new NoPetTypeException());
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/beauticians/{beauticianId}/beauty-centers/new", BeautyCenterControllerTests.TEST_BEAUTICIAN_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "beauty center test 2")
				.param("description", "this is a description 2").param("petType", ""))
			.andExpect(MockMvcResultMatchers.model().attributeHasErrors("beautyCenter")).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("beautyCenter", "petType")).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("beauty-centers/createOrUpdateBeautyCenterForm"));
	}

	//El nombre es de menos de 3 caracteres

	@WithMockUser(username = "beautician1", roles = {
		"beautician"
	}, password = "123")
	@Test
	void testProcessCreationShortNameHasErrors() throws Exception {
		Mockito.when(this.beautyService.save(ArgumentMatchers.any())).thenThrow(new NullOrShortNameException());
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/beauticians/{beauticianId}/beauty-centers/new", BeautyCenterControllerTests.TEST_BEAUTICIAN_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "be")
				.param("description", "this is a description 2").param("petType", "hamster"))
			.andExpect(MockMvcResultMatchers.model().attributeHasErrors("beautyCenter")).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("beautyCenter", "name")).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("beauty-centers/createOrUpdateBeautyCenterForm"));
	}

	/* GetMapping /beauticians/{beauticianId}/beauty-centers */

	//Caso Positivo. Mostrar los beauty centers asociados al id del autenticado

	@WithMockUser(username = "beautician1", roles = {
		"beautician"
	}, password = "123")
	@Test
	void testInitShowForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/beauticians/{beauticianId}/beauty-centers", BeautyCenterControllerTests.TEST_BEAUTICIAN_ID)).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("beautyCenterList")).andExpect(MockMvcResultMatchers.model().attributeExists("beauty-centers"));
	}

	//Caso Negativo. Mostrar los beauty centers asociados a un id distinto al del autenticado

	@WithMockUser(username = "beautician1", roles = {
		"beautician"
	}, password = "123")
	@Test
	void testInitShowFormError() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/beauticians/{beauticianId}/beauty-centers", 0)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	//	Caso positivo: Un esteticista animal pulsa eliminar sobre un servicio suyo y este se elimina satisfactoriamente.
	@WithMockUser(username = "beautician1", roles = {
		"beautician"
	}, password = "123")
	@Test
	void testProcessDeleteBeautyCenterFormSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/beauticians/{beauticianId}/beauty-centers/{beautyCenterId}/delete", BeautyCenterControllerTests.TEST_BEAUTICIAN_ID, BeautyCenterControllerTests.TEST_BEAUTYCENTER2_ID)
			.with(SecurityMockMvcRequestPostProcessors.csrf())).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/beauticians/{beauticianId}"));
	}

	//	Caso negativo: Un esteticista animal accede a eliminar un servicio que no es suyo mediante la url y la aplicación le lanza un mensaje de error.
	@WithMockUser(username = "beautician1", roles = {
		"beautician"
	}, password = "123")
	@Test
	void testProcessDeleteBeautyCenterFormWithErrors() throws Exception {
		Mockito.when(this.authoritiesService.isAuthor(ArgumentMatchers.any())).thenThrow(new InvalidActivityException());
		this.mockMvc.perform(MockMvcRequestBuilders.get("/beauticians/{beauticianId}/beauty-centers/{beautyCenterId}/delete", BeautyCenterControllerTests.TEST_BEAUTICIAN2_ID, BeautyCenterControllerTests.TEST_BEAUTYCENTER3_ID)
			.with(SecurityMockMvcRequestPostProcessors.csrf())).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
	}

	/*
	 * Tests historia de usuario 3
	 */
	@WithMockUser(username = "beautician1", roles = {
		"beautician"
	}, password = "123")
	@Test
	void testInitUpdateBeautyCenterForm() throws Exception {
		PetType pet = new PetType();
		pet.setName("hamster");
		this.mockMvc.perform(MockMvcRequestBuilders.get("/beauticians/{beauticianId}/beauty-centers/{beautyCenterId}/edit", BeautyCenterControllerTests.TEST_BEAUTICIAN_ID, BeautyCenterControllerTests.TEST_BEAUTYCENTER2_ID))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("beautyCenter"))
			.andExpect(MockMvcResultMatchers.model().attribute("beautyCenter", Matchers.hasProperty("name", Matchers.is("beautycenter1"))))
			.andExpect(MockMvcResultMatchers.model().attribute("beautyCenter", Matchers.hasProperty("description", Matchers.is("prueba1"))))
			.andExpect(MockMvcResultMatchers.model().attribute("beautyCenter", Matchers.hasProperty("petType", Matchers.is(pet)))).andExpect(MockMvcResultMatchers.view().name("beauty-centers/createOrUpdateBeautyCenterForm"));
	}
	//Caso Positivo
	@WithMockUser(username = "beautician1", roles = {
		"beautician"
	}, password = "123")
	@Test
	void testProcessUpdateBeautyCenterFormSuccess() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/beauticians/{beauticianId}/beauty-centers/{beautyCenterId}/edit", BeautyCenterControllerTests.TEST_BEAUTICIAN_ID, BeautyCenterControllerTests.TEST_BEAUTYCENTER2_ID)
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "beautycentermodified").param("description", "descriptionmodified").param("petType", "dog"))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/beauticians/{beauticianId}"));
	}

	@WithMockUser(username = "beautician1", roles = {
		"beautician"
	}, password = "123")
	@Test
	void testProcessUpdateBeautyCenterFormSuccessNoDescription() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/beauticians/{beauticianId}/beauty-centers/{beautyCenterId}/edit", BeautyCenterControllerTests.TEST_BEAUTICIAN_ID, BeautyCenterControllerTests.TEST_BEAUTYCENTER2_ID)
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "beautycentermodified").param("description", "").param("petType", "dog"))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/beauticians/{beauticianId}"));
	}

	//Casos negativos
	@WithMockUser(username = "beautician1", roles = {
		"beautician"
	}, password = "123")
	@Test
	void testProcessUpdateBeautyCenterFormHasErrorsPetType() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/beauticians/{beauticianId}/beauty-centers/{beautyCenterId}/edit", BeautyCenterControllerTests.TEST_BEAUTICIAN_ID, BeautyCenterControllerTests.TEST_BEAUTYCENTER2_ID)
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "beautycentermodified").param("description", "descriptionmodified").param("petType", "crocodile"))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeHasErrors("beautyCenter")).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("beautyCenter", "petType"))
			.andExpect(MockMvcResultMatchers.view().name("beauty-centers/createOrUpdateBeautyCenterForm"));
	}

	@WithMockUser(username = "beautician1", roles = {
		"beautician"
	}, password = "123")
	@Test
	void testProcessUpdateBeautyCenterFormHasErrorsEmptyAttribute() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/beauticians/{beauticianId}/beauty-centers/{beautyCenterId}/edit", BeautyCenterControllerTests.TEST_BEAUTICIAN_ID, BeautyCenterControllerTests.TEST_BEAUTYCENTER2_ID)
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "").param("description", "descriptionmodified").param("petType", "dog"))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeHasErrors("beautyCenter")).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("beautyCenter", "name"))
			.andExpect(MockMvcResultMatchers.view().name("beauty-centers/createOrUpdateBeautyCenterForm"));
	}

}

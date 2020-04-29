
package org.springframework.samples.petclinic.web.e2e;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.Beautician;
import org.springframework.samples.petclinic.model.BeautyCenter;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.exceptions.NoPetTypeException;
import org.springframework.samples.petclinic.service.exceptions.NullOrShortNameException;
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
public class BeautyCenterControllerE2ETests {

	private static final int		TEST_BEAUTYCENTER_ID	= 1;

	private static final int		TEST_BEAUTICIAN_ID		= 1;

	private static final int		TEST_BEAUTICIAN2_ID		= 2;

	private static final int		TEST_BEAUTYCENTER2_ID	= 2;

	private static final int		TEST_BEAUTYCENTER3_ID	= 3;

	private MockMvc					mockMvc;

	@Autowired
	private WebApplicationContext	webApplicationContext;


	@BeforeEach
	public void setUp() {

		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
	}

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
		beautyCenter.setId(BeautyCenterControllerE2ETests.TEST_BEAUTYCENTER2_ID);
		beautyCenter.setName("beautycenter1");
		beautyCenter.setDescription("prueba1");
		beautyCenter.setPetType(cat);
		beautyCenter.setBeautician(beautician);

		User user2;

		user2 = new User();
		user2.setId(2);
		user2.setEnabled(true);
		user2.setUsername("beautician2");
		user2.setPassword("beautician2");

		Beautician beautician2 = new Beautician();
		beautician2.setId(BeautyCenterControllerE2ETests.TEST_BEAUTICIAN2_ID);
		beautician2.setFirstName("x");
		beautician2.setLastName("x");
		beautician2.setUser(user2);

		BeautyCenter beautyCenter2 = new BeautyCenter();
		beautyCenter2.setId(BeautyCenterControllerE2ETests.TEST_BEAUTYCENTER3_ID);
		beautyCenter2.setName("beautycenter2");
		beautyCenter2.setDescription("prueba2");
		beautyCenter2.setBeautician(beautician2);

	}

	/* GetMapping /beauticians/{beauticianId}/beauty-centers/new */

	//Caso positivo. Intentar acceder al create con el id del autenticado

	@WithMockUser(username = "f", roles = {
		"beautician"
	}, password = "f")
	@Test
	void testInitCreationForm() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/beauticians/{beauticianId}/beauty-centers/new", 1)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("beauty-centers/createOrUpdateBeautyCenterForm"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("beautyCenter"));
	}

	//Caso negativo. Intentar acceder al create con un id distinto al del autenticado
	@WithMockUser(username = "f", roles = {
		"beautician"
	}, password = "f")
	@Test
	void testInitCreationFormError() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/beauticians/{beauticianId}/beauty-centers/new", 2)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	/* PostMapping /beauticians/{beauticianId}/beauty-centers/new */

	//Casos positivo

	//Todo Correcto

	@WithMockUser(username = "f", roles = {
		"beautician"
	}, password = "f")
	@Test
	void testProcessCreationFormSuccess() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/beauticians/{beauticianId}/beauty-centers/new", 1).with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "beautyc 1").param("description", "this is a description").param("petType", "hamster"))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/beauticians/{beauticianId}"));
	}

	//Todo correcto sin descripción

	@WithMockUser(username = "f", roles = {
		"beautician"
	}, password = "f")
	@Test
	void testProcessCreationNoDescriptionFormSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/beauticians/{beauticianId}/beauty-centers/new", 1).with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "beautyc 1").param("petType", "hamster"))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/beauticians/{beauticianId}"));
	}

	//Casos negativos

	//No tiene PetType

	@WithMockUser(username = "f", roles = {
		"beautician"
	}, password = "f")
	@Test
	void testProcessCreationNoPetTypeHasErrors() throws Exception {

		this.mockMvc
			.perform(
				MockMvcRequestBuilders.post("/beauticians/{beauticianId}/beauty-centers/new", 1).with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "beauty center test 2").param("description", "this is a description 2").param("petType", ""))
			.andExpect(MockMvcResultMatchers.model().attributeHasErrors("beautyCenter")).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("beautyCenter", "petType")).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("beauty-centers/createOrUpdateBeautyCenterForm"));
	}

	//El nombre es de menos de 3 caracteres

	@WithMockUser(username = "f", roles = {
		"beautician"
	}, password = "f")
	@Test
	void testProcessCreationShortNameHasErrors() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.post("/beauticians/{beauticianId}/beauty-centers/new", 1).with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "be").param("description", "this is a description 2").param("petType", "hamster"))
			.andExpect(MockMvcResultMatchers.model().attributeHasErrors("beautyCenter")).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("beautyCenter", "name")).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("beauty-centers/createOrUpdateBeautyCenterForm"));
	}

	/* GetMapping /beauticians/{beauticianId}/beauty-centers */

	//Caso Positivo. Mostrar los beauty centers asociados al id del autenticado

	@WithMockUser(username = "f", roles = {
		"beautician"
	}, password = "f")
	@Test
	void testInitShowForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/beauticians/{beauticianId}/beauty-centers", 1)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("beautyCenterList"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("beauty-centers"));
	}

	//Caso Negativo. Mostrar los beauty centers asociados a un id distinto al del autenticado

	@WithMockUser(username = "f", roles = {
		"beautician"
	}, password = "f")
	@Test
	void testInitShowFormError() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/beauticians/{beauticianId}/beauty-centers", 0)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	//	Caso positivo: Un esteticista animal pulsa eliminar sobre un servicio suyo y este se elimina satisfactoriamente.
	@WithMockUser(username = "f", roles = {
		"beautician"
	}, password = "f")
	@Test
	void testProcessDeleteBeautyCenterFormSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/beauticians/{beauticianId}/beauty-centers/{beautyCenterId}/delete", 1, 1).with(SecurityMockMvcRequestPostProcessors.csrf())).andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/beauticians/{beauticianId}"));
	}

	//	Caso negativo: Un esteticista animal accede a eliminar un servicio que no es suyo mediante la url y la aplicación le lanza un mensaje de error.
	@WithMockUser(username = "f", roles = {
		"beautician"
	}, password = "f")
	@Test
	void testProcessDeleteBeautyCenterFormWithErrors() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/beauticians/{beauticianId}/beauty-centers/{beautyCenterId}/delete", 2, 1).with(SecurityMockMvcRequestPostProcessors.csrf())).andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
	}

	/*
	 * Tests historia de usuario 3
	 */
	@WithMockUser(username = "f", roles = {
		"beautician"
	}, password = "f")
	@Test
	void testInitUpdateBeautyCenterForm() throws Exception {
		PetType pet = new PetType();
		pet.setName("hamster");
		this.mockMvc.perform(MockMvcRequestBuilders.get("/beauticians/{beauticianId}/beauty-centers/{beautyCenterId}/edit", 1, 1)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("beautyCenter"))
			.andExpect(MockMvcResultMatchers.view().name("beauty-centers/createOrUpdateBeautyCenterForm"));
	}
	//Caso Positivo
	@WithMockUser(username = "f", roles = {
		"beautician"
	}, password = "f")
	@Test
	void testProcessUpdateBeautyCenterFormSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/beauticians/{beauticianId}/beauty-centers/{beautyCenterId}/edit", 1, 1).with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "beautycentermodified")
			.param("description", "descriptionmodified").param("petType", "dog")).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/beauticians/{beauticianId}"));
	}

	@WithMockUser(username = "f", roles = {
		"beautician"
	}, password = "f")
	@Test
	void testProcessUpdateBeautyCenterFormSuccessNoDescription() throws Exception {
		this.mockMvc
			.perform(
				MockMvcRequestBuilders.post("/beauticians/{beauticianId}/beauty-centers/{beautyCenterId}/edit", 1, 1).with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "beautycentermodified").param("description", "").param("petType", "dog"))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/beauticians/{beauticianId}"));
	}

	//Casos negativos
	@WithMockUser(username = "f", roles = {
		"beautician"
	}, password = "f")
	@Test
	void testProcessUpdateBeautyCenterFormHasErrorsPetType() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/beauticians/{beauticianId}/beauty-centers/{beautyCenterId}/edit", 1, 1).with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "beautycentermodified").param("description", "descriptionmodified")
				.param("petType", "crocodile"))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeHasErrors("beautyCenter")).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("beautyCenter", "petType"))
			.andExpect(MockMvcResultMatchers.view().name("beauty-centers/createOrUpdateBeautyCenterForm"));
	}

	@WithMockUser(username = "f", roles = {
		"beautician"
	}, password = "f")
	@Test
	void testProcessUpdateBeautyCenterFormHasErrorsEmptyAttribute() throws Exception {

		this.mockMvc
			.perform(
				MockMvcRequestBuilders.post("/beauticians/{beauticianId}/beauty-centers/{beautyCenterId}/edit", 1, 1).with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "").param("description", "descriptionmodified").param("petType", "dog"))
			.andExpect(MockMvcResultMatchers.model().attributeHasErrors("beautyCenter")).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("beautyCenter", "name")).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("beauty-centers/createOrUpdateBeautyCenterForm"));
	}

}

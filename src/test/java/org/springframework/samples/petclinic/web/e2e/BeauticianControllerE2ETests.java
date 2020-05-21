
package org.springframework.samples.petclinic.web.e2e;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.PetType;
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
public class BeauticianControllerE2ETests {

	private static final int		TEST_BEAUTICIAN_ID	= 1;

	private static final int		TEST_BEAUTICIAN_ID2	= 2;

	private MockMvc					mockMvc;

	@Autowired
	private WebApplicationContext	webApplicationContext;


	@BeforeEach
	public void setUp() {

		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
	}
	@WithMockUser(username = "f", roles = {
		"beautician"
	}, password = "f")
	@Test
	void testInitUpdateBeauticianForm() throws Exception {
		Collection<PetType> specialization = new ArrayList<PetType>();
		PetType pet = new PetType();
		pet.setId(1);
		pet.setName("cat");
		PetType pet2 = new PetType();
		pet2.setId(3);
		pet2.setName("lizard");
		PetType pet3 = new PetType();
		pet3.setId(4);
		pet3.setName("snake");
		PetType pet4 = new PetType();
		pet4.setId(6);
		pet4.setName("bird");
		PetType pet5 = new PetType();
		pet5.setId(6);
		pet5.setName("hamster");
		specialization.add(pet);
		specialization.add(pet2);
		specialization.add(pet3);
		specialization.add(pet4);
		specialization.add(pet5);
		this.mockMvc.perform(MockMvcRequestBuilders.get("/beauticians/{beauticianId}/edit", BeauticianControllerE2ETests.TEST_BEAUTICIAN_ID)).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeExists("beautician")).andExpect(MockMvcResultMatchers.model().attribute("beautician", Matchers.hasProperty("lastName", Matchers.is("f"))))
			.andExpect(MockMvcResultMatchers.model().attribute("beautician", Matchers.hasProperty("firstName", Matchers.is("f"))))
			.andExpect(MockMvcResultMatchers.model().attribute("beautician", Matchers.hasProperty("specializations", Matchers.hasSize(specialization.size())))).andExpect(MockMvcResultMatchers.view().name("beauticians/updateBeauticianForm"));
	}

	@WithMockUser(username = "f", roles = {
		"beautician"
	}, password = "f")
	@Test
	void testProcessUpdateBeauticianFormSuccess() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/beauticians/{beauticianId}/edit", BeauticianControllerE2ETests.TEST_BEAUTICIAN_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("firstName", "Michaelangelo").param("lastName", "Skere")
				.param("specializations", "dog").param("user.username", "MicSker").param("user.password", "1234"))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/beauticians/{beauticianId}"));
	}

	@WithMockUser(username = "MicSker", roles = {
		"beautician"
	}, password = "1234")
	@Test
	void testProcessUpdateBeauticianFormHasErrorsFirstName() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/beauticians/{beauticianId}/edit", BeauticianControllerE2ETests.TEST_BEAUTICIAN_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("lastName", "Skere").param("specializations", "cat"))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeHasErrors("beautician")).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("beautician", "firstName"))
			.andExpect(MockMvcResultMatchers.view().name("beauticians/updateBeauticianForm"));
	}

	@WithMockUser(username = "f", roles = {
		"beautician"
	}, password = "f")
	@Test
	void testProcessUpdateBeauticianFormHasErrorsSpecialization() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/beauticians/{beauticianId}/edit", BeauticianControllerE2ETests.TEST_BEAUTICIAN_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("firstName", "Michael").param("lastName", "Skere")
				.param("specializations", "reptil"))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeHasErrors("beautician")).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("beautician", "specializations"))
			.andExpect(MockMvcResultMatchers.view().name("beauticians/updateBeauticianForm"));
	}

	@WithMockUser(username = "f", roles = {
		"beautician"
	}, password = "f")
	@Test
	void testShowBeautician() throws Exception {
		Collection<PetType> specialization = new ArrayList<PetType>();
		PetType pet = new PetType();
		pet.setId(1);
		pet.setName("cat");
		PetType pet2 = new PetType();
		pet2.setId(3);
		pet2.setName("lizard");
		PetType pet3 = new PetType();
		pet3.setId(4);
		pet3.setName("snake");
		PetType pet4 = new PetType();
		pet4.setId(6);
		pet4.setName("bird");
		PetType pet5 = new PetType();
		pet5.setId(6);
		pet5.setName("hamster");
		specialization.add(pet);
		specialization.add(pet2);
		specialization.add(pet3);
		specialization.add(pet4);
		specialization.add(pet5);

		this.mockMvc.perform(MockMvcRequestBuilders.get("/beauticians/{beauticianId}", BeauticianControllerE2ETests.TEST_BEAUTICIAN_ID)).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attribute("beautician", Matchers.hasProperty("firstName", Matchers.is("f")))).andExpect(MockMvcResultMatchers.model().attribute("beautician", Matchers.hasProperty("lastName", Matchers.is("f"))))
			.andExpect(MockMvcResultMatchers.model().attribute("beautician", Matchers.hasProperty("specializations", Matchers.hasSize(specialization.size())))).andExpect(MockMvcResultMatchers.view().name("beauticians/beauticianDetails"));
	}

	@WithMockUser(username = "LolIn", roles = {
		"beautician"
	}, password = "1234")
	@Test
	void testShowBeauticianIncorrectLogIn() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/beauticians/{beauticianId}", BeauticianControllerE2ETests.TEST_BEAUTICIAN_ID).with(SecurityMockMvcRequestPostProcessors.csrf())).andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	// TESTS HISTORIA DE USUARIO 9 (Métodos en BeauticianController)

	/* SEARCH BEAUTY DATES /beauticians/searchBeautyDates/{beauticianUsername} */

	// Caso positivo: redirige a /beauticians/searchDates/1

	@WithMockUser(username = "f", roles = {
		"beautician"
	}, password = "f")
	@Test
	void testSearchBeautyDates() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/beauticians/searchBeautyDates/{beauticianUsername}", "f")).andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/beauticians/searchDates/1"));
	}

	/* SEARCH BEAUTY DATES ID /beauticians/searchDates/{beauticianId} */

	// Caso positivo: Devuelve la vista "beauticians/searchBeautyDates" y existe "beautician" en el modelo (la clave "beautician" se genera automaticamente, mirar addAttribute de ModelMap.class)

	@WithMockUser(username = "f", roles = {
		"beautician"
	}, password = "f")
	@Test
	void testSearchBeautyDatesId() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/beauticians/searchDates/{beauticianId}", BeauticianControllerE2ETests.TEST_BEAUTICIAN_ID)).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("beauticians/searchBeautyDates")).andExpect(MockMvcResultMatchers.model().attributeExists("beautician"));
	}

	// Caso negativo: El usuario no tiene permiso para acceder y se le redirige a la página de excepción (COMPROBAMOS QUE NOS REDIRIGE BIEN SI SALTA EXCEPCION DE isAuthor)
	@WithMockUser(username = "f", roles = {
		"beautician"
	}, password = "f")
	@Test
	void testSearchBeautyDatesIdNoPermission() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/beauticians/searchDates/{beauticianId}", BeauticianControllerE2ETests.TEST_BEAUTICIAN_ID2).with(SecurityMockMvcRequestPostProcessors.csrf()))
			.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	/* SHOW BEAUTY DATES /beauticians/{beauticianId}/beautyDates/{date}/{hour} */

	// Caso positivo: Devuelve la vista "beauticians/beautyDatesList" y existe "beautyDates" en el modelo
	@WithMockUser(username = "f", roles = {
		"beautician"
	}, password = "f")
	@Test
	void testShowBeautyDates() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/beauticians/{beauticianId}/beautyDates/{date}/{hour}", BeauticianControllerE2ETests.TEST_BEAUTICIAN_ID, LocalDate.now().plusDays(10).toString(), 12)).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("beauticians/beautyDatesList")).andExpect(MockMvcResultMatchers.model().attributeExists("beautyDates"));
	}

	// Caso negativo: El usuario no tiene permiso para acceder y se le redirige a la página de excepción
	@WithMockUser(username = "f", roles = {
		"beautician"
	}, password = "f")
	@Test
	void testShowBeautyDatesNoPermission() throws Exception {

		this.mockMvc
			.perform(MockMvcRequestBuilders.get("/beauticians/{beauticianId}/beautyDates/{date}/{hour}", BeauticianControllerE2ETests.TEST_BEAUTICIAN_ID2, LocalDate.now().plusDays(10).toString(), 12).with(SecurityMockMvcRequestPostProcessors.csrf()))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
	}

	// Caso negativo: la fecha introducida es una fecha pasada
	@WithMockUser(username = "f", roles = {
		"beautician"
	}, password = "f")
	@Test
	void testShowBeautyDatesWrongDate() throws Exception {

		this.mockMvc
			.perform(MockMvcRequestBuilders.get("/beauticians/{beauticianId}/beautyDates/{date}/{hour}", BeauticianControllerE2ETests.TEST_BEAUTICIAN_ID, LocalDate.now().minusDays(10).toString(), 12).with(SecurityMockMvcRequestPostProcessors.csrf()))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
	}
}


package org.springframework.samples.petclinic.web.e2e;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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
public class ProductControllerE2ETests {

	private static final int		TEST_BEAUTY_CENTER_ID	= 1;
	private static final int		TEST_BEAUTICIAN_ID		= 1;
	private static final int		TEST_PRODUCT_ID			= 1;

	private MockMvc					mockMvc;

	@Autowired
	private WebApplicationContext	webApplicationContext;


	@BeforeEach
	public void setUp() {

		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
	}

	// TESTS HISTORIA DE USUARIO 5

	// GET MAPPING DE CREATE PRODUCT (Con id de autenticado correcto)
	@WithMockUser(username = "f", roles = {
		"beautician"
	}, password = "f")
	@Test
	void testProductInitCreationForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/beauticians/{beauticianId}/products/new", ProductControllerE2ETests.TEST_BEAUTICIAN_ID)).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeExists("product")).andExpect(MockMvcResultMatchers.view().name("products/createOrUpdateProduct"));
	}

	// GET MAPPING DE CREATE PRODUCT (Con id de autenticado incorrecto)
	@WithMockUser(username = "g", roles = {
		"beautician"
	}, password = "g")
	@Test
	void testProductInitCreationFormError() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/beauticians/{beauticianId}/products/new", 1)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	// POST DE CREATE PRODUCT (Caso positivo)
	@WithMockUser(username = "f", roles = {
		"beautician"
	}, password = "f")
	@Test
	void testProcessCreationFormSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/beauticians/{beauticianId}/products/new", ProductControllerE2ETests.TEST_BEAUTICIAN_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "producto")
			.param("description", "description").param("avaliable", "true").param("type", "cat")).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/"));
	}

	// POST DE CREATE PRODUCT (Faltan campos obligatorios (description) por rellenar)
	@WithMockUser(username = "f", roles = {
		"beautician"
	}, password = "f")
	@Test
	void testProcessCreationEmptyImputsHasErrors() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/beauticians/{beauticianId}/products/new", ProductControllerE2ETests.TEST_BEAUTICIAN_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "producto").param("avaliable", "true").param("type",
				"cat"))
			.andExpect(MockMvcResultMatchers.model().attributeHasErrors("product")).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("product", "description")).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("products/createOrUpdateProduct"));
	}

	// POST DE CREATE PRODUCT (Hay campos erroneos (PetType y avaliable))
	@WithMockUser(username = "f", roles = {
		"beautician"
	}, password = "f")
	@Test
	void testProcessCreationWrongImputsHasErrors() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/beauticians/{beauticianId}/products/new", ProductControllerE2ETests.TEST_BEAUTICIAN_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "producto").param("avaliable", "true")
				.param("description", "descripcion").param("type", "dinosaurio"))
			.andExpect(MockMvcResultMatchers.model().attributeHasErrors("product")).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("product", "type")).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("products/createOrUpdateProduct"));
	}

	// GET SHOW PRODUCT LIST (Caso positivo)
	@WithMockUser(username = "f", roles = {
		"beautician"
	}, password = "f")
	@Test
	void testShowProduct() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/{beautyCenterId}/products", ProductControllerE2ETests.TEST_BEAUTY_CENTER_ID)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("products/productList"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("products"));
	}

	// TESTS HISTORIA DE USUARIO 6

	/* GetMapping "/{beautyCenterId}/products/{productId}/edit" */

	// Caso positivo. Siendo el dueño de un producto accedo a la pagina de editarlo
	@WithMockUser(username = "f", roles = {
		"beautician"
	}, password = "f")
	@Test
	void testInitUpdateForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/{beautyCenterId}/products/{productId}/edit", ProductControllerE2ETests.TEST_BEAUTY_CENTER_ID, ProductControllerE2ETests.TEST_PRODUCT_ID)).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("products/createOrUpdateProduct")).andExpect(MockMvcResultMatchers.model().attributeExists("product")).andExpect(MockMvcResultMatchers.model().attributeExists("specialization"));
	}

	//Caso negativo. No siendo el dueño de un producto accedo a la página de editarlo

	@WithMockUser(username = "g", roles = {
		"beautician"
	}, password = "g")
	@Test
	void testInitUpdateFormError() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/{beautyCenterId}/products/{productId}/edit", ProductControllerE2ETests.TEST_BEAUTY_CENTER_ID, ProductControllerE2ETests.TEST_PRODUCT_ID)).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	/* PostMapping "/{beautyCenterId}/products/{productId}/edit" */

	//Caso positivo. Lo edito todo correcto

	@WithMockUser(username = "f", roles = {
		"beautician"
	}, password = "f")
	@Test
	void testProcessUpdateFormSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/{beautyCenterId}/products/{productId}/edit", ProductControllerE2ETests.TEST_BEAUTY_CENTER_ID, ProductControllerE2ETests.TEST_PRODUCT_ID).with(SecurityMockMvcRequestPostProcessors.csrf())
			.param("name", "other name").param("type", "cat").param("description", "new description").param("avalaible", "true")).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/"));
	}

	//Caso negativo. No introduzco descripción

	@WithMockUser(username = "f", roles = {
		"beautician"
	}, password = "f")
	@Test
	void testProcessUpdateFormNoDescriptionError() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/{beautyCenterId}/products/{productId}/edit", ProductControllerE2ETests.TEST_BEAUTY_CENTER_ID, ProductControllerE2ETests.TEST_PRODUCT_ID).with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("name", "other name").param("type", "cat").param("avalaible", "true"))
			.andExpect(MockMvcResultMatchers.model().attributeHasErrors("product")).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("product", "description")).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("products/createOrUpdateProduct"));
	}

	// TESTS HISTORIA DE USUARIO 7

	// ELIMINAR PRODUCTO (Caso positivo)
	@WithMockUser(username = "f", roles = {
		"beautician"
	}, password = "f")
	@Test
	void testProcessDeleteProductFormSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/{beautyCenterId}/products/{productId}/delete", ProductControllerE2ETests.TEST_BEAUTICIAN_ID, ProductControllerE2ETests.TEST_BEAUTY_CENTER_ID).with(SecurityMockMvcRequestPostProcessors.csrf()))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/"));
	}

	// ELIMINAR PRODUCTO (Caso negativo, usuario sin permiso)
	@WithMockUser(username = "g", roles = {
		"beautician"
	}, password = "g")
	@Test
	void testProcessDeleteProductFormErrors() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/{beautyCenterId}/products/{productId}/delete", 2, ProductControllerE2ETests.TEST_BEAUTY_CENTER_ID)).andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));

	}

}

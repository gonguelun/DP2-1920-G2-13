
package org.springframework.samples.petclinic.web;

import java.util.ArrayList;
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
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Product;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.BeauticianService;
import org.springframework.samples.petclinic.service.BeautyCenterService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.ProductService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 * Test class for the {@link PetController}
 *
 * @author Colin But
 */
@WebMvcTest(controllers = ProductController.class, includeFilters = @ComponentScan.Filter(value = PetTypeFormatter.class, type = FilterType.ASSIGNABLE_TYPE),
	excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
public class ProductControllerTests {

	private static final int	TEST_PRODUCT_ID			= 1;
	private static final int	TEST_BEAUTICIAN_ID		= 1;
	private static final int	TEST_BEAUTY_CENTER_ID	= 1;

	@Autowired
	private ProductController	productController;

	@MockBean
	private ProductService		productService;

	@MockBean
	private BeauticianService	beauticianService;

	@MockBean
	BeautyCenterService			beautyCenterService;

	@MockBean
	PetService					petService;

	@Autowired
	private MockMvc				mockMvc;


	@BeforeEach
	void setup() {
		Product producto = new Product();
		PetType petType = new PetType();
		List<PetType> petTypes = new ArrayList<>();
		User user = new User();
		Beautician beautician = new Beautician();
		BeautyCenter beautyCenter = new BeautyCenter();

		petType.setId(1);
		petType.setName("cat");
		petTypes.add(petType);

		user.setId(1);
		user.setEnabled(true);
		user.setUsername("beautician1");
		user.setPassword("123");

		beautician.setUser(user);
		beautician.setSpecializations(petTypes);
		beautician.setId(ProductControllerTests.TEST_BEAUTICIAN_ID);

		beautyCenter.setId(ProductControllerTests.TEST_BEAUTY_CENTER_ID);
		beautyCenter.setName("BeautyCenter1");
		beautyCenter.setDescription("Description1");
		beautyCenter.setPetType(petType);
		beautyCenter.setBeautician(beautician);

		producto.setId(ProductControllerTests.TEST_PRODUCT_ID);
		producto.setName("producto1");
		producto.setDescription("description1");
		producto.setBeautician(beautician);
		producto.setAvaliable(true);
		producto.setType(beautyCenter.getPetType());

		BDDMockito.given(this.petService.findPetTypes()).willReturn(Lists.newArrayList(petTypes));
		BDDMockito.given(this.productService.findSpecializationsByBeauticianId(ProductControllerTests.TEST_BEAUTICIAN_ID)).willReturn(petTypes);
		BDDMockito.given(this.productService.findBeauticianById(ProductControllerTests.TEST_BEAUTICIAN_ID)).willReturn(beautician);
		BDDMockito.given(this.beautyCenterService.findBeautyCenterByBeautyCenterId(ProductControllerTests.TEST_BEAUTY_CENTER_ID)).willReturn(beautyCenter);
		BDDMockito.given(this.productService.findProductsByPet(beautyCenter.getPetType().getId())).willReturn(Lists.newArrayList(producto));

	}

	// TESTS HISTORIA DE USUARIO 5

	// GET MAPPING DE CREATE PRODUCT (Con id de autenticado correcto)
	@WithMockUser(username = "beautician1", roles = {
		"beautician"
	}, password = "123")
	@Test
	void testProductInitCreationForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/beauticians/{beauticianId}/products/new", ProductControllerTests.TEST_BEAUTICIAN_ID)).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeExists("product")).andExpect(MockMvcResultMatchers.view().name("products/createOrUpdateProduct"));
	}

	// GET MAPPING DE CREATE PRODUCT (Con id de autenticado incorrecto)
	@WithMockUser(username = "beautician1", roles = {
		"beautician"
	}, password = "123")
	@Test
	void testProductInitCreationFormError() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/beauticians/{beauticianId}/products/new", 0)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	// POST DE CREATE PRODUCT (Caso positivo)
	@WithMockUser(username = "beautician1", roles = {
		"beautician"
	}, password = "123")
	@Test
	void testProcessCreationFormSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/beauticians/{beauticianId}/products/new", ProductControllerTests.TEST_BEAUTICIAN_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "producto").param("description", "description")
			.param("avaliable", "true").param("type", "cat")).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/"));
	}

	// POST DE CREATE PRODUCT (Faltan campos obligatorios (description) por rellenar)
	@WithMockUser(username = "beautician1", roles = {
		"beautician"
	}, password = "123")
	@Test
	void testProcessCreationEmptyImputsHasErrors() throws Exception {
		this.mockMvc
			.perform(
				MockMvcRequestBuilders.post("/beauticians/{beauticianId}/products/new", ProductControllerTests.TEST_BEAUTICIAN_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "producto").param("avaliable", "true").param("type", "cat"))
			.andExpect(MockMvcResultMatchers.model().attributeHasErrors("product")).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("product", "description")).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("products/createOrUpdateProduct"));
	}

	// POST DE CREATE PRODUCT (Hay campos erroneos (PetType y avaliable))
	@WithMockUser(username = "beautician1", roles = {
		"beautician"
	}, password = "123")
	@Test
	void testProcessCreationWrongImputsHasErrors() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/beauticians/{beauticianId}/products/new", ProductControllerTests.TEST_BEAUTICIAN_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "producto").param("avaliable", "true")
				.param("description", "descripcion").param("type", "dinosaurio"))
			.andExpect(MockMvcResultMatchers.model().attributeHasErrors("product")).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("product", "type")).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("products/createOrUpdateProduct"));
	}

	// GET SHOW PRODUCT LIST (Caso positivo)
	@WithMockUser(username = "beautician1", roles = {
		"beautician"
	}, password = "123")
	@Test
	void testShowProduct() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/{beautyCenterId}/products", ProductControllerTests.TEST_BEAUTY_CENTER_ID)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("products/productList"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("products"));
	}

}

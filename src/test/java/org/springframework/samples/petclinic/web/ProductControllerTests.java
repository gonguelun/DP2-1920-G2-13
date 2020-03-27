
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
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.BeauticianService;
import org.springframework.samples.petclinic.service.BeautyCenterService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.ProductService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = ProductController.class, includeFilters = @ComponentScan.Filter(value = PetTypeFormatter.class, type = FilterType.ASSIGNABLE_TYPE),
	excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
public class ProductControllerTests {

	private static final int	TEST_BEAUTYCENTER_ID	= 1;

	private static final int	TEST_BEAUTICIAN_ID		= 1;

	private static final int	TEST_PRODUCT_ID			= 1;

	@Autowired
	private ProductController	productController;

	@MockBean
	private ProductService		productService;

	@MockBean
	private PetService			petService;

	@MockBean
	private UserService			userService;

	@MockBean
	private BeautyCenterService	beautyCenterService;

	@MockBean
	private BeauticianService	beauticianService;

	@MockBean
	private AuthoritiesService	authoritiesService;

	@Autowired
	private MockMvc				mockMvc;


	/* Test unitarios para la historia de usuario 6 */

	@BeforeEach
	void setup() {
		PetType cat = new PetType();
		cat.setId(3);
		cat.setName("hamster");
		List<PetType> temp = new ArrayList<>();
		temp.add(cat);

		User user = new User();
		user.setId(1);
		user.setEnabled(true);
		user.setUsername("beautician1");
		user.setPassword("123");

		Beautician beautician = new Beautician();
		beautician.setId(ProductControllerTests.TEST_BEAUTICIAN_ID);
		beautician.setFirstName("juan");
		beautician.setLastName("aurora");
		beautician.setSpecializations(temp);
		beautician.setUser(user);

		User user2 = new User();
		user2.setId(2);
		user2.setEnabled(true);
		user2.setUsername("beautician2");
		user2.setPassword("123");

		Beautician beautician2 = new Beautician();
		beautician2.setId(2);
		beautician2.setFirstName("juan");
		beautician2.setLastName("aurora");
		beautician2.setSpecializations(temp);
		beautician2.setUser(user);

		BeautyCenter beautyCenter = new BeautyCenter();
		beautyCenter.setId(ProductControllerTests.TEST_BEAUTYCENTER_ID);
		beautyCenter.setName("beautycenter1");
		beautyCenter.setDescription("prueba1");
		beautyCenter.setPetType(cat);
		beautyCenter.setBeautician(beautician);

		Product product1 = new Product();
		product1.setAvaliable(true);
		product1.setBeautician(beautician);
		product1.setDescription("descrip del producto 1");
		product1.setId(ProductControllerTests.TEST_PRODUCT_ID);
		product1.setName("product 1");
		product1.setType(cat);

		BDDMockito.given(this.petService.findPetTypes()).willReturn(Lists.newArrayList(cat));
		BDDMockito.given(this.productService.findProductById(ProductControllerTests.TEST_PRODUCT_ID)).willReturn(product1);
		BDDMockito.given(this.productService.findSpecializationsByBeauticianId(ProductControllerTests.TEST_BEAUTICIAN_ID)).willReturn(Lists.newArrayList(cat));
		BDDMockito.given(this.productService.findBeauticianByProductId(ProductControllerTests.TEST_PRODUCT_ID)).willReturn(beautician);

	}

	/* GetMapping "/{beautyCenterId}/products/{productId}/edit" */

	// Caso positivo. Siendo el dueño de un producto accedo a la pagina de editarlo

	@WithMockUser(username = "beautician1", roles = {
		"beautician"
	}, password = "123")
	@Test
	void testInitCreationForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/{beautyCenterId}/products/{productId}/edit", ProductControllerTests.TEST_BEAUTYCENTER_ID, ProductControllerTests.TEST_PRODUCT_ID)).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("products/createOrUpdateProduct")).andExpect(MockMvcResultMatchers.model().attributeExists("product")).andExpect(MockMvcResultMatchers.model().attributeExists("specialization"));
	}

	//Caso negativo. No siendo el dueño de un producto accedo a la página de editarlo

	@WithMockUser(username = "beautician2", roles = {
		"beautician"
	}, password = "123")
	@Test
	void testInitCreationFormError() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/{beautyCenterId}/products/{productId}/edit", ProductControllerTests.TEST_BEAUTYCENTER_ID, ProductControllerTests.TEST_PRODUCT_ID)).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	/* PostMapping "/{beautyCenterId}/products/{productId}/edit" */

	//Caso positivo. Lo edito todo correcto

	@WithMockUser(username = "beautician1", roles = {
		"beautician"
	}, password = "123")
	@Test
	void testProcessUpdateFormSuccess() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/{beautyCenterId}/products/{productId}/edit", ProductControllerTests.TEST_BEAUTYCENTER_ID, ProductControllerTests.TEST_PRODUCT_ID).with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("name", "other name").param("type", "hamster").param("description", "new description").param("avalaible", "true"))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/"));
	}

	//Caso negativo. No introduzco descripción

	@WithMockUser(username = "beautician1", roles = {
		"beautician"
	}, password = "123")
	@Test
	void testProcessUpdateFormNoDescriptionError() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/{beautyCenterId}/products/{productId}/edit", ProductControllerTests.TEST_BEAUTYCENTER_ID, ProductControllerTests.TEST_PRODUCT_ID).with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("name", "other name").param("type", "hamster").param("avalaible", "true"))
			.andExpect(MockMvcResultMatchers.model().attributeHasErrors("product")).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("product", "description")).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("products/createOrUpdateProduct"));
	}

}

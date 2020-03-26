
package org.springframework.samples.petclinic.web;

import java.util.ArrayList;
import java.util.Collection;

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
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.BeauticianService;
import org.springframework.samples.petclinic.service.BeautyCenterService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.UserService;
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

	@Autowired
	private BeautyCenterController	beautyCenterController;

	@MockBean
	private BeautyCenterService		beautyService;

	@MockBean
	private BeauticianService		beauticianService;

	@MockBean
	private UserService				userService;

	@MockBean
	private AuthoritiesService		authoritiesService;

	@MockBean
	private PetService				petService;

	@Autowired
	private MockMvc					mockMvc;

	private User					user1;

	private Beautician				beautician1;

	private BeautyCenter			beautyCenter1;

	private User					user2;

	private Beautician				beautician2;

	private BeautyCenter			beautyCenter2;


	@BeforeEach
	void setup() {
		this.user1 = new User();
		this.user1.setId(1);
		this.user1.setEnabled(true);
		this.user1.setUsername("beautician1");
		this.user1.setPassword("beautician1");

		this.beautician1 = new Beautician();
		this.beautician1.setId(BeautyCenterControllerTests.TEST_BEAUTICIAN_ID);
		this.beautician1.setFirstName("z");
		this.beautician1.setLastName("z");

		Collection<PetType> specializations = new ArrayList<>();
		PetType cat = new PetType();
		cat.setId(1);
		cat.setName("gato");
		specializations.add(cat);

		this.beautician1.setSpecializations(specializations);
		this.beautician1.setUser(this.user1);

		this.beautyCenter1 = new BeautyCenter();
		this.beautyCenter1.setId(BeautyCenterControllerTests.TEST_BEAUTYCENTER_ID);
		this.beautyCenter1.setName("beautycenter1");
		this.beautyCenter1.setDescription("prueba1");
		this.beautyCenter1.setPetType(cat);
		this.beautyCenter1.setBeautician(this.beautician1);

		this.user2 = new User();
		this.user2.setId(2);
		this.user2.setEnabled(true);
		this.user2.setUsername("beautician2");
		this.user2.setPassword("beautician2");

		this.beautician2 = new Beautician();
		this.beautician2.setId(BeautyCenterControllerTests.TEST_BEAUTICIAN2_ID);
		this.beautician2.setFirstName("x");
		this.beautician2.setLastName("x");

		PetType dog = new PetType();
		dog.setId(2);
		dog.setName("perro");
		specializations.add(dog);

		this.beautician2.setSpecializations(specializations);
		this.beautician2.setUser(this.user2);

		this.beautyCenter2 = new BeautyCenter();
		this.beautyCenter2.setId(BeautyCenterControllerTests.TEST_BEAUTYCENTER2_ID);
		this.beautyCenter2.setName("beautycenter2");
		this.beautyCenter2.setDescription("prueba2");
		this.beautyCenter2.setPetType(dog);
		this.beautyCenter2.setBeautician(this.beautician2);

		BDDMockito.given(this.petService.findPetTypes()).willReturn(specializations);
		BDDMockito.given(this.beauticianService.findBeauticianById(BeautyCenterControllerTests.TEST_BEAUTICIAN_ID)).willReturn(this.beautician1);
		BDDMockito.given(this.beautyService.findById(BeautyCenterControllerTests.TEST_BEAUTYCENTER_ID)).willReturn(this.beautyCenter1);

		BDDMockito.given(this.beauticianService.findBeauticianById(BeautyCenterControllerTests.TEST_BEAUTICIAN2_ID)).willReturn(this.beautician2);
		BDDMockito.given(this.beautyService.findById(BeautyCenterControllerTests.TEST_BEAUTYCENTER2_ID)).willReturn(this.beautyCenter2);
	}

	//	Caso positivo: Un esteticista animal pulsa eliminar sobre un servicio suyo y este se elimina satisfactoriamente.
	@WithMockUser(username = "beautician1", roles = {
		"beautician"
	}, password = "beautician1")
	@Test
	void testProcessDeleteBeautyCenterFormSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/beauticians/{beauticianId}/beauty-centers/{beautyCenterId}/delete", BeautyCenterControllerTests.TEST_BEAUTICIAN_ID, BeautyCenterControllerTests.TEST_BEAUTYCENTER_ID)
			.with(SecurityMockMvcRequestPostProcessors.csrf())).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/beauticians/{beauticianId}"));
	}

	//	Caso negativo: Un esteticista animal accede a eliminar un servicio que no es suyo mediante la url y la aplicación le lanza un mensaje de error.
	@WithMockUser(username = "beautician1", roles = {
		"beautician"
	}, password = "beautician1")
	@Test
	void testProcessDeleteBeautyCenterFormWithErrors() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/beauticians/{beauticianId}/beauty-centers/{beautyCenterId}/delete", BeautyCenterControllerTests.TEST_BEAUTICIAN2_ID, BeautyCenterControllerTests.TEST_BEAUTYCENTER2_ID)
			.with(SecurityMockMvcRequestPostProcessors.csrf())).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
	}

}

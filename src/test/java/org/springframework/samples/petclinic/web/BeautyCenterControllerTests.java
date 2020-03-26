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
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.BeauticianService;
import org.springframework.samples.petclinic.service.BeautyCenterService;
import org.springframework.samples.petclinic.service.PetService;
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
	private BeautyCenterService		beautyCenterService;

	@MockBean
	private BeauticianService		beauticianService;

	@MockBean
	private PetService				petService;

	@Autowired
	private MockMvc					mockMvc;

	private User					user2;

	private Beautician				beautician2;

	private BeautyCenter			beautyCenter2;


	/* Test controlador hisoria de usuario 2 */

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
		beautician.setId(1);
		beautician.setFirstName("juan");
		beautician.setLastName("aurora");
		beautician.setUser(user);
    
    beautyCenter = new BeautyCenter();
		beautyCenter.setId(BeautyCenterControllerTests.TEST_BEAUTYCENTER_ID);
		beautyCenter.setName("beautycenter1");
		beautyCenter.setDescription("prueba1");
		beautyCenter.setPetType(cat);
		beautyCenter.setBeautician(beautician);
    
    PetType dog = new PetType();
		dog.setId(4);
		dog.setName("perro");
		temp.add(dog);
    
    user2 = new User();
		user2.setId(2);
		user2.setEnabled(true);
		user2.setUsername("beautician2");
		user2.setPassword("beautician2");

		beautician2 = new Beautician();
		beautician2.setId(BeautyCenterControllerTests.TEST_BEAUTICIAN2_ID);
		beautician2.setFirstName("x");
		beautician2.setLastName("x");
		beautician2.setUser(user2);

		beautyCenter2 = new BeautyCenter();
		beautyCenter2.setId(BeautyCenterControllerTests.TEST_BEAUTYCENTER3_ID);
		beautyCenter2.setName("beautycenter2");
		beautyCenter2.setDescription("prueba2");
		beautyCenter2.setPetType(dog);
		beautyCenter2.setBeautician(this.beautician2);

		BDDMockito.given(this.petService.findPetTypes()).willReturn(Lists.newArrayList(cat));
    BDDMockito.given(this.petService.findPetTypes()).willReturn(Lists.newArrayList(dog));
		BDDMockito.given(this.beauticianService.findBeauticianById(BeautyCenterControllerTests.TEST_BEAUTICIAN_ID)).willReturn(beautician);
    BDDMockito.given(this.beauticianService.findBeauticianById(BeautyCenterControllerTests.TEST_BEAUTICIAN2_ID)).willReturn(beautician2);
		BDDMockito.given(this.beautyCenterService.findBeautyCenterByBeautyCenterId(BeautyCenterControllerTests.TEST_BEAUTYCENTER_ID)).willReturn(new BeautyCenter());
    BDDMockito.given(this.beautyCenterService.findBeautyCenterByBeautyCenterId(BeautyCenterControllerTests.TEST_BEAUTYCENTER2_ID)).willReturn(beautyCenter));
    BDDMockito.given(this.beautyCenterService.findBeautyCenterByBeautyCenterId(BeautyCenterControllerTests.TEST_BEAUTYCENTER3_ID)).willReturn(beautyCenter2));

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
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/beauticians/{beauticianId}/beauty-centers/new", BeautyCenterControllerTests.TEST_BEAUTICIAN_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "beauty center test 2").param("description",
				"this is a description 2"))
			.andExpect(MockMvcResultMatchers.model().attributeHasErrors("beautyCenter")).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("beautyCenter", "petType")).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("beauty-centers/createOrUpdateBeautyCenterForm"));
	}

	//El nombre es de menos de 3 caracteres

	@WithMockUser(username = "beautician1", roles = {
		"beautician"
	}, password = "123")
	@Test
	void testProcessCreationShortNameHasErrors() throws Exception {
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
		this.mockMvc.perform(MockMvcRequestBuilders.get("/beauticians/{beauticianId}/beauty-centers/{beautyCenterId}/delete", BeautyCenterControllerTests.TEST_BEAUTICIAN_ID, BeautyCenterControllerTests.TEST_BEAUTYCENTER_ID)
			.with(SecurityMockMvcRequestPostProcessors.csrf())).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/beauticians/{beauticianId}"));
	}

	//	Caso negativo: Un esteticista animal accede a eliminar un servicio que no es suyo mediante la url y la aplicación le lanza un mensaje de error.
	@WithMockUser(username = "beautician1", roles = {
		"beautician"
	}, password = "123")
	@Test
	void testProcessDeleteBeautyCenterFormWithErrors() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/beauticians/{beauticianId}/beauty-centers/{beautyCenterId}/delete", BeautyCenterControllerTests.TEST_BEAUTICIAN2_ID, BeautyCenterControllerTests.TEST_BEAUTYCENTER3_ID)
			.with(SecurityMockMvcRequestPostProcessors.csrf())).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
	}

}

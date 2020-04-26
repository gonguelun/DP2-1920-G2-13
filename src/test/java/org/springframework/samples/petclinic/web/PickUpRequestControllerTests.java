
package org.springframework.samples.petclinic.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.PickUpRequest;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.PickUpRequestService;
import org.springframework.samples.petclinic.service.exceptions.NoPetTypeException;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = PickUpRequestController.class, includeFilters = @ComponentScan.Filter(value = PetTypeFormatter.class, type = FilterType.ASSIGNABLE_TYPE), excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
public class PickUpRequestControllerTests {

	private static final int TEST_PICK_UP_REQUEST_ID = 1;

	private static final int TEST_OWNER_ID = 1;
	
	private static final String TEST_OWNER_USERNAME="User1";

	@Autowired
	private PickUpRequestController pickUpRequestController;

	@MockBean
	private PickUpRequestService pickUpRequestService;

	@MockBean
	private AuthoritiesService authoritiesService;

	@MockBean
	private OwnerService ownerService;

	@MockBean
	private PetService petService;

	@Autowired
	private MockMvc mockMvc;

	@BeforeEach
	void setup() {
		PickUpRequest pickUp = new PickUpRequest();
		pickUp.setId(1);
		pickUp.setAddress("Calle 1");
		pickUp.setDescription("descripcion");
		pickUp.setIsAccepted(false);
		Owner owner = new Owner();
		User user = new User();
		user.setUsername("User1");
		user.setPassword("1234");
		user.setEnabled(true);
		owner.setFirstName("Paco");
		owner.setLastName("Gonzalez");
		owner.setCity("Sevilla");
		owner.setAddress("Calle 1");
		owner.setId(1);
		owner.setTelephone("666777888");
		owner.setUser(user);
		pickUp.setOwner(owner);
		pickUp.setPhysicalStatus("good");
		PetType petType = new PetType();
		petType.setId(1);
		petType.setName("dog");
		pickUp.setPetType(petType);

		List<PetType> temp2 = new ArrayList<>();
		PetType dog = new PetType();
		dog.setId(1);
		dog.setName("dog");
		temp2.add(dog);

		Collection<PickUpRequest> pickUps = new ArrayList<PickUpRequest>();
		pickUps.add(pickUp);
		BDDMockito.given(this.ownerService.findOwnerById(PickUpRequestControllerTests.TEST_OWNER_ID)).willReturn(owner);
		BDDMockito.given(this.petService.findPetTypes()).willReturn(temp2);
		BDDMockito.given(this.pickUpRequestService.findPickUpRequestsByOwnerUsername(PickUpRequestControllerTests.TEST_OWNER_USERNAME)).willReturn(pickUps);
		BDDMockito.given(this.pickUpRequestService.findOwnerByUsername(PickUpRequestControllerTests.TEST_OWNER_USERNAME)).willReturn(owner);

	}

	// TESTS HISTORIA DE USUARIO 20 (Métodos en PickUpRequestController)

	/* DELETE PICK UP REQUEST /vets/pick-up-requests/{pickUpId}/delete */

	// Caso positivo: redirige a /vets/pick-up-requests

	@WithMockUser(username = "owner1", roles = { "owner" }, password = "123")
	@Test
	void testProcessDeleteBeautyCenterFormSuccess() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders
						.get("/vets/pick-up-requests/{pickUpId}/delete",
								PickUpRequestControllerTests.TEST_PICK_UP_REQUEST_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf()))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.view().name("redirect:/vets/pick-up-requests"));
	}

	// Historia de usuario 16
	// Caso positivo acceder al formulario de crear
	@WithMockUser(username = "owner1", roles = { "owner" }, password = "123")
	@Test
	void testInitCreationForm() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/owners/{ownerId}/pick-up-requests/new",
						PickUpRequestControllerTests.TEST_OWNER_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("pick-up-requests/createOrUpdatePickUpRequestForm"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("pickUpRequest"));
	}

	// Caso negativo acceder con id invalido
	@WithMockUser(username = "owner1", roles = { "owner" }, password = "123")
	@Test
	void testInitCreationFormError() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners/{ownerId}/pick-up-requests/new", 2))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	// Caso positivo creacion con exito
	@WithMockUser(username = "owner1", roles = { "owner" }, password = "123")
	@Test
	void testProcessCreationFormSuccess() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders
						.post("/owners/{ownerId}/pick-up-requests/new", PickUpRequestControllerTests.TEST_OWNER_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf()).param("description", "descripcion")
						.param("physicalStatus", "good").param("address", "Calle 1").param("petType", "dog")
						.param("isAccepted", "true"))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.view().name("redirect:/"));
	}

	// Caso positivo creacion sin descripcion
	@WithMockUser(username = "owner1", roles = { "owner" }, password = "123")
	@Test
	void testProcessCreationFormNoDescriptionSuccess() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders
						.post("/owners/{ownerId}/pick-up-requests/new", PickUpRequestControllerTests.TEST_OWNER_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf()).param("physicalStatus", "good")
						.param("address", "Calle 1").param("petType", "dog").param("isAccepted", "true"))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.view().name("redirect:/"));
	}

	// Caso positivo creacion sin descripcion ni estado fisico
	@WithMockUser(username = "owner1", roles = { "owner" }, password = "123")
	@Test
	void testProcessCreationFormNoDescriptionAndPhysicalStatusSuccess() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders
						.post("/owners/{ownerId}/pick-up-requests/new", PickUpRequestControllerTests.TEST_OWNER_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf()).param("address", "Calle 1")
						.param("petType", "dog").param("isAccepted", "true"))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.view().name("redirect:/"));
	}

	// Caso negativo creacion sin direccion
	@WithMockUser(username = "owner1", roles = { "owner" }, password = "123")
	@Test
	void testProcessCreationFormNoAddressIncorrect() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders
						.post("/owners/{ownerId}/pick-up-requests/new", PickUpRequestControllerTests.TEST_OWNER_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf()).param("description", "descripcion")
						.param("physicalStatus", "good").param("petType", "dog").param("isAccepted", "true"))
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("pickUpRequest"))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("pickUpRequest", "address"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("pick-up-requests/createOrUpdatePickUpRequestForm"));
	}

	// Caso negativo creacion sin petType
	@WithMockUser(username = "owner1", roles = { "owner" }, password = "123")
	@Test
	void testProcessCreationFormNoPetTypeIncorrect() throws Exception {
		Mockito.when(this.pickUpRequestService.savePickUpRequest(ArgumentMatchers.any()))
				.thenThrow(new NoPetTypeException());
		this.mockMvc
				.perform(MockMvcRequestBuilders
						.post("/owners/{ownerId}/pick-up-requests/new", PickUpRequestControllerTests.TEST_OWNER_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf()).param("description", "descripcion")
						.param("physicalStatus", "good").param("address", "Calle 1").param("petType", "")
						.param("isAccepted", "true"))
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("pickUpRequest"))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("pickUpRequest", "petType"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("pick-up-requests/createOrUpdatePickUpRequestForm"));
	}

	// Caso negativo creacion sin aceptado
	@WithMockUser(username = "owner1", roles = { "owner" }, password = "123")
	@Test
	void testProcessCreationFormNoAcceptedIncorrect() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders
						.post("/owners/{ownerId}/pick-up-requests/new", PickUpRequestControllerTests.TEST_OWNER_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf()).param("description", "descripcion")
						.param("physicalStatus", "good").param("address", "Calle 1").param("petType", "")
						.param("isAccepted", ""))
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("pickUpRequest"))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("pickUpRequest", "isAccepted"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("pick-up-requests/createOrUpdatePickUpRequestForm"));
	}
	//Listado de peticiones
	//Caso positivo listado correcto
	@WithMockUser(username = "owner1", roles = {
			"owner"
		}, password = "123")
		@Test
		void testInitShowForm() throws Exception {
			this.mockMvc.perform(MockMvcRequestBuilders.get("/owners/{ownerUsername}/pick-up-requests", 
				PickUpRequestControllerTests.TEST_OWNER_USERNAME))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeExists("pickUpRequests"))
			.andExpect(MockMvcResultMatchers.view().name("pick-up-requests/pickUpRequestsList"));
		}
	//Caso negativo acceso invalido
		@WithMockUser(username = "owner1", roles = {
				"owner"
			}, password = "123")
			@Test
			void testInitShowInvalidAccessForm() throws Exception {
			BDDMockito.given(this.authoritiesService.isAuthor(PickUpRequestControllerTests.TEST_OWNER_USERNAME)).willReturn(false);
			this.mockMvc.perform(MockMvcRequestBuilders.get("/owners/{ownerUsername}/pick-up-requests", "MicSker"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("exception"));
			}
		
		
}

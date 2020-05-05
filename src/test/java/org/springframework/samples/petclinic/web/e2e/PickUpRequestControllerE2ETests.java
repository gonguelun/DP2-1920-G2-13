
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
public class PickUpRequestControllerE2ETests {

	private static final int		TEST_PICK_UP_REQUEST_ID	= 1;

	private static final int		TEST_OWNER_ID			= 1;

	private MockMvc					mockMvc;

	@Autowired
	private WebApplicationContext	webApplicationContext;


	@BeforeEach
	public void setUp() {

		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
	}

	// TESTS HISTORIA DE USUARIO 20 (Métodos en PickUpRequestController)

	/* DELETE PICK UP REQUEST /vets/pick-up-requests/{pickUpId}/delete */

	// Caso positivo: redirige a /vets/pick-up-requests

	@WithMockUser(username = "vet1", roles = {
		"vet"
	}, password = "v3t")
	@Test
	void testProcessDeletePickUpRequestAsVetSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/vets/pick-up-requests/{pickUpId}/delete", PickUpRequestControllerE2ETests.TEST_PICK_UP_REQUEST_ID).with(SecurityMockMvcRequestPostProcessors.csrf()))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/vets/pick-up-requests"));
	}

	// Historia de usuario 16
	// Caso positivo acceder al formulario de crear
	@WithMockUser(username = "owner1", roles = {
		"owner"
	}, password = "0wn3r")
	@Test
	void testInitCreationForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners/{ownerId}/pick-up-requests/new", PickUpRequestControllerE2ETests.TEST_OWNER_ID)).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("pick-up-requests/createOrUpdatePickUpRequestForm")).andExpect(MockMvcResultMatchers.model().attributeExists("pickUpRequest"));
	}

	// Caso negativo acceder con id invalido
	@WithMockUser(username = "owner2", roles = {
		"owner"
	}, password = "0wn3r")
	@Test
	void testInitCreationFormError() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners/{ownerId}/pick-up-requests/new", 1)).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
	}

	// Caso positivo creacion con exito
	@WithMockUser(username = "owner1", roles = {
		"owner"
	}, password = "0wn3r")
	@Test
	void testProcessCreationFormSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/owners/{ownerId}/pick-up-requests/new", PickUpRequestControllerE2ETests.TEST_OWNER_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("description", "descripcion")
			.param("physicalStatus", "good").param("address", "Calle 1").param("petType", "dog").param("isAccepted", "true").param("isClosed", "false")).andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/"));
	}

	// Caso positivo creacion sin descripcion
	@WithMockUser(username = "owner1", roles = {
		"owner"
	}, password = "0wn3r")
	@Test
	void testProcessCreationFormNoDescriptionSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/owners/{ownerId}/pick-up-requests/new", PickUpRequestControllerE2ETests.TEST_OWNER_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("physicalStatus", "good").param("address", "Calle 1")
			.param("petType", "dog").param("isAccepted", "true").param("isClosed", "false")).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/"));
	}

	// Caso positivo creacion sin descripcion ni estado fisico
	@WithMockUser(username = "owner1", roles = {
		"owner"
	}, password = "0wn3r")
	@Test
	void testProcessCreationFormNoDescriptionAndPhysicalStatusSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/owners/{ownerId}/pick-up-requests/new", PickUpRequestControllerE2ETests.TEST_OWNER_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("address", "Calle 1").param("petType", "dog")
			.param("isAccepted", "true").param("isClosed", "false")).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/"));
	}

	// Caso negativo creacion sin direccion
	@WithMockUser(username = "owner1", roles = {
		"owner"
	}, password = "0wn3r")
	@Test
	void testProcessCreationFormNoAddressIncorrect() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/owners/{ownerId}/pick-up-requests/new", PickUpRequestControllerE2ETests.TEST_OWNER_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("description", "descripcion").param("physicalStatus", "good")
				.param("petType", "dog").param("isAccepted", "true"))
			.andExpect(MockMvcResultMatchers.model().attributeHasErrors("pickUpRequest")).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("pickUpRequest", "address")).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("pick-up-requests/createOrUpdatePickUpRequestForm"));
	}

	// Caso negativo creacion sin petType
	@WithMockUser(username = "owner1", roles = {
		"owner"
	}, password = "0wn3r")
	@Test
	void testProcessCreationFormNoPetTypeIncorrect() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/owners/{ownerId}/pick-up-requests/new", PickUpRequestControllerE2ETests.TEST_OWNER_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("description", "descripcion").param("physicalStatus", "good")
				.param("address", "Calle 1").param("petType", "").param("isAccepted", "true").param("isClosed", "false"))
			.andExpect(MockMvcResultMatchers.model().attributeHasErrors("pickUpRequest")).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("pickUpRequest", "petType")).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("pick-up-requests/createOrUpdatePickUpRequestForm"));
	}

	// Caso negativo creacion sin aceptado
	@WithMockUser(username = "owner1", roles = {
		"owner"
	}, password = "0wn3r")
	@Test
	void testProcessCreationFormNoAcceptedIncorrect() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/owners/{ownerId}/pick-up-requests/new", PickUpRequestControllerE2ETests.TEST_OWNER_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("description", "descripcion").param("physicalStatus", "good")
				.param("address", "Calle 1").param("petType", "").param("isAccepted", ""))
			.andExpect(MockMvcResultMatchers.model().attributeHasErrors("pickUpRequest")).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("pickUpRequest", "isAccepted")).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("pick-up-requests/createOrUpdatePickUpRequestForm"));
	}
	//Listado de peticiones
	//Caso positivo listado correcto
	@WithMockUser(username = "owner1", roles = {
		"owner"
	}, password = "0wn3r")
	@Test
	void testInitShowForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners/{ownerUsername}/pick-up-requests", "owner1")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("pickUpRequests"))
			.andExpect(MockMvcResultMatchers.view().name("pick-up-requests/pickUpRequestsList"));
	}
	//Caso negativo acceso invalido
	@WithMockUser(username = "owner2", roles = {
		"owner"
	}, password = "0wn3r")
	@Test
	void testInitShowInvalidAccessForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners/{ownerUsername}/pick-up-requests", "owner1")).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
	}

	/*
	 * Test de la historia de usuario 17
	 */

	// Get mapping de /owners/{ownerUsername}/pick-up-requests/{pickUpId}/delete

	//Caso positivo, intento borrar una petición de recogida siendo el autor de dicha petición

	@WithMockUser(username = "owner1", roles = {
		"owner"
	}, password = "0wn3r")
	@Test
	void testProcessDeleteOwnerPickUpRequestSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners/{ownerUsername}/pick-up-requests/{pickUpId}/delete", "owner1", PickUpRequestControllerE2ETests.TEST_PICK_UP_REQUEST_ID).with(SecurityMockMvcRequestPostProcessors.csrf()))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/owners/{ownerUsername}/pick-up-requests"));
	}

	//Caso negativo, no es el autor de la petición

	@WithMockUser(username = "owner2", roles = {
		"owner"
	}, password = "owner")
	@Test
	void testProcessDeleteOwnerPickUpRequestError() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners/{ownerUsername}/pick-up-requests/{pickUpId}/delete", "owner1", PickUpRequestControllerE2ETests.TEST_PICK_UP_REQUEST_ID).with(SecurityMockMvcRequestPostProcessors.csrf()))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
	}

	/*
	 * Test de la historia de usuario 19
	 */

	// Get mapping de /vets/pick-up-requests/{pickUpId}/update

	//Caso positivo accedo al formulario de una solicitud abierta
	@WithMockUser(username = "vet1", roles = {
		"vet"
	}, password = "v3t")
	@Test
	void testProcessUpdatePickUpRequestAsVetSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/vets/pick-up-requests/{pickUpId}/update", PickUpRequestControllerE2ETests.TEST_PICK_UP_REQUEST_ID).with(SecurityMockMvcRequestPostProcessors.csrf())).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("pick-up-requests/acceptOrDenyPickUpRequest"));
	}

	//Caso negativo accedo al formulario de una solicitud cerrada

	@WithMockUser(username = "vet1", roles = {
		"vet"
	}, password = "v3t")
	@Test
	void testProcessUpdatePickUpRequestAsVetError() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/vets/pick-up-requests/{pickUpId}/update", 2).with(SecurityMockMvcRequestPostProcessors.csrf())).andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
	}

	// Post mapping de /vets/pick-up-requests/{pickUpId}/update

	//Caso positivo, deniego la solicitud que aun estaba abierta

	@WithMockUser(username = "vet1", roles = {
		"vet"
	}, password = "v3t")
	@Test
	void testProcessUpdateFormSuccess() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/vets/pick-up-requests/{pickUpId}/update", PickUpRequestControllerE2ETests.TEST_PICK_UP_REQUEST_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("description", "descripcion")
				.param("physicalStatus", "good").param("address", "Calle 1").param("isAccepted", "false").param("isClosed", "true").param("contact", " no te lo recojo"))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/vets/pick-up-requests"));
	}

	//Caso negativo, intento aceptar una solicitud cerrada
	@WithMockUser(username = "vet1", roles = {
		"vet"
	}, password = "v3t")
	@Test
	void testProcessUpdateFormError() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/vets/pick-up-requests/{pickUpId}/update", 2).with(SecurityMockMvcRequestPostProcessors.csrf()).param("description", "descripcion").param("physicalStatus", "good").param("address", "Calle 1")
			.param("isAccepted", "false").param("isClosed", "true").param("contact", " no te lo recojo")).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
	}

	// Historia de usuario 18
	// Caso positivo
	@WithMockUser(username = "owner1", roles = {
		"owner"
	}, password = "0wn3r")
	@Test
	void testInitShowVetForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/vets/pick-up-requests")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("pickUpRequests"))
			.andExpect(MockMvcResultMatchers.view().name("pick-up-requests/allPickUpRequestsList"));
	}

}

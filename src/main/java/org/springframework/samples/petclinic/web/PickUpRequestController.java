
package org.springframework.samples.petclinic.web;

import java.util.Collection;
import java.util.Map;

import javax.activity.InvalidActivityException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Beautician;
import org.springframework.samples.petclinic.model.BeautyCenter;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.PickUpRequest;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.PickUpRequestService;
import org.springframework.samples.petclinic.service.exceptions.NoPetTypeException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PickUpRequestController {

	private static final String		VIEWS_PICK_UP_REQUEST_CREATE_OR_UPDATE_FORM	= "pick-up-requests/createOrUpdatePickUpRequestForm";

	private PickUpRequestService	pickUpRequestService;

	private AuthoritiesService		authoritiesService;

	private OwnerService			ownerService;

	private PetService				petService;


	@Autowired
	public PickUpRequestController(final PickUpRequestService pickUpRequestService, final AuthoritiesService authoritiesService, final OwnerService ownerService, final PetService petService) {
		this.authoritiesService = authoritiesService;
		this.pickUpRequestService = pickUpRequestService;
		this.ownerService = ownerService;
		this.petService = petService;
	}

	@InitBinder
	public void setAllowedFields(final WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@GetMapping(value = "/owners/{ownerId}/pick-up-requests/new")
	public String initCreatePickUpRequestForm(@PathVariable("ownerId") final int ownerId, final Model model) throws Exception {
		Owner owner = this.ownerService.findOwnerById(ownerId);
		try {
			this.authoritiesService.isAuthor(owner.getUser().getUsername());

		} catch (InvalidActivityException a) {
			return "redirect:/oups";
		}
		PickUpRequest pickUpRequest = new PickUpRequest();
		pickUpRequest.setIsAccepted(false);
		pickUpRequest.setOwner(owner);

		model.addAttribute(pickUpRequest);
		return PickUpRequestController.VIEWS_PICK_UP_REQUEST_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/owners/{ownerId}/pick-up-requests/new")
	public String processCreatePickUpRequestForm(@Valid final PickUpRequest pickUpRequest, final BindingResult result, @PathVariable("ownerId") final int ownerId, final ModelMap model) {

		if (result.hasErrors()) {
			model.put("pickUpRequest", pickUpRequest);
			return PickUpRequestController.VIEWS_PICK_UP_REQUEST_CREATE_OR_UPDATE_FORM;
		} else {
			Owner owner = this.ownerService.findOwnerById(ownerId);
			pickUpRequest.setOwner(owner);
			try {
				this.pickUpRequestService.savePickUpRequest(pickUpRequest);
			} catch (NoPetTypeException b) {
				model.put("pickUpRequest", pickUpRequest);
				result.rejectValue("petType", "notnull", "It's mandatory");
				return PickUpRequestController.VIEWS_PICK_UP_REQUEST_CREATE_OR_UPDATE_FORM;

			}
			return "redirect:/";

		}
	}

	@GetMapping(value = "/owners/{ownerUsername}/pick-up-requests")
	public String showPickUpRequests(@PathVariable("ownerUsername") final String ownerUsername, final Map<String, Object> model) throws Exception {
		try {
			this.authoritiesService.isAuthor(ownerUsername);
			model.put("pickUpRequests", this.pickUpRequestService.findPickUpRequestsByOwnerUsername(ownerUsername));
		} catch (InvalidActivityException e) {
			return "redirect:/oups";
		}
		Owner owner = this.pickUpRequestService.findOwnerByUsername(ownerUsername);
		model.put("ownerId", owner.getId());
		return "pick-up-requests/pickUpRequestsList";
	}

	@ModelAttribute("types")
	public Collection<PetType> populatePetTypes() {
		return this.petService.findPetTypes();
	}

	@GetMapping("/vets/pick-up-requests")
	public String listPickUpRequets(final Map<String, Object> model) {
		model.put("pickUpRequests", this.pickUpRequestService.findAllPickUpRequests());
		return "pick-up-requests/allPickUpRequestsList";
	}
	
	@GetMapping(value = "/vets/pick-up-requests/{pickUpId}/delete")
	public String deletePickUpRequest(@PathVariable("pickUpId") final int pickUpId) throws Exception {
		this.pickUpRequestService.remove(pickUpId);
		return "redirect:/vets/pick-up-requests";
	}
}

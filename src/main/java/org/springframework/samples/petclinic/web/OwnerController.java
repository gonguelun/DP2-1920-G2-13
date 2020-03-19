/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.samples.petclinic.web;

import java.util.Collection;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.BeautyCenter;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.BeautyDateService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Michael Isvy
 */
@Controller
public class OwnerController {

	private static final String		VIEWS_OWNER_UPDATE_FORM	= "owners/UpdateOwnerForm";

	private final OwnerService		ownerService;

	private final UserService		userService;

	private final PetService		petService;

	private final BeautyDateService	beautyDateService;


	@Autowired
	public OwnerController(final OwnerService ownerService, final UserService userService, final PetService petService, final BeautyDateService beautyDateService, final AuthoritiesService authoritiesService) {
		this.ownerService = ownerService;
		this.userService = userService;
		this.petService = petService;
		this.beautyDateService = beautyDateService;
	}

	@InitBinder
	public void setAllowedFields(final WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@GetMapping(value = "/owners/find")
	public String initFindForm(final Map<String, Object> model) {
		model.put("owner", new Owner());
		return "owners/findOwners";
	}

	@GetMapping(value = "/owners")
	public String processFindForm(Owner owner, final BindingResult result, final Map<String, Object> model) {

		// allow parameterless GET request for /owners to return all records
		if (owner.getLastName() == null) {
			owner.setLastName(""); // empty string signifies broadest possible search
		}

		// find owners by last name
		Collection<Owner> results = this.ownerService.findOwnerByLastName(owner.getLastName());
		if (results.isEmpty()) {
			// no owners found
			result.rejectValue("lastName", "notFound", "not found");
			return "owners/findOwners";
		} else if (results.size() == 1) {
			// 1 owner found
			owner = results.iterator().next();
			return "redirect:/owners/" + owner.getId();
		} else {
			// multiple owners found
			model.put("selections", results);
			return "owners/ownersList";
		}
	}

	@GetMapping(value = "/owners/{ownerId}/edit")
	public String initUpdateOwnerForm(@PathVariable("ownerId") final int ownerId, final Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();
		Owner owner = this.ownerService.findOwnerById(ownerId);
		if (currentPrincipalName.equals(owner.getUser().getUsername())) {
			model.addAttribute(owner);
			return OwnerController.VIEWS_OWNER_UPDATE_FORM;
		} else {
			return "redirect:/oups";
		}
	}

	@PostMapping(value = "/owners/{ownerId}/edit")
	public String processUpdateOwnerForm(@Valid final Owner owner, final BindingResult result, @PathVariable("ownerId") final int ownerId, final ModelMap model) {
		if (result.hasErrors()) {
			model.put("owner", owner);
			return OwnerController.VIEWS_OWNER_UPDATE_FORM;
		} else {
			owner.setId(ownerId);
			Owner ownerGuardado = this.ownerService.findOwnerById(ownerId);
			User usuario = this.userService.findUserWithSameName(owner.getUser().getUsername());

			if (owner.getUser().getUsername().equals(ownerGuardado.getUser().getUsername()) || usuario == null) {
				owner.getUser().setId(ownerGuardado.getUser().getId());
				this.ownerService.saveOwner(owner);
				return "redirect:/owners/{ownerId}";

			} else {
				result.rejectValue("user.username", "duplicate", "already exists");
				model.put("owner", owner);
				return OwnerController.VIEWS_OWNER_UPDATE_FORM;
			}
		}
	}

	/**
	 * Custom handler for displaying an owner.
	 *
	 * @param ownerId
	 *            the ID of the owner to display
	 * @return a ModelMap with the model attributes for the view
	 */
	@GetMapping("/owners/{ownerId}")
	public ModelAndView showOwner(@PathVariable("ownerId") final int ownerId) {
		ModelAndView mav = new ModelAndView("owners/ownerDetails");
		mav.addObject(this.ownerService.findOwnerById(ownerId));
		return mav;
	}

	@GetMapping(value = "/owners/beauty-centers/{petTypeId}")
	public String showBeautyCenter(final Map<String, Object> model, @PathVariable("petTypeId") final int petTypeId) {
		Collection<BeautyCenter> bc = this.ownerService.findAllBeautyCentersByPetType(petTypeId);
		model.put("beautyCenters", bc);
		return "owners/beautyCenterList";
	}

	@GetMapping(value = "/owners/search-beauty-center")
	public String searcheautyCenter(final Map<String, Object> model) {
		Collection<PetType> type = this.petService.findPetTypes();
		model.put("type", type);
		return "owners/searchBeautyCenter";
	}

	@GetMapping(value = "/owners/{ownerUsername}/beauty-dates")
	public String showBeautyDates(@PathVariable("ownerUsername") final String ownerUsername, final Map<String, Object> model) {
		model.put("beautyDates", this.beautyDateService.findBeautyDatesByOwnerUsername(ownerUsername));
		return "beauty-dates/beautyDatesList";
	}
}

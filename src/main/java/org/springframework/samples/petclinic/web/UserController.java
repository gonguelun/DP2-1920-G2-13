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
import org.springframework.samples.petclinic.model.Beautician;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.service.BeauticianService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Michael Isvy
 */
@Controller
public class UserController {

	private static final String	VIEWS_OWNER_CREATE_FORM			= "users/createOwnerForm";

	private static final String	VIEWS_VET_CREATE_FORM			= "users/createVetForm";

	private static final String	VIEWS_BEAUTICIAN_CREATE_FORM	= "users/createBeauticianForm";

	private final OwnerService	ownerService;

	private final VetService	vetService;


	@Autowired
	public UserController(final OwnerService clinicService, final VetService vetService, final BeauticianService beauticianService) {

		this.ownerService = clinicService;
		this.vetService = vetService;
		this.beauticianService = beauticianService;
	}


	@Autowired
	private BeauticianService	beauticianService;

	@Autowired
	private UserService			userService;

	@Autowired
	private PetService			petService;


	@InitBinder
	public void setAllowedFields(final WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@GetMapping(value = "/users/new-owner")
	public String initCreationFormOwner(final Map<String, Object> model) {
		Owner owner = new Owner();
		model.put("owner", owner);
		return UserController.VIEWS_OWNER_CREATE_FORM;
	}

	@PostMapping(value = "/users/new-owner")
	public String processCreationFormOwner(@Valid final Owner owner, final BindingResult result, final ModelMap model) {
		if (result.hasErrors()) {
			model.put("owner", owner);
			return UserController.VIEWS_OWNER_CREATE_FORM;
		} else {
			User usuario = this.userService.findUserWithSameName(owner.getUser().getUsername());
			if (usuario != null) {
				result.rejectValue("user.username", "duplicate", "already exists");
				model.put("owner", owner);
				return UserController.VIEWS_OWNER_CREATE_FORM;

			} else {
				this.ownerService.saveOwner(owner);
				;
				return "redirect:/";
			}
		}
	}

	@GetMapping(value = "/users/new-vet")
	public String initCreationFormVet(final Map<String, Object> model) {
		Vet vet = new Vet();
		model.put("vet", vet);
		return UserController.VIEWS_VET_CREATE_FORM;
	}

	@PostMapping(value = "/users/new-vet")
	public String processCreationFormVet(@Valid final Vet vet, final BindingResult result, final ModelMap model) {
		if (result.hasErrors()) {
			model.put("vet", vet);
			return UserController.VIEWS_VET_CREATE_FORM;
		} else {
			User usuario = this.userService.findUserWithSameName(vet.getUser().getUsername());
			if (usuario != null) {
				result.rejectValue("user.username", "duplicate", "already exists");
				model.put("vet", vet);
				return UserController.VIEWS_VET_CREATE_FORM;

			} else {
				this.vetService.saveVet(vet);
				return "redirect:/";
			}
		}
	}

	@ModelAttribute("types")
	public Collection<PetType> populatePetTypes() {
		return this.petService.findPetTypes();
	}

	@GetMapping(value = "/users/new-beautician")
	public String initCreationForm(final Map<String, Object> model) {
		Beautician beautician = new Beautician();
		model.put("beautician", beautician);
		return UserController.VIEWS_BEAUTICIAN_CREATE_FORM;
	}

	@PostMapping(value = "/users/new-beautician")
	public String processCreationForm(@Valid final Beautician beautician, final BindingResult result, final ModelMap model) {

		if (result.hasErrors()) {
			model.put("beautician", beautician);
			return UserController.VIEWS_BEAUTICIAN_CREATE_FORM;

		} else {
			User usuario = this.userService.findUserWithSameName(beautician.getUser().getUsername());
			if (usuario != null) {
				result.rejectValue("user.username", "duplicate", "Already exists");
				model.put("beautician", beautician);
				return UserController.VIEWS_BEAUTICIAN_CREATE_FORM;

			} else {
				this.beauticianService.saveBeautician(beautician);
				return "redirect:/";
			}
		}
	}

	@ModelAttribute("specialties")
	public Collection<Specialty> populateSpecialties() {
		return this.vetService.findSpecialties();
	}

}

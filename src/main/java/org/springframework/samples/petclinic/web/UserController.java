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
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.service.BeauticianService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.stereotype.Controller;
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

	private static final String	VIEWS_BEAUTICIAN_CREATE_FORM	= "users/createBeauticianForm";

	private final OwnerService	ownerService;


	@Autowired
	public UserController(final OwnerService clinicService) {
		this.ownerService = clinicService;
	}


	@Autowired
	private BeauticianService	beauticianService;

	@Autowired
	private PetService			petService;


	@InitBinder
	public void setAllowedFields(final WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
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
	public String processCreationForm(@Valid final Beautician beautician, final BindingResult result) {
		if (result.hasErrors()) {
			return UserController.VIEWS_BEAUTICIAN_CREATE_FORM;
		} else {
			this.beauticianService.saveBeautician(beautician);

			return "redirect:/";
		}
	}

}

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
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.model.Vets;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Juergen Hoeller
 * @author Mark Fisher
 * @author Ken Krebs
 * @author Arjen Poutsma
 */
@Controller
public class VetController {

	private static final String	VIEWS_VET_CREATE_OR_UPDATE_FORM	= "vets/createOrUpdateVetForm";

	private final VetService	vetService;


	@Autowired
	public VetController(final VetService vetService, final UserService userService, final AuthoritiesService authoritiesService) {
		this.vetService = vetService;
	}

	@InitBinder
	public void setAllowedFields(final WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@ModelAttribute("specialties")
	public Collection<Specialty> populateSpecialties() {
		return this.vetService.findSpecialties();
	}

	@GetMapping(value = {
		"/vets"
	})
	public String showVetList(final Map<String, Object> model) {
		// Here we are returning an object of type 'Vets' rather than a collection of Vet
		// objects
		// so it is simpler for Object-Xml mapping
		Vets vets = new Vets();
		vets.getVetList().addAll(this.vetService.findVets());
		model.put("vets", vets);
		return "vets/vetList";
	}

	@GetMapping(value = {
		"/vets.xml"
	})
	public @ResponseBody Vets showResourcesVetList() {
		// Here we are returning an object of type 'Vets' rather than a collection of Vet
		// objects
		// so it is simpler for JSon/Object mapping
		Vets vets = new Vets();
		vets.getVetList().addAll(this.vetService.findVets());
		return vets;
	}

	@GetMapping(value = "/vets/new")
	public String initCreationForm(final Map<String, Object> model) {
		Vet vet = new Vet();
		model.put("vet", vet);
		return VetController.VIEWS_VET_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/vets/new")
	public String processCreationForm(@Valid final Vet vet, final BindingResult result) {
		if (result.hasErrors()) {
			return VetController.VIEWS_VET_CREATE_OR_UPDATE_FORM;
		} else {
			//creating owner, user and authorities
			this.vetService.saveVet(vet);

			return "redirect:/vets/" + vet.getId();
		}
	}

	@GetMapping(value = "/vets/{vetId}/edit")
	public String initUpdateVetForm(@PathVariable("vetId") final int vetId, final Model model) {
		Vet vet = this.vetService.findVetById(vetId);
		model.addAttribute(vet);
		return VetController.VIEWS_VET_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/vets/{vetId}/edit")
	public String processUpdateVetForm(@Valid final Vet vet, final BindingResult result, @PathVariable("vetId") final int vetId) {
		if (result.hasErrors()) {
			return VetController.VIEWS_VET_CREATE_OR_UPDATE_FORM;
		} else {
			vet.setId(vetId);
			this.vetService.saveVet(vet);
			return "redirect:/vets/{vetId}";
		}
	}

	/**
	 * Custom handler for displaying an owner.
	 *
	 * @param ownerId
	 *            the ID of the owner to display
	 * @return a ModelMap with the model attributes for the view
	 */
	@GetMapping("/vets/{vetId}")
	public ModelAndView showOwner(@PathVariable("vetId") final int vetId) {
		ModelAndView mav = new ModelAndView("vets/vetDetails");
		mav.addObject(this.vetService.findVetById(vetId));
		return mav;
	}

}


package org.springframework.samples.petclinic.web;

import java.util.Collection;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Beautician;
import org.springframework.samples.petclinic.model.BeautyCenter;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.BeautyCenterService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class BeautyCenterController {

	@Autowired
	private BeautyCenterService	beautyService;

	@Autowired
	private PetService			petService;


	@Autowired
	public BeautyCenterController(final BeautyCenterService beautyService, final UserService userService, final AuthoritiesService authoritiesService) {
		this.beautyService = beautyService;
	}

	@InitBinder
	public void setAllowedFields(final WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@GetMapping(value = "/beauticians/{beauticianId}/beauty-centers/new")
	public String initCreationFormBeautyCenter(@PathVariable("beauticianId") final int beauticianId, final Map<String, Object> model) {
		Beautician beautician = this.beautyService.findBeauticianById(beauticianId);
		BeautyCenter beautyCenter = new BeautyCenter();
		beautyCenter.setBeautician(beautician);
		model.put("beautyCenter", beautyCenter);
		return "beauty-centers/createOrUpdateBeautyCenterForm";
	}

	@PostMapping(value = "/beauticians/{beauticianId}/beauty-centers/new")
	public String processCreationFormBeautyCenter(@Valid final BeautyCenter beautyCenter, @PathVariable("beauticianId") final int beauticianId, final BindingResult result, final ModelMap model) {
		if (result.hasErrors()) {
			model.put("beautyCenter", beautyCenter);
			return "beauty-centers/createOrUpdateBeautyCenterForm";
		} else {
			Beautician beautician = this.beautyService.findBeauticianById(beauticianId);
			beautyCenter.setBeautician(beautician);
			this.beautyService.save(beautyCenter);
			return "redirect:/";
		}
	}

	@GetMapping(value = "/beauticians/{beauticianId}/beauty-centers")
	public String showBeautyCenter(@PathVariable("beauticianId") final int beauticianId, final Map<String, Object> model) {
		model.put("beauty-centers", this.beautyService.findAllBeautyCenterByBeauticianId(beauticianId));
		return "beautyCenterList";
	}

	@ModelAttribute("types")
	public Collection<PetType> populatePetTypes() {
		return this.petService.findPetTypes();
	}

}

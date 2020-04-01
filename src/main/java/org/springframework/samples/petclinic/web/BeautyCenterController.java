
package org.springframework.samples.petclinic.web;

import java.util.Collection;
import java.util.Map;

import javax.activity.InvalidActivityException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Beautician;
import org.springframework.samples.petclinic.model.BeautyCenter;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.BeauticianService;
import org.springframework.samples.petclinic.service.BeautyCenterService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.samples.petclinic.service.exceptions.NoPetTypeException;
import org.springframework.samples.petclinic.service.exceptions.NullOrShortNameException;
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

	private BeautyCenterService	beautyService;

	private BeauticianService	beauticianService;

	private AuthoritiesService	authoritiesService;


	@Autowired
	public BeautyCenterController(final BeautyCenterService beautyService, final UserService userService, final AuthoritiesService authoritiesService, final BeauticianService beauticianService) {
		this.beautyService = beautyService;
		this.beauticianService = beauticianService;
		this.authoritiesService = authoritiesService;
	}

	@InitBinder
	public void setAllowedFields(final WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@ModelAttribute("petTypes")
	public Collection<PetType> populatePetTypes() {
		return this.beautyService.findPetTypes();
	}

	@GetMapping(value = "/beauticians/{beauticianId}/beauty-centers/new")
	public String initCreationFormBeautyCenter(@PathVariable("beauticianId") final int beauticianId, final Map<String, Object> model) throws Exception {

		Beautician beautician = this.beauticianService.findBeauticianById(beauticianId);
		try {

			this.authoritiesService.isAuthor(beautician.getUser().getUsername());
			BeautyCenter beautyCenter = new BeautyCenter();
			beautyCenter.setBeautician(beautician);
			model.put("beautyCenter", beautyCenter);

		} catch (InvalidActivityException e) {
			return "exception";
		}

		return "beauty-centers/createOrUpdateBeautyCenterForm";
	}

	@PostMapping(value = "/beauticians/{beauticianId}/beauty-centers/new")
	public String processCreationFormBeautyCenter(@Valid final BeautyCenter beautyCenter, final BindingResult result, @PathVariable("beauticianId") final int beauticianId, final ModelMap model) throws NoPetTypeException, NullOrShortNameException {
		Beautician bea = this.beauticianService.findBeauticianById(beauticianId);
		beautyCenter.setBeautician(bea);

		if (result.hasErrors()) {
			model.put("beautyCenter", beautyCenter);
			return "beauty-centers/createOrUpdateBeautyCenterForm";
		} else {

			try {
				this.beautyService.save(beautyCenter);
			} catch (NullOrShortNameException a) {
				model.put("beautyCenter", beautyCenter);
				result.rejectValue("name", "length", "Name length must be at least 3 characters long");
				return "beauty-centers/createOrUpdateBeautyCenterForm";
			} catch (NoPetTypeException b) {
				model.put("beautyCenter", beautyCenter);
				result.rejectValue("petType", "notnull", "It's mandatory");
				return "beauty-centers/createOrUpdateBeautyCenterForm";
			}

			return "redirect:/beauticians/{beauticianId}";

		}

	}

	@GetMapping(value = "/beauticians/{beauticianId}/beauty-centers")
	public String showBeautyCenter(@PathVariable("beauticianId") final int beauticianId, final Map<String, Object> model) throws Exception {

		Beautician beautician = this.beauticianService.findBeauticianById(beauticianId);
		try {
			this.authoritiesService.isAuthor(beautician.getUser().getUsername());
			model.put("beauty-centers", this.beautyService.findAllBeautyCenterByBeauticianId(beauticianId));

		} catch (InvalidActivityException e) {
			return "redirect:/oups";
		}

		return "beautyCenterList";
	}

	@GetMapping(value = "/beauticians/{beauticianId}/beauty-centers/{beautyCenterId}/edit")
	public String initUpdateOwnerForm(@PathVariable("beautyCenterId") final int beautyCenterId, @PathVariable("beauticianId") final int beauticianId, final ModelMap model) throws Exception {
		BeautyCenter beautyCenter = this.beautyService.findById(beautyCenterId);
		Beautician beautician = this.beauticianService.findBeauticianById(beauticianId);
		try {
			this.authoritiesService.isAuthor(beautician.getUser().getUsername());
			beautyCenter.setBeautician(beautician);
			model.put("beautyCenter", beautyCenter);
		} catch (InvalidActivityException a) {
			return "redirect:/oups";
		}

		return "beauty-centers/createOrUpdateBeautyCenterForm";
	}

	@PostMapping(value = "/beauticians/{beauticianId}/beauty-centers/{beautyCenterId}/edit")
	public String processUpdateBeauticianForm(@Valid final BeautyCenter beautyCenter, final BindingResult result, @PathVariable("beauticianId") final int beauticianId, @PathVariable("beautyCenterId") final int beautyCenterId, final ModelMap model) {
		Beautician bea = this.beauticianService.findBeauticianById(beauticianId);
		beautyCenter.setBeautician(bea);

		if (result.hasErrors()) {
			model.put("beautyCenter", beautyCenter);
			return "beauty-centers/createOrUpdateBeautyCenterForm";
		} else {

			if (beautyCenter.getName().length() >= 3 && !beautyCenter.getName().isEmpty()) {

				if (beautyCenter.getPetType() != null) {
					beautyCenter.setId(beautyCenterId);
					this.beautyService.update(beautyCenter, beautyCenterId);
					return "redirect:/beauticians/{beauticianId}";

				} else {
					result.rejectValue("petType", "notnull", "It's mandatory");
					model.put("beautyCenter", beautyCenter);
					return "beauty-centers/createOrUpdateBeautyCenterForm";
				}

			} else {
				result.rejectValue("name", "length", "Name length must be at least 3 characters long");
				model.put("beautyCenter", beautyCenter);
				return "beauty-centers/createOrUpdateBeautyCenterForm";
			}
		}

	}

	@GetMapping(value = "/beauticians/{beauticianId}/beauty-centers/{beautyCenterId}/delete")
	public String deleteBeautyCenter(@PathVariable("beauticianId") final int beauticianId, @PathVariable("beautyCenterId") final int beautyCenterId) throws Exception {

		Beautician beautician = this.beauticianService.findBeauticianById(beauticianId);
		try {
			this.authoritiesService.isAuthor(beautician.getUser().getUsername());
			BeautyCenter aux = this.beautyService.findById(beautyCenterId);
			aux.setBeautician(null);
			this.beautyService.remove(beautyCenterId);

		} catch (InvalidActivityException a) {
			return "redirect:/oups";
		}

		return "redirect:/beauticians/{beauticianId}";
	}

}

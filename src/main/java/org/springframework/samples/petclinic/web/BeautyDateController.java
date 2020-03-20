
package org.springframework.samples.petclinic.web;

import java.util.Collection;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.BeautyCenter;
import org.springframework.samples.petclinic.model.BeautyDate;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.service.BeautyCenterService;
import org.springframework.samples.petclinic.service.BeautyDateService;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedPetNameException;
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
public class BeautyDateController {

	private static final String	CREATE_UPDATE_BEAUTY_DATES	= "beauty-dates/createOrUpdateBeautyDateForm";

	private BeautyDateService	beautyDateService;

	private BeautyCenterService	beautyCenterService;


	@Autowired
	public BeautyDateController(final BeautyDateService beautyDateService, final BeautyCenterService beautyCenterService) {
		this.beautyDateService = beautyDateService;
		this.beautyCenterService = beautyCenterService;
	}

	@InitBinder
	public void setAllowedFields(final WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@GetMapping(value = "/owners/{ownerUsername}/beauty-centers/{beautyCenterId}/{petTypeId}/beauty-dates/new")
	public String initCreationFormOwner(@PathVariable("beautyCenterId") final int beautyCenterId, final Map<String, Object> model) {
		BeautyCenter beautyCenter = this.beautyCenterService.findById(beautyCenterId);
		BeautyDate beautyDate = new BeautyDate();
		beautyDate.setBeautyCenter(beautyCenter);
		model.put("beautyDate", beautyDate);
		return BeautyDateController.CREATE_UPDATE_BEAUTY_DATES;
	}

	@PostMapping(value = "/owners/{ownerUsername}/beauty-centers/{beautyCenterId}/{petTypeId}/beauty-dates/new")
	public String processCreationFormOwner(@PathVariable("beautyCenterId") final int beautyCenterId, @Valid final BeautyDate beautyDate, final BindingResult result, final ModelMap model) throws DataAccessException, DuplicatedPetNameException {
		BeautyCenter beautyCenter = this.beautyCenterService.findById(beautyCenterId);
		beautyDate.setBeautyCenter(beautyCenter);

		if (result.hasErrors()) {
			model.put("beautyDate", beautyDate);
			return BeautyDateController.CREATE_UPDATE_BEAUTY_DATES;

		} else {
			BeautyDate guardado = this.beautyDateService.findBeautyDateByPetId(beautyDate.getPet().getId());
			if (guardado != null) {
				result.rejectValue("pet", "duplicate", "Already has a date to a beauty service");
				model.put("beautyDate", beautyDate);
				return BeautyDateController.CREATE_UPDATE_BEAUTY_DATES;

			} else {
				this.beautyDateService.saveBeautyDate(beautyDate);
				return "redirect:/";
			}
		}
	}

	@ModelAttribute("ownerPets")
	public Collection<Pet> petsByOwner(@PathVariable("ownerUsername") final String ownerUsername, @PathVariable("petTypeId") final int petTypeId) {
		return this.beautyDateService.findPetsByOwnerAndType(ownerUsername, petTypeId);
	}

}

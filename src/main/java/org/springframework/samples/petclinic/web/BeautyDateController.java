
package org.springframework.samples.petclinic.web;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.activity.InvalidActivityException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.BeautyCenter;
import org.springframework.samples.petclinic.model.BeautyDate;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Product;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.BeautyCenterService;
import org.springframework.samples.petclinic.service.BeautyDateService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.exceptions.AlreadyDateException;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedPetNameException;
import org.springframework.samples.petclinic.service.exceptions.EmptyPetException;
import org.springframework.samples.petclinic.service.exceptions.IsNotInTimeException;
import org.springframework.samples.petclinic.service.exceptions.IsWeekendException;
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
public class BeautyDateController {

	private static final String	CREATE_UPDATE_BEAUTY_DATES	= "beauty-dates/createOrUpdateBeautyDateForm";

	private BeautyDateService	beautyDateService;

	private BeautyCenterService	beautyCenterService;

	private AuthoritiesService	authoritiesService;

	private PetService			petService;


	@Autowired
	public BeautyDateController(final BeautyDateService beautyDateService, final BeautyCenterService beautyCenterService, final AuthoritiesService authoritiesService, final PetService petService) {
		this.beautyDateService = beautyDateService;
		this.beautyCenterService = beautyCenterService;
		this.authoritiesService = authoritiesService;
		this.petService = petService;
	}

	@InitBinder
	public void setAllowedFields(final WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@GetMapping(value = "/owners/{ownerUsername}/beauty-centers/{beautyCenterId}/{petTypeId}/beauty-dates/new")
	public String initCreationFormOwner(@PathVariable("beautyCenterId") final int beautyCenterId, @PathVariable("ownerUsername") final String ownerUsername, final Map<String, Object> model) throws Exception {
		BeautyCenter beautyCenter = this.beautyCenterService.findById(beautyCenterId);

		try {
			this.authoritiesService.isAuthor(ownerUsername);
			BeautyDate beautyDate = new BeautyDate();
			beautyDate.setBeautyCenter(beautyCenter);
			model.put("beautyDate", beautyDate);
			List<Product> products = this.beautyDateService.bringProductsFromBeauticianWithPetType(beautyCenter.getBeautician(), beautyCenter.getPetType());
			model.put("products", products);
		} catch (InvalidActivityException a) {
			return "redirect:/oups";
		}

		return BeautyDateController.CREATE_UPDATE_BEAUTY_DATES;
	}

	@PostMapping(value = "/owners/{ownerUsername}/beauty-centers/{beautyCenterId}/{petTypeId}/beauty-dates/new")
	public String processCreationFormOwner(@PathVariable("beautyCenterId") final int beautyCenterId, @PathVariable("ownerUsername") final String ownerUsername, @Valid final BeautyDate beautyDate, final BindingResult result, final ModelMap model)
		throws DataAccessException, DuplicatedPetNameException {
		BeautyCenter beautyCenter = this.beautyCenterService.findById(beautyCenterId);
		beautyDate.setBeautyCenter(beautyCenter);

		if (result.hasErrors()) {
			model.put("beautyDate", beautyDate);
			return BeautyDateController.CREATE_UPDATE_BEAUTY_DATES;

		} else {

			try {
				this.beautyDateService.saveBeautyDate(beautyDate);
			} catch (AlreadyDateException e) {
				result.rejectValue("pet", "beautyDateError", "Already has a date to a beauty service");
				return BeautyDateController.CREATE_UPDATE_BEAUTY_DATES;
			} catch (IsWeekendException a) {
				result.rejectValue("startDate", "finde", "it's a weekend!");
				return BeautyDateController.CREATE_UPDATE_BEAUTY_DATES;
			} catch (IsNotInTimeException b) {
				result.rejectValue("startDate", "horario", "it's not working time!");
				return BeautyDateController.CREATE_UPDATE_BEAUTY_DATES;
			} catch (EmptyPetException c) {
				result.rejectValue("pet", "mandatorydate", "you must choose a pet!");
				return BeautyDateController.CREATE_UPDATE_BEAUTY_DATES;
			}

			return "redirect:/";

		}

	}

	@GetMapping(value = "/owners/{ownerUsername}/beauty-dates/{beautyDateId}/update")
	public String initUpdateBeautyDateForm(@PathVariable("ownerUsername") final String ownerUsername, @PathVariable("beautyDateId") final int beautyDateId, final Model model) throws Exception {
		try {
			this.authoritiesService.isAuthor(ownerUsername);
		} catch (InvalidActivityException a) {
			return "redirect:/oups";
		}

		BeautyDate aux = this.beautyDateService.findById(beautyDateId);
		model.addAttribute(aux);
		return BeautyDateController.CREATE_UPDATE_BEAUTY_DATES;

	}

	@PostMapping(value = "/owners/{ownerUsername}/beauty-dates/{beautyDateId}/update")
	public String processUpdateBeautyDateForm(@Valid final BeautyDate beautyDate, final BindingResult result, @PathVariable("ownerUsername") final String ownerUsername, @PathVariable("beautyDateId") final int beautyDateId, final ModelMap model)
		throws Exception {
		if (result.hasErrors()) {
			model.put("beautyDate", beautyDate);
			return BeautyDateController.CREATE_UPDATE_BEAUTY_DATES;
		} else {
			BeautyDate aux = this.beautyDateService.findBeautyDateById(beautyDateId);
			aux.setStartDate(beautyDate.getStartDate());
			try {
				this.beautyDateService.saveBeautyDate(aux);
			} catch (AlreadyDateException e) {
				result.rejectValue("pet", "beautyDateError", "Already has a date to a beauty service");
				return BeautyDateController.CREATE_UPDATE_BEAUTY_DATES;
			} catch (IsWeekendException a) {
				result.rejectValue("startDate", "finde", "it's a weekend!");
				return BeautyDateController.CREATE_UPDATE_BEAUTY_DATES;
			} catch (IsNotInTimeException b) {
				result.rejectValue("startDate", "horario", "it's not working time!");
				return BeautyDateController.CREATE_UPDATE_BEAUTY_DATES;
			} catch (EmptyPetException c) {
				result.rejectValue("pet", "mandatorydate", "you must choose a pet!");
				return BeautyDateController.CREATE_UPDATE_BEAUTY_DATES;
			}
		}
		return "redirect:/";
	}

	@ModelAttribute("ownerPets")
	public Collection<Pet> petsByOwner(@PathVariable("ownerUsername") final String ownerUsername, @PathVariable(value = "petTypeId", required = false) final Integer petTypeId) {
		try {
			return this.beautyDateService.findPetsByOwnerAndType(ownerUsername, petTypeId);
		} catch (Exception e) {
			return null;
		}
	}

	@ModelAttribute("dateWeek")
	public List<LocalDateTime> datesInAWeek() {
		return this.beautyDateService.datesInAWeek();
	}

}


package org.springframework.samples.petclinic.web;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Map;

import javax.activity.InvalidActivityException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Beautician;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.BeauticianService;
import org.springframework.samples.petclinic.service.BeautyDateService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.samples.petclinic.service.exceptions.InvalidBeauticianException;
import org.springframework.samples.petclinic.service.exceptions.InvalidSpecializationException;
import org.springframework.samples.petclinic.service.exceptions.NullOrShortNameException;
import org.springframework.samples.petclinic.service.exceptions.PastDateException;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@RequestMapping("/beauticians")
@Controller
public class BeauticianController {

	private static final String	VIEWS_BEAUTICIAN_UPDATE_FORM	= "beauticians/updateBeauticianForm";

	private BeauticianService	beauticianService;

	private PetService			petService;

	private AuthoritiesService	authoritiesService;

	private UserService			userService;

	private BeautyDateService	beautyDateService;


	@Autowired
	public BeauticianController(final BeauticianService beauticianService, final PetService petService, final AuthoritiesService authoritiesService, final UserService userService, final BeautyDateService beautyDateService) {
		this.authoritiesService = authoritiesService;
		this.beauticianService = beauticianService;
		this.beautyDateService = beautyDateService;
		this.petService = petService;
		this.userService = userService;
	}

	@InitBinder
	public void setAllowedFields(final WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@GetMapping(value = "/{beauticianId}/edit")
	public String initUpdateOwnerForm(@PathVariable("beauticianId") final int beauticianId, final Model model) throws Exception {
		Beautician beautician = this.beauticianService.findBeauticianById(beauticianId);
		try {
			this.authoritiesService.isAuthor(beautician.getUser().getUsername());
			model.addAttribute(beautician);

		} catch (InvalidActivityException a) {
			return "redirect:/oups";
		}

		return BeauticianController.VIEWS_BEAUTICIAN_UPDATE_FORM;
	}

	@PostMapping(value = "/{beauticianId}/edit")
	public String processUpdateBeauticianForm(@Valid final Beautician beautician, final BindingResult result, @PathVariable("beauticianId") final int beauticianId, final ModelMap model)
		throws InvalidBeauticianException, InvalidSpecializationException, NullOrShortNameException {
		if (result.hasErrors()) {
			model.put("beautician", beautician);
			return BeauticianController.VIEWS_BEAUTICIAN_UPDATE_FORM;
		} else {
			beautician.setId(beauticianId);
			Beautician beauticianGuardado = this.beauticianService.findBeauticianById(beauticianId);
			User usuario = this.userService.findUserWithSameName(beautician.getUser().getUsername());
			try {
				this.beauticianService.isBeauticianGuardado(usuario, beautician, beauticianGuardado);
				this.beauticianService.saveBeautician(beautician);

			} catch (InvalidBeauticianException a) {
				result.rejectValue("user.username", "duplicate", "already exists");
				model.put("beautician", beautician);
				return BeauticianController.VIEWS_BEAUTICIAN_UPDATE_FORM;
			}
		}
		return "redirect:/beauticians/{beauticianId}";
	}

	@GetMapping("/{beauticianId}")
	public ModelAndView showBeautician(@PathVariable("beauticianId") final int beauticianId) throws Exception {
		Beautician beautician = this.beauticianService.findBeauticianById(beauticianId);
		try {
			this.authoritiesService.isAuthor(beautician.getUser().getUsername());

		} catch (InvalidActivityException a) {
			ModelAndView mv = new ModelAndView("exception");
			return mv;
		}
		ModelAndView mav = new ModelAndView("beauticians/beauticianDetails");
		mav.addObject(this.beauticianService.findBeauticianById(beauticianId));
		return mav;
	}

	@GetMapping("/principal/{beauticianUsername}")
	public String showBeauticianByUsername(@PathVariable("beauticianUsername") final String beauticianUsername, final Model model) throws Exception {

		int beauticianId = this.beauticianService.findBeauticianByUsername(beauticianUsername).getId();

		return "redirect:/beauticians/" + beauticianId;

	}

	@GetMapping("/searchBeautyDates/{beauticianUsername}")
	public String searchBeautyDates(@PathVariable("beauticianUsername") final String beauticianUsername, final Model model) throws Exception {
		int beauticianId = this.beauticianService.findBeauticianByUsername(beauticianUsername).getId();
		return "redirect:/beauticians/searchDates/" + beauticianId;

	}

	@GetMapping("/searchDates/{beauticianId}")
	public ModelAndView searchBeautyDatesId(@PathVariable("beauticianId") final int beauticianId) throws Exception {
		Beautician beautician = this.beauticianService.findBeauticianById(beauticianId);
		try {
			this.authoritiesService.isAuthor(beautician.getUser().getUsername());

		} catch (InvalidActivityException a) {
			ModelAndView mv = new ModelAndView("exception");
			return mv;
		}
		ModelAndView mav = new ModelAndView("beauticians/searchBeautyDates");
		mav.addObject(this.beauticianService.findBeauticianById(beauticianId));
		return mav;
	}

	@GetMapping(value = "/{beauticianId}/beautyDates/{date}/{hour}")
	public String showBeautyDates(@PathVariable("beauticianId") final int beauticianId, @PathVariable("hour") final int hour, @PathVariable("date") final String date, final Map<String, Object> model) throws Exception {
		Beautician beautician = this.beauticianService.findBeauticianById(beauticianId);
		LocalDate dateMax = LocalDate.parse(date);
		LocalTime time = LocalTime.of(hour, 0);
		LocalDateTime dateHourMax = LocalDateTime.of(dateMax, time);
		LocalDateTime actual = LocalDateTime.now();
		try {
			this.authoritiesService.isAuthor(beautician.getUser().getUsername());
			this.beautyDateService.isDateValid(dateMax);
			model.put("beautyDates", this.beautyDateService.findBeautyDatesByBeauticianIdAndDate(beauticianId, actual, dateHourMax));
		} catch (PastDateException e) {
			return "redirect:/oups";
		} catch (InvalidActivityException a) {
			return "redirect:/oups";
		}
		return "beauticians/beautyDatesList";
	}

	@ModelAttribute("types")
	public Collection<PetType> populatePetTypes() {
		return this.petService.findPetTypes();
	}

}

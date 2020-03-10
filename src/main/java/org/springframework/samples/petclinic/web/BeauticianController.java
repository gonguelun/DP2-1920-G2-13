
package org.springframework.samples.petclinic.web;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Beautician;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.BeauticianService;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@RequestMapping("/beauticians")
@Controller
public class BeauticianController {

	private static final String	VIEWS_BEAUTICIAN_UPDATE_FORM	= "beauticians/updateBeauticianForm";

	@Autowired
	private BeauticianService	beauticianService;

	@Autowired
	private PetService			petService;

	@Autowired
	private UserService			userService;


	@InitBinder
	public void setAllowedFields(final WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@GetMapping(value = "/{beauticianId}/edit")
	public String initUpdateOwnerForm(@PathVariable("beauticianId") final int beauticianId, final Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();
		Beautician beautician = this.beauticianService.findBeauticianById(beauticianId);
		if (currentPrincipalName.equals(beautician.getUser().getUsername())) {
			model.addAttribute(beautician);
			return BeauticianController.VIEWS_BEAUTICIAN_UPDATE_FORM;
		} else {
			return "redirect:/oups";
		}
	}

	@PostMapping(value = "/{beauticianId}/edit")
	public String processUpdateBeauticianForm(@Valid final Beautician beautician, final BindingResult result, @PathVariable("beauticianId") final int beauticianId, final ModelMap model) {
		if (result.hasErrors()) {
			model.put("beautician", beautician);
			return BeauticianController.VIEWS_BEAUTICIAN_UPDATE_FORM;
		} else {
			beautician.setId(beauticianId);
			Beautician beauticianGuardado = this.beauticianService.findBeauticianById(beauticianId);
			User usuario = this.userService.findUserWithSameName(beautician.getUser().getUsername());
			if (beautician.getUser().getUsername().equals(beauticianGuardado.getUser().getUsername()) || usuario == null) {
				beautician.getUser().setId(beauticianGuardado.getUser().getId());
				this.beauticianService.saveBeautician(beautician);
				return "redirect:/beauticians/{beauticianId}";

			} else {
				result.rejectValue("user.username", "duplicate", "already exists");
				model.put("beautician", beautician);
				return BeauticianController.VIEWS_BEAUTICIAN_UPDATE_FORM;
			}
		}

	}

	@GetMapping("/{beauticianId}")
	public ModelAndView showBeautician(@PathVariable("beauticianId") final int beauticianId) {
		ModelAndView mav = new ModelAndView("beauticians/beauticianDetails");
		mav.addObject(this.beauticianService.findBeauticianById(beauticianId));
		return mav;
	}

	@GetMapping("/principal/{beauticianUsername}")
	public String showBeauticianByUsername(@PathVariable("beauticianUsername") final String beauticianUsername) {

		int beauticianId = this.beauticianService.findBeauticianByUsername(beauticianUsername).getId();

		return "redirect:/beauticians/" + beauticianId;
	}

	@ModelAttribute("types")
	public Collection<PetType> populatePetTypes() {
		return this.petService.findPetTypes();
	}

}


package org.springframework.samples.petclinic.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.service.BeauticianService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

@Controller
public class BeauticianController {

	private static final String	VIEWS_BEAUTICIAN_UPDATE_FORM	= "beauticians/beauticianForm";

	@Autowired
	private BeauticianService	beauticianService;

	@Autowired
	private PetService			petService;


	@InitBinder
	public void setAllowedFields(final WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	/*
	 * @GetMapping(value = "/{beauticianId}/edit")
	 * public String initUpdateOwnerForm(@PathVariable("beauticianId") final int beauticianId, final Model model) {
	 * Beautician beautician = this.beauticianService.findBeauticianById(beauticianId);
	 * model.addAttribute(beautician);
	 * return BeauticianController.VIEWS_BEAUTICIAN_FORM;
	 * }
	 *
	 * @PostMapping(value = "/{beauticianId}/edit")
	 * public String processUpdateBeauticianForm(@Valid final Beautician beautician, final BindingResult result, @PathVariable("beauticianId") final int beauticianId) {
	 * if (result.hasErrors()) {
	 * return BeauticianController.VIEWS_BEAUTICIAN_FORM;
	 * } else {
	 * beautician.setId(beauticianId);
	 * this.beauticianService.saveBeautician(beautician);
	 * ;
	 * return "redirect:/beautician/{beauticianId}";
	 * }
	 * }
	 *
	 * @GetMapping("/{beauticianId}")
	 * public ModelAndView showOwner(@PathVariable("beauticianId") final int beauticianId) {
	 * ModelAndView mav = new ModelAndView("beauticians/beauticianDetails");
	 * mav.addObject(this.beauticianService.findBeauticianById(beauticianId));
	 * return mav;
	 * }
	 *
	 */

}

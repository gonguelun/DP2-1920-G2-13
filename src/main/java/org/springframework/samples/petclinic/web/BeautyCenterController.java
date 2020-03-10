
package org.springframework.samples.petclinic.web;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.BeautyCenter;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.BeautyCenterService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class BeautyCenterController {

	@Autowired
	private BeautyCenterService beautyService;


	@Autowired
	public BeautyCenterController(final BeautyCenterService beautyService, final UserService userService, final AuthoritiesService authoritiesService) {
		this.beautyService = beautyService;
	}

	@InitBinder
	public void setAllowedFields(final WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	/*
	 * @ModelAttribute("beautyCenter")
	 * public List<BeautyCenter> loadBeautyCenterWithBeautician(@PathVariable("beauticianId") final int beauticianId) {
	 * Beautician beautician = this.beautyService.findBeauticianById(beauticianId);
	 * BeautyCenter beautyCenter = new BeautyCenter();
	 * return beautyCenter;
	 * }
	 */

	@GetMapping(value = "/beautyCenters")
	public String listadoBeauty(final ModelMap modelMap) {
		String vista = "beautyCenter/listadoBeauty";
		Iterable<BeautyCenter> beautyCenter = this.beautyService.findAll();
		modelMap.addAttribute("beautyCenter", beautyCenter);
		return vista;
	}

	@GetMapping(value = "/beauticians/{beauticianId}/beautyCenter/new")
	public String initNewBeautyCenterForm(@PathVariable("beauticianId") final int beauticianId, final Map<String, Object> model) {
		return "beauticians/createOrUpdateBeautyCenterForm";
	}

	@PostMapping(value = "/beauticians/{beauticianId}/beautyCenter/new")
	public String processNewBeautyCenterForm(@Valid final BeautyCenter beautyCenter, final BindingResult result) {
		if (result.hasErrors()) {
			return "beauticians/createOrUpdateBeautyCenterForm";
		} else {
			this.beautyService.save(beautyCenter);
			return "redirect:/beautician/{beauticianId}";
		}
	}

	@GetMapping(value = "/beauticians/{beauticianId}/beautyCenter")
	public String showBeautyCenter(@PathVariable final int beauticianId, final Map<String, Object> model) {
		model.put("beautyCenter", this.beautyService.findBeauticianById(beauticianId).get().getBeautyCenter());
		return "listadoBeauty";
	}

	/*
	 * @GetMapping(path = "/new")
	 * public String crearBeauty(final ModelMap modelMap) {
	 * String vista = "beautyCenter/editBeautyCenter";
	 * modelMap.addAttribute("beautyCenter", new BeautyCenter());
	 * return vista;
	 * }
	 *
	 * @PostMapping(path = "/save")
	 * public String salvarBeauty(@Valid final BeautyCenter beautyCenter, final BindingResult result, final ModelMap modelMap) {
	 * String vista = "beautyCenter/listadoBeauty";
	 * if (result.hasErrors()) {
	 * modelMap.addAttribute("beautyCenter", beautyCenter);
	 * return "beautyCenter/editBeautyCenter";
	 * } else {
	 * this.beautyService.save(beautyCenter);
	 * modelMap.addAttribute("message", "Beauty Center successfully saved");
	 * }
	 * return vista;
	 * }
	 */

}

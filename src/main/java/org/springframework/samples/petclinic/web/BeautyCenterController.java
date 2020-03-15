
package org.springframework.samples.petclinic.web;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Beautician;
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

	@GetMapping(value = "/beauticians/{beauticianId}/beautyCenter/new")
	public String initCreationFormBeautyCenter(@PathVariable("beauticianId") final int beauticianId, final Map<String, Object> model) {
		Beautician beautician = this.beautyService.findBeauticianById(beauticianId);
		BeautyCenter beautyCenter = new BeautyCenter();
		beautyCenter.setBeautician(beautician);
		model.put("beautyCenter", beautyCenter);
		return "beautyCenter/createOrUpdateBeautyCenterForm";
	}

	@PostMapping(value = "/beauticians/{beauticianId}/beautyCenter/new")
	public String processCreationFormBeautyCenter(@Valid final BeautyCenter beautyCenter, final BindingResult result, final ModelMap model) {
		if (result.hasErrors()) {
			model.put("beautyCenter", beautyCenter);
			return "beautyCenter/createOrUpdateBeautyCenterForm";
		} else {
			this.beautyService.save(beautyCenter);
			return "";
		}
	}

}


package org.springframework.samples.petclinic.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Beautician;
import org.springframework.samples.petclinic.model.BeautyCenter;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Product;
import org.springframework.samples.petclinic.service.BeautyCenterService;
import org.springframework.samples.petclinic.service.ProductService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ProductController {

	private final ProductService		productService;
	private final BeautyCenterService	beautyCenterService;


	@Autowired
	public ProductController(final ProductService productService, final BeautyCenterService beautyCenterService) {
		this.productService = productService;
		this.beautyCenterService = beautyCenterService;
	}

	@InitBinder
	public void setAllowedFields(final WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@GetMapping(value = {
		"{beautyCenterId}/products"
	})
	public String showProductList(final Map<String, Object> model, @PathVariable("beautyCenterId") final int beautyCenterId) {
		Collection<Product> product = new ArrayList<>();
		BeautyCenter beautyCenter = this.beautyCenterService.findBeautyCenterByBeautyCenterId(beautyCenterId);
		product = this.productService.findProductsByPet(beautyCenter.getPetType().getId());
		model.put("products", product);
		return "products/productList";
	}

	@GetMapping(value = "/beauticians/{beauticianId}/products/new")
	public String initCreationForm(final ModelMap model, @PathVariable("beauticianId") final int beauticianId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();
		Beautician beautician = this.productService.findBeauticianById(beauticianId);

		if (currentPrincipalName.equals(beautician.getUser().getUsername())) {
			Product product = new Product();
			model.put("product", product);
			Collection<PetType> spe = this.productService.findSpecializationsByBeauticianId(beauticianId);
			model.put("specialization", spe);
			return "products/createOrUpdateProduct";

		} else {
			return "exception";
		}

	}

	@PostMapping(value = "/beauticians/{beauticianId}/products/new")
	public String processCreationForm(@Valid final Product product, final BindingResult result, @PathVariable("beauticianId") final int beauticianId, final ModelMap model) {
		if (result.hasErrors()) {
			model.put("product", product);
			return "products/createOrUpdateProduct";
		} else {
			Beautician beautician = this.productService.findBeauticianById(beauticianId);
			product.setBeautician(beautician);
			this.productService.save(product);
			return "redirect:/";
		}
	}

	@GetMapping(value = "/{beautyCenterId}/products/{productId}/edit")
	public String initUpdateForm(@PathVariable("productId") final int productId, final ModelMap model) {

		Product product = this.productService.findProductById(productId);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();
		if (currentPrincipalName.equals(product.getBeautician().getUser().getUsername())) {

			Collection<PetType> specializations = this.productService.findSpecializationsByBeauticianId(product.getBeautician().getId());
			model.put("specialization", specializations);
			model.put("product", product);
			return "products/createOrUpdateProduct";
		} else {
			return "exception";
		}
	}

	@PostMapping(value = "/{beautyCenterId}/products/{productId}/edit")
	public String processUpdateForm(@Valid final Product product, final BindingResult result, @PathVariable("productId") final int productId, final ModelMap model) {
		product.setId(productId);
		Beautician beautician = this.productService.findBeauticianByProductId(productId);
		product.setBeautician(beautician);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();
		if (currentPrincipalName.equals(product.getBeautician().getUser().getUsername())) {
			Collection<PetType> specializations = this.productService.findSpecializationsByBeauticianId(beautician.getId());
			if (result.hasErrors()) {
				model.put("specialization", specializations);
				model.put("product", product);
				return "products/createOrUpdateProduct";
			} else {
				product.setBeautician(beautician);
				this.productService.save(product);
				return "redirect:/";
			}
		} else {
			return "redirect:/oups";
		}
	}

	@RequestMapping(value = "/{beautyCenterId}/products/{productId}/delete", method = RequestMethod.GET)
	public String delete(@PathVariable("productId") final int productId, final Model model) {
		Product product = this.productService.findProductById(productId);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();
		if (currentPrincipalName.equals(product.getBeautician().getUser().getUsername())) {
			product.setBeautician(null);
			this.productService.deleteProduct(product);
			return "redirect:/";
		} else {
			return "redirect:/oups";
		}
	}

}

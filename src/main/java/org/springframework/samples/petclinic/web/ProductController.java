package org.springframework.samples.petclinic.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Beautician;
import org.springframework.samples.petclinic.model.BeautyCenter;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Product;
import org.springframework.samples.petclinic.model.Vets;
import org.springframework.samples.petclinic.service.BeauticianService;
import org.springframework.samples.petclinic.service.BeautyCenterService;
import org.springframework.samples.petclinic.service.ProductService;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedPetNameException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ProductController {
	private final ProductService productService;
	private final BeautyCenterService beauticianCenterService;

	@Autowired
	public ProductController(ProductService productService, BeautyCenterService beauticianCenterService) {
		this.productService = productService;
		this.beauticianCenterService=beauticianCenterService;
	}
	

	@GetMapping(value = { "{beautyCenterId}/products" })
	public String showProductList(Map<String, Object> model,@PathVariable("beautyCenterId") int beautyCenterId) {
		// Here we are returning an object of type 'Vets' rather than a collection of Vet
		// objects
		// so it is simpler for Object-Xml mapping
		Collection<Product> product = new ArrayList<>();
		BeautyCenter beautyCenter=this.beauticianCenterService.findBeautyCenterByBeautyCenterId(beautyCenterId);
		product=this.productService.findProductsByPet(beautyCenter.getPetType().getId());
		model.put("products", product);
		return "products/productList";
	}
	
	@GetMapping(value = "/beauticians/{beauticianId}/products/new")
	public String initCreationForm(ModelMap model,@PathVariable("beauticianId") int beauticianId) {
		Product product=new Product();
		Collection<PetType> spe=this.productService.findSpecializationsByBeauticianId(beauticianId);
		model.put("specialization",spe);
		model.put("product", product);
		return "products/createProduct";
	}

	@PostMapping(value = "/beauticians/{beauticianId}/products/new")
	public String processCreationForm(@Valid final Product product,BindingResult result, ModelMap model) {		
		if (result.hasErrors()) {
			model.put("product", product);
			return "products/createProduct";
		} else {
			this.productService.save(product);
			return "redirect:/";
		}
	}

}


package org.springframework.samples.petclinic.web;

import java.text.ParseException;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.samples.petclinic.model.Product;
import org.springframework.samples.petclinic.service.ProductService;
import org.springframework.stereotype.Component;

@Component
public class ProductFormatter implements Formatter<Product> {

	private final ProductService productService;


	@Autowired
	public ProductFormatter(final ProductService productService) {
		this.productService = productService;
	}

	@Override
	public String print(final Product product, final Locale locale) {
		String productId = Integer.toString(product.getId());
		return product.getName() + " - " + productId;
	}

	@Override
	public Product parse(final String text, final Locale locale) throws ParseException {
		String aux = text.trim();
		String[] trozos = aux.split("-");
		Product product = this.productService.findProductById(Integer.parseInt(trozos[1].trim()));
		if (product == null) {
			throw new ParseException("type not found: " + text, 0);
		}

		return product;
	}

}

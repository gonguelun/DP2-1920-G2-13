
package org.springframework.samples.petclinic.web;

import java.text.ParseException;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.stereotype.Component;

@Component
public class PetFormatter implements Formatter<Pet> {

	private final PetService petService;


	@Autowired
	public PetFormatter(final PetService petService) {
		this.petService = petService;
	}

	@Override
	public String print(final Pet pet, final Locale locale) {
		String petId = Integer.toString(pet.getId());
		return pet.getName() + " - " + petId;
	}

	@Override
	public Pet parse(final String text, final Locale locale) throws ParseException {
		String aux = text.trim();
		String[] trozos = aux.split("-");
		Pet pet = this.petService.findPetById(Integer.parseInt(trozos[1].trim()));
		if (pet == null) {
			throw new ParseException("type not found: " + text, 0);
		}

		return pet;
	}

}

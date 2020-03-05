
package org.springframework.samples.petclinic.model;

import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import lombok.Data;

@Data
@Entity
public class Beautician extends Person {

	@OneToMany
	private Collection<PetType>	specializations; // TODO: Poner typeSpecializations

	@OneToOne(cascade = CascadeType.ALL)
	private User				user;

}

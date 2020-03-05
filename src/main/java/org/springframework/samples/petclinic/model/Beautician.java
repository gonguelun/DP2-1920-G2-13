
package org.springframework.samples.petclinic.model;

import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;

import lombok.Data;

@Data
@Entity
public class Beautician extends Person {

	@ManyToMany
	private Collection<PetType>	specializations;

	@OneToOne(cascade = CascadeType.ALL)
	private User				user;

}

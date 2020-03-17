
package org.springframework.samples.petclinic.model;

import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.Valid;

import lombok.Data;

@Data
@Entity
public class Beautician extends Person {

	@ManyToMany
	private Collection<PetType>			specializations;

	@OneToOne(cascade = CascadeType.ALL)
	@Valid
	private User						user;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "beautician", fetch = FetchType.EAGER)
	private Collection<BeautyCenter>	beautyCenters;

}

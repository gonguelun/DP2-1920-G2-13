
package org.springframework.samples.petclinic.model;

import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import lombok.Data;

@Data
@Entity
public class Beautician extends Person {

	@ManyToMany
	private Collection<PetType>	specializations;

	@OneToOne(cascade = CascadeType.ALL)
	private User				user;

	@OneToMany(cascade = CascadeType.ALL)
	private List<BeautyCenter>	beautyCenter;

}

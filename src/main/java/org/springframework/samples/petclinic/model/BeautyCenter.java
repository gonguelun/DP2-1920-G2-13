
package org.springframework.samples.petclinic.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import lombok.Data;

@Entity
@Data
public class BeautyCenter extends NamedEntity {

	private String		description;

	@ManyToOne(optional = false,cascade = CascadeType.ALL)
	private PetType		petType;

	@ManyToOne(cascade = CascadeType.ALL, optional = false)
	private Beautician	beautician;
}

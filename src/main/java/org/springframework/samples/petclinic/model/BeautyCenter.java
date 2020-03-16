
package org.springframework.samples.petclinic.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Entity
@Data
public class BeautyCenter extends NamedEntity {

	private String		description;

	@ManyToOne
	@NotEmpty
	private PetType		petType;

	@ManyToOne(cascade = CascadeType.ALL)
	private Beautician	beautician;
}

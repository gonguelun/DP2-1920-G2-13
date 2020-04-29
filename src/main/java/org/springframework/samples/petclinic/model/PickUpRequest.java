
package org.springframework.samples.petclinic.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
@Entity
public class PickUpRequest extends BaseEntity {

	private String	description;

	@ManyToOne(optional = false, cascade = CascadeType.ALL)
	private PetType	petType;

	private String	physicalStatus;

	@NotNull
	private Boolean	isAccepted;

	@NotBlank
	private String	address;

	@NotNull
	private Boolean	isClosed;

	private String	contact;

	@Valid
	@ManyToOne(cascade = CascadeType.ALL)
	private Owner	owner;

}


package org.springframework.samples.petclinic.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "products")
public class Product extends NamedEntity {

	@ManyToOne
	@JoinColumn(name = "type_id")
	private PetType		type;

	@Column(name = "description")
	@NotEmpty
	private String		description;

	@Column(name = "avaliable")
	private boolean		avaliable;

	@ManyToOne(cascade = CascadeType.ALL)
	private Beautician	beautician;


	public PetType getType() {
		return this.type;
	}

	public void setType(final PetType type) {
		this.type = type;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public boolean isAvaliable() {
		return this.avaliable;
	}

	public void setAvaliable(final boolean avaliable) {
		this.avaliable = avaliable;
	}

	public Beautician getBeautician() {
		return this.beautician;
	}

	public void setBeautician(final Beautician beautician) {
		this.beautician = beautician;
	}

}

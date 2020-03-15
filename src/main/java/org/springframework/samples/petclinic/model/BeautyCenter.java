
package org.springframework.samples.petclinic.model;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Entity
@Data
public class BeautyCenter extends NamedEntity {

	private String		description;

	@DateTimeFormat(pattern = "H:MM:SS")
	private LocalDate	duration;

	@ManyToOne
	private PetType		petType;

	@ManyToOne(cascade = CascadeType.ALL)
	private Beautician	beautician;

}

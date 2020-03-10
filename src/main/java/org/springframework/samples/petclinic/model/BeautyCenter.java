
package org.springframework.samples.petclinic.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Entity
@Data
public class BeautyCenter extends NamedEntity {

	private String		description;

	@DateTimeFormat(pattern = "H:MM:SS")
	@NotEmpty
	private LocalDate	duration;

	@ManyToOne
	@NotEmpty
	private PetType		petType;

}

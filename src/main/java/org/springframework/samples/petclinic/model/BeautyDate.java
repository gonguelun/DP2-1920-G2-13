
package org.springframework.samples.petclinic.model;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
@Entity
public class BeautyDate extends BaseEntity {

	private String			description;

	@NotNull
	@DateTimeFormat(pattern = "yyyy/MM/dd HH:mm")
	private LocalDateTime	startDate;

	@Valid
	@OneToOne(cascade = CascadeType.ALL)
	private Pet				pet;

	@Valid
	@ManyToOne(cascade = CascadeType.ALL)
	private BeautyCenter	beautyCenter;


	public LocalDateTime endTime() {
		return this.startDate.plusHours(1);
	}

}

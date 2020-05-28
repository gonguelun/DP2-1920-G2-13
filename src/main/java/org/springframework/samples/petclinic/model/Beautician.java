
package org.springframework.samples.petclinic.model;

import java.util.Collection;

import java.util.LinkedHashSet;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.Valid;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import lombok.Data;
import lombok.ToString;

@Data
@Entity
public class Beautician extends Person {

	@ManyToMany
	private Collection<PetType>			specializations;

	@OneToOne(cascade = CascadeType.ALL)
	@Valid
	private User						user;
	
	@ToString.Exclude
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "beautician", fetch = FetchType.EAGER)
	private Collection<BeautyCenter>	beautyCenters;

}


package org.springframework.samples.petclinic.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User extends BaseEntity {

	@Column(unique = true)
	@NotEmpty
	String	username;

	@NotEmpty
	String	password;

	boolean	enabled;
}

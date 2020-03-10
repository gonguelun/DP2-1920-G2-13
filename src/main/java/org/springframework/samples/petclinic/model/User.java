
package org.springframework.samples.petclinic.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User extends BaseEntity {

	@Column(unique = true)
	String	username;

	String	password;

	boolean	enabled;
}

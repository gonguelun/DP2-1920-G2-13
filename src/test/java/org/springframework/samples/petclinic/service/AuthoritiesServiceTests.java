
package org.springframework.samples.petclinic.service;

import javax.activity.InvalidActivityException;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.User;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class AuthoritiesServiceTests {

	@Autowired
	private AuthoritiesService authoritiesService;


	// CASO POSITIVO: EL NOMBRE DE USUARIO EN LA URL COINCIDE CON EL DEL USUARIO AUTENTICADO

	@WithMockUser(username = "usuario", roles = {
		"user"
	}, password = "usuario")
	@Test
	void isAuthorUsernameCorrect() throws InvalidActivityException {

		User user = new User();
		user.setId(1);
		user.setUsername("usuario");
		user.setPassword("usuario");
		user.setEnabled(true);

		Assert.assertEquals(true, this.authoritiesService.isAuthor(user.getUsername()));
	}

	// CASO NEGATIVO: EL NOMBRE DE USUARIO EN LA URL NO ES EL DEL USUARIO AUTENTICADO

	@WithMockUser(username = "user", roles = {
		"user"
	}, password = "1234")
	@Test
	void isAuthorUsernameIncorrect() throws InvalidActivityException {

		User user = new User();
		user.setId(1);
		user.setUsername("usuario");
		user.setPassword("usuario");
		user.setEnabled(true);

		Assertions.assertThrows(InvalidActivityException.class, () -> {
			this.authoritiesService.isAuthor(user.getUsername());
		});
	}

}

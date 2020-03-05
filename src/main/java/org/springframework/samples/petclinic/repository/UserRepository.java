
package org.springframework.samples.petclinic.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.User;

public interface UserRepository extends CrudRepository<User, String> {

	@Query("SELECT us.username FROM User us")
	Collection<String> findAllUsernames();

	@Query("SELECT us FROM User us where (us.username = ?1 and us.id != ?2)")
	User findUserWithSameName(String username, int id);

}

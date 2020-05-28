/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.samples.petclinic.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.BeautyCenter;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.repository.BeautyCenterRepository;
import org.springframework.samples.petclinic.repository.OwnerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Mostly used as a facade for all Petclinic controllers Also a placeholder
 * for @Transactional and @Cacheable annotations
 *
 * @author Michael Isvy
 */
@Service
public class OwnerService {

	private OwnerRepository			ownerRepository;

	private BeautyCenterRepository	beautyCenterRepository;

	private UserService				userService;

	private AuthoritiesService		authoritiesService;


	@Autowired
	public OwnerService(final OwnerRepository ownerRepository, final BeautyCenterRepository beautyCenterRepository, final UserService userService, final AuthoritiesService authoritiesService) {
		this.ownerRepository = ownerRepository;
		this.beautyCenterRepository = beautyCenterRepository;
		this.userService = userService;
		this.authoritiesService = authoritiesService;
	}

	@Transactional(readOnly = true)
	public Owner findOwnerById(final int id) throws DataAccessException {
		return this.ownerRepository.findById(id);
	}

	@Transactional(readOnly = true)
	@Cacheable("ownerByLastName")
	public Collection<Owner> findOwnerByLastName(final String lastName) throws DataAccessException {
		return this.ownerRepository.findByLastName(lastName);
	}

	@Transactional
	@CacheEvict(cacheNames="ownerByLastName",allEntries=true)
	public void saveOwner(final Owner owner) throws DataAccessException {
		this.userService.saveUser(owner.getUser());
		this.ownerRepository.save(owner);
		this.authoritiesService.saveAuthorities(owner.getUser().getUsername(), "owner");
	}
	
	@Transactional(readOnly=true)
	@Cacheable("beautyCenters")
	public Collection<BeautyCenter> findAllBeautyCentersByPetType(final int petTypeId) {
		return this.beautyCenterRepository.findAllBeautyCentersByPetType(petTypeId);
	}

}

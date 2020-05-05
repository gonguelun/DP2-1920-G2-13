
package org.springframework.samples.petclinic.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Beautician;
import org.springframework.samples.petclinic.model.BeautyCenter;
import org.springframework.samples.petclinic.model.BeautyDate;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.exceptions.AlreadyDateException;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedPetNameException;
import org.springframework.samples.petclinic.service.exceptions.EmptyPetException;
import org.springframework.samples.petclinic.service.exceptions.IsNotInTimeException;
import org.springframework.samples.petclinic.service.exceptions.IsWeekendException;
import org.springframework.samples.petclinic.service.exceptions.PastDateException;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class BeautyDateServiceTests {

	@Autowired
	BeautyDateService	beautyDateService;

	@Autowired
	BeauticianService	beauticianService;

	@Autowired
	PetService			petService;

	@Autowired
	BeautyCenterService	beautyService;


	//Caso positivo. Se introduce un BeautyDate correcto
	@Test
	public void testSavingBeautyDateCorrect() throws DataAccessException, DuplicatedPetNameException, IsWeekendException, IsNotInTimeException, AlreadyDateException, EmptyPetException {

		PetType cat = new PetType();
		cat.setId(1);
		cat.setName("cat");
		List<PetType> temp = new ArrayList<>();
		temp.add(cat);
		Collection<Pet> temp2 = new ArrayList<>();
		Pet pet = new Pet();
		pet.setId(1);
		pet.setName("currupipi");
		pet.setBirthDate(LocalDate.of(2020, Month.JANUARY, 5));
		pet.setType(cat);
		temp2.add(pet);

		Pet peto = new Pet();
		peto.setId(2);
		peto.setBirthDate(LocalDate.of(2020, Month.JANUARY, 5));
		peto.setType(cat);
		peto.setName("peto");
		temp2.add(pet);

		User user = new User();
		user.setId(1);
		user.setEnabled(true);
		user.setUsername("beautician1");
		user.setPassword("123");

		Beautician beautician = new Beautician();
		beautician.setId(1);
		beautician.setFirstName("juan");
		beautician.setLastName("aurora");
		beautician.setSpecializations(temp);
		beautician.setUser(user);

		BeautyCenter beautyCenter = new BeautyCenter();
		beautyCenter.setId(1);
		beautyCenter.setName("beautycenter1");
		beautyCenter.setDescription("prueba1");
		beautyCenter.setPetType(cat);
		beautyCenter.setBeautician(beautician);

		Owner owner = new Owner();
		owner.setId(1);
		owner.setFirstName("Michael");
		owner.setLastName("Skere");
		owner.setCity("Badajoz");
		owner.setAddress("Calle 1");
		owner.setTelephone("666777888");
		User user2 = new User();
		user2.setId(2);
		user2.setEnabled(true);
		user2.setUsername("owner");
		user2.setPassword("123");
		owner.setUser(user2);

		owner.addPet(peto);
		owner.addPet(peto);

		BeautyDate bc = new BeautyDate();
		bc.setId(1);
		bc.setDescription("jjjajaja");
		bc.setBeautyCenter(beautyCenter);
		bc.setPet(peto);

		bc.setStartDate(LocalDateTime.of(LocalDate.of(2020, 3, 31), LocalTime.of(16, 0)));
		Assert.assertTrue(this.beautyDateService.saveBeautyDate(bc));
	}
	// Caso negativo. La fecha está vacia

	@Test
	public void testSavingBeautyDateIncorrectEmptyPet() throws DataAccessException, DuplicatedPetNameException, IsWeekendException, IsNotInTimeException, AlreadyDateException, EmptyPetException {

		PetType cat = new PetType();
		cat.setId(1);
		cat.setName("cat");
		List<PetType> temp = new ArrayList<>();
		temp.add(cat);
		Collection<Pet> temp2 = new ArrayList<>();
		Pet pet = new Pet();
		pet.setId(1);
		pet.setName("currupipi");
		pet.setBirthDate(LocalDate.of(2020, Month.JANUARY, 5));
		pet.setType(cat);
		temp2.add(pet);

		Pet peto = new Pet();
		peto.setId(2);
		peto.setBirthDate(LocalDate.of(2020, Month.JANUARY, 5));
		peto.setType(cat);
		peto.setName("peto");
		temp2.add(pet);

		User user = new User();
		user.setId(1);
		user.setEnabled(true);
		user.setUsername("beautician1");
		user.setPassword("123");

		Beautician beautician = new Beautician();
		beautician.setId(1);
		beautician.setFirstName("juan");
		beautician.setLastName("aurora");
		beautician.setSpecializations(temp);
		beautician.setUser(user);

		BeautyCenter beautyCenter = new BeautyCenter();
		beautyCenter.setId(1);
		beautyCenter.setName("beautycenter1");
		beautyCenter.setDescription("prueba1");
		beautyCenter.setPetType(cat);
		beautyCenter.setBeautician(beautician);

		Owner owner = new Owner();
		owner.setId(1);
		owner.setFirstName("Michael");
		owner.setLastName("Skere");
		owner.setCity("Badajoz");
		owner.setAddress("Calle 1");
		owner.setTelephone("666777888");
		User user2 = new User();
		user2.setId(2);
		user2.setEnabled(true);
		user2.setUsername("owner");
		user2.setPassword("123");
		owner.setUser(user2);

		owner.addPet(peto);
		owner.addPet(peto);

		BeautyDate bc = new BeautyDate();
		bc.setId(1);
		bc.setDescription("jjjajaja");
		bc.setBeautyCenter(beautyCenter);
		bc.setPet(null);

		bc.setStartDate(LocalDateTime.of(LocalDate.of(2020, 3, 31), LocalTime.of(16, 0)));

		Assertions.assertThrows(EmptyPetException.class, () -> this.beautyDateService.saveBeautyDate(bc));
	}
	//Caso negativo. Intento introducir un BeautyDate para un pet que ya tenia
	@Test
	public void testSavingBeautyDateConcurrent() throws DataAccessException, DuplicatedPetNameException, IsWeekendException, IsNotInTimeException, AlreadyDateException, EmptyPetException {

		PetType cat = new PetType();
		cat.setId(1);
		cat.setName("cat");
		List<PetType> temp = new ArrayList<>();
		temp.add(cat);
		Collection<Pet> temp2 = new ArrayList<>();
		Pet pet = new Pet();
		pet.setId(1);
		pet.setName("currupipi");
		pet.setBirthDate(LocalDate.of(2020, Month.JANUARY, 5));
		pet.setType(cat);
		temp2.add(pet);

		Pet peto = new Pet();
		peto.setId(2);
		peto.setBirthDate(LocalDate.of(2020, Month.JANUARY, 5));
		peto.setType(cat);
		peto.setName("peto");
		temp2.add(pet);

		User user = new User();
		user.setId(1);
		user.setEnabled(true);
		user.setUsername("beautician1");
		user.setPassword("123");

		Beautician beautician = new Beautician();
		beautician.setId(1);
		beautician.setFirstName("juan");
		beautician.setLastName("aurora");
		beautician.setSpecializations(temp);
		beautician.setUser(user);

		BeautyCenter beautyCenter = new BeautyCenter();
		beautyCenter.setId(1);
		beautyCenter.setName("beautycenter1");
		beautyCenter.setDescription("prueba1");
		beautyCenter.setPetType(cat);
		beautyCenter.setBeautician(beautician);

		Owner owner = new Owner();
		owner.setId(1);
		owner.setFirstName("Michael");
		owner.setLastName("Skere");
		owner.setCity("Badajoz");
		owner.setAddress("Calle 1");
		owner.setTelephone("666777888");
		User user2 = new User();
		user2.setId(2);
		user2.setEnabled(true);
		user2.setUsername("owner");
		user2.setPassword("123");
		owner.setUser(user2);

		owner.addPet(peto);
		owner.addPet(pet);

		BeautyDate bc = new BeautyDate();
		bc.setId(1);
		bc.setDescription("jjjajaja");
		bc.setBeautyCenter(beautyCenter);
		bc.setPet(peto);

		bc.setStartDate(LocalDateTime.of(LocalDate.of(2021, 4, 1), LocalTime.of(16, 0)));

		this.beautyDateService.saveBeautyDate(bc);

		BeautyDate bc2 = new BeautyDate();
		bc2.setId(2);
		bc2.setDescription("jjjajaja");
		bc2.setBeautyCenter(beautyCenter);
		bc2.setPet(pet);

		bc2.setStartDate(LocalDateTime.of(LocalDate.of(2021, 4, 1), LocalTime.of(16, 0)));

		Assertions.assertThrows(AlreadyDateException.class, () -> this.beautyDateService.saveBeautyDate(bc2));
	}

	//Caso negativo. Intento introducir un BeautyDate un domingo
	@Test
	public void testSavingBeautyIncorrectWeekend() throws DataAccessException, DuplicatedPetNameException, IsWeekendException, IsNotInTimeException, AlreadyDateException {

		PetType cat = new PetType();
		cat.setId(1);
		cat.setName("cat");
		List<PetType> temp = new ArrayList<>();
		temp.add(cat);
		Collection<Pet> temp2 = new ArrayList<>();
		Pet pet = new Pet();
		pet.setId(1);
		pet.setName("currupipi");
		pet.setBirthDate(LocalDate.of(2020, Month.JANUARY, 5));
		pet.setType(cat);
		temp2.add(pet);

		Pet peto = new Pet();
		peto.setId(2);
		peto.setBirthDate(LocalDate.of(2020, Month.JANUARY, 5));
		peto.setType(cat);
		peto.setName("peto");
		temp2.add(pet);

		User user = new User();
		user.setId(1);
		user.setEnabled(true);
		user.setUsername("beautician1");
		user.setPassword("123");

		Beautician beautician = new Beautician();
		beautician.setId(1);
		beautician.setFirstName("juan");
		beautician.setLastName("aurora");
		beautician.setSpecializations(temp);
		beautician.setUser(user);

		BeautyCenter beautyCenter = new BeautyCenter();
		beautyCenter.setId(1);
		beautyCenter.setName("beautycenter1");
		beautyCenter.setDescription("prueba1");
		beautyCenter.setPetType(cat);
		beautyCenter.setBeautician(beautician);

		Owner owner = new Owner();
		owner.setId(1);
		owner.setFirstName("Michael");
		owner.setLastName("Skere");
		owner.setCity("Badajoz");
		owner.setAddress("Calle 1");
		owner.setTelephone("666777888");
		User user2 = new User();
		user2.setId(2);
		user2.setEnabled(true);
		user2.setUsername("owner");
		user2.setPassword("123");
		owner.setUser(user2);

		owner.addPet(peto);
		owner.addPet(peto);

		BeautyDate bc = new BeautyDate();
		bc.setId(1);
		bc.setDescription("jjjajaja");
		bc.setBeautyCenter(beautyCenter);
		bc.setPet(peto);

		bc.setStartDate(LocalDateTime.of(LocalDate.of(2020, 3, 29), LocalTime.of(16, 0)));
		Assertions.assertThrows(IsWeekendException.class, () -> this.beautyDateService.saveBeautyDate(bc));
	}

	//Caso negativo. Intento introducir un BeautyDate a las 12:00

	@Test
	public void testSavingBeautyIncorrectTime() throws DataAccessException, DuplicatedPetNameException, IsWeekendException, IsNotInTimeException, AlreadyDateException {

		PetType cat = new PetType();
		cat.setId(1);
		cat.setName("cat");
		List<PetType> temp = new ArrayList<>();
		temp.add(cat);
		Collection<Pet> temp2 = new ArrayList<>();
		Pet pet = new Pet();
		pet.setId(1);
		pet.setName("currupipi");
		pet.setBirthDate(LocalDate.of(2020, Month.JANUARY, 5));
		pet.setType(cat);
		temp2.add(pet);

		Pet peto = new Pet();
		peto.setId(2);
		peto.setBirthDate(LocalDate.of(2020, Month.JANUARY, 5));
		peto.setType(cat);
		peto.setName("peto");
		temp2.add(pet);

		User user = new User();
		user.setId(1);
		user.setEnabled(true);
		user.setUsername("beautician1");
		user.setPassword("123");

		Beautician beautician = new Beautician();
		beautician.setId(1);
		beautician.setFirstName("juan");
		beautician.setLastName("aurora");
		beautician.setSpecializations(temp);
		beautician.setUser(user);

		BeautyCenter beautyCenter = new BeautyCenter();
		beautyCenter.setId(1);
		beautyCenter.setName("beautycenter1");
		beautyCenter.setDescription("prueba1");
		beautyCenter.setPetType(cat);
		beautyCenter.setBeautician(beautician);

		Owner owner = new Owner();
		owner.setId(1);
		owner.setFirstName("Michael");
		owner.setLastName("Skere");
		owner.setCity("Badajoz");
		owner.setAddress("Calle 1");
		owner.setTelephone("666777888");
		User user2 = new User();
		user2.setId(2);
		user2.setEnabled(true);
		user2.setUsername("owner");
		user2.setPassword("123");
		owner.setUser(user2);

		owner.addPet(peto);
		owner.addPet(peto);

		BeautyDate bc = new BeautyDate();
		bc.setId(1);
		bc.setDescription("jjjajaja");
		bc.setBeautyCenter(beautyCenter);
		bc.setPet(peto);

		bc.setStartDate(LocalDateTime.of(LocalDate.of(2020, 3, 31), LocalTime.of(12, 0)));
		Assertions.assertThrows(IsNotInTimeException.class, () -> this.beautyDateService.saveBeautyDate(bc));
	}

	// Test para el metodo datesInAWeek

	//Caso positivo. Contiene un día de lunes a viernes de 16:00 a 19:00
	@Test
	public void testDaysInAWeek() {

		List<LocalDateTime> dias = this.beautyDateService.datesInAWeek();

		boolean result;

		boolean noWeekend = dias.stream().anyMatch(d -> d.getDayOfWeek().equals(DayOfWeek.SATURDAY) || d.getDayOfWeek().equals(DayOfWeek.SUNDAY));

		boolean inTime = dias.stream().anyMatch(d -> !(d.getHour() == 16 || d.getHour() == 17 || d.getHour() == 18 || d.getHour() == 19));

		result = !noWeekend && !inTime;

		Assert.assertTrue(result);
	}

	//Caso negativo. Contiene un domingo
	@Test
	public void testDaysInAWeekWeekend() {

		List<LocalDateTime> dias = this.beautyDateService.datesInAWeek();

		Assert.assertTrue(!dias.stream().anyMatch(d -> d.getDayOfWeek().equals(DayOfWeek.SUNDAY)));

	}

	//Caso negativo. Contiene una hora a las 12:00
	@Test
	public void testDaysInAWeekTime() {

		List<LocalDateTime> dias = this.beautyDateService.datesInAWeek();

		Assert.assertTrue(!dias.stream().anyMatch(d -> d.getHour() == 12));

	}

	// TESTS HISTORIA DE USUARIO 9 (Métodos en BeautyDateService)

	/* IS DATE VALID */

	// Caso positivo: la fecha es futura

	@Test
	public void testIsDateValid() {
		LocalDate fechaFutura = LocalDate.now().plusDays(10);
		try {
			Assert.assertTrue(!this.beautyDateService.isDateValid(fechaFutura));
		} catch (PastDateException e) {

		}
	}

	// Caso negativo: la fecha es pasada

	@Test
	public void testIsDateNotValid() {
		LocalDate fechaFutura = LocalDate.now().minusDays(10);
		Assertions.assertThrows(PastDateException.class, () -> {
			this.beautyDateService.isDateValid(fechaFutura);
		});

	}

}

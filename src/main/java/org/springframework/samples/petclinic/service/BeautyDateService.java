
package org.springframework.samples.petclinic.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Beautician;
import org.springframework.samples.petclinic.model.BeautyDate;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Product;
import org.springframework.samples.petclinic.repository.BeautyDateRepository;
import org.springframework.samples.petclinic.service.exceptions.AlreadyDateException;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedPetNameException;
import org.springframework.samples.petclinic.service.exceptions.EmptyPetException;
import org.springframework.samples.petclinic.service.exceptions.IsNotInTimeException;
import org.springframework.samples.petclinic.service.exceptions.IsWeekendException;
import org.springframework.samples.petclinic.service.exceptions.PastDateException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BeautyDateService {

	private BeautyDateRepository	beautyDateRepository;

	private PetService				petService;


	@Autowired
	public BeautyDateService(final BeautyDateRepository beautyDateRepository, final PetService petService) {
		this.beautyDateRepository = beautyDateRepository;
		this.petService = petService;
	}

	@Transactional(readOnly = true)
	public BeautyDate findBeautyDateById(final int id) throws DataAccessException {
		return this.beautyDateRepository.findById(id).get();
	}

	@Transactional
	public boolean saveBeautyDate(final BeautyDate beautyDate) throws DataAccessException, DuplicatedPetNameException, IsWeekendException, IsNotInTimeException, AlreadyDateException, EmptyPetException {
		if (beautyDate.getPet() == null) {
			throw new EmptyPetException();
		} else if (this.isConcurrent(beautyDate)) {
			throw new AlreadyDateException();
		} else if (beautyDate.getStartDate().getDayOfWeek().equals(DayOfWeek.SATURDAY) || beautyDate.getStartDate().getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
			throw new IsWeekendException();
		} else if (!(beautyDate.getStartDate().getHour() == 16 || beautyDate.getStartDate().getHour() == 17 || beautyDate.getStartDate().getHour() == 18 || beautyDate.getStartDate().getHour() == 19)) {
			throw new IsNotInTimeException();
		} else {
			this.petService.savePet(beautyDate.getPet());
			this.beautyDateRepository.save(beautyDate);
			return true;
		}
	}

	private Boolean isConcurrent(final BeautyDate beautyDate) {
		Boolean res = false;

		Beautician bea = beautyDate.getBeautyCenter().getBeautician();
		List<BeautyDate> lista = this.beautyDateRepository.findBeautyDatesByBeauticianId(bea.getId());
		res = !lista.stream().noneMatch(f -> f.getStartDate().compareTo(beautyDate.getStartDate()) == 0);

		return res;
	}

	public BeautyDate findBeautyDateByPetId(final int id) {
		return this.beautyDateRepository.findBeautyDateByPetId(id);
	}

	public Collection<Pet> findPetsByOwnerAndType(final String ownerUsername, final int petTypeId) {
		return this.beautyDateRepository.findPetsByOwnerAndType(ownerUsername, petTypeId);
	}

	public Collection<BeautyDate> findBeautyDatesByOwnerUsername(final String ownerUsername) {
		return this.beautyDateRepository.findBeautyDatesByOwnerUsername(ownerUsername);
	}

	public BeautyDate findById(final int beautyDateId) {
		return this.beautyDateRepository.findById(beautyDateId).get();
	}

	public void remove(final int beautyDateId) {
		this.beautyDateRepository.remove(beautyDateId);

	}

	public List<BeautyDate> findBeautyDatesByBeauticianId(final int beauticianId) {
		return this.beautyDateRepository.findBeautyDatesByBeauticianId(beauticianId);
	}

	@Transactional
	public Integer countBeautyDates() {
		return (int) this.beautyDateRepository.count();
	}

	@Transactional
	public List<LocalDateTime> datesInAWeek() {
		List<LocalDateTime> hours = new ArrayList<>();
		LocalDateTime fechaActual = LocalDateTime.now();
		LocalDateTime fechaEn1Mes = fechaActual.plusMonths(1);
		fechaEn1Mes = fechaEn1Mes.withHour(16);
		fechaEn1Mes = fechaEn1Mes.withMinute(0);
		hours.add(fechaEn1Mes);
		for (int i = 0; i < 32; i = i + 4) {
			LocalDateTime aux = hours.get(i);
			for (int j = 0; j < 3; j++) {
				aux = aux.plusHours(1);
				hours.add(aux);
			}
			if (i < 28) {
				aux = hours.get(i);
				aux = aux.plusDays(1);
				hours.add(aux);
			}
		}
		for (int k = 0; k < hours.size(); k++) {
			LocalDateTime aux2 = hours.get(k);
			if (aux2.getDayOfWeek() == DayOfWeek.SATURDAY || aux2.getDayOfWeek() == DayOfWeek.SUNDAY) {
				hours.remove(aux2);
				k--;
			}
		}
		return hours;

	}

	public List<Product> bringProductsFromBeauticianWithPetType(final Beautician beautician, final PetType petType) {
		return this.beautyDateRepository.bringProductsFromBeauticianWithPetType(beautician.getId(), petType.getId());
	}

	public Collection<BeautyDate> findBeautyDatesByBeauticianIdAndDate(final int beauticianId, final LocalDateTime dateHourMax) {
		return this.beautyDateRepository.findBeautyDatesByBeauticianIdAndDate(beauticianId, dateHourMax);
	}

	public boolean isDateValid(final LocalDate dateMax) throws PastDateException {
		Boolean res = dateMax.isBefore(LocalDate.now());
		if (res) {
			throw new PastDateException();
		}

		return res;
	}
}

package com.petclinic.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import com.petclinic.domain.PatientRegistration;
import com.petclinic.domain.Pet;
import com.petclinic.enums.TreatmentStatus;

public interface PatientService {

    public Optional<PatientRegistration> findRecordById(Long id);

    public void findAllByReleaseDateInDay(LocalDate date);

    public List<PatientRegistration> findByStatus(TreatmentStatus status);

    public List<PatientRegistration> findByReleaseDateIsNotNull();

    public List<PatientRegistration> findAllByReleaseDate(LocalDate date);

    public void createNewPatientRecord(Pet patient, String complaint);

    public PatientRegistration editPatientRecord(TreatmentStatus status, PatientRegistration record, String review);

    public void delete(Long id);

    public void deleteAllById(Set<Long> ids);

    public void deleteRecord(PatientRegistration record);

}

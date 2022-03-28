package com.petclinic.service;

import com.petclinic.repository.PatientRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import com.petclinic.domain.PatientRegistration;
import com.petclinic.domain.Pet;
import com.petclinic.enums.TreatmentStatus;

import org.springframework.stereotype.Service;

@Service
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;

    public PatientServiceImpl(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public Optional<PatientRegistration> findRecordById(Long id) {

        return patientRepository.findById(id);

    }

    public void findAllByReleaseDateInDay(LocalDate date) {

        patientRepository.findAllByReleaseDate(date);

    }

    public void createNewPatientRecord(Pet patient, String complaint) {

        PatientRegistration newRecord = new PatientRegistration();

        newRecord.setPatient(patient);
        newRecord.setEntryDate(LocalDate.now());
        newRecord.setStatus(TreatmentStatus.INLINE);
        newRecord.setComplaint(complaint);

        patientRepository.save(newRecord);

    }

    public PatientRegistration editPatientRecord(TreatmentStatus status, PatientRegistration record, String review) {

        if (status == TreatmentStatus.COMPLETED) {

            record.setReview(review);
        }

        if (status == TreatmentStatus.RELEASE) {

            record.setReleaseDate(LocalDate.now());
        }

        record.setStatus(status);

        return patientRepository.save(record);

    }

    public List<PatientRegistration> findByStatus(TreatmentStatus status) {

        return patientRepository.findByStatus(status);
    };

    public List<PatientRegistration> findByReleaseDateIsNotNull() {

        return patientRepository.findByReleaseDateIsNotNull();
    };

    public List<PatientRegistration> findAllByReleaseDate(LocalDate date) {

        return patientRepository.findAllByReleaseDate(date);
    };

    public void delete(Long id) {

        patientRepository.deleteById(id);

    }

    public void deleteAllById(Set<Long> ids) {

        patientRepository.deleteAllById(ids);

    }

    public void deleteRecord(PatientRegistration record) {

        patientRepository.delete(record);

    }

}

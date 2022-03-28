package com.petclinic.repository;

import java.time.LocalDate;
import java.util.List;
import com.petclinic.domain.PatientRegistration;
import com.petclinic.enums.TreatmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<PatientRegistration, Long> {

    List<PatientRegistration> findByStatus(TreatmentStatus status);

    List<PatientRegistration> findByReleaseDateIsNotNull();

    List<PatientRegistration> findAllByReleaseDate(LocalDate date);

}

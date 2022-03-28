package com.petclinic.domain;

import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import com.petclinic.enums.TreatmentStatus;

@Entity
@Table(name = "tbl_patient_registration")
public class PatientRegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "PET_ID")
    private Pet patient;
    private LocalDate releaseDate;
    private LocalDate entryDate;
    @Enumerated(EnumType.ORDINAL)
    private TreatmentStatus status;
    private String complaint;
    private String review;

    public PatientRegistration(Long id, Pet patient, LocalDate releaseDate, LocalDate entryDate, TreatmentStatus status,
            String complaint, String review) {
        this.id = id;
        this.patient = patient;
        this.releaseDate = releaseDate;
        this.entryDate = entryDate;
        this.status = status;
        this.complaint = complaint;
        this.review = review;
    }

    public PatientRegistration() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Pet getPatient() {
        return patient;
    }

    public void setPatient(Pet patient) {
        this.patient = patient;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public LocalDate getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(LocalDate entryDate) {
        this.entryDate = entryDate;
    }

    public TreatmentStatus getStatus() {
        return status;
    }

    public void setStatus(TreatmentStatus status) {
        this.status = status;
    }

    public String getComplaint() {
        return complaint;
    }

    public void setComplaint(String complaint) {
        this.complaint = complaint;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    @Override
    public String toString() {
        return "PatientRegistration [complaint=" + complaint + ", entryDate=" + entryDate + ", id=" + id + ", patient="
                + patient + ", releaseDate=" + releaseDate + ", review=" + review + ", status=" + status + "]";
    }

}

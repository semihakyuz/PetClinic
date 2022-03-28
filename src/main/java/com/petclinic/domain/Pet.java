package com.petclinic.domain;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "tbl_pet")
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long animalId;
    @NotEmpty
    private String type;
    @NotEmpty
    private String breed;
    @NotEmpty
    private String name;
    private int age;
    @ManyToOne
    @JoinColumn(name = "OWNER_ID")
    private Owner owner;
    @OneToMany(mappedBy = "patient", cascade = CascadeType.REMOVE)
    private Set<PatientRegistration> records;

    public Pet(Set<PatientRegistration> records) {
        this.records = records;
    }

    public Pet(Long animalId, String type, String breed, @NotBlank String name,
            @Max(value = 2, message = " 2 den büyük değer giriniz") int age,
            Owner owner) {
        this.animalId = animalId;
        this.type = type;
        this.breed = breed;
        this.name = name;
        this.age = age;
        this.owner = owner;
    }

    public Pet() {
    }

    public Long getAnimalId() {
        return animalId;
    }

    public void setAnimalId(Long animalId) {
        this.animalId = animalId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public Set<PatientRegistration> getPatientRegistration() {
        return records;
    }

    public void setPatientRegistration(Set<PatientRegistration> records) {
        this.records = records;
    }

    public Set<PatientRegistration> getRecords() {
        return records;
    }

    public void setRecords(Set<PatientRegistration> records) {
        this.records = records;
    }

    @Override
    public String toString() {
        return "Pet [age=" + age + ", animalId=" + animalId + ", breed=" + breed + ", name=" + name + ", owner=" + owner
                + ", records=" + records + ", type=" + type + "]";
    }

}

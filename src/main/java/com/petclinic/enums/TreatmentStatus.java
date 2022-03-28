package com.petclinic.enums;

public enum TreatmentStatus {
    INLINE("INLINE"), ON("ON"), COMPLETED("COMPLETED"), RELEASE("RELEASE");

    private final String value;

    TreatmentStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

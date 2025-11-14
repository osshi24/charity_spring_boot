package com.example.charitybe.enums;

public enum TrangThaiGiaiNgan {
    CHO_DUYET("cho_duyet"),
    DA_DUYET("da_duyet"),
    TU_CHOI("tu_choi");

    private final String value;

    TrangThaiGiaiNgan(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static TrangThaiGiaiNgan fromValue(String value) {
        for (TrangThaiGiaiNgan status : TrangThaiGiaiNgan.values()) {
            if (status.value.equals(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}

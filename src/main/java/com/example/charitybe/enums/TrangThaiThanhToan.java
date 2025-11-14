package com.example.charitybe.enums;

public enum TrangThaiThanhToan {
    CHO_XU_LY("cho_xu_ly"),
    THANH_CONG("thanh_cong"),
    THAT_BAI("that_bai");

    private final String value;

    TrangThaiThanhToan(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static TrangThaiThanhToan fromValue(String value) {
        for (TrangThaiThanhToan status : TrangThaiThanhToan.values()) {
            if (status.value.equals(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}

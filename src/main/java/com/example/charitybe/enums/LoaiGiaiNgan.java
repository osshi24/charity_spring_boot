package com.example.charitybe.enums;

public enum LoaiGiaiNgan {
    TIEN_MAT("tien_mat"),
    CHUYEN_KHOAN("chuyen_khoan");

    private final String value;

    LoaiGiaiNgan(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static LoaiGiaiNgan fromValue(String value) {
        for (LoaiGiaiNgan type : LoaiGiaiNgan.values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}

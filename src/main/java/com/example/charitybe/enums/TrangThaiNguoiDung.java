package com.example.charitybe.enums;

public enum TrangThaiNguoiDung {
    HOAT_DONG("hoat_dong"),
    KHONG_HOAT_DONG("khong_hoat_dong");

    private final String value;

    TrangThaiNguoiDung(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static TrangThaiNguoiDung fromValue(String value) {
        for (TrangThaiNguoiDung status : TrangThaiNguoiDung.values()) {
            if (status.value.equals(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}

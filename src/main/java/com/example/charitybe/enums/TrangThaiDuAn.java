package com.example.charitybe.enums;

public enum TrangThaiDuAn {
    BAN_NHAP("ban_nhap"),
    HOAT_DONG("hoat_dong"),
    HOAN_THANH("hoan_thanh"),
    HUY_BO("huy_bo");

    private final String value;

    TrangThaiDuAn(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static TrangThaiDuAn fromValue(String value) {
        for (TrangThaiDuAn status : TrangThaiDuAn.values()) {
            if (status.value.equals(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}

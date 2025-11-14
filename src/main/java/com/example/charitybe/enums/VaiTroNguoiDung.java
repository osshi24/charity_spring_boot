package com.example.charitybe.enums;

public enum VaiTroNguoiDung {
    QUAN_TRI_VIEN("quan_tri_vien"),
    DIEU_HANH_VIEN("dieu_hanh_vien"),
    NGUOI_DUNG("nguoi_dung"),
    TINH_NGUYEN_VIEN("tinh_nguyen_vien");

    private final String value;

    VaiTroNguoiDung(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static VaiTroNguoiDung fromValue(String value) {
        for (VaiTroNguoiDung role : VaiTroNguoiDung.values()) {
            if (role.value.equals(value)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
